package ru.meat.game.service;

import static ru.meat.game.settings.Constants.MAIN_ZOOM;
import static ru.meat.game.settings.Constants.TEXTURE_PARAMETERS;
import static ru.meat.game.settings.Constants.WORLD_TO_VIEW;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
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
import ru.meat.game.model.weapons.Bullet;
import ru.meat.game.model.weapons.BulletBodyUserData;
import ru.meat.game.model.weapons.BulletType;
import ru.meat.game.model.weapons.explosions.ExplosionsService;
import ru.meat.game.settings.Filters;
import ru.meat.game.utils.GDXUtils;
import ru.meat.game.settings.Constants;

@Data
public class BulletService {

  private static BulletService instance;
  private final SpriteBatch batch;

  public static BulletService getInstance() {
    if (instance == null) {
      instance = new BulletService();
    }
    return instance;
  }

  private Animation<Texture> acidBulletAnimation;

  private List<Bullet> playerBullets = new ArrayList<>();

  private List<Bullet> enemyBullets = new ArrayList<>();

  public BulletService() {
    batch = new SpriteBatch();

    Texture[] l = new Texture[19];

    for (int i = 0; i < 19; i++) {
      l[i] = LoaderManager.getInstance().get("ani/widowAttack/fly/" + i + ".png");
    }
    acidBulletAnimation = new Animation<>(0.025f, l);
  }


  public static void initResources() {
    for (int i = 0; i < 19; i++) {
      LoaderManager.getInstance().load("ani/widowAttack/fly/" + i + ".png", Texture.class, TEXTURE_PARAMETERS);
    }
  }

  /**
   * создать пулю, задает её скорость и направление полёта
   *
   * @param fromX       точка х откуда стреляют
   * @param fromY       точка у откуда стреляют
   * @param screenX     точка х куда стреляют
   * @param screenY     точка у куда стреляют
   * @param bulletSpeed множитель скорости пули
   * @param damage
   * @param bulletType
   * @param bulletScale
   * @return обект пули
   */
  public void createBullet(float fromX, float fromY, float screenX, float screenY, float bulletSpeed, int damage,
      Texture texture, float bulletRadius,
      BulletType bulletType, float bulletScale) {
    Bullet bullet = new Bullet();
    //создать данные для бокс2д
    Body bulletBody = createCircleForBullet(fromX, fromY, damage, bulletRadius, bulletType);
    bulletBody.getFixtureList().get(0).setFilterData(Filters.getPlayerBulletFilter());
    bulletBody.setBullet(true);
    bulletBody.setLinearVelocity((screenX - fromX) * bulletSpeed * MAIN_ZOOM,
        (screenY - fromY) * bulletSpeed * MAIN_ZOOM);
    bullet.setBody(bulletBody);

    //создать спрайт текстуры
    Sprite sprite = new Sprite(texture);
    sprite.setScale(bulletScale);
    sprite.setOrigin(sprite.getWidth() - sprite.getWidth() / 12, sprite.getHeight() / 2);
    sprite.rotate(bulletBody.getLinearVelocity().angleDeg());
    bullet.setSprite(sprite);

    bullet.setBornDate(TimeUtils.millis());

    playerBullets.add(bullet);
  }

  private Body createCircleForBullet(float x, float y, int damage, float bulletRadius, BulletType bulletType) {

    BodyDef def = new BodyDef();

    def.type = BodyType.DynamicBody;
    def.position.set(x, y);
    if (Box2dWorld.getInstance() != null) {
      Body box;
      synchronized (Box2dWorld.getInstance().getWorld()) {
         box = Box2dWorld.getInstance().getWorld().createBody(def);
      }

      CircleShape circle = new CircleShape();
      circle.setRadius(bulletRadius * MAIN_ZOOM / Constants.WORLD_TO_VIEW);

      box.createFixture(circle, (float) 100);
      box.getFixtureList().get(0)
          .setUserData(
              new BulletBodyUserData(UUID.randomUUID(), "bullet",
                  damage * RpgStatsService.getInstance().getStats().getDamage(),
                  false,
                  bulletType));

      circle.dispose();
      return box;
    }
    return null;
  }


