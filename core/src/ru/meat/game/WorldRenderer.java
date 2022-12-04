package ru.meat.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import java.util.List;
import ru.meat.game.model.Enemy;

public class WorldRenderer {

  Box2DDebugRenderer renderer;

  MyWorld world;
  public OrthographicCamera cam;


  public WorldRenderer(MyWorld world, boolean debug, OrthographicCamera cam) {
    renderer = new Box2DDebugRenderer();
    this.world = world;
    this.cam = cam;
  }


  public void dispose() {
    world.getWorld().dispose();
  }

  public void render(float delta, List<Enemy> enemies) {
    renderer.render(world.getWorld(), cam.combined);
    enemies.forEach(x -> {
      x.getBox().setTransform(x.getCenter().getX(), x.getCenter().getY(), 0);
    });
    world.getWorld().step(delta, 4, 4);
  }
}
