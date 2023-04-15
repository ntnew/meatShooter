package ru.meat.game.model.enemies.scripts;

import static ru.meat.game.settings.Constants.MAIN_ZOOM;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.TimeUtils;
import java.util.function.BiFunction;
import ru.meat.game.Box2dWorld;
import ru.meat.game.model.EnemyStatus;
import ru.meat.game.model.FloatPair;
import ru.meat.game.model.enemies.Enemy;
import ru.meat.game.model.weapons.BulletType;
import ru.meat.game.service.BulletService;
import ru.meat.game.service.EnemyService;
import ru.meat.game.utils.GDXUtils;

public class BlackWidowScripts {

  public static BiFunction<Enemy, FloatPair, Boolean> blackWidow() {
    return ((enemy, playerPos) -> {
      if (enemy.getActionCounter() == null) {
        enemy.setActionCounter(TimeUtils.millis());
      }

      if (TimeUtils.timeSinceMillis(enemy.getActionCounter()) > 4000 && !enemy.getStatus().equals(EnemyStatus.ATTACK)) {
        enemy.setActionCounter(TimeUtils.millis());
        enemy.setStatus(EnemyStatus.ATTACK);
        enemy.getState().setAnimation(0, "attack", false);
        enemy.setNeedCreateBullet(true);
        enemy.setTimestampFromAttackBegin(TimeUtils.millis());
      } else if (TimeUtils.timeSinceMillis(enemy.getActionCounter()) > 1000 && enemy.getStatus()
          .equals(EnemyStatus.ATTACK)) {
        enemy.setActionCounter(TimeUtils.millis());
        enemy.setStatus(EnemyStatus.MOVE);
        enemy.getState().setAnimation(0, "walk", true);
      }
      if (enemy.getNeedCreateBullet() && TimeUtils.timeSinceMillis(enemy.getTimestampFromAttackBegin()) > 300) {
        enemy.setNeedCreateBullet(false);
        if (enemy.getBody() != null && Box2dWorld.getInstance() != null) {
          Gdx.app.postRunnable(() ->
              BulletService.getInstance().createEnemyBullet(
                  enemy.getBody().getPosition().x, enemy.getBody().getPosition().y,
                  playerPos.getX(), playerPos.getY(), 0.2f, 40, 40 / MAIN_ZOOM, BulletType.ENEMY_COMMON, 0.25f
              ));
        }
      }

      EnemyService.rotateModel(playerPos.getX() - enemy.getBody().getPosition().x,
          playerPos.getY() - enemy.getBody().getPosition().y, enemy);
      if (!enemy.getStatus().equals(EnemyStatus.ATTACK)) {
        float catetPrilezjaschiy = playerPos.getX() - enemy.getBody().getPosition().x;
        float catetProtivo = playerPos.getY() - enemy.getBody().getPosition().y;
        float gip = GDXUtils.calcGipotenuza(catetPrilezjaschiy, catetProtivo);
        float sin = catetProtivo / gip;
        float cos = catetPrilezjaschiy / gip;
        enemy.setSpeedY(sin * enemy.getSpeed());
        enemy.setSpeedX(cos * enemy.getSpeed());
        synchronized (enemy.getBody()) {
          Gdx.app.postRunnable(() -> {
            if (enemy.getBody() != null && enemy.getBody().isActive()) {
              enemy.getBody().setTransform(enemy.getBody().getPosition().x + enemy.getSpeedX(),
                  enemy.getSpeedY() + enemy.getBody().getPosition().y, 0);
            }
          });
        }
      }
      return true;
    });
  }
}
