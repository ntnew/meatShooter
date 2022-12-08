package ru.meat.game.utils;

public class StaticFloats {

  private static float SCREEN_WIDTH = 1280; //  1920/3
  private static float SCREEN_HEIGHT = 720f;  // 1080/3

  public static float WORLD_WIDTH = SCREEN_WIDTH/40;
  public static float WORLD_HEIGHT = SCREEN_HEIGHT/40;

  public static float WORLD_TO_VIEW = 40;
  public static float VIEW_TO_WORLD = 1 / WORLD_TO_VIEW;

}
