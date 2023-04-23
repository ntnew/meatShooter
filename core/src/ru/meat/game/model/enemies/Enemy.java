package ru.meat.game.model.enemies;

import static ru.meat.game.settings.Constants.MAIN_ZOOM;
import static ru.meat.game.settings.Constants.WORLD_TO_VIEW;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.Skeleton;
import java.util.function.BiFunction;
import lombok.Data;
import lombok.SneakyThrows;
import ru.meat.game.Box2dWorld;
import ru.meat.game.MyGame;
import ru.meat.game.model.EnemyStatus;
import ru.meat.game.model.FloatPair;
import ru.meat.game.model.bodyData.BodyUserData;
import ru.meat.game.model.player.PlayerService;
import ru.meat.game.service.AudioService;

@Data
//@Builder
public class Enemy extends Image {

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
  private float speedX;
  /**
   * размер шага по оси У, меняется постоянно
   */
  private float speedY;
  /**
   * Позиции
   */
  private volatile Float posX;
  private volatile Float posY;

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
  private float animationAngle;

  /**
   * Очки вознаграждения за убийство
   */
  private int rewardPoint;

  /**
   * Прозрачность
   */
  private float transparency;

  private Boolean needCreateBullet = false;
  private Long timestampFromAttackBegin;

  private BiFunction<Enemy, FloatPair, Boolean> enemyScript;

  public Enemy(float posX, float posY, int hp, float speed, float actionPing, FloatPair playerCoord) {
    this.actionPing = actionPing;
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
    enemyScript.apply(this, new FloatPair(posX, posY));
  }

  @Override
  public void act(float delta) {
    state.apply(skeleton);
    skeleton.updateWorldTransform();

    if (!status.equals(EnemyStatus.IDLE)) {
      state.update(Gdx.graphics.getDeltaTime());
    }

    if (hp <= 0 && !status.equals(EnemyStatus.DIED)) {
      status = EnemyStatus.DIED;
      Gdx.app.postRunnable(() -> body.setActive(false));
      state.setAnimation(0, "dead", false);
    }

    if (!status.equals(EnemyStatus.DIED) && body != null) {
      body.setAwake(true);
      updateEnemyHp();
      updateEnemyPos();
      enemyScript.apply(this,
          new FloatPair(PlayerService.getInstance().getBodyPosX(), PlayerService.getInstance().getBodyPosY()));
      skeleton.setPosition(posX * WORLD_TO_VIEW, posY * WORLD_TO_VIEW);
    } else if (body != null && !body.getFixtureList().isEmpty()) {
      Gdx.app.postRunnable(() -> {
        if (this != null && body != null) {
          Box2dWorld.getInstance().destroyBody(body);
          body = null;
          AudioService.getInstance().playEnemyDie();
        }
      });
      new ThreadForTransparency(this).start();
//      rewardPointCount.set(rewardPointCount.get() + enemy.getRewardPoint());
//      killCount.set(killCount.get() + 1);
    }

//    ((TextureRegionDrawable) getDrawable()).setRegion(
//        new TextureRegion(animation.getKeyFrame(stateTime += delta, false)));
    super.act(delta);
  }

  private void updateEnemyPos() {
    posX = body.getPosition().x;
    posY = body.getPosition().y;
  }

  private void updateEnemyHp() {
    if (body.getFixtureList().get(0).getUserData() instanceof BodyUserData) {
      BodyUserData userData = (BodyUserData) body.getFixtureList().get(0).getUserData();
      if (userData != null && userData.getDamage() != 0) {
        hp = hp - userData.getDamage();
        userData.setDamage(0);
      }
    }
  }

  @Override
  public void draw(Batch batch, float parentAlpha) {
    MyGame.getInstance().getGameZone().getRenderer().draw(batch, skeleton);
  }

  class ThreadForTransparency extends Thread {

    final Enemy enemy;

    ThreadForTransparency(Enemy enemy) {
      this.enemy = enemy;
    }

    @SneakyThrows
    @Override
    public void run() {
      while (enemy.getTransparency() > 0.5f) {
        enemy.setTransparency(enemy.getTransparency() - 0.05f);
        Thread.sleep(100);
      }
    }
  }
}
