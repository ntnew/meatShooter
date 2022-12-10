package ru.meat.game.service;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.World;
import ru.meat.game.model.weapons.Bullet;
import ru.meat.game.utils.StaticFloats;

public class BulletService {

  /**
   *  создать пулю, задает её скорость и направление полёта
   * @param world мир, в котором создастся объект для bpx2d
   * @param fromX точка х откуда стреляют
   * @param fromY точка у откуда стреляют
   * @param screenX точка х куда стреляют
   * @param screenY точка у куда стреляют
   * @param bulletSpeed множитель скорости пули
   * @return обект пули
   */
  public static Bullet createBullet(World world, float fromX, float fromY, float screenX, float screenY, float bulletSpeed){
    Body bulletBody = createCircleForBullet(world, fromX, fromY);
    bulletBody.setBullet(true);
    Bullet bullet = new Bullet();
    bulletBody.setLinearVelocity((screenX - fromX) * bulletSpeed,(screenY - fromY) * bulletSpeed);
    bullet.setBody(bulletBody);

    return bullet;
  }

  private static Body createCircleForBullet(World world, float x, float y) {

    BodyDef def = new BodyDef();

    def.type = BodyType.KinematicBody;
    def.position.set(x, y);
    Body box = world.createBody(def);

    CircleShape circle = new CircleShape();
    circle.setRadius((float) 3/ StaticFloats.WORLD_TO_VIEW);

    box.createFixture(circle, (float) 100);
    box.getFixtureList().get(0).setUserData("bullet");

    circle.dispose();

    return box;

  }

}
