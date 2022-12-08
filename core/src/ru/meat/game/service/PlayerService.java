package ru.meat.game.service;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import lombok.Data;
import ru.meat.game.model.CharacterFeetStatus;
import ru.meat.game.model.CharacterTopStatus;
import ru.meat.game.model.Player;
import ru.meat.game.model.weapons.Weapon;
import ru.meat.game.model.weapons.WeaponEnum;

@Data
public class PlayerService {

  /**
   * Положение игрока по Х
   */
  private float posX;
  /**
   * Положение игрока по У
   */
  private float posY;

  private float moveMultiplier = 1f;

  private float feetStateTime;

  private float topStateTime;

  private float speed = 1f;

  private float moveDirectionAngle = 0;

  private float modelFrontAngle = 0;

  public PlayerService(float x, float y) {
    initPlayer();
    posX = x;
    posY = y;
  }

  private Player player;

  public void updateStateTime() {
    if (CharacterFeetStatus.RUN.equals(player.getFeetStatus())) {
      feetStateTime += Gdx.graphics.getDeltaTime();
    }
    topStateTime += Gdx.graphics.getDeltaTime();
  }

  public void rotateModel() {
    float xInput = Gdx.input.getX();
    float yInput = (Gdx.graphics.getHeight() - Gdx.input.getY());

    modelFrontAngle = MathUtils.radiansToDegrees * MathUtils.atan2(yInput - posY, xInput - posX) + 20;
  }

  public void moveLeft() {
    posX = posX - (speed * moveMultiplier);
    moveDirectionAngle = 220;
  }

  public void moveRight() {
    posX = posX + (speed * moveMultiplier);
    moveDirectionAngle = 40;
  }

  public void moveUp() {
    posY = posY + (speed * moveMultiplier);
    moveDirectionAngle = 130;
  }

  public void moveDown() {
    posY = posY - (speed * moveMultiplier);
    moveDirectionAngle = 300;
  }

  public void drawPlayer(SpriteBatch batch) {
    drawFeetSprite(batch);
    drawTopSprite(batch);
  }

  private void drawFeetSprite(SpriteBatch batch) {
    Sprite sprite = new Sprite(getActualFrame(feetStateTime));
    sprite.setX(posX + (20f / player.getZoomMultiplier()));
    sprite.setY(posY + (30f / player.getZoomMultiplier()));
    sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);
    sprite.setRotation(modelFrontAngle-20);
    sprite.draw(batch);
  }

  private void drawTopSprite(SpriteBatch batch) {
    Sprite sprite = new Sprite(getActualFrameTop(topStateTime));
    sprite.setX(posX);
    sprite.setY(posY);
    sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
    sprite.setRotation(modelFrontAngle-20);
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
    Weapon animationStack = getAnimationStack();
    if (player.getTopStatus() == CharacterTopStatus.MOVE) {
      return animationStack.getMoveAnimation().getKeyFrame(stateTime, true);
    } else if (player.getTopStatus() == CharacterTopStatus.RELOAD) {
      return  animationStack.getReloadAnimation().getKeyFrame(stateTime, true);
    } else if (player.getTopStatus() == CharacterTopStatus.SHOOT) {
      return  animationStack.getShootAnimation().getKeyFrame(stateTime, true);
    } else if (player.getTopStatus() == CharacterTopStatus.MELEE_ATTACK) {
      return  animationStack.getMeleeAttackAnimation().getKeyFrame(stateTime, true);
    }
    return animationStack.getIdleAnimation().getKeyFrame(stateTime, true);
  }

  private Weapon getAnimationStack() {
    return player.getWeapons().stream().filter(x -> x.getName().equals(player.getCurrentWeapon())).findFirst().orElse(player.getWeapons().get(0));
  }

  private void initPlayer() {
    player = new Player();
  }

  public void changeTopStatus(CharacterTopStatus status) {
    player.setTopStatus(status);
  }


  public void changeFeetStatus(CharacterFeetStatus status) {
    player.setFeetStatus(status);
  }

  public void moveOnChangeMap(float x, float y) {
    posX += x;
    posY += y;
  }

  public void shoot() {
    player.setTopStatus(CharacterTopStatus.SHOOT);
  }

  public void changeWeapon(int i) {
    player.setCurrentWeapon(WeaponEnum.getByPos(i));
  }
}
