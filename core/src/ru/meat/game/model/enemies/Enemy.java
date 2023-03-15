package ru.meat.game.model.enemies;

import static ru.meat.game.settings.Constants.MAIN_ZOOM;

import com.badlogic.gdx.physics.box2d.Body;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.Skeleton;
import lombok.Data;
import ru.meat.game.model.EnemyStatus;
import ru.meat.game.model.FloatPair;

@Data
//@Builder
public class Enemy {

  private Body body;

  private AnimationState state;
  private Skeleton skeleton;

  private float actionPing;
  private Long actionCounter;

  /**
   * Радиус модельки бокс2д
   */
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

  /**
   * Угол поворота модельки, меняется в зависимости от направления движения
   */
  private float animationAngle = 0;

  /**
   * Очки вознаграждения за убийство
   */
  private int rewardPoint;

  /**
   * Прозрачность
   */
  private float transparency;

  public Enemy(float posX, float posY, float zoom, int hp, float speed, float actionPing, FloatPair playerCoord) {
    this.actionPing = actionPing;
    this.zoom = zoom;
    this.hp = hp;
    this.speed = speed;
    this.speedX = 0;
    this.speedY = 0;
    this.posX = posX;
    this.posY = posY;

    this.transparency = 1;

    this.status = EnemyStatus.IDLE;
    this.animationAngle = 0;

    if (playerCoord != null) {
      this.destination = playerCoord;
    } else {
      this.destination = new FloatPair(posX, posY);
      this.floatDestination = new FloatPair(posX, posY);
    }
  }

  public void doSomething(float posX, float posY) {
  }
}
