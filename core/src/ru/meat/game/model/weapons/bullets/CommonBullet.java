package ru.meat.game.model.weapons.bullets;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import ru.meat.game.Box2dWorld;
import ru.meat.game.settings.Constants;

public class CommonBullet extends Bullet {

  public CommonBullet(Texture texture, Body body, float scale) {
    super(texture, body, scale);
    this.setOrigin(this.getDrawable().getMinWidth() - this.getDrawable().getMinWidth() / 12,
        this.getDrawable().getMinHeight() / 2);
    this.setRotation(body.getLinearVelocity().angleDeg());
  }

  @Override
  public void act(float delta) {
    Array<Fixture> fixtureList = getBody().getFixtureList();
    if (!fixtureList.isEmpty()) {
      if (fixtureList.get(0).getUserData() instanceof BulletBodyUserData) {
        BulletBodyUserData userData = (BulletBodyUserData) fixtureList.get(0).getUserData();
        if (userData.isNeedDispose() || TimeUtils.timeSinceMillis(getBornDate()) > 8000 && getBody() != null) {
          try {
            Box2dWorld.getInstance().destroyBody(getBody());
            setBody(null);
            remove();
          } catch (Exception e) {
            System.out.println(e.getMessage() + "\n" + Arrays.toString(e.getStackTrace()));
          }
        } else {
          //Переместить пулю
          Vector2 position = fixtureList.get(0).getBody().getPosition();
          setPosition(
              position.x * Constants.WORLD_TO_VIEW - getDrawable().getMinWidth() + getDrawable().getMinWidth() / 12,
              position.y * Constants.WORLD_TO_VIEW - getDrawable().getMinHeight() / 2);
        }
      } else {
        System.out.println(fixtureList.get(0).getUserData());
      }
    } else {
      remove();
    }
    super.act(delta);
  }
}
