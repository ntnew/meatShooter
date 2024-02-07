package ru.meat.game.model.player;

import static ru.meat.game.settings.Constants.DEBUG;
import static ru.meat.game.settings.Constants.MAIN_ZOOM;
import static ru.meat.game.settings.Constants.PLAYER_MOVE_SPEED;
import static ru.meat.game.settings.Constants.WORLD_TO_VIEW;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.TimeUtils;
import java.util.Optional;
import lombok.Data;
import ru.meat.game.Box2dWorld;
import ru.meat.game.gui.GUI;
import ru.meat.game.model.weapons.Weapon;
import ru.meat.game.model.weapons.WeaponEnum;
import ru.meat.game.service.AudioService;
import ru.meat.game.service.RpgStatsService;

@Data
public class PlayerService {

  private static PlayerService instance;

  private Float finalSpeed;

  private volatile Float xOffset = 0f;

  private volatile Float yOffset = 0f;

  public static PlayerService getInstance() {
    if (instance == null) {
      instance = new PlayerService();
    }
    return instance;
  }

  private Player player;

  private Long weaponChangeLock = 0L;

  public PlayerService() {
    player = new Player(Gdx.graphics.getWidth() / 2f * MAIN_ZOOM,
        Gdx.graphics.getHeight() / 2f * MAIN_ZOOM);

    finalSpeed = calcSpeed();
  }

