package ru.meat.game.model.weapons.bullets;

import static ru.meat.game.settings.Constants.WORLD_TO_VIEW;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import ru.meat.game.Box2dWorld;
import ru.meat.game.model.FloatPair;
import ru.meat.game.model.weapons.explosions.ExplosionsService;
import ru.meat.game.service.BulletService;
import ru.meat.game.settings.Constants;

public class EnemyAcidBullet extends Bullet {

  private float stateTime;

  public EnemyAcidBullet(Body body, float bulletScale) {
    super(BulletService.getInstance().getAcidBulletAnimation().getKeyFrame(0), body, bulletScale);
    this.setOrigin(this.getDrawable().getMinWidth() / 2, this.getDrawable().getMinHeight() / 2.4f);
    this.setRotation(body.getLinearVelocity().angleDeg() + 90);
    this.stateTime = 0;
  }

  @Override
  public void act(float delta) {
    if (getBody() != null) {
      Array<Fixture> fixtureList = getBody().getFixtureList();
      if (!fixtureList.isEmpty() && fixtureList.get(0).getUserData() instanceof BulletBodyUserData) {
        BulletBodyUserData userData = (BulletBodyUserData) fixtureList.get(0).getUserData();
        if (userData.isNeedDispose() || TimeUtils.timeSinceMillis(getBornDate()) > 6000) {
          createExplosionAndDeleteBody();
        } else {
          setAnimationFrame(delta);
          setPosition(fixtureList);
        }
      } else {
        deleteBulletBody();
        remove();
      }
    } else {
      remove();
    }
    super.act(delta);
  }

  /**
   * Создать взрыв кислоты и удалить тело пули
   */
  private void createExplosionAndDeleteBody() {
    try {
      ExplosionsService.getInstance().createAcidExplosion(
          new FloatPair(getBody().getPosition().x * WORLD_TO_VIEW, getBody().getPosition().y * WORLD_TO_VIEW));
      deleteBulletBody();
    } catch (Exception e) {

    }
  }

  private void setPosition(Array<Fixture> fixtureList) {
    Vector2 position = fixtureList.get(0).getBody().getPosition();
    setPosition(
        position.x * Constants.WORLD_TO_VIEW - this.getDrawable().getMinWidth() / 2f,
        position.y * Constants.WORLD_TO_VIEW - this.getDrawable().getMinHeight() / 2.4f);
  }

  private void deleteBulletBody() {
    if (getBody() != null) {
      Box2dWorld.getInstance().destroyBody(getBody());
      setBody(null);
    }
  }

  /**
   * Установить кадр для пули вражеской
   */
  private void setAnimationFrame(float delta) {
    ((TextureRegionDrawable) getDrawable()).setRegion(
        new TextureRegion(BulletService.getInstance().getAcidBulletAnimation().getKeyFrame(stateTime += delta, true)));
    super.act(delta);
  }
}
