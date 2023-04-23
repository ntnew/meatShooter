package ru.meat.game.model.weapons.explosions;

import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import ru.meat.game.model.FloatPair;

public class AcidExplosion extends Explosion{


  public AcidExplosion(FloatPair pos, float angle, Long bornDate,
      float scale) {
    super(pos, angle, bornDate, scale, ExplosionsService.getInstance().getAcidExplosionAnimation());
  }

  @Override
  public void act(float delta) {
    ((TextureRegionDrawable) getDrawable()).setRegion(
        ExplosionsService.getInstance().getAcidExplosionAnimation().getKeyFrame(stateTime += delta, false));
    super.act(delta);
  }
}
