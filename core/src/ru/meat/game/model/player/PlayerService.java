package ru.meat.game.model.player;

import static ru.meat.game.settings.Constants.MAIN_ZOOM;
import static ru.meat.game.settings.Constants.PLAYER_MOVE_SPEED;
import static ru.meat.game.settings.Constants.WORLD_TO_VIEW;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.TimeUtils;
import com.esotericsoftware.spine.SkeletonRenderer;
import java.util.Objects;
import lombok.Data;
import lombok.SneakyThrows;
import ru.meat.game.Box2dWorld;
import ru.meat.game.gui.GUI;
import ru.meat.game.model.bodyData.BodyUserData;
import ru.meat.game.model.weapons.Weapon;
import ru.meat.game.model.weapons.WeaponEnum;
import ru.meat.game.service.AudioService;
import ru.meat.game.service.RpgStatsService;
import ru.meat.game.utils.GDXUtils;

@Data
public class PlayerService {

  private Player player;

  private float feetRotationAngle = 0;

  private float modelFrontAngle = 0;

  private float transformX = 0;

  private float transformY;


  public PlayerService(float x, float y) {
    player = new Player(x, y);
  }

  public void updateState() {
    //Обновить верхнюю часть анимации
    if (!player.isDead()
        && getActualWeapon().isReloading()
        && ((!player.getTopState().getTracks().isEmpty()
        && !Objects.equals("reload_" + player.getCurrentWeapon().getAniTag(),
        player.getTopState().getTracks().get(player.getTopState().getTracks().size - 1).getAnimation().getName()))
        || player.getTopState().getTracks().isEmpty())) {
      player.getTopState()
          .setAnimation(0, "reload_" + player.getCurrentWeapon().getAniTag(), true);
    }

    if (player.isDead()
        && ((!Objects.equals("death_" + player.getCurrentWeapon().getAniTag(),
        player.getTopState().getTracks().get(0).getAnimation().getName()))
        || player.getTopState().getTracks().isEmpty())) {
      player.getTopState().setAnimation(0, "death_" + player.getCurrentWeapon().getAniTag(), false);
    }

    player.getTopState().update(Gdx.graphics.getDeltaTime());
    player.getTopState().apply(player.getTopSkeleton());
    player.getTopSkeleton().updateWorldTransform();

    // обновить нижнюю часть анимации
    if (!player.isDead() && CharacterFeetStatus.MOVE.equals(player.getFeetStatus())) {
      player.getFeetState().update(Gdx.graphics.getDeltaTime());
    }

    player.getFeetState().apply(player.getFeetSkeleton());
    player.getFeetSkeleton().updateWorldTransform();

    handlePlayerHp();
  }

  /**
   * Обработать значения хп игрока
   */
  private void handlePlayerHp() {
    BodyUserData userData = (BodyUserData) player.getBody().getFixtureList().get(0).getUserData();
    if (userData.getDamage() != 0) {
      player.setHp(player.getHp() - userData.getDamage());
      userData.setDamage(0);
      AudioService.getInstance().playHit();
    }

    if (player.getHp() <= 0) {
      player.setDead(true);
    }
  }

  /**
   * Повернуть подельку за мышью
   */
  public void rotateModel() {
    Vector3 tmpVec3 = new Vector3();
    tmpVec3.set(Gdx.input.getX(), Gdx.input.getY(), 0);
    tmpVec3 = Box2dWorld.getInstance().getCameraBox2D().unproject(tmpVec3);

    modelFrontAngle = MathUtils.radiansToDegrees * MathUtils.atan2(tmpVec3.y - getBodyPosY(),
        tmpVec3.x - getBodyPosX());
  }

  public void drawPlayer(PolygonSpriteBatch polyBatch, SkeletonRenderer renderer) {
    if (!player.isDead()) {
      drawFeetSprite(polyBatch, renderer);
    }
    drawTopSprite(polyBatch, renderer);
  }

