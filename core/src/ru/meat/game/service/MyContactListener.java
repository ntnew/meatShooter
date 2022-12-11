package ru.meat.game.service;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import lombok.NoArgsConstructor;
import ru.meat.game.model.EnemyBodyUserData;
import ru.meat.game.model.weapons.BulletBodyUserData;

@NoArgsConstructor
public class MyContactListener implements ContactListener {

  @Override
  public void endContact(Contact contact) {

  }

  @Override
  public void beginContact(Contact contact) {

    Fixture fa = contact.getFixtureA();
    Fixture fb = contact.getFixtureB();
    setDamageToEnemyFromBullet(fa, fb);
  }

  private void setDamageToEnemyFromBullet(Fixture fa, Fixture fb) {
    if (fa.getUserData() instanceof EnemyBodyUserData && fb.getUserData() instanceof BulletBodyUserData){
      EnemyBodyUserData userData = (EnemyBodyUserData) fa.getUserData();
      userData.setDamage(((BulletBodyUserData) fb.getUserData()).getDamage());
    }
  }

  @Override
  public void preSolve(Contact contact, Manifold oldManifold) {

  }

  @Override
  public void postSolve(Contact contact, ContactImpulse impulse){
  }

}