package ru.meat.game.utils;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;

public class GDXUtils {


  /**
   * изменить масштаб текстуры
   * @param texture изменяемая тексура
   * @param zoomMultiplier делитель масштаба
   * @return текстура в масштабе
   */
  public static Texture resizeTexture(FileHandle texture, int zoomMultiplier) {
    Pixmap pixmap200 = new Pixmap(texture);

    Pixmap pixmap100 = new Pixmap(pixmap200.getWidth()/zoomMultiplier, pixmap200.getHeight()/zoomMultiplier, pixmap200.getFormat());
    pixmap100.drawPixmap(pixmap200,
        0, 0, pixmap200.getWidth(), pixmap200.getHeight(),
        0, 0, pixmap100.getWidth(), pixmap100.getHeight()
    );
    Texture texture2 = new Texture(pixmap100);
    pixmap200.dispose();
    pixmap100.dispose();
    return texture2;
  }

  public static float findGipotenuza(float catet1, float catet2){
   return (float) Math.sqrt(catet1 * catet1 + catet2 * catet2);
  }
}
