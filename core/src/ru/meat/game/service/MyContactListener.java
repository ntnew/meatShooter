package ru.meat.game.service;

import static ru.meat.game.settings.Constants.WORLD_TO_VIEW;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.utils.TimeUtils;
import lombok.AllArgsConstructor;
import ru.meat.game.gui.GUI;
import ru.meat.game.model.FloatPair;
import ru.meat.game.model.bodyData.BodyUserData;
import ru.meat.game.model.enemies.EnemyBodyUserData;
import ru.meat.game.model.weapons.BulletBodyUserData;
import ru.meat.game.model.weapons.BulletType;
import ru.meat.game.model.weapons.explosions.ExplosionBodyUserData;
import ru.meat.game.model.weapons.explosions.Explosions;


@AllArgsConstructor
public class MyContactListener implements ContactListener {

  @Override
  public void endContact(Contact contact) {
  }

  @Override
  public void beginContact(Contact contact) {

  }


  @Override
  public void preSolve(Contact contact, Manifold oldManifold) {
    Fixture fa = contact.getFixtureA();
    Fixture fb = contact.getFixtureB();
    if (fa.getUserData() instanceof EnemyBodyUserData && fb.getUserData() instanceof BodyUserData
        && ((BodyUserData) fb.getUserData()).getName().equals("player")) {
      attackPlayer(fa, fb);
    } else if (fb.getUserData() instanceof EnemyBodyUserData && fa.getUserData() instanceof BodyUserData
        && ((BodyUserData) fa.getUserData()).getName().equals("player")) {
      attackPlayer(fb, fa);
    } else if (fa.getUserData() instanceof BulletBodyUserData && fb.getUserData() instanceof BodyUserData
        && ((BodyUserData) fb.getUserData()).getName().equals("player")) {
      contactPlayerWithAcid(fb, fa);
    } else if (fb.getUserData() instanceof BulletBodyUserData && fa.getUserData() instanceof BodyUserData
        && ((BodyUserData) fa.getUserData()).getName().equals("player")) {
      contactPlayerWithAcid(fa, fb);
    }
  }

  private void contactPlayerWithAcid(Fixture fa, Fixture fb) {
    Explosions.getInstance().createAcidExplosion(
        new FloatPair(fb.getBody().getPosition().x * WORLD_TO_VIEW, fb.getBody().getPosition().y * WORLD_TO_VIEW));
    BulletBodyUserData bulletBodyUserData = (BulletBodyUserData) fb.getUserData();
    bulletBodyUserData.setNeedDispose(true);
    fb.setUserData(bulletBodyUserData);

    BodyUserData playerUserData = (BodyUserData) fa.getUserData();
    playerUserData.setDamage(playerUserData.getDamage() + bulletBodyUserData.getDamage());

    GUI.getInstance().handleHit();
  }

  private void attackPlayer(Fixture fa, Fixture fb) {
    EnemyBodyUserData enemyData = (EnemyBodyUserData) fa.getUserData();
    enemyData.setNeedAttack(true);

    BodyUserData playerUserData = (BodyUserData) fb.getUserData();
    if (enemyData.getPreviousAttackTime() == null
        || TimeUtils.timeSinceMillis(enemyData.getPreviousAttackTime()) > enemyData.getAttackSpeed() * 1000) {
      playerUserData.setDamage(playerUserData.getDamage() + enemyData.getAttack());
      enemyData.setPreviousAttackTime(TimeUtils.millis());
    }
    GUI.getInstance().handleHit();
  }

  @Override
  public void postSolve(Contact contact, ContactImpulse impulse) {
    Fixture fa = contact.getFixtureA();
    Fixture fb = contact.getFixtureB();
    if (fa.getUserData() instanceof EnemyBodyUserData && fb.getUserData() instanceof BulletBodyUserData) {
      contactForBullets(fa, fb);
    } else if (fb.getUserData() instanceof EnemyBodyUserData && fa.getUserData() instanceof BulletBodyUserData) {
      contactForBullets(fb, fa);
    } else if (fa.getUserData() instanceof EnemyBodyUserData && fb.getUserData() instanceof ExplosionBodyUserData) {
      setDamageToEnemyFromExplosion(fa, fb);
    } else if (fb.getUserData() instanceof EnemyBodyUserData && fa.getUserData() instanceof ExplosionBodyUserData) {
      setDamageToEnemyFromExplosion(fb, fa);
    }
  }

  /**
   * @param fa фикстура врага
   * @param fb фикстура пули
   */
  private void contactForBullets(Fixture fa, Fixture fb) {
    if (((BulletBodyUserData) fb.getUserData()).getType().equals(BulletType.COMMON)) {
      setDamageToEnemyFromBullet(fa, fb);
    } else if (((BulletBodyUserData) fb.getUserData()).getType().equals(BulletType.EXPLOSIVE)) {
      BulletBodyUserData bulletBodyUserData = (BulletBodyUserData) fb.getUserData();
      if (!bulletBodyUserData.isNeedDispose()) {
        createExplosion(
            new FloatPair(fb.getBody().getPosition().x * WORLD_TO_VIEW, fb.getBody().getPosition().y * WORLD_TO_VIEW),
            bulletBodyUserData.getDamage());
      }
      bulletBodyUserData.setNeedDispose(true);
      fb.setUserData(bulletBodyUserData);
    }
  }

  private void createExplosion(FloatPair floatPair, float damage) {
    Explosions.getInstance().createFireExplosion(floatPair, damage);
  }

  private void setDamageToEnemyFromBullet(Fixture fa, Fixture fb) {
    EnemyBodyUserData bodyUserData = (EnemyBodyUserData) fa.getUserData();
    BulletBodyUserData bulletBodyUserData = (BulletBodyUserData) fb.getUserData();

    if (!bodyUserData.getIdContactedBullets().contains(bulletBodyUserData.getId())) {
      bodyUserData.setDamage(bodyUserData.getDamage() + bulletBodyUserData.getDamage());
      bodyUserData.getIdContactedBullets().add(bulletBodyUserData.getId());
      BloodService.getInstance().createBleeding(fb.getBody().getPosition().x, fb.getBody().getPosition().y);
      addBlood(
          new FloatPair(fa.getBody().getPosition().x * WORLD_TO_VIEW, fa.getBody().getPosition().y * WORLD_TO_VIEW));
    }
    bulletBodyUserData.setNeedDispose(true);
    fb.setUserData(bulletBodyUserData);
  }

  private void setDamageToEnemyFromExplosion(Fixture fa, Fixture fb) {
    EnemyBodyUserData bodyUserData = (EnemyBodyUserData) fa.getUserData();
    ExplosionBodyUserData explosionBodyUserData = (ExplosionBodyUserData) fb.getUserData();
    if (!bodyUserData.getIdContactedBullets().contains(explosionBodyUserData.getId())) {
      bodyUserData.setDamage(bodyUserData.getDamage() + explosionBodyUserData.getDamage());
      bodyUserData.getIdContactedBullets().add(explosionBodyUserData.getId());
      fb.setUserData(explosionBodyUserData);
      //создать несколько точек крови от взрыва
      for (int i = 0; i < 4; i++) {
        addBlood(
            new FloatPair(fa.getBody().getPosition().x * WORLD_TO_VIEW, fa.getBody().getPosition().y * WORLD_TO_VIEW));
      }
    }
  }

  private void addBlood(FloatPair coord) {
    BloodService.getInstance().createLittleBloodSpot(coord);
  }
}