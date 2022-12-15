package ru.meat.game.service;

import com.badlogic.gdx.Gdx;
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
import ru.meat.game.utils.StaticFloats;

@Data
public class PlayerService {

  private final AudioService audioService;

  /**
   * Положение игрока по Х
   */
  private float posX;
  /**
   * Положение игрока по У
   */
  private float posY;

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

  private float speed = 1f;

  private float moveDirectionAngle = 0;

  private float modelFrontAngle = 0;

  public PlayerService(float x, float y, World world) {
    player = new Player(world);
    audioService = new AudioService();
    posX = x;
    posY = y;
  }

  private Player player;

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
    camera.unproject(tmpVec3);

    modelFrontAngle = MathUtils.radiansToDegrees * MathUtils.atan2(tmpVec3.y - centerY, tmpVec3.x - centerX) + 20;
  }

  public float moveLeft() {
    posX = posX - (speed * moveMultiplier);
    moveDirectionAngle = 220;
    return - (speed * moveMultiplier);
  }

  public float moveRight() {
    posX = posX + (speed * moveMultiplier);
    moveDirectionAngle = 40;
    return (speed * moveMultiplier);
  }

  public float moveUp() {
    posY = posY + (speed * moveMultiplier);
    moveDirectionAngle = 130;
    return (speed * moveMultiplier);
  }

  public float moveDown() {
    posY = posY - (speed * moveMultiplier);
    moveDirectionAngle = 300;
    return - (speed * moveMultiplier);
  }

  public void drawPlayer(SpriteBatch batch) {
    drawFeetSprite(batch);
    drawTopSprite(batch);
  }

  private void drawFeetSprite(SpriteBatch batch) {
    Sprite sprite = new Sprite(getActualFrame(feetStateTime));
    sprite.setX(posX + (20f / player.getZoomMultiplier()));
    sprite.setY(posY + (30f / player.getZoomMultiplier()));
    sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
    sprite.setRotation(modelFrontAngle - 20);
    sprite.draw(batch);
  }

  private void drawTopSprite(SpriteBatch batch) {
    Sprite sprite = new Sprite(getActualFrameTop(topStateTime));
    sprite.setX(posX);
    sprite.setY(posY);
    sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
    centerX = posX + sprite.getWidth() / 2;
    centerY = posY + sprite.getHeight() / 2;
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
    if (weapon.getCurrentLockCounter() == 0 || TimeUtils.timeSinceMillis(weapon.getCurrentLockCounter()) > weapon.getFireRate()) {
      weapon.setCurrentLockCounter(TimeUtils.millis());
      Vector3 tmpVec3 = new Vector3();
      tmpVec3.set(screenX, screenY, 0);
      camera.unproject(tmpVec3);
      weapon.shoot(posX / StaticFloats.WORLD_TO_VIEW,
          posY / StaticFloats.WORLD_TO_VIEW,
          tmpVec3.x / StaticFloats.WORLD_TO_VIEW,
          tmpVec3.y / StaticFloats.WORLD_TO_VIEW);
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
}
