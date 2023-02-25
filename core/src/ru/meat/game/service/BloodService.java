package ru.meat.game.service;

import static ru.meat.game.settings.Constants.MAIN_ZOOM;
import static ru.meat.game.settings.Constants.TEXTURE_PARAMETERS;
import static ru.meat.game.settings.Constants.WORLD_TO_VIEW;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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

  private List<String> littleBloodPngName = Arrays.asList(
      "blood/spot/1.png",
      "blood/spot/2.png",
      "blood/spot/3.png");
  private List<String> bigBloodPngName = Arrays.asList(
      "blood/spot/4.png",
      "blood/spot/5.png",
      "blood/spot/6.png",
      "blood/spot/7.png");

  private final String bleedAniPath = "blood/ani1/b";

  private SpriteBatch batch;

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
    littleBloodPngName.forEach(x -> LoaderManager.getInstance().load(x, Texture.class, TEXTURE_PARAMETERS));
    bigBloodPngName.forEach(x -> LoaderManager.getInstance().load(x, Texture.class, TEXTURE_PARAMETERS));

    for (int i = 0; i < 8; i++) {
      LoaderManager.getInstance().load(bleedAniPath + i + ".png", Texture.class, TEXTURE_PARAMETERS);
    }

    batch = new SpriteBatch();
  }

  public void createBleeding(float x, float y) {
    if (bleedAnimation == null) {
      Texture[] bleedTextures = new Texture[8];
      for (int i = 0; i < 8; i++) {
        bleedTextures[i] = LoaderManager.getInstance().get(bleedAniPath + i + ".png");
      }
      for (Texture bleedTexture : bleedTextures) {
        bleedTexture.setFilter(TextureFilter.MipMapLinearLinear, TextureFilter.MipMapLinearLinear);
      }
      bleedAnimation = new Animation<>(0.07f, bleedTextures);
    }
    float z = Gdx.graphics.getDeltaTime();

    Sprite sprite = new Sprite(bleedAnimation.getKeyFrame(z));
    sprite.setPosition(x * WORLD_TO_VIEW - sprite.getWidth() / 2, y * WORLD_TO_VIEW - sprite.getHeight() / 2);
    sprite.setScale(1.5f);
    bleeds.add(new BloodSpot(sprite, z));
  }

  public void drawBleeds(OrthographicCamera camera) {
    batch.setProjectionMatrix(camera.combined);
    batch.begin();
    bleeds.forEach(x -> {
      x.setStateTime(x.getStateTime() + Gdx.graphics.getDeltaTime());
      x.getSprite().setTexture(bleedAnimation.getKeyFrame(x.getStateTime()));
      x.getSprite().draw(batch);
    });
    batch.end();

    bleeds.removeIf(x -> Objects.equals(x.getSprite().getTexture(), bleedAnimation.getKeyFrames()[bleedAnimation.getKeyFrames().length-1]));
  }

  public void createBloodSpot(FloatPair coord) {

  }

  public void drawBloodSpots(OrthographicCamera camera) {
    batch.setProjectionMatrix(camera.combined);
    batch.begin();
    spots.forEach(x -> x.draw(batch));
    batch.end();
  }

  /**
   * Создать маленькую текстуру крови
   *
   * @param coord координаты где создать в мире текстур
   */
  public void createLittleBloodSpot(FloatPair coord) {
    int random = MathUtils.random(0, littleBloodPngName.size() - 1);

    Texture texture = LoaderManager.getInstance().get(littleBloodPngName.get(random));
    texture.setFilter(TextureFilter.MipMapLinearLinear, TextureFilter.MipMapLinearLinear);
    Sprite sprite = new Sprite(texture);

    sprite.setPosition(coord.getX() - sprite.getWidth() / 2 + MathUtils.random(-bloodDiffusion, +bloodDiffusion),
        coord.getY() - sprite.getHeight() / 2 - MathUtils.random(-bloodDiffusion, +bloodDiffusion));

    sprite.rotate(MathUtils.random(0, 359));

    spots.add(sprite);
  }

  public void dispose() {
    spots.clear();
    bleeds.clear();
  }
}