  private void drawFeetSprite(PolygonSpriteBatch polyBatch, SkeletonRenderer renderer) {
    player.getFeetSkeleton().setPosition(getBodyPosX() * WORLD_TO_VIEW, getBodyPosY() * WORLD_TO_VIEW);
    player.getFeetSkeleton().getRootBone().setRotation(feetRotationAngle);
    renderer.draw(polyBatch, player.getFeetSkeleton());
  }

  private void drawTopSprite(PolygonSpriteBatch polyBatch, SkeletonRenderer renderer) {
    player.getTopSkeleton().setPosition(getBodyPosX() * WORLD_TO_VIEW, getBodyPosY() * WORLD_TO_VIEW);
    player.getTopSkeleton().getRootBone().setRotation(modelFrontAngle);
    renderer.draw(polyBatch, player.getTopSkeleton());
  }

  private void changeTopStatus(CharacterTopStatus status) {
    player.setTopStatus(status);
  }


  private void changeFeetStatus(CharacterFeetStatus status) {
    player.setFeetStatus(status);
  }

  /**
   * Стрелять
   */
  public void shoot() {
    Weapon weapon = getActualWeapon();
    if (weapon.getCurrentLockCounter() == 0
        || TimeUtils.timeSinceMillis(weapon.getCurrentLockCounter()) > weapon.getFireRate()
        * RpgStatsService.getInstance().getStats().getFireSpeed()) {
      weapon.setCurrentLockCounter(TimeUtils.millis());

      Vector3 point = new Vector3();
      point.set(Gdx.input.getX(), Gdx.input.getY(), 0);
      Box2dWorld.getInstance().getCameraBox2D().unproject(point);

      weapon.shoot(getBodyPosX(), getBodyPosY(), point.x, point.y,
          !player.getFeetStatus().equals(CharacterFeetStatus.IDLE));
      if (!weapon.isReloading()) {
        player.getTopState()
            .setAnimation(0, "shoot_" + player.getCurrentWeapon().getAniTag(), false);
      }
    }
  }


  /**
   * Получить выбранное оружие у игрока
   */
  public Weapon getActualWeapon() {
    return player.getWeapons().stream().filter(x -> x.getName().equals(player.getCurrentWeapon())).findFirst()
        .orElse(player.getWeapons().get(0));
  }

  public void changeWeapon(int i) {
    player.setCurrentWeapon(WeaponEnum.getByPos(i));
    player.getTopState()
        .setAnimation(0, "move_" + player.getCurrentWeapon().getAniTag(), true);
  }

  /**
   * обработать нажатие на клавиши ходьбы
   *
   * @param camera      камера отрисовки текстур
   * @param cameraBox2D камера box2d
   */
  public void handleMoveKey(OrthographicCamera camera, OrthographicCamera cameraBox2D) {
    if (!player.isDead()) {
      handleMovingStatus();

      float x = 0;
      float y = 0;

      if (Gdx.input.isKeyPressed(Input.Keys.A)) {
        x = -getSpeed();
      }

      if (Gdx.input.isKeyPressed(Input.Keys.W)) {
        y = getSpeed();
      }

      if (Gdx.input.isKeyPressed(Input.Keys.S)) {
        y = -getSpeed();
      }

      if (Gdx.input.isKeyPressed(Input.Keys.D)) {
        x = getSpeed();
      }

      transformX = x;
      transformY = y;

      player.getBody().setTransform(getBodyPosX() + x / WORLD_TO_VIEW, getBodyPosY() + y / WORLD_TO_VIEW, 0);

      handleCameraTransform(camera, x, y);
    }
  }

