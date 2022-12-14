package ru.meat.game.service;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import lombok.AllArgsConstructor;
import ru.meat.game.model.EnemyBodyUserData;
import ru.meat.game.model.weapons.BulletBodyUserData;


@AllArgsConstructor
public class MyContactListener implements ContactListener {


  private final World world;

  @Override
  public void endContact(Contact contact) {

  }

  @Override
  public void beginContact(Contact contact) {

    Fixture fa = contact.getFixtureA();
    Fixture fb = contact.getFixtureB();
    if (fa.getUserData() instanceof EnemyBodyUserData && fb.getUserData() instanceof BulletBodyUserData) {
      setDamageToEnemyFromBullet(fa, fb);
    }
  }

  private void setDamageToEnemyFromBullet(Fixture fa, Fixture fb) {
    EnemyBodyUserData enemyBodyUserData = (EnemyBodyUserData) fa.getUserData();
    BulletBodyUserData bulletBodyUserData = (BulletBodyUserData) fb.getUserData();

    enemyBodyUserData.setDamage(bulletBodyUserData.getDamage());

    bulletBodyUserData.setNeedDispose(true);
    fb.setUserData(bulletBodyUserData);
  }

  @Override
  public void preSolve(Contact contact, Manifold oldManifold) {

  }

  @Override
  public void postSolve(Contact contact, ContactImpulse impulse) {
  }

}