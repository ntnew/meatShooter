package ru.meat.game.model.enemies;

import static ru.meat.game.settings.Constants.MAIN_ZOOM;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.TimeUtils;
import lombok.SneakyThrows;
import ru.meat.game.model.EnemyStatus;
import ru.meat.game.model.weapons.BulletType;
import ru.meat.game.service.BulletService;
import ru.meat.game.service.EnemyService;
import ru.meat.game.utils.GDXUtils;

public class EnemiesScripts {

  public static void blackWidowActions(Enemy enemy, float x, float y) {
    if (enemy.getActionCounter() == null) {
      enemy.setActionCounter(TimeUtils.millis());
    }

    if (TimeUtils.timeSinceMillis(enemy.getActionCounter()) > 3000 && !enemy.getStatus().equals(EnemyStatus.ATTACK)) {
      enemy.setActionCounter(TimeUtils.millis());
      enemy.setStatus(EnemyStatus.ATTACK);
      enemy.getState().setAnimation(0, "attack", false);
      new ThreadForDelayFoeBullet(enemy, x, y).start();
    } else if (TimeUtils.timeSinceMillis(enemy.getActionCounter()) > 1000 && enemy.getStatus()
        .equals(EnemyStatus.ATTACK)) {
      enemy.setActionCounter(TimeUtils.millis());
      enemy.setStatus(EnemyStatus.MOVE);
      enemy.getState().setAnimation(0, "walk", true);
    }

    EnemyService.rotateModel(x - enemy.getBody().getPosition().x, y - enemy.getBody().getPosition().y, enemy);
    if (!enemy.getStatus().equals(EnemyStatus.ATTACK)) {
      float catetPrilezjaschiy = x - enemy.getBody().getPosition().x;
      float catetProtivo = y - enemy.getBody().getPosition().y;
      float gip = GDXUtils.calcGipotenuza(catetPrilezjaschiy, catetProtivo);
      float sin = catetProtivo / gip;
      float cos = catetPrilezjaschiy / gip;
      enemy.setSpeedY(sin * enemy.getSpeed());
      enemy.setSpeedX(cos * enemy.getSpeed());
      enemy.getBody().setTransform(enemy.getBody().getPosition().x + enemy.getSpeedX(),
          enemy.getSpeedY() + enemy.getBody().getPosition().y, 0);
    }
  }


  /**
   * Метод действия врага простой идти на врага, при касании атаковать
   * <p>
   * для пауков и маленького жучка
   *
   * @param x     x координата игрока
   * @param y     у координата игрока
   * @param enemy враг
   */
  public static void simpleActions(float x, float y, Enemy enemy) {
    EnemyBodyUserData userData = (EnemyBodyUserData) enemy.getBody().getFixtureList().get(0).getUserData();

    if (userData.isNeedAttack()) {
      if (!enemy.getStatus().equals(EnemyStatus.ATTACK)) {
        enemy.setStatus(EnemyStatus.ATTACK);
        enemy.getState().setAnimation(0, "attack", false);
        enemy.getState().addAnimation(0, "walk", true, 0);
      }
      userData.setNeedAttack(false);
    } else {
      if (!enemy.getStatus().equals(EnemyStatus.MOVE) && enemy.getState().getTracks().isEmpty()) {
        enemy.getState().setAnimation(0, "walk", true);
      }
      enemy.setStatus(EnemyStatus.MOVE);
    }

    EnemyService.rotateModel(x - enemy.getBody().getPosition().x, y - enemy.getBody().getPosition().y, enemy);

    float catetPrilezjaschiy = x - enemy.getBody().getPosition().x;
    float catetProtivo = y - enemy.getBody().getPosition().y;
    float gip = GDXUtils.calcGipotenuza(catetPrilezjaschiy, catetProtivo);
    float sin = catetProtivo / gip;
    float cos = catetPrilezjaschiy / gip;
    enemy.setSpeedY(sin * enemy.getSpeed());
    enemy.setSpeedX(cos * enemy.getSpeed());
    enemy.getBody().setTransform(enemy.getBody().getPosition().x + enemy.getSpeedX(),
        enemy.getSpeedY() + enemy.getBody().getPosition().y, 0);
  }


  static class ThreadForDelayFoeBullet extends Thread {

    private final Enemy enemy;
    private final float x;
    private final float y;

    public ThreadForDelayFoeBullet(Enemy enemy, float x, float y) {
      this.enemy = enemy;
      this.x = x;
      this.y = y;
    }

    @SneakyThrows
    @Override
    public void run() {
      Thread.sleep(300);
      Gdx.app.postRunnable(() -> {
        if (enemy.getBody() != null) {
          BulletService.getInstance().createEnemyBullet(
              enemy.getBody().getPosition().x, enemy.getBody().getPosition().y,
              x, y, 0.2f, 40, 40 / MAIN_ZOOM, BulletType.ENEMY_COMMON, 0.25f
          );
        }
      });
    }
  }
}
