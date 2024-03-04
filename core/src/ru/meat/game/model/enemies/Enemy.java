package ru.meat.game.model.enemies;

import static ru.meat.game.settings.Constants.WORLD_TO_VIEW;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Body;
import com.esotericsoftware.spine.utils.SkeletonActor;
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
public class Enemy extends SkeletonActor {

  private Body body;

  private float actionPing;
  private Long actionCounter;

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
  private float transparency = 1;

  private Boolean needCreateBullet = false;
  private Long timestampFromAttackBegin;

  private BiFunction<Enemy, FloatPair, Boolean> enemyScript;

  private Boolean needDispose = false;

  public Enemy(int hp, float speed, float actionPing, Body body) {
    this.actionPing = actionPing;
    this.hp = hp;
    this.speed = speed;
    this.speedX = 0;
    this.speedY = 0;
    this.body = body;

    this.status = EnemyStatus.IDLE;
    this.animationAngle = 0;
    setRenderer(MyGame.getInstance().getGameZone().getRenderer());
  }

  @Override
  public void act(float delta) {
    if (!status.equals(EnemyStatus.IDLE)) {
      getAnimationState().update(Gdx.graphics.getDeltaTime());
    }

    if (hp <= 0 && !status.equals(EnemyStatus.DIED)) {
      status = EnemyStatus.DIED;
      Gdx.app.postRunnable(() -> body.setActive(false));
      getAnimationState().setAnimation(0, "dead", false);
    }

    if (!status.equals(EnemyStatus.DIED) && body != null) {
      body.setAwake(true);
      updateEnemyHp();
      enemyScript.apply(this,
          new FloatPair(PlayerService.getInstance().getBodyPosX(), PlayerService.getInstance().getBodyPosY()));
      setPosition( body.getPosition().x * WORLD_TO_VIEW, body.getPosition().y * WORLD_TO_VIEW);
    } else if (body != null && !body.getFixtureList().isEmpty()) {
      handleDeath();
    }

    if (needDispose){
      remove();
      setSkeleton(null);
      setAnimationState(null);
    } else {
      super.act(delta);
    }
  }

  private void handleDeath() {
    Gdx.app.postRunnable(() -> {
      if (body != null) {
        Box2dWorld.getInstance().destroyBody(body);
        body = null;
        AudioService.getInstance().playEnemyDie();
      }
    });
    new ThreadForTransparency().start();
    MyGame.getInstance().getGameZone().getEnemyService().getKillCount().incrementAndGet();
    MyGame.getInstance().getGameZone().getEnemyService().getRewardPointCount().addAndGet(this.getRewardPoint());
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

  class ThreadForTransparency extends Thread {


    ThreadForTransparency() {
      setDaemon(true);
    }

    @SneakyThrows
    @Override
    public void run() {
      while (getSkeleton().getColor().a > 0.05f) {
        getSkeleton().getColor().a = getSkeleton().getColor().a - 0.05f;
        Thread.sleep(100);
      }
      needDispose = true;
    }
  }
}
