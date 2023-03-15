package ru.meat.game.model.weapons.explosions;

import static ru.meat.game.settings.Constants.MAIN_ZOOM;
import static ru.meat.game.settings.Constants.TEXTURE_PARAMETERS;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.Data;
import ru.meat.game.Box2dWorld;
import ru.meat.game.loader.LoaderManager;
import ru.meat.game.model.FloatPair;
import ru.meat.game.service.AudioService;
import ru.meat.game.settings.Filters;
import ru.meat.game.utils.GDXUtils;

@Data
public class Explosions {

  private final List<Explosion> explosions = new ArrayList<>();

  private SpriteBatch batch = new SpriteBatch();

  private Texture fireAnimationSheet;

  private Animation<TextureRegion> fireExplosionAnimation;


  private Animation<Texture> acidExplosionAnimation;

  private List<Body> expBodies = new ArrayList<>();

  private static final int FRAME_COLS = 8, FRAME_ROWS = 6;

  private static Explosions instance;
  private final static long  explosionLifeTime = 6000;

  public static Explosions getInstance() {
    if (instance == null) {
      instance = new Explosions();
    }
    return instance;
  }

  public Explosions() {
    fireAnimationSheet = LoaderManager.getInstance().get("ani/explosion.png");
    fireAnimationSheet.setFilter(TextureFilter.MipMapLinearLinear, TextureFilter.MipMapLinearLinear);

    TextureRegion[][] tmp = TextureRegion.split(fireAnimationSheet,
        fireAnimationSheet.getWidth() / FRAME_COLS,
        fireAnimationSheet.getHeight() / FRAME_ROWS);

    TextureRegion[] frames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
    int index = 0;
    for (int i = 0; i < FRAME_ROWS; i++) {
      for (int j = 0; j < FRAME_COLS; j++) {
        frames[index++] = tmp[i][j];
      }
    }

    fireExplosionAnimation = new Animation<>(0.01f, frames);

    Texture[] l = new Texture[10];

    for (int i = 0; i < 10 ; i++) {
      l[i] = LoaderManager.getInstance().get("ani/widowAttack/explosion/" + i +".png");
      l[i].setFilter(TextureFilter.MipMapLinearLinear, TextureFilter.MipMapLinearLinear);
    }

    acidExplosionAnimation = new Animation<>(0.03f, l);
  }

  public static void initResources() {
    LoaderManager.getInstance().load("ani/explosion.png", Texture.class, TEXTURE_PARAMETERS);

    for (int i = 0; i < 10 ; i++) {
      LoaderManager.getInstance().load("ani/widowAttack/explosion/" + i +".png", Texture.class, TEXTURE_PARAMETERS);
    }
  }

  public void createFireExplosion(FloatPair pos, float damage) {
    AudioService.getInstance().playExplosionSound();
    explosions.add(new Explosion(pos, 0, MathUtils.random(0, 359), ExplosionType.FIRE, TimeUtils.millis(), 12));
    ExplosionBodyUserData explosionBodyUserData = new ExplosionBodyUserData(UUID.randomUUID(), TimeUtils.millis());
    explosionBodyUserData.setDamage(damage);
    explosionBodyUserData.setName("explosion");

    Gdx.app.postRunnable(() -> {
      Body body = GDXUtils.createCircleForModel(12/MAIN_ZOOM, 100, explosionBodyUserData, pos.getX(), pos.getY(), true);
      body.getFixtureList().get(0).setFilterData(Filters.getPlayerBulletFilter());
      expBodies.add(body);
    });
  }

  public void createAcidExplosion(FloatPair pos) {
    explosions.add(new Explosion(pos, 0, MathUtils.random(0, 359), ExplosionType.ACID, TimeUtils.millis(),1.5f));
  }

  public void drawExplosions(OrthographicCamera camera) {
    expBodies.forEach(b -> {
      Array<Fixture> fixtureList = b.getFixtureList();
      if (!fixtureList.isEmpty() && fixtureList.get(0).getUserData() instanceof ExplosionBodyUserData) {
        ExplosionBodyUserData userData = (ExplosionBodyUserData) fixtureList.get(0).getUserData();
        if (TimeUtils.timeSinceMillis(userData.getBornDate()) > explosionLifeTime) {
          try {
            b.setActive(false);
            Box2dWorld.getInstance().getWorld().destroyBody(b);
          } catch (Exception e) {

          }
        }
      }
    });
    expBodies.removeIf(body -> !body.isActive());

    batch.setProjectionMatrix(camera.combined);
    batch.begin();
    explosions.forEach(x -> {
      x.setStateTime(x.getStateTime() + Gdx.graphics.getDeltaTime());
      Sprite sprite = new Sprite();
      if (x.getType().equals(ExplosionType.ACID)) {
        sprite =new Sprite(acidExplosionAnimation.getKeyFrame(x.getStateTime()));
      } else if (x.getType().equals(ExplosionType.FIRE)) {
        sprite =new Sprite(fireExplosionAnimation.getKeyFrame(x.getStateTime()));
      }
      sprite.setOrigin(sprite.getWidth() - sprite.getWidth() / 2, sprite.getHeight() / 2);
      sprite.setPosition(x.getPos().getX() - sprite.getWidth() / 2,
          x.getPos().getY() - sprite.getHeight() / 2);
      sprite.rotate(x.getAngle());
      sprite.setScale(x.getScale()/MAIN_ZOOM);
      sprite.draw(batch);
    });
    batch.end();
  }

  public void dispose(){
    explosions.clear();
    expBodies.clear();
  }
}
