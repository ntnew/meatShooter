package ru.meat.game.utils;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.World;
import java.math.BigDecimal;
import ru.meat.game.model.bodyData.BodyUserData;

public class GDXUtils {


  /**
   * изменить масштаб текстуры
   *
   * @param texture        изменяемая тексура
   * @param zoomMultiplier делитель масштаба
   * @return текстура в масштабе
   */
  public static Texture resizeTexture(FileHandle texture, float zoomMultiplier) {
    Pixmap pixmap200 = new Pixmap(texture);

    Pixmap pixmap100 = new Pixmap(
        BigDecimal.valueOf(pixmap200.getWidth() / zoomMultiplier).intValue(),
        BigDecimal.valueOf(pixmap200.getHeight() / zoomMultiplier).intValue(),
        pixmap200.getFormat());
    pixmap100.drawPixmap(pixmap200,
        0, 0, pixmap200.getWidth(), pixmap200.getHeight(),
        0, 0, pixmap100.getWidth(), pixmap100.getHeight()
    );
    Texture texture2 = new Texture(pixmap100);
    pixmap200.dispose();
    pixmap100.dispose();
    return texture2;
  }

  /**
   * Посчитать гипотенузу для двух точек
   *
   * @param x1
   * @param y1
   * @param x2
   * @param y2
   * @return
   */
  public static float calcGipotenuza(float x1, float y1, float x2, float y2) {
    return calcGipotenuza(x1 - x2, y1 - y2);
  }

  public static float calcGipotenuza(float catet1, float catet2) {
    return (float) Math.sqrt(catet1 * catet1 + catet2 * catet2);
  }

  /**
   * Создание блока.
   *
   * @param radius  радиус
   * @param density плотность
   * @return
   */

  public static Body createCircleForEnemy(World world, float radius, float density, BodyUserData bodyData, float x,
      float y) {

    BodyDef def = new BodyDef();

    def.type = BodyType.DynamicBody;
    def.position.set(x / Settings.WORLD_TO_VIEW, y / Settings.WORLD_TO_VIEW);

    Body box = world.createBody(def);

    CircleShape circle = new CircleShape();
    circle.setRadius(radius);

    box.createFixture(circle, density);
    box.getFixtureList().get(0).setUserData(bodyData);
    box.setLinearDamping(1);

    circle.dispose();

    return box;

  }

  public static Filter getFilter() {
    Filter f = new Filter();
    f.groupIndex = -1;
    return f;
  }


  public static float calcAngleBetweenTwoPoints(float x1, float y1, float x2, float y2) {
    return MathUtils.radiansToDegrees * MathUtils.atan2(y2 - y1, x2 - x1);
  }
}
