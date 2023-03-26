package ru.meat.game.gui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import lombok.Data;
import ru.meat.game.utils.GDXUtils;

@Data
public class Joystick {
  private JoystickController left;

  private JoystickController right;

  public Joystick(float screenWidth) {

    left = new JoystickController(0);

    right = new JoystickController(screenWidth);
  }

  public void draw(SpriteBatch batch) {
    left.draw(batch);
    right.draw(batch);
  }

  public boolean isLeftControllerTouched(Vector3 point) {
    float xBound = left.getBigCircle().getX() + left.getBigCircle().getWidth() * 4f;
    float yBound = left.getBigCircle().getY() + left.getBigCircle().getHeight() * 3.5f;
    return point.x < xBound && point.y < yBound;
  }

  public boolean isRightControllerTouched(Vector3 point) {
    float xBound = right.getBigCircle().getX() - 700;
    float yBound = right.getBigCircle().getY() + right.getBigCircle().getHeight() * 3.5f;
    return point.x > xBound && point.y < yBound;
  }


  public float getAngleForLeftController(Vector3 point) {
    return GDXUtils.calcAngleBetweenTwoPoints(point.x, point.y,
        left.getBigCircle().getX() + left.getBigCircle().getWidth() / 2,
        left.getBigCircle().getY() + left.getBigCircle().getHeight() / 2);
  }

  public float getAngleForRightController(Vector3 point) {
    return GDXUtils.calcAngleBetweenTwoPoints(point.x, point.y,
        right.getBigCircle().getX() + right.getBigCircle().getWidth() / 2,
        right.getBigCircle().getY() + right.getBigCircle().getHeight() / 2);
  }
}
