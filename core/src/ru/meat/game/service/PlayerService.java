package ru.meat.game.service;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import ru.meat.game.model.CharacterFeetStatus;
import ru.meat.game.model.CharacterTopStatus;
import ru.meat.game.model.Player;

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

  private float stateTime;

  private float speed = 1f;

  private float feetAngle = 0;

  private float topAngle = 0;

  public PlayerService() {
    initPlayer();
    posX = 500;
    posY = 500;
  }
  private Player player;

  public void updateStateTime(){
    if (CharacterFeetStatus.RUN.equals(player.getFeetStatus())){
      stateTime += Gdx.graphics.getDeltaTime();
    }

  }

  private void rotateFeetTo(float angle) {
    float v = (angle - feetAngle);
    v = v < 0 ? 360 + v : v;

    if (feetAngle != angle) {
      if (v <= 180) {
        if (feetAngle < 0) {
          feetAngle = 358;
        }
        feetAngle = feetAngle - 2;
      } else if (v > 180) {
        if (feetAngle > 360) {
          feetAngle = 2;
        }
        feetAngle = feetAngle + 2;
      }
    }
  }

  public void rotateTop(){
    float xInput = Gdx.input.getX();
    float yInput = (Gdx.graphics.getHeight() - Gdx.input.getY());

    topAngle = MathUtils.radiansToDegrees * MathUtils.atan2(yInput - posY, xInput - posX) + 20;
  }

  public void moveLeft() {
    posX = posX - (speed * moveMultiplier);
    rotateFeetTo(0);
  }

  public void moveRight() {
    posX = posX + (1 * moveMultiplier);
    rotateFeetTo(180);
  }

  public void moveUp() {
    posY = posY + (1 * moveMultiplier);
    rotateFeetTo(270);
  }

  public void moveDown() {
    posY = posY - (1 * moveMultiplier);
    rotateFeetTo(90);
  }

  public void drawPlayer(SpriteBatch batch) {
    drawFeetSprite(batch);
    drawTopSprite(batch);
  }

  private void drawFeetSprite(SpriteBatch batch) {
    Sprite sprite = new Sprite(getActualFrame(stateTime));
    sprite.setX(posX+(20f/player.getZoomMultiplier()));
    sprite.setY(posY+(30f/player.getZoomMultiplier()));
    sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
    sprite.setRotation(topAngle);
    sprite.draw(batch);
  }

  private void drawTopSprite(SpriteBatch batch) {
    Sprite sprite = new Sprite(getActualFrameTop(stateTime));
    sprite.setX(posX);
    sprite.setY(posY);
    sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
    sprite.setRotation(topAngle);
    sprite.draw(batch);
  }

  private Texture getActualFrame(float stateTime) {
//    if (player.getFeetStatus() == CharacterFeetStatus.WALK) {
//      return player.getWalkAnimation().getKeyFrame(stateTime, true);
//    } else if (player.getFeetStatus() == CharacterFeetStatus.RUN) {
//      return player.getRunAnimation().getKeyFrame(stateTime, true);
//    } else if (player.getFeetStatus() == CharacterFeetStatus.STRAFE_LEFT) {
//      return player.getStrafeLeftAnimation().getKeyFrame(stateTime, true);
//    } else if (player.getFeetStatus() == CharacterFeetStatus.STRAFE_RIGHT) {
//      return player.getStrafeRightAnimation().getKeyFrame(stateTime, true);
//    }
//    return player.getIdle().getKeyFrame(stateTime, true);

    return player.getRunAnimation().getKeyFrame(stateTime, true);
  }

  public Texture getActualFrameTop(float stateTime) {
    if (player.getTopStatus() == CharacterTopStatus.MOVE) {
      return player.getHandgunMoveAnimation().getKeyFrame(stateTime, true);
    } else if (player.getTopStatus() == CharacterTopStatus.RELOAD) {
      return player.getHandgunReloadAnimation().getKeyFrame(stateTime, true);
    } else if (player.getTopStatus() == CharacterTopStatus.SHOOT) {
      return player.getHandgunShootAnimation().getKeyFrame(stateTime, true);
    } else if (player.getTopStatus() == CharacterTopStatus.MELEE_ATTACK) {
      return player.getHandgunMeleeAttackAnimation().getKeyFrame(stateTime, true);
    }
    return player.getHandgunIdleAnimation().getKeyFrame(stateTime, true);
  }


  private void initPlayer() {
    player = new Player();
  }

  public float getPosX() {
    return posX;
  }

  public void setPosX(float posX) {
    this.posX = posX;
  }

  public float getPosY() {
    return posY;
  }

  public void setPosY(float posY) {
    this.posY = posY;
  }

  public void changeFeetStatus(CharacterFeetStatus status) {
    player.setFeetStatus(status);
  }

  public float getMoveMultiplier() {
    return moveMultiplier;
  }

  public void setMoveMultiplier(float moveMultiplier) {
    this.moveMultiplier = moveMultiplier;
  }

  public float getStateTime() {
    return stateTime;
  }

  public void setStateTime(float stateTime) {
    this.stateTime = stateTime;
  }

  public Player getPlayer() {
    return player;
  }

  public void setPlayer(Player player) {
    this.player = player;
  }

  public float getFeetAngle() {
    return feetAngle;
  }

  public void setFeetAngle(float feetAngle) {
    this.feetAngle = feetAngle;
  }

  public float getTopAngle() {
    return topAngle;
  }

  public void setTopAngle(float topAngle) {
    this.topAngle = topAngle;
  }

}
