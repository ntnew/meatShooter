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
import ru.meat.game.loader.LoaderManager;
import ru.meat.game.model.FloatPair;
import ru.meat.game.model.enemies.BloodSpot;

@Data
public class BloodService {

  private static List<String> littleBloodPngName = Arrays.asList(
      "blood/spot/1.png",
      "blood/spot/2.png",
      "blood/spot/3.png");

  private List<Texture> bloodSpotTextures = new ArrayList<>();
  private static List<String> bigBloodPngName = Arrays.asList(
      "blood/spot/4.png",
      "blood/spot/5.png",
      "blood/spot/6.png",
      "blood/spot/7.png");

  private static final String bleedAniPath = "blood/ani1/b";

  public List<Sprite> spots = new ArrayList<>();
  public List<BloodSpot> bleeds = new ArrayList<>();


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

      bloodSpotTextures.add(texture);
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
    if (bleedAnimation == null) {
      initializeBleedingAnimation();
    }
    float z = Gdx.graphics.getDeltaTime();

    Sprite sprite = new Sprite(bleedAnimation.getKeyFrame(z));
    sprite.setPosition(x * WORLD_TO_VIEW - sprite.getWidth() / 2, y * WORLD_TO_VIEW - sprite.getHeight() / 2);
    sprite.setScale(1.5f);
    bleeds.add(new BloodSpot(sprite, z));
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
    bleeds.forEach(x -> x.getSprite().draw(batch));
  }

  public void createBloodSpot(FloatPair coord) {

  }

  public void drawBloodSpots(Batch batch) {
    //TODO лагает
    spots.forEach(x -> x.draw(batch));
  }

  /**
   * Создать маленькую текстуру крови
   *
   * @param coord координаты где создать в мире текстур
   */
  public void createLittleBloodSpot(FloatPair coord) {
    Sprite sprite = new Sprite(bloodSpotTextures.get(MathUtils.random(0, bloodSpotTextures.size() - 1)));

    sprite.setPosition(coord.getX() - sprite.getWidth() / 2 + MathUtils.random(-bloodDiffusion, +bloodDiffusion),
        coord.getY() - sprite.getHeight() / 2 - MathUtils.random(-bloodDiffusion, +bloodDiffusion));

    sprite.rotate(MathUtils.random(0, 359));

    spots.add(sprite);
  }

  public void dispose() {
    spots.clear();
    bleeds.clear();
  }

  public void update() {
    bleeds.removeIf(x -> Objects.equals(x.getSprite().getTexture(),
        bleedAnimation.getKeyFrames()[bleedAnimation.getKeyFrames().length - 1]));
    bleeds.parallelStream().forEach(x -> {
      x.setStateTime(x.getStateTime() + Gdx.graphics.getDeltaTime());
      x.getSprite().setTexture(bleedAnimation.getKeyFrame(x.getStateTime()));
    });
  }
}
