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
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.World;
import java.util.ArrayList;
import java.util.List;
import ru.meat.game.model.CharacterFeetStatus;
import ru.meat.game.model.CharacterTopStatus;
import ru.meat.game.model.Enemy;
import ru.meat.game.model.FloatPair;
import ru.meat.game.model.weapons.Bullet;
import ru.meat.game.service.BulletService;
import ru.meat.game.service.EnemyService;
import ru.meat.game.service.MapService;
import ru.meat.game.service.PlayerService;
import ru.meat.game.utils.GDXUtils;
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

  private List<Enemy> enemies = new ArrayList<>();
  private SpriteBatch spriteBatch;
  private World world;
  private WorldRenderer worldRenderer;

  private MapService mapService;

  private List<Bullet> bullets = new ArrayList<>();
  public MeatShooterClass(InputProcessor inputProcessor) {
    this.inputProcessor = inputProcessor;
  }

  @Override
  public void create() {
    float w = Gdx.graphics.getWidth();
    float h = Gdx.graphics.getHeight();

//    Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
// fullscreen
    playerService = new PlayerService(w / 2, h / 2);
    enemyService = new EnemyService();
    mapService = new MapService();
    mapService.initMap();
    world = new World(new Vector2(0, 0), true);

    camera = new OrthographicCamera();
    camera.setToOrtho(false, w, h);
    camera.zoom = 1f;
    camera.update();

    spriteBatch = new SpriteBatch();
    Gdx.input.setInputProcessor(this);

    worldRenderer = new WorldRenderer(world, true, camera);

    enemies.add(enemyService.createZombieEnemy(50f, 50f));
//    enemies.add(enemyService.createZombieEnemy(100f, 100f));
//    enemies.add(enemyService.createZombieEnemy(MathUtils.random(0, Gdx.graphics.getWidth()),
//        MathUtils.random(0, Gdx.graphics.getHeight())));
//    enemies.add(enemyService.createZombieEnemy(MathUtils.random(0, Gdx.graphics.getWidth()),
//        MathUtils.random(0, Gdx.graphics.getHeight())));
//    enemies.add(enemyService.createZombieEnemy(MathUtils.random(0, Gdx.graphics.getWidth()),
//        MathUtils.random(0, Gdx.graphics.getHeight())));

    enemies.forEach(
        x -> x.setBox(GDXUtils.createCircleForEnemy(world, x.getRadius()/ StaticFloats.WORLD_TO_VIEW, 80, "z1", x.getPosX(), x.getPosY())));
  }

  @Override
  public void render() {

    camera.update();

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


    playerService.rotateModel(camera);
    worldRenderer.getCameraBox2D().update();

    spriteBatch.setProjectionMatrix(camera.combined);
    spriteBatch.begin();

    mapService.draw(spriteBatch);
    playerService.drawPlayer(spriteBatch);
    enemies.forEach(enemy -> {
      enemyService.drawEnemySprite(spriteBatch, enemy, stateTime);
      enemy.setPreviousStatus(enemy.getStatus());
    });
    spriteBatch.end();
    worldRenderer.render(stateTime, enemies);

  }

  private void handleKey() {
    if (Gdx.input.isKeyPressed(Input.Keys.A)) {
      float x = playerService.moveLeft();
      camera.translate(x,0);
      worldRenderer.getCameraBox2D().translate(x/StaticFloats.WORLD_TO_VIEW,0);
    }

    if (Gdx.input.isKeyPressed(Input.Keys.W)) {
      float y = playerService.moveUp();
      camera.translate(0,y);
      worldRenderer.getCameraBox2D().translate(0,y/StaticFloats.WORLD_TO_VIEW);
    }

    if (Gdx.input.isKeyPressed(Input.Keys.S)) {
      float y = playerService.moveDown();
      camera.translate(0,y);
      worldRenderer.getCameraBox2D().translate(0, y/StaticFloats.WORLD_TO_VIEW);
    }

    if (Gdx.input.isKeyPressed(Input.Keys.D)) {
      float x = playerService.moveRight();
      camera.translate(x,0);
      worldRenderer.getCameraBox2D().translate(x/StaticFloats.WORLD_TO_VIEW,0);
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
      playerService.shoot(camera, world, screenX, screenY);
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
