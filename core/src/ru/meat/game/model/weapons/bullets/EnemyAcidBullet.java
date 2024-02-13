package ru.meat.game.model.weapons.bullets;

import static ru.meat.game.settings.Constants.WORLD_TO_VIEW;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import lombok.Data;
import ru.meat.game.Box2dWorld;
import ru.meat.game.model.FloatPair;
import ru.meat.game.model.weapons.explosions.ExplosionsService;
import ru.meat.game.service.BulletService;
import ru.meat.game.settings.Constants;

@Data
public class EnemyAcidBullet extends Image {

  private float stateTime;

  private Body body;

  private Long bornDate;

  public EnemyAcidBullet(Body body, float bulletScale) {
    super(BulletService.getInstance().getAcidBulletAnimation().getKeyFrame(0));

    this.body = body;
    this.bornDate = TimeUtils.millis();
    this.setOrigin(this.getDrawable().getMinWidth() / 2 , this.getDrawable().getMinHeight()/ 3f);
    setScale(bulletScale);
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
          remove();
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
      System.out.println("Error by deleting acid bullet and creating explosion");
    }
  }

  private void setPosition(Array<Fixture> fixtureList) {
    Vector2 position = fixtureList.get(0).getBody().getPosition();
    setPosition(
        position.x * Constants.WORLD_TO_VIEW - this.getDrawable().getMinWidth() / 2f,
        position.y * Constants.WORLD_TO_VIEW - this.getDrawable().getMinHeight() / 3f);
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
