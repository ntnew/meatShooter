package ru.meat.game.model.enemies.scripts;

import java.util.function.BiFunction;
import ru.meat.game.model.EnemyStatus;
import ru.meat.game.model.FloatPair;
import ru.meat.game.model.enemies.Enemy;
import ru.meat.game.model.enemies.EnemyBodyUserData;
import ru.meat.game.service.EnemyService;
import ru.meat.game.utils.GDXUtils;

public class LittleBugScripts {

  public static BiFunction<Enemy, FloatPair, Boolean> littleBugActions() {
    return ((enemy, floatPair) -> {
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

      EnemyService.rotateModel(floatPair.getX() - enemy.getBody().getPosition().x, floatPair.getY() - enemy.getBody().getPosition().y, enemy);

      float catetPrilezjaschiy = floatPair.getX() - enemy.getBody().getPosition().x;
      float catetProtivo = floatPair.getY() - enemy.getBody().getPosition().y;
      float gip = GDXUtils.calcGipotenuza(catetPrilezjaschiy, catetProtivo);
      float sin = catetProtivo / gip;
      float cos = catetPrilezjaschiy / gip;
      enemy.setSpeedY(sin * enemy.getSpeed());
      enemy.setSpeedX(cos * enemy.getSpeed());
      enemy.getBody().setTransform(enemy.getBody().getPosition().x + enemy.getSpeedX(),
          enemy.getSpeedY() + enemy.getBody().getPosition().y, 0);
      return true;
    });
  }
}
