package ru.meat.game;

import static ru.meat.game.utils.Settings.*;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import ru.meat.game.model.CharacterFeetStatus;
import ru.meat.game.model.CharacterTopStatus;
import ru.meat.game.service.EnemyService;
import ru.meat.game.service.MapService;
import ru.meat.game.service.MyContactListener;
import ru.meat.game.service.PlayerService;

public class MeatShooterClass extends ApplicationAdapter implements InputProcessor {

  @Override
  public void dispose() {
    super.dispose();
    worldRenderer.dispose();
  }

  private final InputProcessor inputProcessor;
  private OrthographicCamera camera;
  private PlayerService playerService;
  private EnemyService enemyService;
  private float stateTime;

  private SpriteBatch spriteBatch;
  private World world;
  private WorldRenderer worldRenderer;

  private MapService mapService;

  public MeatShooterClass(InputProcessor inputProcessor) {
    this.inputProcessor = inputProcessor;
  }

  @Override
  public void create() {
    float w = Gdx.graphics.getWidth();
    float h = Gdx.graphics.getHeight();

//    Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
// fullscreen

    enemyService = new EnemyService();
    mapService = new MapService();
    mapService.initMap();
    world = new World(new Vector2(0, 0), true);
    world.step(1/60f, 6, 6);

    world.setContactListener(new MyContactListener(world));

    camera = new OrthographicCamera();
    camera.setToOrtho(false, w, h);
    camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0f);

    camera.zoom = MAIN_ZOOM;
    camera.update();

    spriteBatch = new SpriteBatch();
    Gdx.input.setInputProcessor(this);

    worldRenderer = new WorldRenderer(world, true,w ,h);

    playerService = new PlayerService(w / 2, h / 2, world);
    enemyService.createEnemies(world);
  }

  @Override
  public void render() {
    camera.update(false);
    worldRenderer.getCameraBox2D().update(false);


    Gdx.gl.glClearColor(0, 0, 0, 1);
    Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    stateTime += Gdx.graphics.getDeltaTime();

    world.step(1 / 60f, 6, 2);

    playerService.updateState();
    playerService.handleKey(camera, worldRenderer.getCameraBox2D());
    playerService.rotateModel(camera);

//    enemyService.correctDistanceBetweenEnemies();
    enemyService.actionEnemies(playerService.getPlayer().getPosX(), playerService.getPlayer().getPosY(), world);


    spriteBatch.setProjectionMatrix(camera.combined);

    spriteBatch.begin();

    mapService.draw(spriteBatch);
    playerService.drawPlayer(spriteBatch);
    enemyService.drawEnemies(spriteBatch, stateTime);
    playerService.drawBullets(spriteBatch);

    spriteBatch.end();
    worldRenderer.render();

  }

  @Override
  public boolean keyDown(int keycode) {
    if (keycode == Input.Keys.LEFT) {
      camera.translate(-50, 0);
    }
    if (keycode == Input.Keys.RIGHT) {
      camera.translate(50, 0);
    }
    if (keycode == Input.Keys.UP) {
      camera.translate(0, -50);
    }
    if (keycode == Input.Keys.DOWN) {
      camera.translate(0, 50);
    }
    if (Gdx.input.isKeyPressed(Keys.BACKSPACE)) {
      camera.zoom += 0.1;
    }
    if (Gdx.input.isKeyPressed(Keys.MINUS)) {
      camera.zoom -= 0.1;
    }
    if (keycode == Keys.NUM_1) {
      playerService.changeWeapon(1);
    }
    if (keycode == Keys.NUM_2) {
      playerService.changeWeapon(2);
    }
    return false;
  }

  @Override
  public boolean keyUp(int keycode) {
    return false;
  }

  @Override
  public boolean keyTyped(char character) {
    return false;
  }

  @Override
  public boolean touchDown(int screenX, int screenY, int pointer, int button) {
    if (button == Input.Buttons.LEFT) {
      playerService.shoot(camera, screenX, screenY);
    }
    return false;
  }

  @Override
  public boolean touchUp(int screenX, int screenY, int pointer, int button) {
    return false;
  }

  @Override
  public boolean touchDragged(int screenX, int screenY, int pointer) {
    return false;
  }

  @Override
  public boolean mouseMoved(int screenX, int screenY) {
    return false;
  }

  @Override
  public boolean scrolled(float amountX, float amountY) {
    return false;
  }


}
