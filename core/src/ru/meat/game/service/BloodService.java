package ru.meat.game.service;

import static ru.meat.game.settings.Constants.MAIN_ZOOM;
import static ru.meat.game.settings.Constants.MOBILE;
import static ru.meat.game.settings.Constants.TEXTURE_PARAMETERS;
import static ru.meat.game.settings.Constants.WORLD_TO_VIEW;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import java.util.Arrays;
import java.util.List;
import lombok.Data;
import ru.meat.game.MyGame;
import ru.meat.game.loader.LoaderManager;
import ru.meat.game.model.Bleeding;
import ru.meat.game.model.BloodSpot;
import ru.meat.game.model.FloatPair;
import ru.meat.game.utils.AniLoadUtil;

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

  private static final String bleedAniPath = "blood/ani1/bloodBlow1.png";

  private Animation<TextureRegion> bleedAnimation;

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

    LoaderManager.getInstance().load(bleedAniPath, Texture.class, TEXTURE_PARAMETERS);
  }

  public void createBleeding(float x, float y) {
    new Thread(() -> MyGame.getInstance().getGameZone().getSecondStage()
        .addBleeding(new Bleeding(bleedAnimation, new FloatPair(x * WORLD_TO_VIEW, y * WORLD_TO_VIEW)))).start();
  }

  /**
   * Загрузить анимацию кровотечения от попадания пули
   */
  private void initializeBleedingAnimation() {
    bleedAnimation = AniLoadUtil.getAniFromResources(bleedAniPath, 0.06f, 4, 2);
  }

  /**
   * Создать маленькую текстуру крови
   *
   * @param coord координаты где создать в мире текстур
   */
  public void createLittleBloodSpot(FloatPair coord) {
    new Thread(() -> MyGame.getInstance().getGameZone().getStage().addBlood(new BloodSpot(coord))).start();
  }
}
