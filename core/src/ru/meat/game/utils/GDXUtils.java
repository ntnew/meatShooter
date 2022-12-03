package ru.meat.game.utils;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import ru.meat.game.model.Player;

public class GDXUtils {


  /**
   * изменить масштаб текстуры
   *
   * @param texture        изменяемая тексура
   * @param zoomMultiplier делитель масштаба
   * @return текстура в масштабе
   */
  public static Texture resizeTexture(FileHandle texture, int zoomMultiplier) {
    Pixmap pixmap200 = new Pixmap(texture);

    Pixmap pixmap100 = new Pixmap(pixmap200.getWidth() / zoomMultiplier, pixmap200.getHeight() / zoomMultiplier,
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

  public static float findGipotenuza(float catet1, float catet2) {
    return (float) Math.sqrt(catet1 * catet1 + catet2 * catet2);
  }

  /**
   * 02. Создание объектов мира 03.
   */

  private void createWorld() {

//создание игрока

//    BodyDef def = new BodyDef();
//
////установить тип тела
//
//    def.type = BodyType.DynamicBody;
//
////создать объект с определёнными заранее параметрами
//
//    Body boxP = world.createBody(def);
//
//     Player player = new Player(boxP);

//переместить объект по указанным координатам

//    player.getBody().setTransform(1.0f, 4.0f, 0);
//
//    player.getBody().setFixedRotation(true);

//создание платформы

//    platforms.add(new MovingPlatform(world, 3F, 3, 1, 0.25F, 2, 0, 2));

//создание блоков

//    for (int i = 0; i < width; ++i) {
//
//      Body boxGround = createBox(BodyType.StaticBody, 0.5F, 0.5F, 2);
//
//      boxGround.setTransform(i, 0, 0);
//
//      boxGround.getFixtureList().get(0).setUserData("bd");
//
//      boxGround = createBox(BodyType.StaticBody, 0.5F, 0.5F, 0);
//
//      boxGround.setTransform(i, height - 1, 0);
//
//      boxGround.getFixtureList().get(0).setUserData("b");
//
//    }

  }

  /**
   *  Создание блока.
   *
   * @param radius  радиус
   * @param density плотность
   * @return
   */

  public static Body createCircleForEnemy(World world, float radius, float density) {

    BodyDef def = new BodyDef();

    def.type = BodyType.DynamicBody;

    Body box = world.createBody(def);

    CircleShape poly = new CircleShape();
    poly.setRadius(radius);

    box.createFixture(poly, density);

    poly.dispose();

    return box;

  }
}
