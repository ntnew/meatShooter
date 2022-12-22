package ru.meat.game.service;

import static ru.meat.game.utils.Settings.MAIN_ZOOM;
import static ru.meat.game.utils.Settings.WORLD_TO_VIEW;

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
import ru.meat.game.model.CharacterFeetStatus;
import ru.meat.game.model.CharacterTopStatus;
import ru.meat.game.model.Player;
import ru.meat.game.model.weapons.Weapon;
import ru.meat.game.model.weapons.WeaponEnum;
import ru.meat.game.utils.Settings;

@Data
public class PlayerService {

  private final AudioService audioService;


  private Player player;

  /**
   * Положение игрока по Х
   */
  private float centerX;
  /**
   * Положение игрока по У
   */
  private float centerY;

  private float moveMultiplier = 1f;

  private float feetStateTime;

  private float topStateTime;

  private float speed = 1f * MAIN_ZOOM;

  private float moveDirectionAngle = 0;

  private float modelFrontAngle = 0;

  public PlayerService(float x, float y, World world) {
    player = new Player(world, x, y);
    audioService = new AudioService();
  }

  public void updateState() {
    if (CharacterFeetStatus.RUN.equals(player.getFeetStatus())) {
      feetStateTime += Gdx.graphics.getDeltaTime();
    }
    topStateTime += Gdx.graphics.getDeltaTime();

    getActualWeapon().updateState();
  }

  public void rotateModel(OrthographicCamera camera) {
    float xInput = Gdx.input.getX();
    float yInput = Gdx.input.getY();

    Vector3 tmpVec3 = new Vector3();
    tmpVec3.set(xInput, yInput, 0);
    tmpVec3 = camera.unproject(tmpVec3);

//    System.out.println( tmpVec3.x +  " z " +  centerX + "     f    "+tmpVec3.y + " z " +  centerY);
    modelFrontAngle = MathUtils.radiansToDegrees * MathUtils.atan2(tmpVec3.y - centerY, tmpVec3.x - centerX) + 20;
  }

  public float moveLeft() {
    player.setPosX(player.getPosX() - (speed * moveMultiplier));
    moveDirectionAngle = 220;
    return -(speed * moveMultiplier);
  }

  public float moveRight() {
    player.setPosX(player.getPosX() + (speed * moveMultiplier));

    moveDirectionAngle = 40;
    return (speed * moveMultiplier);
  }

  public float moveUp() {
    player.setPosY(player.getPosY() + (speed * moveMultiplier));
    player.getBody().setLinearVelocity(0, (speed * moveMultiplier));

    moveDirectionAngle = 130;
    return (speed * moveMultiplier);
  }

  public float moveDown() {
    player.setPosY(player.getPosY() - (speed * moveMultiplier));
    player.getBody().setLinearVelocity(0, -(speed * moveMultiplier));

    moveDirectionAngle = 300;
    return -(speed * moveMultiplier);
  }

  public void drawPlayer(SpriteBatch batch) {
    drawFeetSprite(batch);
    drawTopSprite(batch);
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
    centerX = player.getPosX() + sprite.getWidth() / 2;
    centerY = player.getPosY() + sprite.getHeight() / 2;
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

  public void changeTopStatus(CharacterTopStatus status) {
    player.setTopStatus(status);
  }


  public void changeFeetStatus(CharacterFeetStatus status) {
    player.setFeetStatus(status);
  }

  public void shoot(OrthographicCamera camera, float screenX, float screenY) {
    Weapon weapon = getActualWeapon();
    if (weapon.getCurrentLockCounter() == 0
        || TimeUtils.timeSinceMillis(weapon.getCurrentLockCounter()) > weapon.getFireRate()) {
      weapon.setCurrentLockCounter(TimeUtils.millis());
      Vector3 tmpVec3 = new Vector3();
      tmpVec3.set(screenX, screenY, 0);
      camera.unproject(tmpVec3);
      weapon.shoot(player.getBody().getPosition().x,
          player.getBody().getPosition().y -1,
          tmpVec3.x / Settings.WORLD_TO_VIEW,
          tmpVec3.y / Settings.WORLD_TO_VIEW);
      audioService.playShoot(weapon.getShootSound());
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

  public void handleKey(OrthographicCamera camera, OrthographicCamera cameraBox2D) {
    if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(
        Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.D)) {
      changeFeetStatus(CharacterFeetStatus.RUN);
      changeTopStatus(CharacterTopStatus.MOVE);
    } else {
      changeFeetStatus(CharacterFeetStatus.IDLE);
      changeTopStatus(CharacterTopStatus.IDLE);
    }
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

    player.getBody().setLinearVelocity(x, y);
    camera.position.set(player.getBody().getPosition().x *WORLD_TO_VIEW, player.getBody().getPosition().y *WORLD_TO_VIEW,0);
    cameraBox2D.position.set(player.getBody().getPosition().x, player.getBody().getPosition().y ,0);
    camera.update(false);
    cameraBox2D.update(false);
  }

  private float getSpeed() {
    return (speed * moveMultiplier);
  }

  public void drawBullets(SpriteBatch spriteBatch) {
    getActualWeapon().getBulletService().drawBullets(spriteBatch, player.getBody().getPosition().x, player.getBody().getPosition().y);
  }
}
