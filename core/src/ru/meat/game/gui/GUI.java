package ru.meat.game.gui;

import static ru.meat.game.settings.Constants.GUI_ZOOM;
import static ru.meat.game.settings.Constants.MAIN_ZOOM;
import static ru.meat.game.settings.Constants.MOBILE;
import static ru.meat.game.settings.Constants.TEXTURE_PARAMETERS;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import java.math.BigDecimal;
import lombok.Data;
import ru.meat.game.loader.LoaderManager;

@Data
public class GUI {

  private static GUI instance;

  public static GUI getInstance() {
    if (instance == null) {
      instance = new GUI();
    }
    return instance;
  }

  private Stage stage;

  private OrthographicCamera camera;

  private Cursor cursor;

  private DamageScreen damageScreen;
  private Joystick joystick;

  private Image button;

  public GUI() {
    //init camera
    camera = new OrthographicCamera();
    camera.zoom = MAIN_ZOOM;
    camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0f);
    camera.update();

    stage = new Stage(new ScreenViewport(camera));

    //init hp sprite
    stage.addActor(new HPItem(stage));

    //init damage red screen sprite
    damageScreen = new DamageScreen();
    stage.addActor(damageScreen);

    //init левый джойстик
    if (MOBILE) {
      createChangeWeaponButton();
      createJoysticks();
    }
  }

  private void createChangeWeaponButton() {
    Texture texture = LoaderManager.getInstance().get("gui/changeWeaponButton.png");
    texture.setFilter(TextureFilter.MipMapLinearLinear, TextureFilter.MipMapLinearLinear);
    button = new Image(texture);
    button.setPosition(camera.viewportWidth,
        camera.viewportHeight);
    button.setScale(1.2f);
    stage.addActor(button);
    StringBuffer buffer;
    StringBuilder builder;
  }

  /**
   * Создать левый и правый контроллеры джойстика
   */
  private void createJoysticks() {
    this.joystick = new Joystick(stage);
  }


  /**
   * Рисовать интерфейс, вызывается в рендере
   */
  public void draw() {
    stage.act();
    stage.draw();
  }

  /**
   * Установить курсор "цель"
   */
  public void setAimCursor() {
    if (cursor == null) {
      createCursor();
    }

    Gdx.graphics.setCursor(cursor);
  }

  private void createCursor() {
    Pixmap pm = new Pixmap(Gdx.files.internal("gui/cursorAim.png"));

    float aimScale = 8;

    Pixmap pixmap100 = new Pixmap(
        BigDecimal.valueOf(pm.getWidth() / aimScale).intValue(),
        BigDecimal.valueOf(pm.getHeight() / aimScale).intValue(),
        pm.getFormat());
    pixmap100.drawPixmap(pm,
        0, 0, pm.getWidth(), pm.getHeight(),
        0, 0, pixmap100.getWidth(), pixmap100.getHeight()
    );
    cursor = Gdx.graphics.newCursor(pixmap100, pixmap100.getWidth() / 2, pixmap100.getHeight() / 2);
    pixmap100.dispose();
    pm.dispose();
  }

  public static void loadResources() {
    for (int i = 0; i < 51; i++) {
      LoaderManager.getInstance().load("gui/hpBar/" + i + ".png", Texture.class, TEXTURE_PARAMETERS);
    }

    LoaderManager.getInstance().load("gui/damageScreen.png", Texture.class, TEXTURE_PARAMETERS);

    LoaderManager.getInstance().load("gui/joystick/small.png", Texture.class, TEXTURE_PARAMETERS);
    LoaderManager.getInstance().load("gui/joystick/big.png", Texture.class, TEXTURE_PARAMETERS);

    LoaderManager.getInstance().load("gui/changeWeaponButton.png", Texture.class, TEXTURE_PARAMETERS);
  }

  /**
   * Обработать удар по игроку, сделать красным экран
   */
  public void handleHit() {
    damageScreen.handleHit();
  }

  public Float handleLeftJoystickTouch(int i) {
    return joystick.getAngleForLeftController(unprojectTouchPos(i));
  }

  public Float handleRightJoystickTouch(int i) {
    return joystick.getAngleForRightController(unprojectTouchPos(i));
  }

  /**
   * Является ли касание пальца на левом контроллере
   *
   * @param i индекс касания
   * @return
   */
  public boolean isOnLeftJoystick(int i) {
    Vector3 touchPos = unprojectTouchPos(i);
    return joystick.isLeftControllerTouched(touchPos);
  }

  public boolean isOnChangeWeaponButton(int i) {
    Vector3 point = unprojectTouchPos(i);
    float xBound = camera.viewportWidth * MAIN_ZOOM * GUI_ZOOM / 5 * 4;
    float yBound = camera.viewportHeight * MAIN_ZOOM * GUI_ZOOM / 5 * 4;
    return point.x > xBound && point.y > yBound;
  }

  /**
   * Является ли касание пальца на правом контроллере
   *
   * @param i индекс касания
   * @return
   */
  public boolean isOnRightJoystick(int i) {
    return joystick.isRightControllerTouched(unprojectTouchPos(i));
  }

  /**
   * Перевести координаты касания в координаты камеры гуи
   *
   * @param i
   * @return
   */
  private Vector3 unprojectTouchPos(int i) {
    Vector3 touchPos = new Vector3();
    touchPos.set(Gdx.input.getX(i), Gdx.input.getY(i), 0);
    camera.unproject(touchPos);
    return touchPos;
  }
}