package ru.meat.game.utils;

import java.nio.file.Path;

public class FilesUtils {

  public static boolean compareTwoFilenames(Path x, Path y) {
    return Integer.parseInt(x.toString().replaceAll("\\D+", "")) > Integer.parseInt(
        y.toString().replaceAll("\\D+", ""));
  }

  public static int convertBoolToInt(boolean b) {
    return b ? 1 : -1;
  }
}