  /**
   * Повернуть подельку за мышью
   */
  public void rotateModel(OrthographicCamera camera) {
    Vector3 tmpVec3 = new Vector3();
    tmpVec3.set(Gdx.input.getX(), Gdx.input.getY(), 0);
    tmpVec3 = camera.unproject(tmpVec3);

    player.setModelFrontAngle(MathUtils.radiansToDegrees * MathUtils.atan2(tmpVec3.y - getBodyPosY() * WORLD_TO_VIEW,
        tmpVec3.x - getBodyPosX() * WORLD_TO_VIEW));
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
  public void shoot(OrthographicCamera camera) {
    Weapon weapon = getActualWeapon();
    if (weapon.getCurrentLockCounter() == 0
        || TimeUtils.timeSinceMillis(weapon.getCurrentLockCounter()) > weapon.getFireRate()
        * RpgStatsService.getInstance().getStats().getFireSpeed()
        && !weapon.isReloading()) {
      weapon.setCurrentLockCounter(TimeUtils.millis());

      Vector3 point = new Vector3();
      point.set(Gdx.input.getX(), Gdx.input.getY(), 0);
      camera.unproject(point);

      weapon.shoot(getBodyPosX(), getBodyPosY(), point.x/WORLD_TO_VIEW, point.y/WORLD_TO_VIEW,
          !player.getFeetStatus().equals(CharacterFeetStatus.IDLE));
      player.getTopState()
          .setAnimation(0, "shoot_" + player.getCurrentWeapon().getAniTag(), false);
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
    finalSpeed = calcSpeed();
  }

  public void changeToNextWeapon() {
    AudioService.getInstance().playClick();

    int pos = 0;
    Optional<Weapon> first = player.getWeapons().stream().filter(x -> x.getName().equals(player.getCurrentWeapon()))
        .findFirst();
    if (first.isPresent()) {
      pos = player.getWeapons().indexOf(first.get()) + 1;
      if (pos > player.getWeapons().size() - 1) {
        pos = 0;
      }
    }

    player.setCurrentWeapon(player.getWeapons().get(pos).getName());
    player.getTopState()
        .setAnimation(0, "move_" + player.getCurrentWeapon().getAniTag(), true);
    finalSpeed = calcSpeed();
  }

  /**
   * обработать нажатие на клавиши ходьбы
   */
  public void handleMoveKey() {
    handleMovingStatus();

    if (Gdx.input.isKeyPressed(Input.Keys.A)) {
      xOffset = -getSpeed();
    }

    if (Gdx.input.isKeyPressed(Input.Keys.W)) {
      yOffset = getSpeed();
    }

    if (Gdx.input.isKeyPressed(Input.Keys.S)) {
      yOffset = -getSpeed();
    }

    if (Gdx.input.isKeyPressed(Input.Keys.D)) {
      xOffset = getSpeed();
    }
  }

  /**
   * Переместить игрока на свиг х и у
   */
  public void transformPlayerBody() {
    player.getBody().setTransform(getBodyPosX() + xOffset / WORLD_TO_VIEW, getBodyPosY() + yOffset / WORLD_TO_VIEW, 0);
  }

  /**
   * обработать нажатие на клавиши ходьбы
   *
   * @param camera камера отрисовки текстур
   */
  public void handleMobileTouch(OrthographicCamera camera) {
    //TODO вынести в новый поток
    if (!player.isDead()) {
      for (int i = 0; i < 10; i++) {
        if (Gdx.input.isTouched(i) && GUI.getInstance().isOnLeftJoystick(i)) {
          changeFeetStatus(CharacterFeetStatus.MOVE);
          player.getFeetState().update(Gdx.graphics.getDeltaTime());
          Float dirAngle = GUI.getInstance().handleLeftJoystickTouch(i);

          player.setFeetRotationAngle(dirAngle + 180);

          float gipotenuzaSpeed = getSpeed();

          float x = MathUtils.sinDeg((dirAngle * -1) - 90) * gipotenuzaSpeed;
          float y = MathUtils.cosDeg((dirAngle * -1) - 90) * gipotenuzaSpeed;

          player.getBody().setTransform(getBodyPosX() + x / WORLD_TO_VIEW, getBodyPosY() + y / WORLD_TO_VIEW, 0);

          handleCameraTransform(camera);
        } else {
          changeFeetStatus(CharacterFeetStatus.IDLE);
        }

        if (Gdx.input.isTouched(i) && GUI.getInstance().isOnRightJoystick(i)) {

          Float dirAngle = GUI.getInstance().handleRightJoystickTouch(i);
          player.setModelFrontAngle(dirAngle + 180);

          Weapon weapon = getActualWeapon();
          if (weapon.getCurrentLockCounter() == 0
              || TimeUtils.timeSinceMillis(weapon.getCurrentLockCounter()) > weapon.getFireRate()
              * RpgStatsService.getInstance().getStats().getFireSpeed()
              && !weapon.isReloading()) {
            weapon.setCurrentLockCounter(TimeUtils.millis());

            float gip = 20;

            float x = MathUtils.sinDeg((dirAngle * -1) - 90) * gip;
            float y = MathUtils.cosDeg((dirAngle * -1) - 90) * gip;

            weapon.shootMobile(getBodyPosX(), getBodyPosY(), getBodyPosX() + x, getBodyPosY() + y,
                !player.getFeetStatus().equals(CharacterFeetStatus.IDLE));

            player.getTopState()
                .setAnimation(0, "shoot_" + player.getCurrentWeapon().getAniTag(), false);
          }
        }

        if (Gdx.input.isTouched(i) && GUI.getInstance().isOnChangeWeaponButton(i)
            && TimeUtils.timeSinceMillis(weaponChangeLock) > 400) {
          weaponChangeLock = TimeUtils.millis();
          changeToNextWeapon();
        }
      }
    }
  }

  public void handleCameraTransform(OrthographicCamera camera) {
    //Держит камеру примерно по центру игрока
    float x1;
    float y1;
    if ((getBodyPosX() * WORLD_TO_VIEW > camera.position.x && xOffset < 0)
        || (getBodyPosX() * WORLD_TO_VIEW < camera.position.x && xOffset > 0)) {
      x1 = 0;
    } else {
      x1 = xOffset;
    }

    if ((getBodyPosY() * WORLD_TO_VIEW > camera.position.y && yOffset < 0)
        || (getBodyPosY() * WORLD_TO_VIEW < camera.position.y && yOffset > 0)) {
      y1 = 0;
    } else {
      y1 = yOffset;
    }

    camera.translate(x1, y1);
    if (DEBUG) {
      Box2dWorld.getInstance().getCameraBox2D().translate(x1 / WORLD_TO_VIEW, y1 / WORLD_TO_VIEW);
    }
    xOffset = 0f;
    yOffset = 0f;
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
    return finalSpeed;
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

  public static void dispose() {
    instance.getPlayer().setDead(true);
    instance = null;
  }


  private float calcSpeed() {
    return PLAYER_MOVE_SPEED
        * MAIN_ZOOM
        * RpgStatsService.getInstance().getStats().getMoveSpeed()
        * getActualWeapon().getMoveSpeedMultiplier();
  }
}
