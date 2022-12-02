package ru.meat.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import java.util.ArrayList;
import java.util.List;
import ru.meat.game.model.CharacterFeetStatus;
import ru.meat.game.model.CharacterTopStatus;
import ru.meat.game.model.Enemy;
import ru.meat.game.service.EnemyService;
import ru.meat.game.service.PlayerService;

public class MeatShooterClass extends ApplicationAdapter implements InputProcessor {


  private final InputProcessor inputProcessor;
  TiledMap tiledMap;
  OrthographicCamera camera;
  TiledMapRenderer tiledMapRenderer;
  private PlayerService playerService;
  private EnemyService enemyService;

   private float stateTime;

  private List<Enemy> enemies = new ArrayList<>();
  SpriteBatch spriteBatch;


  public MeatShooterClass(InputProcessor inputProcessor) {
    this.inputProcessor = inputProcessor;
  }

  @Override
  public void create() {
//    Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
// fullscreen
    playerService = new PlayerService();
    enemyService = new EnemyService();

    float w = Gdx.graphics.getWidth();
    float h = Gdx.graphics.getHeight();

    camera = new OrthographicCamera();
    camera.setToOrtho(false, w, h);
    camera.zoom = 2.4f;
    camera.update();

    tiledMap = new TmxMapLoader().load("1map.tmx");
    tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
    Gdx.input.setInputProcessor(this);
    spriteBatch = new SpriteBatch();
    camera.position.set(1600,1000,0);


    enemies.add(new Enemy(50,50));
  }

  @Override
  public void render() {

    playerService.rotateModel();

    handleKey();
    Gdx.gl.glClearColor(0, 0, 0, 1);
    Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    stateTime += Gdx.graphics.getDeltaTime();
    playerService.updateStateTime();
//    enemyService.updateStateTime();
    camera.update();

    tiledMapRenderer.setView(camera);
    tiledMapRenderer.render();

    enemies.parallelStream().forEach( enemy -> {
      enemyService.move(playerService.getPosX(), playerService.getPosY(), enemy);
    });

    spriteBatch.begin();
    playerService.drawPlayer(spriteBatch);
    enemies.parallelStream().forEach(enemy -> enemyService.drawEnemySprite(spriteBatch,enemy, stateTime));

    spriteBatch.end();
  }

  private void handleKey() {
    if (Gdx.input.isKeyPressed(Input.Keys.A)) {
      playerService.moveLeft();
    }
    if (Gdx.input.isKeyPressed(Input.Keys.W)) {
      playerService.moveUp();
    }
    if (Gdx.input.isKeyPressed(Input.Keys.S)) {
      playerService.moveDown();
    }
    if (Gdx.input.isKeyPressed(Input.Keys.D)) {
      playerService.moveRight();
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
    if (keycode == Input.Keys.NUM_1) {
      tiledMap.getLayers().get(0).setVisible(!tiledMap.getLayers().get(0).isVisible());
    }
    if (keycode == Input.Keys.NUM_2) {
      tiledMap.getLayers().get(1).setVisible(!tiledMap.getLayers().get(1).isVisible());
    }
    if (Gdx.input.isKeyPressed(Keys.BACKSPACE)) {
      camera.zoom += 0.1;
    }
    if (Gdx.input.isKeyPressed(Keys.MINUS)) {
      camera.zoom -= 0.1;
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


}
