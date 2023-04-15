package ru.meat.game.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

public class BloodSpot extends Image {

  @Getter
  private static final List<Texture> bloodSpotTextures = new ArrayList<>();

  public BloodSpot(FloatPair coords) {
    super(bloodSpotTextures.get(MathUtils.random(0, bloodSpotTextures.size() - 1)));
    this.setPosition(coords.getX() - this.getWidth() / 2, coords.getY() - this.getHeight() / 2);
    this.setOrigin(this.getWidth() / 2, this.getHeight() / 2);
    this.setRotation(MathUtils.random(0, 359));
    this.setColor(1, 1, 1, MathUtils.random(0.6f, 1));
  }
}
