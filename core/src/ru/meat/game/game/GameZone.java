package ru.meat.game.game;

import static ru.meat.game.settings.Constants.DEBUG;
import static ru.meat.game.settings.Constants.MAIN_ZOOM;
import static ru.meat.game.settings.Constants.MOBILE;
import static ru.meat.game.settings.Constants.WORLD_TO_VIEW;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Cursor.SystemCursor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.PerformanceCounter;
import com.esotericsoftware.spine.SkeletonRenderer;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.SneakyThrows;
import ru.meat.game.Box2dWorld;
import ru.meat.game.MyGame;
import ru.meat.game.gui.GUI;
import ru.meat.game.menu.EndGameMenu;
import ru.meat.game.model.maps.Map;
import ru.meat.game.model.player.PlayerService;
import ru.meat.game.service.AudioService;
import ru.meat.game.service.EnemyService;
import ru.meat.game.service.FaderService;
import ru.meat.game.service.MapService;
import ru.meat.game.service.RpgStatsService;

@Data
public abstract class GameZone implements Screen {

  protected EnemyService enemyService;

  /**
   * Основная камера с текстурами
   */
  protected final OrthographicCamera camera;

  protected LocalDateTime beginDate;

  protected SpriteBatch spriteBatch;

  protected SkeletonRenderer renderer;
  private boolean started = false;


  private ThreadForZoom threadForZoom;
  protected MainStage stage;
  protected SecondStage secondStage;

  PerformanceCounter main = new PerformanceCounter("Main");
  PerformanceCounter secondary = new PerformanceCounter("secondary");
  PerformanceCounter third = new PerformanceCounter("third");

  private final Map map;

  public GameZone(int map) {
    Box2dWorld.dispose();
    Box2dWorld.getInstance();
    //создание камеры
    camera = new OrthographicCamera();
    camera.zoom = MAIN_ZOOM;
    camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0f);
    camera.update();

    GUI.getInstance().setAimCursor();

    beginDate = LocalDateTime.now();

    enemyService = new EnemyService();

    renderer = new SkeletonRenderer();
    renderer.setPremultipliedAlpha(true);

    if (!MOBILE) {
      new PlayerControlHandlerThread(camera).start();
    }

    this.stage = new MainStage(camera);
    this.map = MapService.initMap(map);

    this.secondStage = new SecondStage(camera);
    this.stage.addMap(this.map);

    this.secondStage.addPlayer(PlayerService.getInstance().getPlayer());
    Gdx.input.setInputProcessor(new MyInputProcessor(this));
  }

  @Override
  public void show() {

  }

  @Override
  public void render(float delta) {
    main.start();
//
//    System.out.println("time: " + main.time.value * 1000 + " : load: " + main.load.value * 1000);

    //1
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    AudioService.getInstance().playGameMusic();

    //0,003

    camera.update();

    stage.act();
    secondStage.act();

    if (MOBILE) {
      PlayerService.getInstance().handleMobileTouch(camera);
    } else {
      PlayerService.getInstance().transformPlayerBody();
      PlayerService.getInstance().handleCameraTransform(camera);
    }

//    handleWorldBounds();

    renderSpec(delta);

    //рисовать текстуры
    //4 около 50%

    secondary.start();
//    System.out.println("secondary time: " + secondary.time.value * 1000 + " : load: " + secondary.load.value * 1000);

    stage.draw();
    secondStage.draw();

    secondary.stop();
    secondary.tick();

    Box2dWorld.getInstance().render();

    GUI.getInstance().draw();

//6 до конца 0.006, тут норм
    //Нарисовать осветление экрана при старте
    if (!started) {
      started = FaderService.getInstance().drawFadeIn();
    }

    if (PlayerService.getInstance().getPlayer().isDead()) {
      if (threadForZoom == null) {
        secondStage.changeGroups();
        threadForZoom = new ThreadForZoom();
        threadForZoom.setDaemon(true);
        threadForZoom.start();
      }
      //нарисовать затемнение экрана при смерти
      if (FaderService.getInstance().drawFadeOut()) {
        endGameSession();
      }
    }
    Box2dWorld.getInstance().update();

    main.stop();
    main.tick();

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
  public void dispose() {
    Box2dWorld.dispose();
  }


  public void resumeGame() {
    GUI.getInstance().setAimCursor();
    Gdx.input.setInputProcessor(new MyInputProcessor(this));
  }

  public void endGameSession() {
    Gdx.gl.glClearColor(0, 0, 0, 1);
    Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    new Thread(() -> RpgStatsService.getInstance().increaseExp(enemyService.getRewardPointCount().get())).start();

    PlayerService.dispose();
    AudioService.getInstance().smoothStopMusic();

    MyGame.getInstance().setScreen(
        new EndGameMenu(enemyService.getRewardPointCount().get(),
            this.map.getMapPos(),
            beginDate,
            enemyService.getKillCount().get()));

    stage.dispose();
    secondStage.dispose();
    Gdx.graphics.setSystemCursor(SystemCursor.Arrow);
  }


  /**
   * обработать рамки камеры, чтобы не заходили за края
   */
  private void handleWorldBounds() {
    //TODO сделать потокобезопасным
//    new Thread(() -> {
    float camX = camera.position.x;
    float camY = camera.position.y;

    Vector2 camMin = new Vector2(camera.viewportWidth / 2, camera.viewportHeight / 2);
    camMin.scl(camera.zoom); //bring to center and scale by the zoom level
    Vector2 camMax = new Vector2(this.map.getWidth(), this.map.getHeight());
    camMax.sub(camMin); //bring to center

    //keep camera within borders
    camX = Math.min(camMax.x, Math.max(camX, camMin.x));
    camY = Math.min(camMax.y, Math.max(camY, camMin.y));

    camera.position.set(camX, camY, 0);

    if (DEBUG) {
      float camX2 = Box2dWorld.getInstance().getCameraBox2D().position.x;
      float camY2 = Box2dWorld.getInstance().getCameraBox2D().position.y;

      Vector2 camMin2 = new Vector2(Box2dWorld.getInstance().getCameraBox2D().viewportWidth / 2,
          Box2dWorld.getInstance().getCameraBox2D().viewportHeight / 2);
      camMin2.scl(Box2dWorld.getInstance().getCameraBox2D().zoom); //bring to center and scale by the zoom level
      Vector2 camMax2 = new Vector2(this.map.getWidth() / WORLD_TO_VIEW, this.map.getHeight() / WORLD_TO_VIEW);
      camMax2.sub(camMin2); //bring to center

      camX2 = Math.min(camMax2.x, Math.max(camX2, camMin2.x));
      camY2 = Math.min(camMax2.y, Math.max(camY2, camMin2.y));

      Box2dWorld.getInstance().getCameraBox2D().position.set(camX2, camY2, 0);
    }
//    }).start();
  }

  class ThreadForZoom extends Thread {

    @SneakyThrows
    @Override
    public void run() {
      while (true) {
        Thread.sleep(50);
        camera.zoom = camera.zoom - 0.12f;
        if (camera.zoom < 1) {
          break;
        }
      }
    }
  }
}
