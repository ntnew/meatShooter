package ru.meat.game.utils;

import static ru.meat.game.utils.GDXUtils.resizeTexture;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FilesUtils {

  public static boolean compareTwoFilenames(Path x, Path y) {
    return Integer.parseInt(x.toString().replaceAll("\\D+", "")) > Integer.parseInt(
        y.toString().replaceAll("\\D+", ""));
  }

  public static int convertBoolToInt(boolean b) {
    return b ? 1 : -1;
  }


  public static Animation<Texture> initAnimationFrames(String animationFilesPath, float zoomMultiplier,
      float frameDuration) {
    Texture[] collect = collectTextures(animationFilesPath, zoomMultiplier);

    return new Animation<>(frameDuration, collect);
  }

  public static Texture[] collectTextures(String filesPath, float zoomMultiplier) {
    try {
      return Files.walk(Paths.get(filesPath))
          .filter(Files::isRegularFile)
          .sorted((x, y) -> convertBoolToInt(compareTwoFilenames(x, y)))
          .map(file -> resizeTexture(Gdx.files.internal(file.toAbsolutePath().toString()), zoomMultiplier))
          .toArray(Texture[]::new);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
