package ru.meat.game.settings;

public class Constants {

  /**
   * Основной зум камер
   */
  public static float MAIN_ZOOM = 4.5f;

  /**
   * зум камеры меню
   */
  public static float MENU_ZOOM = 0.5f;

  /**
   * Множитель между миром box2d и отрисованным
   */
  public static float WORLD_TO_VIEW = 80;

  /**
   * Скорость бега игрока
   */
  public static float PLAYER_MOVE_SPEED = 2f;

  public static boolean DEBUG = false;

  public static int LVL_EXP_STEP = 100;

  public static float GUI_ZOOM = 1.4f;


  public static float SHOOT_SOUND_MULTIPLY = 0.8f;

  public static float EXPLODE_SOUND_MULTIPLY = 1.6f;

  public static String PATH_WEAPON_SOUND = "sound/weapons/";

  public static boolean MOBILE;
}
