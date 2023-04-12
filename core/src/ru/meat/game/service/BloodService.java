package ru.meat.game.service;

import static ru.meat.game.settings.Constants.MAIN_ZOOM;
import static ru.meat.game.settings.Constants.MOBILE;
import static ru.meat.game.settings.Constants.TEXTURE_PARAMETERS;
import static ru.meat.game.settings.Constants.WORLD_TO_VIEW;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import lombok.Data;
import ru.meat.game.MyGame;
import ru.meat.game.loader.LoaderManager;
import ru.meat.game.model.BloodSpot;
import ru.meat.game.model.FloatPair;
import ru.meat.game.model.Bleeding;

@Data
public class BloodService {

  private static List<String> littleBloodPngName = Arrays.asList(
      "blood/spot/1.png",
      "blood/spot/2.png",
      "blood/spot/3.png");

  private static List<String> bigBloodPngName = Arrays.asList(
      "blood/spot/4.png",
      "blood/spot/5.png",
      "blood/spot/6.png",
      "blood/spot/7.png");

  private static final String bleedAniPath = "blood/ani1/b";
  public final List<Bleeding> bleeds = new ArrayList<>();


  private Animation<Texture> bleedAnimation;

  public final float bloodDiffusion = 230 / MAIN_ZOOM;
  private static BloodService instance;

  public static BloodService getInstance() {
    if (instance == null) {
      instance = new BloodService();
    }
    return instance;
  }

  public BloodService() {
    littleBloodPngName.forEach(s -> {
      Texture texture = LoaderManager.getInstance().get(s);
      if (MOBILE) {
        texture.setFilter(TextureFilter.MipMapNearestNearest, TextureFilter.MipMapNearestNearest);
      } else {
        texture.setFilter(TextureFilter.MipMapLinearLinear, TextureFilter.MipMapLinearLinear);
      }
      initializeBleedingAnimation();

      BloodSpot.getBloodSpotTextures().add(texture);
    });
  }

  public static void loadResources() {
    littleBloodPngName.forEach(x -> LoaderManager.getInstance().load(x, Texture.class, TEXTURE_PARAMETERS));
    bigBloodPngName.forEach(x -> LoaderManager.getInstance().load(x, Texture.class, TEXTURE_PARAMETERS));

    for (int i = 0; i < 8; i++) {
      LoaderManager.getInstance().load(bleedAniPath + i + ".png", Texture.class, TEXTURE_PARAMETERS);
    }
  }

  public void createBleeding(float x, float y) {
    new Thread(() -> {
      float z = Gdx.graphics.getDeltaTime();

      Sprite sprite = new Sprite(bleedAnimation.getKeyFrame(z));
      sprite.setPosition(x * WORLD_TO_VIEW - sprite.getWidth() / 2, y * WORLD_TO_VIEW - sprite.getHeight() / 2);
      sprite.setScale(1.5f);
      synchronized (bleeds) {
        bleeds.add(new Bleeding(sprite, z));
      }
    }).start();
  }

  /**
   * Загрузить анимацию кровотечения от попадания пули
   */
  private void initializeBleedingAnimation() {
    Texture[] bleedTextures = new Texture[8];
    for (int i = 0; i < 8; i++) {
      bleedTextures[i] = LoaderManager.getInstance().get(bleedAniPath + i + ".png");
    }
    for (Texture bleedTexture : bleedTextures) {
      bleedTexture.setFilter(TextureFilter.MipMapLinearNearest, TextureFilter.MipMapLinearNearest);
    }
    bleedAnimation = new Animation<>(0.07f, bleedTextures);
  }

  public void drawBleeds(Batch batch) {
    synchronized (bleeds) {
      for (Bleeding x : bleeds) {
        x.getSprite().draw(batch);
      }
      bleeds.removeIf(x -> Objects.equals(x.getSprite().getTexture(),
          bleedAnimation.getKeyFrames()[bleedAnimation.getKeyFrames().length - 1]));
    }
  }

  /**
   * Создать маленькую текстуру крови
   *
   * @param coord координаты где создать в мире текстур
   */
  public void createLittleBloodSpot(FloatPair coord) {
    new Thread(() -> MyGame.getInstance().addActor(new BloodSpot(coord))).start();
  }

  public void dispose() {
    synchronized (bleeds) {
      bleeds.clear();
    }
  }

  public void update() {
    synchronized (bleeds) {
      for (Bleeding x : bleeds) {
        x.setStateTime(x.getStateTime() + Gdx.graphics.getDeltaTime());
        x.getSprite().setTexture(bleedAnimation.getKeyFrame(x.getStateTime()));
      }
    }
  }
}
