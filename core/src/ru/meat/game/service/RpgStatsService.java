package ru.meat.game.service;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import lombok.Getter;
import ru.meat.game.model.player.RpgStats;


public class RpgStatsService {

  @Getter
  private final RpgStats stats;

  private final Preferences prefs;

  private static RpgStatsService instance;

  public static RpgStatsService getInstance() {
    if (instance == null) {
      instance = new RpgStatsService();
    }
    return instance;
  }

  public RpgStatsService() {
    stats = new RpgStats();
    prefs = Gdx.app.getPreferences("My Preferences");

    if (!prefs.contains("HP")) {
      prefs.putFloat("HP", 100f);
    }
    stats.setHp(prefs.getFloat("HP"));

    if (!prefs.contains("EXP")) {
      prefs.putLong("EXP", 0);
    }
    stats.setExperience(prefs.getLong("EXP"));

    if (!prefs.contains("LVL")) {
      prefs.putLong("LVL", 1);
    }
    stats.setLvl(prefs.getLong("LVL"));

    if (!prefs.contains("DAMAGE")) {
      prefs.putFloat("DAMAGE", 1);
    }
    stats.setDamage(prefs.getFloat("DAMAGE"));

    if (!prefs.contains("RESIST")) {
      prefs.putFloat("RESIST", 0);
    }
    stats.setResist(prefs.getFloat("RESIST"));

    if (!prefs.contains("MOVE_SPEED")) {
      prefs.putFloat("MOVE_SPEED", 1);
    }
    stats.setMoveSpeed(prefs.getFloat("MOVE_SPEED"));

    if (!prefs.contains("RELOAD_SPEED")) {
      prefs.putFloat("RELOAD_SPEED", 1);
    }
    stats.setReloadSpeed(prefs.getFloat("RELOAD_SPEED"));

    if (!prefs.contains("FIRE_SPEED")) {
      prefs.putFloat("FIRE_SPEED", 1);
    }
    stats.setFireSpeed(prefs.getFloat("FIRE_SPEED"));

    prefs.flush();
  }

  public void saveStats() {
    prefs.putFloat("HP", Float.parseFloat(String.format("%.0f", stats.getHp())));
    prefs.putLong("EXP", stats.getExperience());
    prefs.putLong("LVL", stats.getLvl());
    prefs.putFloat("DAMAGE", stats.getDamage());
    prefs.putFloat("RESIST", stats.getResist());
    prefs.putFloat("MOVE_SPEED", stats.getMoveSpeed());
    prefs.putFloat("RELOAD_SPEED", stats.getReloadSpeed());
    prefs.putFloat("FIRE_SPEED", stats.getFireSpeed());

    prefs.flush();
  }
}
