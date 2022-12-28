package ru.meat.game.utils;

public class Settings {

  /**
   * Основной зум камер
   */
  public static float MAIN_ZOOM = 4f;

  private static float SCREEN_WIDTH = 1280f;
  private static float SCREEN_HEIGHT = 720f;


  /**
   * Множитель между миром box2d и отрисованным
   */
  public static float WORLD_TO_VIEW = 40;

  public static float WORLD_WIDTH = SCREEN_WIDTH/WORLD_TO_VIEW;
  public static float WORLD_HEIGHT = SCREEN_HEIGHT/WORLD_TO_VIEW;

  public static float VIEW_TO_WORLD = 1 / WORLD_TO_VIEW;


  public static final float VOLUME = 0.1f;

}
