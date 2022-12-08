package ru.meat.game.service;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.World;
import ru.meat.game.model.weapons.Bullet;
import ru.meat.game.utils.StaticFloats;

public class BulletService {

  public static Bullet createBullet(World world, float fromX, float fromY, float screenX, float screenY){
    Body bulletBody = createCircleForBullet(world, fromX, fromY);
    Bullet bullet = new Bullet();
    bullet.setBody(bulletBody);
    bullet.getBody().setBullet(true);
    bullet.getBody().setLinearVelocity((screenX - fromX) * 0.5f,(screenY - fromY) * 0.5f);
    return bullet;
  }

  private static Body createCircleForBullet(World world, float x, float y) {

    BodyDef def = new BodyDef();

    def.type = BodyType.KinematicBody;
    def.position.set(x, y);
    Body box = world.createBody(def);


    CircleShape circle = new CircleShape();
    circle.setRadius((float) 10/ StaticFloats.WORLD_TO_VIEW);

    box.createFixture(circle, (float) 100);
    box.getFixtureList().get(0).setUserData("bullet");

    circle.dispose();

    return box;

  }

}
