package ru.meat.game.game;

import static ru.meat.game.settings.Constants.MAIN_ZOOM;
import static ru.meat.game.settings.Constants.MOBILE;
import static ru.meat.game.settings.Constants.WORLD_TO_VIEW;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Cursor.SystemCursor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.esotericsoftware.spine.SkeletonRenderer;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.SneakyThrows;
import ru.meat.game.Box2dWorld;
import ru.meat.game.MyGame;
import ru.meat.game.gui.GUI;
import ru.meat.game.menu.EndGameMenu;
import ru.meat.game.menu.PauseMenu;
import ru.meat.game.model.EnemyStatus;
import ru.meat.game.model.weapons.explosions.Explosions;
import ru.meat.game.service.AudioService;
import ru.meat.game.service.BloodService;
import ru.meat.game.service.BulletService;
import ru.meat.game.service.EnemyService;
import ru.meat.game.service.FaderService;
import ru.meat.game.service.MapService;
import ru.meat.game.service.PlayerService;
import ru.meat.game.service.RpgStatsService;

@Data
public abstract class GameZone implements Screen, InputProcessor {

  protected MapService mapService;

  protected final MyGame game;
  /**
   * Интерфейс пользователя
   */
  protected GUI gui;

  protected PlayerService playerService;


  protected EnemyService enemyService;

  /**
   * Основная камера с текстурами
   */
  protected final OrthographicCamera camera;


  protected LocalDateTime beginDate;

  protected SpriteBatch spriteBatch;

  protected PolygonSpriteBatch polyBatch;

  protected SkeletonRenderer renderer;
  private boolean started = false;

  private long comparatorTime = 0;

  private ThreadForZoom threadForZoom;


  public GameZone(MyGame game, int map) {
    this.game = game;

    Gdx.input.setInputProcessor(this);

    //создание камеры
    camera = new OrthographicCamera();
    camera.zoom = MAIN_ZOOM;
    camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0f);
    camera.update();

    this.mapService = new MapService();
    mapService.initMap(map);
    //создание игрока и интерфейса
    playerService = new PlayerService(Gdx.graphics.getWidth() / 2f * MAIN_ZOOM,
        Gdx.graphics.getHeight() / 2f * MAIN_ZOOM);
    gui = new GUI(playerService.getPlayer().getHp());
    gui.setAimCursor();

    beginDate = LocalDateTime.now();

    enemyService = new EnemyService();

    spriteBatch = new SpriteBatch();

    renderer = new SkeletonRenderer();
    renderer.setPremultipliedAlpha(true);

