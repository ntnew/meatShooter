package ru.meat.game.gui;

import static ru.meat.game.settings.Constants.GUI_ZOOM;
import static ru.meat.game.settings.Constants.MAIN_ZOOM;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import lombok.Data;
import ru.meat.game.loader.LoaderManager;

@Data
public class JoystickController {

  private static float bigControllerScale = 3f;
  private static float smallControllerScale = bigControllerScale / 4;


  private Image smallCircle;

  private Image bigCircle;

  public JoystickController(Stage stage, boolean isLeft) {
    bigCircle = new Image((Texture) LoaderManager.getInstance().get("gui/joystick/big.png"));
    bigCircle.setPosition(calcOffsetX(stage.getWidth(), isLeft), -stage.getHeight());
    bigCircle.setColor(1, 1, 1, 0.5f);
    bigCircle.setScale(bigControllerScale);

    stage.addActor(bigCircle);
    smallCircle = new Image((Texture) LoaderManager.getInstance().get("gui/joystick/small.png"));
    smallCircle.setPosition(
        calcOffsetX(stage.getWidth(), isLeft) + bigCircle.getWidth() * bigControllerScale / 2
            - smallCircle.getWidth() * smallControllerScale / 2,
        -stage.getHeight() + bigCircle.getHeight() * bigControllerScale / 2
            - smallCircle.getHeight() * smallControllerScale / 2);
    smallCircle.setScale(smallControllerScale);
    smallCircle.setColor(1, 1, 1, 0.5f);

    stage.addActor(smallCircle);
  }

  private float calcOffsetX(float screenWidth, boolean isLeft) {
    if (isLeft) {
      return -screenWidth;
    } else {
      return screenWidth * GUI_ZOOM;
    }
  }
}
