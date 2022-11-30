package ru.meat.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import ru.meat.game.model.Player;

public class MeatShooterClass extends ApplicationAdapter implements InputProcessor {

  TiledMap tiledMap;
  OrthographicCamera camera;
  TiledMapRenderer tiledMapRenderer;
  Player player;
  float stateTime;
  SpriteBatch spriteBatch;

  @Override
  public void create() {

    Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());

    player = new Player();

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
    stateTime = 0f;
  }

  @Override
  public void render() {
    handlekey();
    Gdx.gl.glClearColor(0, 0, 0, 1);
    Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    stateTime += Gdx.graphics.getDeltaTime(); // Accumulate elapsed animation time

    // Get current frame of animation for the current stateTime
//    TextureRegion currentFrame = walkAnimation.getKeyFrame(stateTime, true);
    camera.update();

    Texture textureF = player.getRunAnimation().getKeyFrame(stateTime, true);
    tiledMapRenderer.setView(camera);
    tiledMapRenderer.render();
    spriteBatch.begin();
    spriteBatch.draw(textureF, 50, 50);
    spriteBatch.end();
  }

  private void handlekey() {
    if (Gdx.input.isKeyPressed(Keys.BACKSPACE)) {
      camera.translate(-50, 0);
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
    return false;
  }

  @Override
  public boolean keyUp(int keycode) {
    return false;
  }

  @Override
  public boolean keyTyped(char character) {
    if (Gdx.input.isKeyPressed(Input.Keys.A)) {
      camera.zoom += 0.1;
    }
    if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
      camera.zoom -= 0.1;
    }
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
