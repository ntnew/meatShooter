package ru.meat.game.service;

import static ru.meat.game.settings.Constants.MAIN_ZOOM;
import static ru.meat.game.settings.Constants.PLAYER_MOVE_SPEED;
import static ru.meat.game.settings.Constants.WORLD_TO_VIEW;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.TimeUtils;
import lombok.Data;
import ru.meat.game.model.player.CharacterFeetStatus;
import ru.meat.game.model.player.CharacterTopStatus;
import ru.meat.game.model.player.Player;
import ru.meat.game.model.bodyData.BodyUserData;
import ru.meat.game.model.weapons.Weapon;
import ru.meat.game.model.weapons.WeaponEnum;

@Data
public class PlayerService {

  private Player player;

  private float moveMultiplier = 1f;

  private float feetStateTime;

  private float topStateTime;

  private float speed = PLAYER_MOVE_SPEED ;

  private float moveDirectionAngle = 0;

  private float modelFrontAngle = 0;

  public PlayerService(float x, float y) {
    player = new Player(x, y);
  }

  public void updateState() {
    if (CharacterFeetStatus.RUN.equals(player.getFeetStatus())) {
      feetStateTime += Gdx.graphics.getDeltaTime();
    }
    topStateTime += Gdx.graphics.getDeltaTime();

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
      System.out.println(player.getHp());
    }

    if (player.getHp() <= 0) {
      player.setDead(true);
    }
  }

  /**
   * Повернуть подельку за мышью
   *
   * @param camera отрисовывающая камера
   */
  public void rotateModel(OrthographicCamera camera) {
    Vector3 tmpVec3 = new Vector3();
    tmpVec3.set(Gdx.input.getX(), Gdx.input.getY(), 0);
    tmpVec3 = camera.unproject(tmpVec3);

    modelFrontAngle = MathUtils.radiansToDegrees * MathUtils.atan2(tmpVec3.y - getBodyPosY(),
        tmpVec3.x - getBodyPosX()) + 20;
  }

  public void drawPlayer(SpriteBatch batch) {
    if (player.isDead()) {
      drawDie(batch);
    } else {
      drawFeetSprite(batch);
      drawTopSprite(batch);
    }
  }

  private void drawDie(SpriteBatch batch) {
    Sprite sprite = new Sprite(player.getDiedAnimation().getKeyFrame(Gdx.graphics.getDeltaTime()));
    sprite.setX(getBodyPosX() * WORLD_TO_VIEW + (20f / player.getZoomMultiplier()) - sprite.getWidth() / 2);
    sprite.setY(getBodyPosY() * WORLD_TO_VIEW + (30f / player.getZoomMultiplier()) - sprite.getHeight() / 2);
    sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
    sprite.setRotation(modelFrontAngle - 20);
    sprite.draw(batch);
  }

  private void drawFeetSprite(SpriteBatch batch) {
    Sprite sprite = new Sprite(getActualFrame(feetStateTime));
    sprite.setX(
        player.getBody().getPosition().x * WORLD_TO_VIEW + (20f / player.getZoomMultiplier()) - sprite.getWidth() / 2);
    sprite.setY(
        player.getBody().getPosition().y * WORLD_TO_VIEW + (30f / player.getZoomMultiplier()) - sprite.getHeight() / 2);
    sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
    sprite.setRotation(modelFrontAngle - 20);
    sprite.draw(batch);
  }

  private void drawTopSprite(SpriteBatch batch) {
    Sprite sprite = new Sprite(getActualFrameTop(topStateTime));
    sprite.setX(player.getBody().getPosition().x * WORLD_TO_VIEW - sprite.getWidth() / 2);
    sprite.setY(player.getBody().getPosition().y * WORLD_TO_VIEW - sprite.getHeight() / 2);
    sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
    sprite.setRotation(modelFrontAngle - 20);
    sprite.draw(batch);
  }

  private Texture getActualFrame(float stateTime) {
    float v = modelFrontAngle + 360 + 20;
    v = angleMathAdd(v);
    if (angleMathAdd(45 + v) > moveDirectionAngle && moveDirectionAngle >= angleMathAdd(v - 45)) {
      return player.getRunAnimation().getKeyFrame(stateTime, true);
    } else if (angleMathAdd(135 + v) > moveDirectionAngle && moveDirectionAngle >= angleMathAdd(v + 45)) {
      return player.getStrafeRightAnimation().getKeyFrame(stateTime, true);
    } else if (angleMathAdd(v - 45) > moveDirectionAngle && moveDirectionAngle >= angleMathAdd(v - 135)) {
      return player.getStrafeLeftAnimation().getKeyFrame(stateTime, true);
    } else {
      return player.getRunAnimation().getKeyFrame(stateTime, true);
    }
  }

  private float angleMathAdd(float v) {
    if (v < 0) {
      return v + 360;
    } else if (v > 360) {
      return v - 360;
    } else if (v == 360) {
      return 0;
    }
    return v;
  }

  public Texture getActualFrameTop(float stateTime) {
    Weapon animationStack = getActualWeapon();
    if (player.getTopStatus() == CharacterTopStatus.MOVE) {
      return animationStack.getMoveAnimation().getKeyFrame(stateTime, true);
    } else if (player.getTopStatus() == CharacterTopStatus.RELOAD) {
      return animationStack.getReloadAnimation().getKeyFrame(stateTime, true);
    } else if (player.getTopStatus() == CharacterTopStatus.SHOOT) {
      return animationStack.getShootAnimation().getKeyFrame(stateTime, true);
    } else if (player.getTopStatus() == CharacterTopStatus.MELEE_ATTACK) {
      return animationStack.getMeleeAttackAnimation().getKeyFrame(stateTime, true);
    }
    return animationStack.getIdleAnimation().getKeyFrame(stateTime, true);
  }

  private void changeTopStatus(CharacterTopStatus status) {
    player.setTopStatus(status);
  }


  private void changeFeetStatus(CharacterFeetStatus status) {
    player.setFeetStatus(status);
  }

  /**
   * Стрелять
   *
   * @param cameraBox2D камера box2d
   */
  public void shoot(OrthographicCamera cameraBox2D) {
    Weapon weapon = getActualWeapon();
    if (weapon.getCurrentLockCounter() == 0
        || TimeUtils.timeSinceMillis(weapon.getCurrentLockCounter()) > weapon.getFireRate()
        * RpgStatsService.getInstance().getStats().getFireSpeed()) {
      weapon.setCurrentLockCounter(TimeUtils.millis());

      Vector3 point = new Vector3();
      point.set(Gdx.input.getX(), Gdx.input.getY(), 0);
      cameraBox2D.unproject(point);

      weapon.shoot(getBodyPosX(), getBodyPosY(), point.x, point.y, !player.getFeetStatus().equals(CharacterFeetStatus.IDLE));
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

      player.getBody().setTransform(getBodyPosX() + x / WORLD_TO_VIEW, getBodyPosY() + y / WORLD_TO_VIEW, 0);

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
  }

  /**
   * Сменить статус действия текстур
   */
  private void handleMovingStatus() {
    if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(
        Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.D)) {
      AudioService.getInstance().playStep();
      changeFeetStatus(CharacterFeetStatus.RUN);
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
    return speed * moveMultiplier * MAIN_ZOOM * RpgStatsService.getInstance().getStats().getMoveSpeed();
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
