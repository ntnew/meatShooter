package ru.meat.game.service;

import static ru.meat.game.settings.Constants.MAIN_ZOOM;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.utils.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.Data;
import ru.meat.game.Box2dWorld;
import ru.meat.game.model.weapons.Bullet;
import ru.meat.game.model.weapons.BulletBodyUserData;
import ru.meat.game.model.weapons.BulletType;
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

  private List<Bullet> bullets = new ArrayList<>();

  private final ArrayList<Integer> bulletsToRemove = new ArrayList<>();

  public BulletService() {
    batch = new SpriteBatch();
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
    bulletBody.getFixtureList().get(0).setFilterData(GDXUtils.getFilter());
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

    bullets.add(bullet);
  }

  private Body createCircleForBullet(float x, float y, int damage, float bulletRadius, BulletType bulletType) {

    BodyDef def = new BodyDef();

    def.type = BodyType.DynamicBody;
    def.position.set(x, y);
    Body box = Box2dWorld.getInstance().getWorld().createBody(def);

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

  public void updateBullets() {
    for (int i = 0; i < bullets.size(); i++) {
      Bullet bullet = bullets.get(i);
      Array<Fixture> fixtureList = bullet.getBody().getFixtureList();
      if (!fixtureList.isEmpty()) {
        BulletBodyUserData userData = (BulletBodyUserData) fixtureList.get(0).getUserData();
        if (userData.isNeedDispose()) {
          deleteBulletBody(i);
        }
      }
    }
    bullets.removeIf(x -> x.getBody().getFixtureList().isEmpty());
  }

  private void deleteBulletBody(int i) {
    try {
      bullets.get(i).getBody().setActive(false);
      Box2dWorld.getInstance().getWorld().destroyBody(bullets.get(i).getBody());
    } catch (Exception e) {

    }
  }

  public void drawBullets(OrthographicCamera camera) {
    batch.setProjectionMatrix(camera.combined);
    batch.begin();
    bullets.forEach(b -> {
          try {
            Array<Fixture> fixtureList = b.getBody().getFixtureList();
            if (!fixtureList.isEmpty()) {
              Vector2 position = fixtureList.get(0).getBody().getPosition();

              b.getSprite().setPosition(
                  position.x * Constants.WORLD_TO_VIEW - b.getSprite().getWidth() + b.getSprite().getWidth() / 12,
                  position.y * Constants.WORLD_TO_VIEW - b.getSprite().getHeight() / 2);

              b.getSprite().draw(batch);
            }
          } catch (NullPointerException e) {

          }
        }
    );
    batch.end();
  }

  public static void dispose() {
    instance = null;
  }
}
