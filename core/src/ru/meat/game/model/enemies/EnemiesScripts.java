package ru.meat.game.model.enemies;

import static ru.meat.game.settings.Constants.MAIN_ZOOM;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.TimeUtils;
import lombok.SneakyThrows;
import ru.meat.game.Box2dWorld;
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
        BulletService.getInstance().createEnemyBullet(
            enemy.getBody().getPosition().x, enemy.getBody().getPosition().y,
            x, y, 0.2f, 40, 40 / MAIN_ZOOM, BulletType.ENEMY_COMMON, 0.25f
        );
      }
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

  /**
   * Метод действия врага простой идти на врага, при касании атаковать
   * <p>
   * для пауков
   *
   * @param x     x координата игрока
   * @param y     у координата игрока
   * @param enemy враг
   */
  public static void spiderActions(float x, float y, Enemy enemy) {
    if (enemy.getActionCounter() == null) {
      enemy.setActionCounter(TimeUtils.millis());
    }

    EnemyBodyUserData userData = (EnemyBodyUserData) enemy.getBody().getFixtureList().get(0).getUserData();

    if (userData.isNeedAttack()) {
      if (!enemy.getStatus().equals(EnemyStatus.ATTACK)) {
        enemy.setStatus(EnemyStatus.ATTACK);
        enemy.getState().setAnimation(0, "attack", false);
        enemy.getState().addAnimation(0, "walk", true, 0);
      }
      userData.setNeedAttack(false);
    }

    EnemyService.rotateModel(x - enemy.getBody().getPosition().x, y - enemy.getBody().getPosition().y, enemy);

    if (TimeUtils.timeSinceMillis(enemy.getActionCounter()) > 2000 && enemy.getStatus().equals(EnemyStatus.IDLE)
        && !userData.isNeedAttack()) {
      enemy.setActionCounter(TimeUtils.millis());
      enemy.setStatus(EnemyStatus.MOVE);
      if (!enemy.getStatus().equals(EnemyStatus.MOVE) && enemy.getState().getTracks().isEmpty()) {
        enemy.getState().setAnimation(0, "walk", true);
      }
    } else if (TimeUtils.timeSinceMillis(enemy.getActionCounter()) > 2000 && !enemy.getStatus()
        .equals(EnemyStatus.IDLE)) {
      enemy.setActionCounter(TimeUtils.millis());
      enemy.setStatus(EnemyStatus.IDLE);
    }

    if (enemy.getStatus().equals(EnemyStatus.MOVE)) {
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
}
