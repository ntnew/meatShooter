package ru.meat.game.service;

import static ru.meat.game.settings.Constants.MAIN_ZOOM;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.TimeUtils;
import lombok.SneakyThrows;
import ru.meat.game.loader.LoaderManager;

public class FaderService {

  /**
   * Прозрачность чёрного экрана
   */
  private float alpha = 0;

  /**
   * флаг, что активирован поток изменения прозрачности
   */
  private boolean activated = false;

  /**
   * метка времени, по которой меняется прозрачность экрана
   */
  private long alphaTimeStamp = 0;

  /**
   * Камера фейдера
   */
  private final OrthographicCamera camera;

  /**
   * батч отрисовки чёрного экрана
   */
  private final SpriteBatch spriteBatch;

  /**
   * Спрайт чёрного экрана
   */
  private final Sprite blackoutSprite;

  /**
   * Флаг, что закончено затемнение/осветление
   */
  private boolean isDone = false;

  /**
   * Скорость затемнения/осветления экрана
   */
  private final long transparencySpeed = 6;

  private static FaderService instance;

  public static FaderService getInstance() {
    if (instance == null) {
      instance = new FaderService();
    }
    return instance;
  }

  public FaderService() {
    this.blackoutSprite = new Sprite((Texture) LoaderManager.getInstance().get("blackTexture.png"));
    this.blackoutSprite.setScale(5 * MAIN_ZOOM);

    camera = new OrthographicCamera();
    camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0f);
    spriteBatch = new SpriteBatch();
    spriteBatch.setProjectionMatrix(camera.combined);
  }

  public boolean drawFadeOut() {
    if (!activated) {
      activated = true;
      new ThreadForTransparency(true).start();
    }

    if (isDone) {
      activated = false;
      isDone = false;
      return true;
    } else {
      drawBlackScreen();
      return false;
    }
  }

  public boolean drawFadeIn() {
    if (!activated) {
      activated = true;
      new ThreadForTransparency(false).start();
    }

    if (isDone) {
      activated = false;
      isDone = false;
      return true;
    } else {
      drawBlackScreen();
      return false;
    }
  }

  private void drawBlackScreen() {
    spriteBatch.begin();
    blackoutSprite.setPosition(camera.position.x - camera.viewportWidth * MAIN_ZOOM / 2,
        camera.position.y - camera.viewportHeight * MAIN_ZOOM / 2);
    blackoutSprite.setAlpha(alpha);
    blackoutSprite.draw(spriteBatch);
    spriteBatch.end();
  }


  class ThreadForTransparency extends Thread {

    private final boolean isIncreasingAlpha;

    ThreadForTransparency(boolean isIncreasingAlpha) {
      this.isIncreasingAlpha = isIncreasingAlpha;
      alpha = isIncreasingAlpha ? 0 : 1;
    }

    @SneakyThrows
    @Override
    public void run() {
      while (true) {
        if (alphaTimeStamp == 0 || TimeUtils.timeSinceMillis(alphaTimeStamp) > transparencySpeed) {
          alpha = isIncreasingAlpha ? alpha + 0.005f : alpha - 0.005f;
          alphaTimeStamp = TimeUtils.millis();
        }
        if ((isIncreasingAlpha && alpha >= 1) || (!isIncreasingAlpha && alpha <= 0)) {
          isDone = true;
          break;
        }
      }
    }
  }
}