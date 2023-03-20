package ru.meat.game.gui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import lombok.Data;

@Data
public class Joystick {

  private final float screenWidth;

  private JoystickController left;

  private JoystickController right;

  public Joystick(float screenWidth) {
    this.screenWidth = screenWidth;
    left = new JoystickController(0);

    right = new JoystickController(screenWidth);


  }

  public void draw(SpriteBatch batch) {
    left.draw(batch);
    right.draw(batch);
  }
}
