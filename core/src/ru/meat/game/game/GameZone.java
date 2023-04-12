package ru.meat.game.game;

import static ru.meat.game.settings.Constants.MAIN_ZOOM;
import static ru.meat.game.settings.Constants.MOBILE;

import com.badlogic.gdx.Gdx;
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
import java.util.stream.Collectors;
import lombok.Data;
import lombok.SneakyThrows;
import ru.meat.game.Box2dWorld;
import ru.meat.game.MyGame;
import ru.meat.game.gui.GUI;
import ru.meat.game.menu.EndGameMenu;
import ru.meat.game.model.EnemyStatus;
import ru.meat.game.model.player.PlayerService;
import ru.meat.game.model.weapons.explosions.ExplosionsService;
import ru.meat.game.service.AudioService;
import ru.meat.game.service.BloodService;
import ru.meat.game.service.BulletService;
import ru.meat.game.service.EnemyService;
import ru.meat.game.service.FaderService;
import ru.meat.game.service.MapService;
import ru.meat.game.service.RpgStatsService;

@Data
public abstract class GameZone implements Screen {

  protected MapService mapService;

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

//  PerformanceCounter main = new PerformanceCounter("Main");
//  PerformanceCounter secondary = new PerformanceCounter("secondary");

  private final int map;

  public GameZone(int map) {
    this.map = map;

    //создание камеры
    camera = new OrthographicCamera();
    camera.zoom = MAIN_ZOOM;
    camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0f);
    camera.update();

    GUI.getInstance().initFullHp();
    GUI.getInstance().setAimCursor();

    beginDate = LocalDateTime.now();

    enemyService = new EnemyService();

    spriteBatch = new SpriteBatch();

    renderer = new SkeletonRenderer();
    renderer.setPremultipliedAlpha(true);

    polyBatch = new PolygonSpriteBatch();

    new PlayerControlHandlerThread().start();

    MyGame.getInstance().initGameStage(camera);
    MyGame.getInstance().addActor(MapService.initMap(map));
    Gdx.input.setInputProcessor(new MyInputProcessor(this));
  }

  @Override
  public void show() {

  }

  @Override
  public void render(float delta) {
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    AudioService.getInstance().playGameMusic();
    if (TimeUtils.timeSinceMillis(comparatorTime) > 4000) {
      sortEnemies();
    }


    camera.update();
    Box2dWorld.getInstance().update();

    BloodService.getInstance().update();
    PlayerService.getInstance().updateState();
    BulletService.getInstance().updateBullets();

    if (MOBILE) {
      PlayerService.getInstance().handleMobileTouch(camera);
    } else {
      PlayerService.getInstance().transformPlayerBody();
      PlayerService.getInstance().handleCameraTransform(camera);
    }

    handleWorldBounds();

    renderSpec(delta);

    enemyService.actionEnemies(PlayerService.getInstance().getBodyPosX(), PlayerService.getInstance().getBodyPosY());

    //рисовать текстуры

    //4 около 50%

    polyBatch.setProjectionMatrix(camera.combined);

    MyGame.getInstance().drawStage();

    spriteBatch.setProjectionMatrix(camera.combined);
    spriteBatch.begin();
    BloodService.getInstance().drawBleeds(spriteBatch);
    spriteBatch.end();

    polyBatch.begin();
    //Если игрок умер, то рисуем раньше врагов
    if (PlayerService.getInstance().getPlayer().isDead()) {
      PlayerService.getInstance().drawPlayer(polyBatch, renderer);
    }
    BulletService.getInstance().drawPlayerBullets(camera);
    enemyService.drawEnemies(polyBatch, renderer);
    BulletService.getInstance().drawEnemyBullets(camera);
    //Если игрок жив, то рисуем после врагов
    if (!PlayerService.getInstance().getPlayer().isDead()) {
      PlayerService.getInstance().drawPlayer(polyBatch, renderer);
    }
    polyBatch.end();

//5 статически, не увеличивается
    ExplosionsService.getInstance().drawExplosions(camera);

    Box2dWorld.getInstance().render();

    GUI.getInstance().draw();

    //Нарисовать осветление экрана при старте
    if (!started) {
      started = FaderService.getInstance().drawFadeIn();
    }

    if (PlayerService.getInstance().getPlayer().isDead()) {
      if (threadForZoom == null) {
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

//    main.stop();
//    main.tick();
  }

  /**
   * Остортировать врагов, чтобы сначала рисовались трупы
   */
  private void sortEnemies() {
    comparatorTime = TimeUtils.millis();
    enemyService.setEnemies(enemyService.getEnemies().parallelStream().sorted((x, y) -> {
      if (x.getStatus().equals(EnemyStatus.DIED) && !y.getStatus().equals(EnemyStatus.DIED)) {
        return -1;
      } else if ((x.getStatus().equals(EnemyStatus.DIED) && y.getStatus().equals(EnemyStatus.DIED))
          || (!x.getStatus().equals(EnemyStatus.DIED) && !y.getStatus().equals(EnemyStatus.DIED))) {
        return 0;
      } else {
        return 1;
      }
    }).collect(Collectors.toList()));
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

    PlayerService.endGameSession();
    AudioService.getInstance().smoothStopMusic();

    MyGame.getInstance().setScreen(
        new EndGameMenu(enemyService.getRewardPointCount().get(),
            map,
            beginDate,
            enemyService.getKillCount().get()));

    Gdx.graphics.setSystemCursor(SystemCursor.Arrow);

    Box2dWorld.dispose();
    BulletService.dispose();
    BloodService.getInstance().dispose();
    ExplosionsService.getInstance().dispose();
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
