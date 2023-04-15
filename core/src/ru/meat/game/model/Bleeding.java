package ru.meat.game.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Bleeding extends Image {

  private Animation<Texture> animation = null;
  private float stateTime = 0;

  public Bleeding(Animation<Texture> animation, FloatPair coord) {
    super(animation.getKeyFrame(0));
    this.setPosition(coord.getX() - this.getDrawable().getMinWidth() / 2,
        coord.getY() - this.getDrawable().getMinHeight() / 2);
    this.scaleBy(1.2f);
    this.setOrigin(this.getDrawable().getMinWidth() / 2, this.getDrawable().getMinHeight() / 2);
    this.setRotation(MathUtils.random(0, 359));
    this.animation = animation;
  }

  @Override
  public void act(float delta) {
    if (Objects.equals(((TextureRegionDrawable) getDrawable()).getRegion().getTexture(),
        animation.getKeyFrame(animation.getKeyFrames().length - 1))) {
      this.remove();
    }
    ((TextureRegionDrawable) getDrawable()).setRegion(
        new TextureRegion(animation.getKeyFrame(stateTime += delta, false)));
    super.act(delta);
  }
}
