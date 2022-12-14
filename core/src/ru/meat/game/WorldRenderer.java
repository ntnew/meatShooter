package ru.meat.game;

import static ru.meat.game.utils.StaticFloats.WORLD_HEIGHT;
import static ru.meat.game.utils.StaticFloats.WORLD_WIDTH;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import lombok.Data;

@Data
public class WorldRenderer {

  Box2DDebugRenderer renderer;



  World world;
  public OrthographicCamera cameraBox2D;


  public WorldRenderer(World world, boolean debug, OrthographicCamera cam) {
    cameraBox2D = new OrthographicCamera();
    cameraBox2D.viewportWidth = WORLD_WIDTH;
    cameraBox2D.viewportHeight = WORLD_HEIGHT;
    cameraBox2D.position.set(cameraBox2D.viewportWidth / 2, cameraBox2D.viewportHeight / 2, 0f);
    cameraBox2D.update();

    renderer = new Box2DDebugRenderer();
    renderer.setDrawBodies(debug);
//    world.setContactListener(new MyContactListener());

    this.world = world;
//    this.cam = cam;
  }


  public void dispose() {
    world.dispose();
  }

  public void render(float delta) {


    renderer.render(world, cameraBox2D.combined);
//    world.step(delta, 4, 4);

  }
}
