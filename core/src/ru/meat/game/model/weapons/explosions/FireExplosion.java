package ru.meat.game.model.weapons.explosions;

import static ru.meat.game.settings.Constants.MAIN_ZOOM;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import ru.meat.game.Box2dWorld;
import ru.meat.game.model.FloatPair;

public class FireExplosion extends Explosion {

  private final static long explosionLifeTime = 600;
  private final Body body;

  public FireExplosion(FloatPair pos, Body body) {
    super(pos, MathUtils.random(0, 359), 4, ExplosionsService.getInstance().getFireExplosionAnimation());
    this.body = body;
  }

  @Override
  public void act(float delta) {
    Array<Fixture> fixtureList = body.getFixtureList();
    if (!fixtureList.isEmpty() && fixtureList.get(0).getUserData() instanceof ExplosionBodyUserData) {
      ExplosionBodyUserData userData = (ExplosionBodyUserData) fixtureList.get(0).getUserData();
      if (TimeUtils.timeSinceMillis(userData.getBornDate()) > explosionLifeTime) {
        try {
          body.setActive(false);
          Box2dWorld.getInstance().destroyBody(body);
          this.remove();
        } catch (Exception e) {
          System.out.println(e);
        }
      }
    }

    ((TextureRegionDrawable) getDrawable()).setRegion(
        ExplosionsService.getInstance().getFireExplosionAnimation().getKeyFrame(stateTime += delta, false));
    super.act(delta);
  }
}