  /**
   * обработать нажатие на клавиши ходьбы
   *
   * @param camera камера отрисовки текстур
   */
  public void handleMobileTouch(OrthographicCamera camera) {
    if (!player.isDead()) {
      for (int i = 0; i < 10; i++) {
        if (Gdx.input.isTouched(i) && GUI.getInstance().isOnLeftJoystick(i)) {
          changeFeetStatus(CharacterFeetStatus.MOVE);

          Float dirAngle = GUI.getInstance().handleLeftJoystickTouch(Gdx.input.getX(i), Gdx.input.getY(i));

          feetRotationAngle = dirAngle + 180;
          float gipotenuzaSpeed = getSpeed();

          float x = MathUtils.sinDeg((dirAngle * -1) - 90) * gipotenuzaSpeed;
          float y = MathUtils.cosDeg((dirAngle * -1) - 90) * gipotenuzaSpeed;

          player.getBody().setTransform(getBodyPosX() + x / WORLD_TO_VIEW, getBodyPosY() + y / WORLD_TO_VIEW, 0);

          handleCameraTransform(camera, x, y);
        } else {
          changeFeetStatus(CharacterFeetStatus.IDLE);
        }

        if (Gdx.input.isTouched(i) && GUI.getInstance().isOnRightJoystick(i)) {
          Weapon weapon = getActualWeapon();
          Float dirAngle = GUI.getInstance().handleRightJoystickTouch(Gdx.input.getX(i), Gdx.input.getY(i));
          modelFrontAngle = dirAngle + 180;

          if (weapon.getCurrentLockCounter() == 0
              || TimeUtils.timeSinceMillis(weapon.getCurrentLockCounter()) > weapon.getFireRate()
              * RpgStatsService.getInstance().getStats().getFireSpeed()) {
            weapon.setCurrentLockCounter(TimeUtils.millis());

            float gip = 20;

            float x = MathUtils.sinDeg((dirAngle * -1) - 90) * gip;
            float y = MathUtils.cosDeg((dirAngle * -1) - 90) * gip;

            Vector3 point = new Vector3();
            point.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            Box2dWorld.getInstance().getCameraBox2D().unproject(point);

            weapon.shootMobile(getBodyPosX(), getBodyPosY(),getBodyPosX() + x , getBodyPosY() + y ,
                !player.getFeetStatus().equals(CharacterFeetStatus.IDLE));
            if (!weapon.isReloading()) {
              player.getTopState()
                  .setAnimation(0, "shoot_" + player.getCurrentWeapon().getAniTag(), false);
            }
          }
        }
      }
    }
  }

  private void handleCameraTransform(OrthographicCamera camera, float x, float y) {
    //Держит камеру примерно по центру игрока
    OrthographicCamera cameraBox2D = Box2dWorld.getInstance().getCameraBox2D();
    if ((getBodyPosX() > cameraBox2D.position.x && x < 0) || (getBodyPosX() < cameraBox2D.position.x && x > 0)) {
      x = 0;
    }

    if ((getBodyPosY() > cameraBox2D.position.y && y < 0) || (getBodyPosY() < cameraBox2D.position.y && y > 0)) {
      y = 0;
    }

    camera.translate(x, y);
    cameraBox2D.translate(x / WORLD_TO_VIEW, y / WORLD_TO_VIEW);

    camera.update();
    cameraBox2D.update();
  }


  /**
   * Сменить статус действия текстур
   */
  private void handleMovingStatus() {
    if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(
        Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.D)) {
      AudioService.getInstance().playStep();
      changeFeetStatus(CharacterFeetStatus.MOVE);
      changeTopStatus(CharacterTopStatus.MOVE);
    } else {
      changeFeetStatus(CharacterFeetStatus.IDLE);
      changeTopStatus(CharacterTopStatus.IDLE);
    }
  }

  /**
   * Получить скорость движения игрока
   */
  private float getSpeed() {
    return
        PLAYER_MOVE_SPEED
            * MAIN_ZOOM
            * RpgStatsService.getInstance().getStats().getMoveSpeed()
            * getActualWeapon().getMoveSpeedMultiplier();
  }

  /**
   * Получить координату Х игрока в box2d
   */
  public float getBodyPosX() {
    return player.getBody().getPosition().x;
  }

  /**
   * Получить координату У игрока в box2d
   */
  public float getBodyPosY() {
    return player.getBody().getPosition().y;
  }
}
