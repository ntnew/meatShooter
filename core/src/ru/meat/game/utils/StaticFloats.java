package ru.meat.game.utils;

public class StaticFloats {

  private static float SCREEN_WIDTH = 1280;
  private static float SCREEN_HEIGHT = 720f;



  public static float WORLD_TO_VIEW = 40;

  public static float WORLD_WIDTH = SCREEN_WIDTH/WORLD_TO_VIEW;
  public static float WORLD_HEIGHT = SCREEN_HEIGHT/WORLD_TO_VIEW;

  public static float VIEW_TO_WORLD = 1 / WORLD_TO_VIEW;

}