  public void createEnemyBullet(float fromX, float fromY, float toX, float toY,
      float bulletSpeed, int damage, float bulletRadius,
      BulletType bulletType, float bulletScale) {
    Bullet bullet = new Bullet();
    //создать данные для бокс2д
    Body bulletBody = createCircleForBullet(fromX, fromY, damage, bulletRadius, bulletType);
    bulletBody.getFixtureList().get(0).setFilterData(Filters.getFilterForEnemiesBullets());
    bulletBody.setBullet(true);
    //найти  новую точку на экране с новоой гипотенузой для одинаковости выстрелов
    float catetPrilezjaschiy = (toX - fromX);
    float newGip = 20;
    float catetProtivo = (toY - fromY);
    float gip = GDXUtils.calcGipotenuza(catetPrilezjaschiy, catetProtivo);
    float sin = catetProtivo / gip;
    float cos = catetPrilezjaschiy / gip;
    float newCatetForY = sin * newGip;
    float newCatetForX = cos * newGip;

    toX = fromX + newCatetForX;
    toY = fromY + newCatetForY;

    bulletBody.setLinearVelocity((toX - fromX) * bulletSpeed * MAIN_ZOOM,
        (toY - fromY) * bulletSpeed * MAIN_ZOOM);
    bullet.setBody(bulletBody);

    bullet.setStateTime(0);
    bullet.setBornDate(TimeUtils.millis());
    //создать спрайт текстуры
    Sprite sprite = new Sprite(acidBulletAnimation.getKeyFrame(0));
    sprite.setPosition(-500, - 500);
    sprite.setScale(bulletScale);
    sprite.setOrigin(sprite.getWidth() / 2f, sprite.getHeight() / 2.4f);
    sprite.rotate(bulletBody.getLinearVelocity().angleDeg() + 90);
    bullet.setSprite(sprite);

    enemyBullets.add(bullet);
  }

  public void updateBullets() {
    playerBullets.parallelStream().forEach(b -> {
      Array<Fixture> fixtureList = b.getBody().getFixtureList();
      if (!fixtureList.isEmpty()) {
        BulletBodyUserData userData = (BulletBodyUserData) fixtureList.get(0).getUserData();
        if (userData.isNeedDispose() || TimeUtils.timeSinceMillis(b.getBornDate()) > 8000) {
          try {
            deleteBulletBody(b);
          } catch (Exception e) {

          }
        } else {
          //Переместить пулю
          Vector2 position = fixtureList.get(0).getBody().getPosition();
          b.getSprite().setPosition(
              position.x * Constants.WORLD_TO_VIEW - b.getSprite().getWidth() + b.getSprite().getWidth() / 12,
              position.y * Constants.WORLD_TO_VIEW - b.getSprite().getHeight() / 2);
        }
      }
    });
    playerBullets.removeIf(x -> x.getBody().getFixtureList().isEmpty());

    enemyBullets.parallelStream().forEach(b -> {
      Array<Fixture> fixtureList = b.getBody().getFixtureList();
      if (!fixtureList.isEmpty() && fixtureList.get(0).getUserData() instanceof BulletBodyUserData) {
        BulletBodyUserData userData = (BulletBodyUserData) fixtureList.get(0).getUserData();
        if (userData.isNeedDispose() || TimeUtils.timeSinceMillis(b.getBornDate()) > 6000) {
          createExplosionAndDeleteBody(b);
        } else {
          setAnimationFrame(b);
          setPosition(b, fixtureList);
        }
      }
    });

    enemyBullets.removeIf(x -> x.getBody().getFixtureList().isEmpty());
  }

  /**
   * Установить кадр для пули вражеской
   */
  private void setAnimationFrame(Bullet b) {
    b.setStateTime(b.getStateTime() + Gdx.graphics.getDeltaTime());
    b.getSprite().setTexture(acidBulletAnimation.getKeyFrame(b.getStateTime(), true));
  }

  /**
   * Установить позицию
   */
  private void setPosition(Bullet b, Array<Fixture> fixtureList) {
    Vector2 position = fixtureList.get(0).getBody().getPosition();
    b.getSprite().setPosition(
        position.x * Constants.WORLD_TO_VIEW - b.getSprite().getWidth() / 2f,
        position.y * Constants.WORLD_TO_VIEW - b.getSprite().getHeight() / 2.4f);
  }

  /**
   * Создать взрыв кислоты и удалить тело пули
   */
  private void createExplosionAndDeleteBody(Bullet b) {
    try {
      ExplosionsService.getInstance().createAcidExplosion(
          new FloatPair(b.getBody().getPosition().x * WORLD_TO_VIEW,
              b.getBody().getPosition().y * WORLD_TO_VIEW));
      deleteBulletBody(b);
    } catch (Exception e) {

    }
  }

  private void deleteBulletBody(Bullet bullet) {
    Gdx.app.postRunnable(() -> {
      bullet.getBody().setActive(false);
      Box2dWorld.getInstance().getWorld().destroyBody(bullet.getBody());
    });
  }

  public void drawPlayerBullets(OrthographicCamera camera){
    batch.setProjectionMatrix(camera.combined);
    batch.begin();
    for (Bullet b : playerBullets) {
      b.getSprite().draw(batch);
    }
  }
  public void drawEnemyBullets(OrthographicCamera camera) {
    for (Bullet b : enemyBullets) {
      b.getSprite().draw(batch);
    }
    batch.end();
  }

  public static void dispose() {
    instance = null;
  }
}
