package ru.meat.game.model.weapons.explosions;

import static ru.meat.game.settings.Constants.MAIN_ZOOM;
import static ru.meat.game.settings.Constants.TEXTURE_PARAMETERS;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.TimeUtils;
import java.util.UUID;
import lombok.Data;
import ru.meat.game.MyGame;
import ru.meat.game.loader.LoaderManager;
import ru.meat.game.model.FloatPair;
import ru.meat.game.service.AudioService;
import ru.meat.game.settings.Filters;
import ru.meat.game.utils.AniLoadUtil;
import ru.meat.game.utils.GDXUtils;

@Data
public class ExplosionsService {

  private Animation<TextureRegion> fireExplosionAnimation;

  private Animation<TextureRegion> acidExplosionAnimation;

  private static final int FRAME_COLS = 8, FRAME_ROWS = 6;

  private static ExplosionsService instance;

  public static ExplosionsService getInstance() {
    if (instance == null) {
      instance = new ExplosionsService();
    }
    return instance;
  }

  public ExplosionsService() {

    fireExplosionAnimation = AniLoadUtil.getAniFromResources("ani/explosion.png", 0.01f,
        FRAME_COLS,
        FRAME_ROWS);

    TextureRegion[] l = new TextureRegion[10];

    for (int i = 0; i < 10; i++) {
      Texture o = LoaderManager.getInstance().get("ani/widowAttack/explosion/" + i + ".png");
      o.setFilter(TextureFilter.MipMapLinearLinear, TextureFilter.MipMapLinearLinear);
      l[i] = new TextureRegion(o);
    }

    acidExplosionAnimation = new Animation<>(0.03f, l);
  }

  public static void initResources() {
    LoaderManager.getInstance().load("ani/explosion.png", Texture.class, TEXTURE_PARAMETERS);

    for (int i = 0; i < 10; i++) {
      LoaderManager.getInstance().load("ani/widowAttack/explosion/" + i + ".png", Texture.class, TEXTURE_PARAMETERS);
    }
  }

  public void createFireExplosion(FloatPair pos, float damage) {
    Gdx.app.postRunnable(() -> {
      AudioService.getInstance().playExplosionSound();
      ExplosionBodyUserData explosionBodyUserData =
          new ExplosionBodyUserData("explosion", damage, UUID.randomUUID(), TimeUtils.millis());
      Body body = GDXUtils.createCircleForModel(12 / MAIN_ZOOM, 100, explosionBodyUserData, pos.getX(), pos.getY(),
          true);
      body.getFixtureList().get(0).setFilterData(Filters.getPlayerBulletFilter());
      MyGame.getInstance().getGameZone().getSecondStage()
          .addExplosion(new FireExplosion(pos, body));
    });
  }

  public void createAcidExplosion(FloatPair pos) {
    new Thread(() ->
        MyGame.getInstance().getGameZone().getStage()
            .addBlood(new AcidExplosion(pos))).start();
  }
}
