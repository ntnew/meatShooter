package ru.meat.game;

import static ru.meat.game.settings.Constants.DEBUG;
import static ru.meat.game.settings.Constants.MAIN_ZOOM;
import static ru.meat.game.settings.Constants.WORLD_TO_VIEW;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import lombok.Data;
import ru.meat.game.service.MyContactListener;

@Data
public class Box2dWorld {

  private static Box2dWorld instance;
  private Box2DDebugRenderer renderer;
  private final World world;
  public OrthographicCamera cameraBox2D;

  public static Box2dWorld getInstance() {
    if (instance == null) {
      instance = new Box2dWorld();
    }
    return instance;
  }


  public Box2dWorld() {
    world = new World(new Vector2(0, 0), true);
    world.setContactListener(new MyContactListener());

    if (DEBUG) {
      cameraBox2D = new OrthographicCamera();
      cameraBox2D.viewportWidth = Gdx.graphics.getWidth() / WORLD_TO_VIEW;
      cameraBox2D.viewportHeight = Gdx.graphics.getHeight() / WORLD_TO_VIEW;
      cameraBox2D.zoom = MAIN_ZOOM;
      cameraBox2D.position.set(cameraBox2D.viewportWidth / 2, cameraBox2D.viewportHeight / 2, 0f);
      cameraBox2D.update();

      renderer = new Box2DDebugRenderer();
      renderer.setDrawBodies(DEBUG);
      renderer.setDrawContacts(DEBUG);
    }
  }


  public static void dispose() {
    instance.getWorld().dispose();
    instance = null;
  }

  public void render() {
    if (DEBUG) {
      renderer.render(world, cameraBox2D.combined);
      cameraBox2D.update();
    }
  }

  public void update() {
    synchronized (world) {
      world.step(1 / 60f, 1, 1);
    }
  }

  public void destroyBody(Body body) {
    synchronized (world) {
      world.destroyBody(body);
    }
  }

  public Body createBody(BodyDef def) {
    synchronized (world) {
      return world.createBody(def);
    }
  }
}
