package ru.meat.game.utils;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import ru.meat.game.loader.LoaderManager;

public class AniLoadUtil {

  /**
   * Распарсить Одну картинку в кадры анимации
   *
   * @param texture   текстура, картинка
   * @param frameCols колонки кадров
   * @param frameRows строки кадров
   * @return
   */
  public static TextureRegion[] getAniFramesFromParsingTexture(Texture texture, int frameCols, int frameRows) {
    texture.setFilter(TextureFilter.MipMapLinearLinear, TextureFilter.MipMapLinearLinear);

    TextureRegion[][] tmp = TextureRegion.split(texture,
        texture.getWidth() / frameCols,
        texture.getHeight() / frameRows);

    TextureRegion[] frames = new TextureRegion[frameCols * frameRows];
    int index = 0;
    for (int i = 0; i < frameRows; i++) {
      for (int j = 0; j < frameCols; j++) {
        frames[index++] = tmp[i][j];
      }
    }
    return frames;
  }

  public static Animation<TextureRegion> getAniFromResources(String pathToAnimation, float frameDuration, int frameCols,
      int frameRows) {
    return new Animation<>(frameDuration,
        getAniFramesFromParsingTexture(LoaderManager.getInstance().get(pathToAnimation),
            frameCols,
            frameRows));
  }
}
