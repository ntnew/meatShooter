package ru.meat.game.model.enemies;

import static ru.meat.game.settings.Constants.MAIN_ZOOM;

import com.badlogic.gdx.physics.box2d.Body;
import lombok.Data;
import ru.meat.game.model.EnemyStatus;
import ru.meat.game.model.FloatPair;

@Data
//@Builder
public class Enemy {

  private Body body;

  private float enemyPing = 100;
  private float enemyPingCounter = 0;

  private float radius;

  /**
   * Направление
   */
  private FloatPair destination;

  /**
   * Направление
   */
  private FloatPair floatDestination;


  /**
   * скорость поворота
   */
  private FloatPair turnSpeed = new FloatPair(1f * MAIN_ZOOM, 1f * MAIN_ZOOM);


  /**
   * множитель на центр
   */
  private FloatPair centerMultip = new FloatPair(1f, 1f);

  /**
   * делитель размера модельки
   */
  private float zoom;

  /**
   * Здоровье
   */
  private float hp = 1;

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

  /**
   * Урон врага
   */
  private int attack;
  /**
   * Скорость атаки секунд длится одна атака
   */
  private Double attackSpeed;

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

  /**
   * Очки вознаграждения за убийство
   */
  private int rewardPoint;

  public Enemy(float posX, float posY, float zoom, int hp, float speed,
      float animationAngle, float enemyPing, FloatPair playerCoord) {
    this.enemyPing = enemyPing;
    this.zoom = zoom;
    this.hp = hp;
    this.speed = speed;
    this.speedX = 0;
    this.speedY = 0;
    this.posX = posX;
    this.posY = posY;

    this.status = EnemyStatus.IDLE;
    this.animationAngle = animationAngle;

    if (playerCoord != null) {
      this.destination = playerCoord;
    } else {
      this.destination = new FloatPair(posX, posY);
      this.floatDestination = new FloatPair(posX, posY);
      this.enemyPingCounter = this.enemyPing;
    }
  }

}
