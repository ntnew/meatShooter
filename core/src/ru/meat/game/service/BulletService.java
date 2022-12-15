package ru.meat.game.service;

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
import com.badlogic.gdx.physics.box2d.World;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import ru.meat.game.model.weapons.Bullet;
import ru.meat.game.model.weapons.BulletBodyUserData;
import ru.meat.game.utils.StaticFloats;

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
    bulletBody.setBullet(true);
    bulletBody.setLinearVelocity((screenX - fromX) * bulletSpeed, (screenY - fromY) * bulletSpeed);
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
    circle.setRadius((float) 3 / StaticFloats.WORLD_TO_VIEW);

    box.createFixture(circle, (float) 100);
    box.getFixtureList().get(0).setUserData(new BulletBodyUserData("bullet", damage));

    circle.dispose();

    return box;
  }

  public void updateBullets() {

    for (int i = 0; i < bullets.size(); i++) {
      Bullet bullet = bullets.get(i);
      BulletBodyUserData userData = (BulletBodyUserData) bullet.getBody().getFixtureList().get(0).getUserData();
      if (userData.isNeedDispose()) {
        deleteBulletBody(i);
      }
    }
    bulletsToRemove.forEach(i -> bullets.remove((int) i));
    bulletsToRemove.clear();
  }

  private void deleteBulletBody( int i) {
    try {
      bullets.get(i).getBody().setActive(false);
      world.destroyBody(bullets.get(i).getBody());
      bulletsToRemove.add(i);
    } catch (Exception e) {

    }
  }

  public void drowBullets(SpriteBatch spriteBatch, float x, float y) {
   bullets.forEach(b -> {
          Vector2 position = b.getBody().getFixtureList().get(0).getBody().getPosition();
          Sprite sprite = new Sprite(b.getTexture());
          sprite.setPosition(position.x*StaticFloats.WORLD_TO_VIEW, position.y*StaticFloats.WORLD_TO_VIEW);
          sprite.setRotation((MathUtils.radiansToDegrees * MathUtils.atan2(y - position.y*StaticFloats.WORLD_TO_VIEW, x - position.x*StaticFloats.WORLD_TO_VIEW)));
          sprite.draw(spriteBatch);
        }
    );
  }
}
