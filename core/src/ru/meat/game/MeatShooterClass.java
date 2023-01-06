package ru.meat.game;

import static ru.meat.game.utils.Settings.*;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import java.util.stream.Collectors;
import ru.meat.game.model.EnemyStatus;
import ru.meat.game.service.AudioService;
import ru.meat.game.service.EnemyService;
import ru.meat.game.service.MapService;
import ru.meat.game.service.MyContactListener;
import ru.meat.game.service.PlayerService;

public class MeatShooterClass implements InputProcessor, Screen {


  private final InputProcessor inputProcessor;
  private OrthographicCamera camera;
  private PlayerService playerService;
  private EnemyService enemyService;
  private float stateTime;

  private SpriteBatch spriteBatch;
  private World world;
  private WorldRenderer worldRenderer;

  private MapService mapService;

  public MeatShooterClass(InputProcessor inputProcessor, int map) {
    this.inputProcessor = inputProcessor;

    float w = Gdx.graphics.getWidth();
    float h = Gdx.graphics.getHeight();

    enemyService = new EnemyService();
    this.mapService = new MapService();
    mapService.initMap(map);
    world = new World(new Vector2(0, 0), true);
    world.step(1 / 60f, 6, 6);

    world.setContactListener(new MyContactListener(world));

    camera = new OrthographicCamera();
    camera.zoom = MAIN_ZOOM;
    camera.setToOrtho(false, w, h);
    camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0f);

    camera.update();

    spriteBatch = new SpriteBatch();
    Gdx.input.setInputProcessor(this);

    worldRenderer = new WorldRenderer(world, true, w, h);

    playerService = new PlayerService(Gdx.graphics.getWidth() / 2 * MAIN_ZOOM, Gdx.graphics.getHeight() / 2 * MAIN_ZOOM,
        world);
    enemyService.createEnemies(world);
  }


  /**
   * обработать рамки камеры, чтобы не заходили за края
   */
  public void handleWorldBounds() {
    float camX = camera.position.x;
    float camY = camera.position.y;

    Vector2 camMin = new Vector2(camera.viewportWidth / 2, camera.viewportHeight / 2);
    camMin.scl(camera.zoom); //bring to center and scale by the zoom level
    Vector2 camMax = new Vector2(mapService.getCurrentMap().getMainTexture().getWidth(),
        mapService.getCurrentMap().getMainTexture().getHeight());
    camMax.sub(camMin); //bring to center

//keep camera within borders
    camX = Math.min(camMax.x, Math.max(camX, camMin.x));
    camY = Math.min(camMax.y, Math.max(camY, camMin.y));

    camera.position.set(camX, camY, 0);

    float camX2 = worldRenderer.getCameraBox2D().position.x;
    float camY2 = worldRenderer.getCameraBox2D().position.y;

    Vector2 camMin2 = new Vector2(worldRenderer.getCameraBox2D().viewportWidth / 2,
        worldRenderer.getCameraBox2D().viewportHeight / 2);
    camMin2.scl(worldRenderer.getCameraBox2D().zoom); //bring to center and scale by the zoom level
    Vector2 camMax2 = new Vector2(mapService.getCurrentMap().getMainTexture().getWidth(),
        mapService.getCurrentMap().getMainTexture().getHeight());
    camMax2.sub(camMin2); //bring to center

    camX2 = Math.min(camMax2.x, Math.max(camX2, camMin2.x));
    camY2 = Math.min(camMax2.y, Math.max(camY2, camMin2.y));

    worldRenderer.getCameraBox2D().position.set(camX2, camY2, 0);
  }

  @Override
  public void show() {

  }

  @Override
  public void render(float delta) {
    createMoreEnemies();
    camera.update();
    worldRenderer.getCameraBox2D().update(false);

    Gdx.gl.glClearColor(0, 0, 0, 1);
    Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    stateTime += Gdx.graphics.getDeltaTime();

    world.step(1 / 60f, 6, 2);

    playerService.updateState();
    playerService.handleMoveKey(camera, worldRenderer.getCameraBox2D());
    handleWorldBounds();

    enemyService.actionEnemies(playerService.getBodyPosX(), playerService.getBodyPosY(), world);
    handleMouse();

    //рисовать текстуры
    spriteBatch.setProjectionMatrix(camera.combined);
    spriteBatch.begin();

    mapService.draw(spriteBatch);
    playerService.drawPlayer(spriteBatch);
    enemyService.drawEnemies(spriteBatch, stateTime);
    playerService.drawBullets(spriteBatch);

    spriteBatch.end();
    worldRenderer.render();
  }

  private void createMoreEnemies() {
    if (enemyService.getEnemies().stream().filter(x -> x.getStatus() != EnemyStatus.DIED).count() < 10) {
      // Инициализация начальной позиции
      float xBound1 = playerService.getBodyPosX() * WORLD_TO_VIEW;
      float xBound2 = playerService.getBodyPosX() * WORLD_TO_VIEW;
      float yBound1 = playerService.getBodyPosY() * WORLD_TO_VIEW;
      float yBound2 = playerService.getBodyPosY() * WORLD_TO_VIEW;
      float deltaByY = Gdx.graphics.getHeight() * MAIN_ZOOM;
      float deltaByX = Gdx.graphics.getWidth() * MAIN_ZOOM;

      //рандомизация позиции появления врагов
      int random = MathUtils.random(1, 4);
      if (random == 1) {
        xBound1 = xBound1 - deltaByX / 2 - 50;
        xBound2 = xBound2 - deltaByX / 2 - 20;
        yBound1 = yBound1 - deltaByY;
        yBound2 = yBound2 + deltaByY;
      } else if (random == 2) {
        xBound1 = xBound1 + deltaByX / 2 + 20;
        xBound2 = xBound2 + deltaByX / 2 + 50;
        yBound1 = yBound1 - deltaByY;
        yBound2 = yBound2 + deltaByY;
      } else if (random == 3) {
        xBound1 = xBound1 - deltaByX;
        xBound2 = xBound2 + deltaByX;
        yBound1 = yBound1 + deltaByY / 2 + 20;
        yBound2 = yBound2 + deltaByY / 2 + 50;
      } else if (random == 4) {
        xBound1 = xBound1 - deltaByX;
        xBound2 = xBound2 + deltaByX;
        yBound1 = yBound1 - deltaByY / 2 - 50;
        yBound2 = yBound2 - deltaByY / 2 - 20;
      }
      //Создание врага
      enemyService.getEnemies().add(
          enemyService.createZombieEnemy(
              MathUtils.random(xBound1, xBound2),
              MathUtils.random(yBound1, yBound2),
              world));
    }
  }

  @Override
  public void resize(int width, int height) {

  }

  @Override
  public void pause() {

  }

  @Override
  public void resume() {

  }

  @Override
  public void hide() {

  }

  private void handleMouse() {
    if (!playerService.getPlayer().isDead()) {
      playerService.rotateModel(worldRenderer.getCameraBox2D());
      if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
        playerService.shoot(worldRenderer.getCameraBox2D());
      }
    }
  }

  @Override
  public boolean keyDown(int keycode) {
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

  @Override
  public void dispose() {
//    super.dispose();
    worldRenderer.dispose();
  }

}
