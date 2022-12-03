package ru.meat.game.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.physics.box2d.Body;
import lombok.Builder;
import lombok.Data;
import ru.meat.game.utils.FilesUtils;

@Data
//@Builder
public class Enemy {

  private Body box;

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
}
