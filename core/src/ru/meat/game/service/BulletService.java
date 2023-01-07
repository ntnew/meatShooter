package ru.meat.game.service;

import static ru.meat.game.settings.Constants.MAIN_ZOOM;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import ru.meat.game.model.weapons.Bullet;
import ru.meat.game.model.weapons.BulletBodyUserData;
import ru.meat.game.utils.GDXUtils;
import ru.meat.game.settings.Constants;

@Data
public class BulletService {

  private final World world;

  private List<Bullet> bullets = new ArrayList<>();

  private final ArrayList<Integer> bulletsToRemove = new ArrayList<>();

  /**
   * создать пулю, задает её скорость и направление полёта
   *
   * @param fromX       точка х откуда стреляют
   * @param fromY       точка у откуда стреляют
   * @param screenX     точка х куда стреляют
   * @param screenY     точка у куда стреляют
   * @param bulletSpeed множитель скорости пули
   * @param damage
   * @return обект пули
   */
  public void createBullet(float fromX, float fromY, float screenX, float screenY, float bulletSpeed, int damage) {
    Bullet bullet = new Bullet();
    Body bulletBody = createCircleForBullet(fromX, fromY, damage);
    bulletBody.getFixtureList().get(0).setFilterData(GDXUtils.getFilter());
    bulletBody.setBullet(true);
    bulletBody.setLinearVelocity((screenX - fromX) * bulletSpeed * MAIN_ZOOM, (screenY - fromY) * bulletSpeed * MAIN_ZOOM);
    bullet.setBody(bulletBody);
    bullet.setTexture(new Texture(Gdx.files.internal("./assets/Bullet1.png")));

    bullets.add(bullet);
  }

  private Body createCircleForBullet(float x, float y, int damage) {

    BodyDef def = new BodyDef();

    def.type = BodyType.DynamicBody;
    def.position.set(x, y);
    Body box = world.createBody(def);

    CircleShape circle = new CircleShape();
    circle.setRadius((float) 3 / Constants.WORLD_TO_VIEW);

    box.createFixture(circle, (float) 100);
    box.getFixtureList().get(0).setUserData(new BulletBodyUserData("bullet", damage));

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
      world.destroyBody(bullets.get(i).getBody());
    } catch (Exception e) {

    }
  }

  public void drawBullets(SpriteBatch spriteBatch, float x, float y) {
    bullets.forEach(b -> {
          try {
            Array<Fixture> fixtureList = b.getBody().getFixtureList();
            if (!fixtureList.isEmpty()) {
              Vector2 position = fixtureList.get(0).getBody().getPosition();
              Sprite sprite = new Sprite(b.getTexture());
              sprite.setPosition(position.x * Constants.WORLD_TO_VIEW - sprite.getWidth() / 12,
                  position.y * Constants.WORLD_TO_VIEW + 20 - sprite.getHeight() / 2);
              sprite.setOrigin(sprite.getWidth() / 12, sprite.getHeight() / 2);
              sprite.rotate((MathUtils.radiansToDegrees * MathUtils.atan2(y - position.y * Constants.WORLD_TO_VIEW,
                  x - position.x * Constants.WORLD_TO_VIEW)));
              sprite.draw(spriteBatch);
            }
          } catch (NullPointerException e) {

          }
        }
    );
  }
}
