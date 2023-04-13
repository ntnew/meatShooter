package ru.meat.game.service;

import static ru.meat.game.settings.Constants.WORLD_TO_VIEW;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.SkeletonRenderer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;
import ru.meat.game.Box2dWorld;
import ru.meat.game.model.enemies.Enemy;
import ru.meat.game.model.EnemyStatus;
import ru.meat.game.model.bodyData.BodyUserData;
import ru.meat.game.model.enemies.EnemyBodyUserData;
import ru.meat.game.utils.GDXUtils;

@NoArgsConstructor
public class EnemyService {

  @Getter
  private final List<Enemy> enemies = new ArrayList<>();
  @Getter
  private final AtomicInteger rewardPointCount = new AtomicInteger(0);
  @Getter
  private final AtomicInteger killCount = new AtomicInteger(0);

  public static void updateEnemyHp(Enemy enemy) {
    BodyUserData userData = (BodyUserData) enemy.getBody().getFixtureList().get(0).getUserData();
    if (userData != null && userData.getDamage() != 0) {
      enemy.setHp(enemy.getHp() - userData.getDamage());
      userData.setDamage(0);
    }
  }

  /**
   * Установить угол поворота для модельки врага на точку
   *
   * @param x координата
   * @param y координата добавлено 10 градусов, чтобы можелька чуть более на ццентр игрока смотрела
   */
  public static void rotateModel(float x, float y, Enemy enemy) {
    float v = MathUtils.radiansToDegrees * MathUtils.atan2(y, x);
    enemy.setAnimationAngle(v + 10);
    enemy.getSkeleton().getRootBone().setRotation(v - 90);
  }

  /**
   * отработать действия врагов
   *
   * @param posX x координата игрока
   * @param posY y координата игрока
   */
  public void actionEnemies(float posX, float posY) {
    synchronized (enemies) {
      for (Enemy enemy : enemies) {
        enemy.getState().apply(enemy.getSkeleton());
        enemy.getSkeleton().updateWorldTransform();

        if (!enemy.getStatus().equals(EnemyStatus.IDLE)) {
          enemy.getState().update(Gdx.graphics.getDeltaTime());
        }

        if (enemy.getHp() <= 0 && !enemy.getStatus().equals(EnemyStatus.DIED)) {
          enemy.setStatus(EnemyStatus.DIED);
          enemy.getBody().setActive(false);
          enemy.getState().setAnimation(0, "dead", false);
        }
        if (!enemy.getStatus().equals(EnemyStatus.DIED)) {
          enemy.getBody().setAwake(true);
          updateEnemyHp(enemy);
          updateEnemyPos(enemy);
          enemy.doSomething(posX, posY);
        } else if (enemy.getBody() != null && !enemy.getBody().getFixtureList().isEmpty()) {
          Box2dWorld.getInstance().getWorld().destroyBody(enemy.getBody());
          enemy.setBody(null);
          new ThreadForTransparency(enemy).start();
          AudioService.getInstance().playEnemyDie();
          rewardPointCount.set(rewardPointCount.get() + enemy.getRewardPoint());
          killCount.set(killCount.get() + 1);
        }
      }
    }
  }

  private void updateEnemyPos(Enemy enemy) {
    enemy.setPosX(enemy.getBody().getPosition().x);
    enemy.setPosY(enemy.getBody().getPosition().y);
  }

  public void drawEnemies(Batch spriteBatch, SkeletonRenderer renderer) {
    synchronized (enemies) {
      for (Enemy enemy : enemies) {
        drawSpineAni(spriteBatch, enemy, renderer);
      }
    }
  }

  private void drawSpineAni(Batch batch, Enemy enemy, SkeletonRenderer renderer) {
    enemy.getSkeleton().getColor().a = enemy.getTransparency();
    enemy.getSkeleton().setPosition(enemy.getPosX() * WORLD_TO_VIEW, enemy.getPosY() * WORLD_TO_VIEW);
    renderer.draw(batch, enemy.getSkeleton());
  }

  public void sortEnemies() {
    new Thread(() -> {
      synchronized (enemies) {
        enemies.sort((x, y) -> {
          if (x.getStatus().equals(EnemyStatus.DIED) && !y.getStatus().equals(EnemyStatus.DIED)) {
            return -1;
          } else if ((x.getStatus().equals(EnemyStatus.DIED) && y.getStatus().equals(EnemyStatus.DIED))
              || (!x.getStatus().equals(EnemyStatus.DIED) && !y.getStatus().equals(EnemyStatus.DIED))) {
            return 0;
          } else {
            return 1;
          }
        });
      }
    }).start();
  }

  public void addEnemy(Enemy enemy) {
    synchronized (enemies) {
      enemies.add(enemy);
    }
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

