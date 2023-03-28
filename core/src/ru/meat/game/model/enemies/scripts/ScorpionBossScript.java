package ru.meat.game.model.enemies.scripts;

import java.util.function.BiFunction;
import ru.meat.game.model.FloatPair;
import ru.meat.game.model.enemies.Enemy;

public class ScorpionBossScript {

  public static BiFunction<Enemy, FloatPair, Boolean> scorpionBoss() {
    return ((enemy, floatPair) -> {

      return true;
    });
  }
}
