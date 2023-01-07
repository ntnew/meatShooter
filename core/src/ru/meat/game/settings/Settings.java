package ru.meat.game.settings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class Settings {


  private static Settings instance;

  public static Settings getInstance() {
    if (instance == null) {
      instance = new Settings();
    }
    return instance;
  }

  public Settings() {
    Preferences prefs = Gdx.app.getPreferences("My Preferences");

    if (!prefs.contains("EFFECT_VOLUME")) {
      prefs.putFloat("EFFECT_VOLUME", 0.1f);
    }
    this.EFFECT_VOLUME = prefs.getFloat("EFFECT_VOLUME");

    if (!prefs.contains("MUSIC_VOLUME")) {
      prefs.putFloat("MUSIC_VOLUME", 0.1f);
    }

    this.MUSIC_VOLUME = prefs.getFloat("MUSIC_VOLUME");

    if (!prefs.contains("SCREEN_WIDTH")) {
      prefs.putInteger("SCREEN_WIDTH", 800);
    }

    this.SCREEN_WIDTH = prefs.getInteger("SCREEN_WIDTH");

    if (!prefs.contains("SCREEN_HEIGHT")) {
      prefs.putInteger("SCREEN_HEIGHT", 600);
    }

    this.SCREEN_HEIGHT = prefs.getInteger("SCREEN_HEIGHT");

    prefs.flush();
  }

  /**
   * Громкость эффектов
   */
  public float EFFECT_VOLUME;

  /**
   * Громкость музыки
   */
  public float MUSIC_VOLUME;

  /**
   * Ширина экрана в пикселях
   */
  public int SCREEN_WIDTH;

  /**
   * Высота экрана в пикселях
   */
  public int SCREEN_HEIGHT;

}
