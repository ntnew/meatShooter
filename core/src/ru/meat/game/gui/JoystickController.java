package ru.meat.game.gui;

import static ru.meat.game.settings.Constants.GUI_ZOOM;
import static ru.meat.game.settings.Constants.MAIN_ZOOM;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import lombok.Data;
import ru.meat.game.loader.LoaderManager;

@Data
public class JoystickController {

  private static float bigControllerScale = 3f;
  private static float smallControllerScale = bigControllerScale / 4;


  private Sprite smallCircle;

  private Sprite bigCircle;

  public JoystickController(float screenWidth) {
    bigCircle = new Sprite((Texture) LoaderManager.getInstance().get("gui/joystick/big.png"));
    bigCircle.setOrigin(bigCircle.getWidth() / 2, bigCircle.getHeight() / 2);
    bigCircle.setOriginBasedPosition(calcOffsetX(screenWidth, bigCircle), bigCircle.getHeight() + 600);
    bigCircle.setAlpha(0.5f);
    bigCircle.setScale(bigControllerScale);

    smallCircle = new Sprite((Texture) LoaderManager.getInstance().get("gui/joystick/small.png"));
    smallCircle.setOrigin(smallCircle.getWidth() / 2, smallCircle.getHeight() / 2);
    smallCircle.setOriginBasedPosition(calcOffsetX(screenWidth, smallCircle), smallCircle.getHeight() + 600);
    smallCircle.setScale(0.75f);
    smallCircle.setAlpha(0.5f);
  }

  private float calcOffsetX(float screenWidth, Sprite joystick) {
    if (screenWidth == 0) {
      return joystick.getWidth() + 600;
    } else {
      return screenWidth - joystick.getWidth() - 600;
    }
  }

  public static void loadResources(){

  }

  public void draw(SpriteBatch batch){
    bigCircle.draw(batch);
    smallCircle.draw(batch);
  }
}
