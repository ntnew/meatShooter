package ru.meat.game.service;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.utils.TimeUtils;
import lombok.AllArgsConstructor;
import ru.meat.game.model.bodyData.BodyUserData;
import ru.meat.game.model.enemies.EnemyBodyUserData;
import ru.meat.game.model.weapons.BulletBodyUserData;


@AllArgsConstructor
public class MyContactListener implements ContactListener {

  @Override
  public void endContact(Contact contact) {
  }

  @Override
  public void beginContact(Contact contact) {

  }
  private void setDamageToEnemyFromBullet(Fixture fa, Fixture fb) {
    BodyUserData bodyUserData = (BodyUserData) fa.getUserData();
    BulletBodyUserData bulletBodyUserData = (BulletBodyUserData) fb.getUserData();

    bodyUserData.setDamage(bodyUserData.getDamage() + bulletBodyUserData.getDamage());

    bulletBodyUserData.setNeedDispose(true);
    fb.setUserData(bulletBodyUserData);
  }

  @Override
  public void preSolve(Contact contact, Manifold oldManifold) {

    Fixture fa = contact.getFixtureA();
    Fixture fb = contact.getFixtureB();
    if (fa.getUserData() instanceof EnemyBodyUserData && fb.getUserData() instanceof BodyUserData
        && ((BodyUserData) fb.getUserData()).getName().equals("player")) {
      attackPlayer(fa, fb);
    }
    if (fb.getUserData() instanceof EnemyBodyUserData && fa.getUserData() instanceof BodyUserData
        && ((BodyUserData) fa.getUserData()).getName().equals("player")) {
      attackPlayer(fb, fa);
    }
  }

  private void attackPlayer(Fixture fa, Fixture fb) {
    EnemyBodyUserData enemyData = (EnemyBodyUserData) fa.getUserData();
    enemyData.setNeedAttack(true);

    BodyUserData playerUserData = (BodyUserData) fb.getUserData();
    if (enemyData.getPreviousAttackTime() == null || TimeUtils.timeSinceMillis(enemyData.getPreviousAttackTime()) > enemyData.getAttackSpeed()*1000) {
      playerUserData.setDamage(playerUserData.getDamage() + enemyData.getAttack());
      enemyData.setPreviousAttackTime(TimeUtils.millis());
    }
  }

  @Override
  public void postSolve(Contact contact, ContactImpulse impulse) {
    Fixture fa = contact.getFixtureA();
    Fixture fb = contact.getFixtureB();
    if (fa.getUserData() instanceof EnemyBodyUserData && fb.getUserData() instanceof BulletBodyUserData) {
      setDamageToEnemyFromBullet(fa, fb);
    } else if (fb.getUserData() instanceof EnemyBodyUserData && fa.getUserData() instanceof BulletBodyUserData) {
      setDamageToEnemyFromBullet(fb, fa);
    }
  }

}