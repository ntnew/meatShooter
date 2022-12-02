package ru.meat.game.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import java.io.IOException;
import ru.meat.game.utils.FilesUtils;

public class Enemy {

  private float attackRange = 40;
  private float enemyPing = 10;

  private int zoom = 3;

  private int hp;

  private float speed = 0.7f;

  private float speedX = 1;
  private float speedY = 1;

  private float posX;
  private float posY;

  private Animation<Texture> walkAnimation;
  private Animation<Texture> idle;
  private Animation<Texture> attack;

  private EnemyStatus status;

  private final float frameDuration = 0.05f;
  private final float attackFrameDuration = 0.18f;

  private float animationAngle = 0;

  public Enemy(float posX, float posY) {
    this.posX = posX;
    this.posY = posY;
    try {
      attack = FilesUtils.initAnimationFrames("./assets/export/attack/", zoom, frameDuration);
      walkAnimation = FilesUtils.initAnimationFrames("./assets/export/move/", zoom, frameDuration);
      idle = FilesUtils.initAnimationFrames("./assets/export/idle/", zoom, frameDuration);
      idle.setPlayMode(PlayMode.LOOP);
      walkAnimation.setPlayMode(PlayMode.LOOP);
      attack.setPlayMode(PlayMode.NORMAL);
      hp = 100;
      status = EnemyStatus.IDLE;
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public Enemy() {
    try {
      attack = FilesUtils.initAnimationFrames("./assets/export/attack/", zoom, attackFrameDuration);
      walkAnimation = FilesUtils.initAnimationFrames("./assets/export/move/", zoom, frameDuration);
      idle = FilesUtils.initAnimationFrames("./assets/export/idle/", zoom, frameDuration);
      idle.setPlayMode(PlayMode.LOOP);
      walkAnimation.setPlayMode(PlayMode.LOOP);
      hp = 100;
      status = EnemyStatus.IDLE;
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void setPosition(float x, float y) {
    posX = x;
    posY = y;
  }


  public int getZoom() {
    return zoom;
  }

  public void setZoom(int zoom) {
    this.zoom = zoom;
  }

  public int getHp() {
    return hp;
  }

  public void setHp(int hp) {
    this.hp = hp;
  }

  public Animation<Texture> getWalkAnimation() {
    return walkAnimation;
  }

  public void setWalkAnimation(Animation<Texture> walkAnimation) {
    this.walkAnimation = walkAnimation;
  }

  public Animation<Texture> getIdle() {
    return idle;
  }

  public void setIdle(Animation<Texture> idle) {
    this.idle = idle;
  }

  public Animation<Texture> getAttack() {
    return attack;
  }

  public void setAttack(Animation<Texture> attack) {
    this.attack = attack;
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

  public EnemyStatus getStatus() {
    return status;
  }

  public void setStatus(EnemyStatus status) {
    this.status = status;
  }

  public float getFrameDuration() {
    return frameDuration;
  }

  public float getAnimationAngle() {
    return animationAngle;
  }

  public void setAnimationAngle(float animationAngle) {
    this.animationAngle = animationAngle;
  }

  public float getSpeed() {
    return speed;
  }

  public void setSpeed(float speed) {
    this.speed = speed;
  }

  public float getEnemyPing() {
    return enemyPing;
  }

  public void setEnemyPing(float enemyPing) {
    this.enemyPing = enemyPing;
  }

  public float getSpeedX() {
    return speedX;
  }

  public void setSpeedX(float speedX) {
    this.speedX = speedX;
  }

  public float getSpeedY() {
    return speedY;
  }

  public void setSpeedY(float speedY) {
    this.speedY = speedY;
  }

  public float getAttackRange() {
    return attackRange;
  }

  public void setAttackRange(float attackRange) {
    this.attackRange = attackRange;
  }
}
