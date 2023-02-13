package ru.meat.game.service;

import static ru.meat.game.settings.Constants.MAIN_ZOOM;
import static ru.meat.game.settings.Constants.WORLD_TO_VIEW;

import com.badlogic.gdx.assets.loaders.TextureLoader.TextureParameter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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

  private SpriteBatch batch;

  public List<BloodSpot> spots = new ArrayList<>();

  public final float bloodDiffusion = 300 / MAIN_ZOOM;
  private static BloodService instance;

  public static BloodService getInstance() {
    if (instance == null) {
      instance = new BloodService();
    }
    return instance;
  }

  public BloodService() {
    TextureParameter param = new TextureParameter();
    param.genMipMaps = true;

    littleBloodPngName.forEach(x -> LoaderManager.getInstance().load(x, Texture.class, param));
    bigBloodPngName.forEach(x -> LoaderManager.getInstance().load(x, Texture.class, param));

    batch = new SpriteBatch();
  }

  public void createBloodSpot(FloatPair coord) {

  }

  public void drawBloodSpots(OrthographicCamera camera) {
    batch.setProjectionMatrix(camera.combined);
    batch.begin();
    spots.forEach(x -> x.getSprite().draw(batch));
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

    spots.add(new BloodSpot(sprite));
  }

  public void dispose() {
    spots.clear();
  }
}
