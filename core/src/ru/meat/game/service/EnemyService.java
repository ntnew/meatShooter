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
import lombok.SneakyThrows;
import ru.meat.game.Box2dWorld;
import ru.meat.game.MyGame;
import ru.meat.game.model.enemies.Enemy;
import ru.meat.game.model.EnemyStatus;
import ru.meat.game.model.bodyData.BodyUserData;

@NoArgsConstructor
public class EnemyService {

  @Getter
  private final AtomicInteger rewardPointCount = new AtomicInteger(0);
  @Getter
  private final AtomicInteger killCount = new AtomicInteger(0);


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

//TODO сортировать врагов
//  public void sortEnemies() {
//    new Thread(() -> {
//      synchronized (enemies) {
//        enemies.sort((x, y) -> {
//          if (x.getStatus().equals(EnemyStatus.DIED) && !y.getStatus().equals(EnemyStatus.DIED)) {
//            return -1;
//          } else if ((x.getStatus().equals(EnemyStatus.DIED) && y.getStatus().equals(EnemyStatus.DIED))
//              || (!x.getStatus().equals(EnemyStatus.DIED) && !y.getStatus().equals(EnemyStatus.DIED))) {
//            return 0;
//          } else {
//            return 1;
//          }
//        });
//      }
//    }).start();
//  }

  public void addEnemy(Enemy enemy) {
    MyGame.getInstance().getGameZone().getSecondStage().addEnemy(enemy);
  }
}

