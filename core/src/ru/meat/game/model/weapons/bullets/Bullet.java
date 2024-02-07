package ru.meat.game.model.weapons.bullets;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.TimeUtils;
import lombok.Data;

@Data
public class Bullet extends Image {

  private Body body;

  private Long bornDate;

  public Bullet(Texture texture, Body body, float scale) {
    super(texture);
    this.body = body;
    this.bornDate = TimeUtils.millis();
    setScale(scale);
  }
}
