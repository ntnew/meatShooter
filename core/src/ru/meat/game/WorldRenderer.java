package ru.meat.game;

import static ru.meat.game.utils.Settings.MAIN_ZOOM;
import static ru.meat.game.utils.Settings.WORLD_HEIGHT;
import static ru.meat.game.utils.Settings.WORLD_TO_VIEW;
import static ru.meat.game.utils.Settings.WORLD_WIDTH;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import lombok.Data;

@Data
public class WorldRenderer {

  Box2DDebugRenderer renderer;
  World world;
  public OrthographicCamera cameraBox2D;


  public WorldRenderer(World world, boolean debug, float w, float h) {
    cameraBox2D = new OrthographicCamera();
    cameraBox2D.viewportWidth = w/WORLD_TO_VIEW;
    cameraBox2D.viewportHeight = h/WORLD_TO_VIEW;
    cameraBox2D.zoom = MAIN_ZOOM;
    cameraBox2D.position.set(cameraBox2D.viewportWidth / 2, cameraBox2D.viewportHeight / 2, 0f);
    cameraBox2D.update();

    renderer = new Box2DDebugRenderer();
    renderer.setDrawBodies(debug);

    this.world = world;
  }


  public void dispose() {
    world.dispose();
  }

  public void render() {
    renderer.render(world, cameraBox2D.combined);
  }
}
