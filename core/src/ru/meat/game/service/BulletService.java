package ru.meat.game.service;

import static ru.meat.game.settings.Constants.MAIN_ZOOM;
import static ru.meat.game.settings.Constants.TEXTURE_PARAMETERS;
import static ru.meat.game.settings.Constants.WORLD_TO_VIEW;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
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
import ru.meat.game.MyGame;
import ru.meat.game.loader.LoaderManager;
import ru.meat.game.model.FloatPair;
import ru.meat.game.model.weapons.bullets.Bullet;
import ru.meat.game.model.weapons.bullets.BulletBodyUserData;
import ru.meat.game.model.weapons.BulletType;
import ru.meat.game.model.weapons.bullets.CommonBullet;
import ru.meat.game.model.weapons.bullets.EnemyAcidBullet;
import ru.meat.game.model.weapons.explosions.ExplosionsService;
import ru.meat.game.settings.Filters;
import ru.meat.game.utils.GDXUtils;
import ru.meat.game.settings.Constants;

@Data
public class BulletService {

  private static BulletService instance;

  private Animation<Texture> acidBulletAnimation;

  public static BulletService getInstance() {
    if (instance == null) {
      instance = new BulletService();
    }
    return instance;
  }

  public BulletService() {

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
   */
  public void createBullet(float fromX, float fromY, float screenX, float screenY, float bulletSpeed, int damage,
      Texture texture, float bulletRadius,
      BulletType bulletType, float bulletScale) {

    //создать данные для бокс2д
    Body bulletBody = createCircleForBullet(fromX, fromY, damage, bulletRadius, bulletType);
    bulletBody.getFixtureList().get(0).setFilterData(Filters.getPlayerBulletFilter());
    bulletBody.setBullet(true);
    bulletBody.setLinearVelocity((screenX - fromX) * bulletSpeed * MAIN_ZOOM,
        (screenY - fromY) * bulletSpeed * MAIN_ZOOM);

    MyGame.getInstance().getGameZone().getStage().addBullet(new CommonBullet(texture, bulletBody, bulletScale));
  }

  private Body createCircleForBullet(float x, float y, int damage, float bulletRadius, BulletType bulletType) {

    BodyDef def = new BodyDef();

    def.type = BodyType.DynamicBody;
    def.position.set(x, y);
    if (Box2dWorld.getInstance() != null) {
      Body box = Box2dWorld.getInstance().createBody(def);

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
    //создать спрайт текстуры

    Bullet bullet = new EnemyAcidBullet(bulletBody, bulletScale);
    MyGame.getInstance().getGameZone().getThirdStage().addBullet(bullet);
  }
}
