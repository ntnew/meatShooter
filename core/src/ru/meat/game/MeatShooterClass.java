package ru.meat.game;

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
import ru.meat.game.utils.StaticFloats;

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
    world.step(1 / 60f, 6, 2);
    playerService = new PlayerService(w / 2, h / 2, world);
    world.setContactListener(new MyContactListener(world));

    camera = new OrthographicCamera();
    camera.setToOrtho(false, w, h);
    camera.zoom = 1f;
    camera.update();

    spriteBatch = new SpriteBatch();
    Gdx.input.setInputProcessor(this);

    worldRenderer = new WorldRenderer(world, true, camera);

    enemyService.createEnemies(world);
  }

  @Override
  public void render() {
    camera.update();
    worldRenderer.getCameraBox2D().update();

    handleKey();
    Gdx.gl.glClearColor(0, 0, 0, 1);
    Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    stateTime += Gdx.graphics.getDeltaTime();
    playerService.updateState();
    world.step(1 / 60f, 6, 2);

    enemyService.correctDistanceBetweenEnemies();
    enemyService.actionEnemies(playerService.getPosX(), playerService.getPosY(), world);

    playerService.rotateModel(camera);

    spriteBatch.setProjectionMatrix(camera.combined);
    spriteBatch.begin();

    mapService.draw(spriteBatch);
    playerService.drawPlayer(spriteBatch);
    enemyService.drawEnemies(spriteBatch, stateTime);
    playerService.getActualWeapon().getBulletService().drowBullets(spriteBatch, playerService.getPosX(),
        playerService.getPosY());

    spriteBatch.end();
    worldRenderer.render(stateTime);

  }

  private void handleKey() {
    if (Gdx.input.isKeyPressed(Input.Keys.A)) {
      float x = playerService.moveLeft();
      camera.translate(x, 0);
      worldRenderer.getCameraBox2D().translate(x / StaticFloats.WORLD_TO_VIEW, 0);
    }

    if (Gdx.input.isKeyPressed(Input.Keys.W)) {
      float y = playerService.moveUp();
      camera.translate(0, y);
      worldRenderer.getCameraBox2D().translate(0, y / StaticFloats.WORLD_TO_VIEW);
    }

    if (Gdx.input.isKeyPressed(Input.Keys.S)) {
      float y = playerService.moveDown();
      camera.translate(0, y);
      worldRenderer.getCameraBox2D().translate(0, y / StaticFloats.WORLD_TO_VIEW);
    }

    if (Gdx.input.isKeyPressed(Input.Keys.D)) {
      float x = playerService.moveRight();
      camera.translate(x, 0);
      worldRenderer.getCameraBox2D().translate(x / StaticFloats.WORLD_TO_VIEW, 0);
    }

    if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(
        Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.D)) {
      playerService.changeFeetStatus(CharacterFeetStatus.RUN);
      playerService.changeTopStatus(CharacterTopStatus.MOVE);
    } else {
      playerService.changeFeetStatus(CharacterFeetStatus.IDLE);
      playerService.changeTopStatus(CharacterTopStatus.IDLE);
    }
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
