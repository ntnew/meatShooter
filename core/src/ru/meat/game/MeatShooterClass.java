package ru.meat.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import java.util.ArrayList;
import java.util.List;
import ru.meat.game.model.CharacterFeetStatus;
import ru.meat.game.model.CharacterTopStatus;
import ru.meat.game.model.Enemy;
import ru.meat.game.model.FloatPair;
import ru.meat.game.service.EnemyService;
import ru.meat.game.service.MapService;
import ru.meat.game.service.PlayerService;

public class MeatShooterClass extends ApplicationAdapter implements InputProcessor {

  private final InputProcessor inputProcessor;
  private OrthographicCamera camera;
  private PlayerService playerService;
  private EnemyService enemyService;
  private float stateTime;

  private List<Enemy> enemies = new ArrayList<>();
  private SpriteBatch spriteBatch;


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
    playerService = new PlayerService(w/2,h/2);
    enemyService = new EnemyService();
    mapService = new MapService();
    mapService.initMap();

    camera = new OrthographicCamera();
    camera.setToOrtho(false, w, h);
    camera.zoom = 1f;
    camera.update();
//    camera.position.set(1600, 1000, 0);

    spriteBatch = new SpriteBatch();

    Gdx.input.setInputProcessor(this);

    enemies.add(enemyService.createZombieEnemy(50f, 50f));
//    enemies.add(enemyService.createZombieEnemy(100f, 100f));
//    enemies.add(enemyService.createZombieEnemy(MathUtils.random(0, Gdx.graphics.getWidth()),
//        MathUtils.random(0, Gdx.graphics.getHeight())));
//    enemies.add(enemyService.createZombieEnemy(MathUtils.random(0, Gdx.graphics.getWidth()),
//        MathUtils.random(0, Gdx.graphics.getHeight())));
//    enemies.add(enemyService.createZombieEnemy(MathUtils.random(0, Gdx.graphics.getWidth()),
//        MathUtils.random(0, Gdx.graphics.getHeight())));

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

    enemyService.correctDistanceBetweenEnemies(enemies);
    enemies.parallelStream().forEach(enemy -> {
      enemyService.doSomething(playerService.getPosX(), playerService.getPosY(), enemy);
    });

    camera.update();
    spriteBatch.begin();
    mapService.draw(spriteBatch);
    playerService.drawPlayer(spriteBatch);
    enemies.forEach(enemy -> {
      enemyService.drawEnemySprite(spriteBatch, enemy, stateTime);
      enemy.setPreviousStatus(enemy.getStatus());
    });
    spriteBatch.end();
  }

  private void handleKey() {
    if (Gdx.input.isKeyPressed(Input.Keys.A)) {
      playerService.moveLeft();
      moveAllObjects(playerService.getSpeed() * playerService.getMoveMultiplier(), 0);
    }
    if (Gdx.input.isKeyPressed(Input.Keys.W)) {
      playerService.moveUp();
      moveAllObjects(0, -playerService.getSpeed() * playerService.getMoveMultiplier());

    }
    if (Gdx.input.isKeyPressed(Input.Keys.S)) {
      playerService.moveDown();
      moveAllObjects(0, playerService.getSpeed() * playerService.getMoveMultiplier());

    }
    if (Gdx.input.isKeyPressed(Input.Keys.D)) {
      playerService.moveRight();
      moveAllObjects(-playerService.getSpeed() * playerService.getMoveMultiplier(), 0);

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
      moveAllObjects(-50, 0);
    }
    if (keycode == Input.Keys.RIGHT) {
      camera.translate(50, 0);
      moveAllObjects(50, 0);
    }
    if (keycode == Input.Keys.UP) {
      camera.translate(0, -50);
      moveAllObjects(0, -50);
    }
    if (keycode == Input.Keys.DOWN) {
      camera.translate(0, 50);
      moveAllObjects(0, 50);
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

  private void moveAllObjectsWithoutPlayer(float x, float y) {
    enemies.parallelStream().forEach(enemy -> {
      FloatPair destination = enemy.getFloatDestination();
      enemy.setFloatDestination(new FloatPair(destination.getX() + x, destination.getY() + y));
      enemy.setPosX(enemy.getPosX() + x);
      enemy.setPosY(enemy.getPosY() + y);
    });
    mapService.moveMap(x, y);
  }

  private void moveAllObjects(float x, float y) {
    playerService.moveOnChangeMap(x, y);
    moveAllObjectsWithoutPlayer(x, y);
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
      playerService.shoot();
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