    polyBatch = new PolygonSpriteBatch();
  }

  @Override
  public void show() {

  }

  @Override
  public void render(float delta) {
    AudioService.getInstance().playGameMusic();
    if (TimeUtils.timeSinceMillis(comparatorTime) > 4000) {
      sortEnemies();
    }
    Gdx.gl.glClearColor(0, 0, 0, 1);
    Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    camera.update();
    Box2dWorld.getInstance().update();
    playerService.updateState();
    BulletService.getInstance().updateBullets();

    if (!MOBILE) {
      playerService.handleMoveKey(camera, Box2dWorld.getInstance().getCameraBox2D());
      handleMouse();
    }

    handleWorldBounds();

    mapService.draw(camera);

    renderSpec(delta);

    BloodService.getInstance().drawBleeds(camera);

    Explosions.getInstance().drawExplosions(camera);

    Box2dWorld.getInstance().render();

    gui.draw(playerService.getPlayer().getHp());

    //Нарисовать осветление экрана при старте
    if (!started) {
      started = FaderService.getInstance().drawFadeIn();
    }

    if (playerService.getPlayer().isDead()) {
      if (threadForZoom == null) {
        threadForZoom = new ThreadForZoom();
        threadForZoom.start();
      }
      //нарисовать затемнение экрана при смерти
      if (FaderService.getInstance().drawFadeOut()) {
        endGameSession();
      }
    }
  }

  /**
   * Остортировать врагов, чтобы сначала рисовались трупы
   */
  private void sortEnemies() {
    comparatorTime = TimeUtils.millis();
    enemyService.getEnemies().sort((x, y) -> {
      if (x.getStatus().equals(EnemyStatus.DIED) && !y.getStatus().equals(EnemyStatus.DIED)) {
        return -1;
      } else if ((x.getStatus().equals(EnemyStatus.DIED) && y.getStatus().equals(EnemyStatus.DIED))
          || (!x.getStatus().equals(EnemyStatus.DIED) && !y.getStatus().equals(EnemyStatus.DIED))) {
        return 0;
      } else {
        return 1;
      }
    });
  }

  /**
   * рендеринг специфических для каждого матча вещей
   *
   * @param delta
   */
  protected abstract void renderSpec(float delta);

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

  @Override
  public boolean keyDown(int keycode) {
    if (keycode == Keys.NUM_1) {
      playerService.changeWeapon(1);
    }
    if (keycode == Keys.NUM_2) {
      playerService.changeWeapon(2);
    }
    if (keycode == Keys.NUM_3) {
      playerService.changeWeapon(3);
    }
    if (keycode == Keys.NUM_4) {
      playerService.changeWeapon(4);
    }
    if (keycode == Keys.NUM_5) {
      playerService.changeWeapon(5);
    }
    if (keycode == Keys.NUM_6) {
      playerService.changeWeapon(6);
    }
    if (keycode == Keys.NUM_7) {
      playerService.changeWeapon(7);
    }
    if (keycode == Keys.NUM_8) {
      playerService.changeWeapon(8);
    }

    if (keycode == Keys.ESCAPE) {
      Gdx.graphics.setSystemCursor(SystemCursor.Arrow);
      game.setScreen(new PauseMenu(game, this));
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
    Box2dWorld.dispose();
  }


  public void resumeGame() {
    gui.setAimCursor();
    Gdx.input.setInputProcessor(this);
  }

  public void endGameSession() {
    Gdx.gl.glClearColor(0, 0, 0, 1);
    Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    RpgStatsService.getInstance().increaseExp(enemyService.getRewardPointCount().get());
    this.game.setScreen(
        new EndGameMenu(game, enemyService.getRewardPointCount().get(), mapService.getMapInfo().getPosition(),
            beginDate, enemyService.getKillCount().get()));
    Gdx.graphics.setSystemCursor(SystemCursor.Arrow);
    AudioService.getInstance().smoothStopMusic();
    Box2dWorld.dispose();
    BulletService.dispose();
    BloodService.getInstance().dispose();
    Explosions.getInstance().dispose();
  }


  /**
   * обработать рамки камеры, чтобы не заходили за края
   */
  private void handleWorldBounds() {
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

    float camX2 = Box2dWorld.getInstance().getCameraBox2D().position.x;
    float camY2 = Box2dWorld.getInstance().getCameraBox2D().position.y;

    Vector2 camMin2 = new Vector2(Box2dWorld.getInstance().getCameraBox2D().viewportWidth / 2,
        Box2dWorld.getInstance().getCameraBox2D().viewportHeight / 2);
    camMin2.scl(Box2dWorld.getInstance().getCameraBox2D().zoom); //bring to center and scale by the zoom level
    Vector2 camMax2 = new Vector2(mapService.getCurrentMap().getMainTexture().getWidth() / WORLD_TO_VIEW,
        mapService.getCurrentMap().getMainTexture().getHeight() / WORLD_TO_VIEW);
    camMax2.sub(camMin2); //bring to center

    camX2 = Math.min(camMax2.x, Math.max(camX2, camMin2.x));
    camY2 = Math.min(camMax2.y, Math.max(camY2, camMin2.y));

    Box2dWorld.getInstance().getCameraBox2D().position.set(camX2, camY2, 0);
  }

  /**
   * Обработать нажатия мыши, если персонаж ещё жив
   */
  private void handleMouse() {
    if (!playerService.getPlayer().isDead()) {
      playerService.rotateModel(Box2dWorld.getInstance().getCameraBox2D());
      if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
        playerService.shoot(Box2dWorld.getInstance().getCameraBox2D());
      }
    }
  }

  class ThreadForZoom extends Thread {
    private long timeStamp = 0;

    @SneakyThrows
    @Override
    public void run() {
      while (true) {
        if (timeStamp == 0 || TimeUtils.timeSinceMillis(timeStamp) > 10) {
          camera.zoom = camera.zoom - 0.02f;
          timeStamp = TimeUtils.millis();
        }
        if (camera.zoom < 1) {
          break;
        }
      }
    }
  }
}
