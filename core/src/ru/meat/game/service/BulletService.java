package ru.meat.game.service;

import static ru.meat.game.settings.Constants.MAIN_ZOOM;
import static ru.meat.game.settings.Constants.TEXTURE_PARAMETERS;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import java.util.UUID;
import lombok.Data;
import ru.meat.game.Box2dWorld;
import ru.meat.game.MyGame;
import ru.meat.game.loader.LoaderManager;
import ru.meat.game.model.weapons.bullets.BulletBodyUserData;
import ru.meat.game.model.weapons.BulletType;
import ru.meat.game.model.weapons.bullets.CommonBullet;
import ru.meat.game.model.weapons.bullets.EnemyAcidBullet;
import ru.meat.game.settings.Filters;
import ru.meat.game.utils.AniLoadUtil;
import ru.meat.game.utils.GDXUtils;
import ru.meat.game.settings.Constants;

@Data
public class BulletService {

  private static BulletService instance;

  private final static String widowAttackResourcePath ="ani/widowAttack/fly/flyall.png";
  private static final int FRAME_COLS = 6, FRAME_ROWS = 3;
  private Animation<TextureRegion> acidBulletAnimation;

  public static BulletService getInstance() {
    if (instance == null) {
      instance = new BulletService();
    }
    return instance;
  }

  public BulletService() {
    loadWidowBulletAni();
  }

  private void loadWidowBulletAni() {
    acidBulletAnimation = AniLoadUtil.getAniFromResources(widowAttackResourcePath,
        0.020f, FRAME_COLS, FRAME_ROWS);
  }


  public static void initResources() {
    LoaderManager.getInstance().load("Bullet1.png", Texture.class, TEXTURE_PARAMETERS);
    LoaderManager.getInstance().load("GBullet.png", Texture.class, TEXTURE_PARAMETERS);
    LoaderManager.getInstance().load(widowAttackResourcePath, Texture.class, TEXTURE_PARAMETERS);
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

//    Bullet bullet = ;
    MyGame.getInstance().getGameZone().getSecondStage().addBullet(new EnemyAcidBullet(bulletBody, bulletScale));
  }
}
