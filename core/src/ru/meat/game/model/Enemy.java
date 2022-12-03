package ru.meat.game.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import ru.meat.game.utils.FilesUtils;

public class Enemy {

  /**
   * Дальность атаки
   */
  private float attackRange = 40;

  private float enemyPing = 100;
  private float enemyPingCounter = 0;

  /**
   * Направление
   */
  private PairOfFloat destination;

  /**
   * Направление
   */
  private PairOfFloat floatDestination;


  /**
   * скорость поворота
   */
  private PairOfFloat turnSpeed = new PairOfFloat(2f,2f);

  /**
   * делитель размера модельки
   */
  private int zoom = 3;

  /**
   * Здоровье
   */
  private int hp = 1;

  /**
   * Максимальная скорость
   */
  private float speed = 0.7f;

  /**
   * размер шага по оси X, меняется постоянно
   */
  private float speedX = 1;
  /**
   * размер шага по оси У, меняется постоянно
   */
  private float speedY = 1;
  /**
   * Позиции
   */
  private float posX = 50;
  private float posY = 50;

  private Animation<Texture> walkAnimation;
  private Animation<Texture> idleAnimation;
  private Animation<Texture> attackAnimation;
  private Animation<Texture> dieAnimation;

  /**
   * Статус действия модельки
   */
  private EnemyStatus status;

  private final float frameDuration = 0.05f;
  private final float attackFrameDuration = 0.1f;


  /**
   * Угол поворота модельки, меняется в зависимости от направления движения
   */
  private float animationAngle = 0;

  public Enemy(float posX, float posY) {
    this.posX = posX;
    this.posY = posY;
    attackAnimation = FilesUtils.initAnimationFrames("./assets/export/attack/", zoom, frameDuration);
    walkAnimation = FilesUtils.initAnimationFrames("./assets/export/move/", zoom, frameDuration);
    idleAnimation = FilesUtils.initAnimationFrames("./assets/export/idle/", zoom, frameDuration);
    idleAnimation.setPlayMode(PlayMode.LOOP);
    walkAnimation.setPlayMode(PlayMode.LOOP);
    attackAnimation.setPlayMode(PlayMode.NORMAL);
    hp = 100;
    status = EnemyStatus.IDLE;
  }


  public Enemy(float posX, float posY, float attackRange, int zoom, int hp, float speed,
      String pathToWalkAnimation, String pathToIdleAnimation, String pathToAttackAnimation, String pathToDieAnimation,
      float animationAngle, float enemyPing, PairOfFloat playerCoord) {
    this.attackRange = attackRange;
    this.enemyPing = enemyPing;
    this.zoom = zoom;
    this.hp = hp;
    this.speed = speed;
    this.speedX = 0;
    this.speedY = 0;
    this.posX = posX;
    this.posY = posY;
    this.attackAnimation = FilesUtils.initAnimationFrames(pathToAttackAnimation, zoom, attackFrameDuration);
    this.walkAnimation = FilesUtils.initAnimationFrames(pathToWalkAnimation, zoom, frameDuration);
    this.idleAnimation = FilesUtils.initAnimationFrames(pathToIdleAnimation, zoom, frameDuration);
    this.dieAnimation = FilesUtils.initAnimationFrames(pathToDieAnimation, zoom, frameDuration);
    this.status = EnemyStatus.IDLE;
    this.animationAngle = animationAngle;

    idleAnimation.setPlayMode(PlayMode.LOOP);
    walkAnimation.setPlayMode(PlayMode.LOOP);
    attackAnimation.setPlayMode(PlayMode.NORMAL);
    if (playerCoord != null) {
      this.destination = playerCoord;
    } else {
      this.destination = new PairOfFloat(posX, posY);
      this.floatDestination = new PairOfFloat(posX, posY);
      this.enemyPingCounter = this.enemyPing;
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

  public Animation<Texture> getIdleAnimation() {
    return idleAnimation;
  }

  public void setIdleAnimation(Animation<Texture> idleAnimation) {
    this.idleAnimation = idleAnimation;
  }

  public Animation<Texture> getAttackAnimation() {
    return attackAnimation;
  }

  public void setAttackAnimation(Animation<Texture> attackAnimation) {
    this.attackAnimation = attackAnimation;
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

  public Animation<Texture> getDieAnimation() {
    return dieAnimation;
  }

  public void setDieAnimation(Animation<Texture> dieAnimation) {
    this.dieAnimation = dieAnimation;
  }

  public float getAttackFrameDuration() {
    return attackFrameDuration;
  }

  public float getEnemyPingCounter() {
    return enemyPingCounter;
  }

  public void setEnemyPingCounter(float enemyPingCounter) {
    this.enemyPingCounter = enemyPingCounter;
  }

  public PairOfFloat getDestination() {
    return destination;
  }

  public void setDestination(PairOfFloat destination) {
    this.destination = destination;
  }

  public PairOfFloat getTurnSpeed() {
    return turnSpeed;
  }

  public void setTurnSpeed(PairOfFloat turnSpeed) {
    this.turnSpeed = turnSpeed;
  }

  public PairOfFloat getFloatDestination() {
    return floatDestination;
  }

  public void setFloatDestination(PairOfFloat floatDestination) {
    this.floatDestination = floatDestination;
  }
}
