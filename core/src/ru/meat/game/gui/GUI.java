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
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.TimeUtils;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import ru.meat.game.loader.LoaderManager;
import ru.meat.game.utils.GDXUtils;

@Data
public class GUI {

  private static GUI instance;

  public static GUI getInstance() {
    if (instance == null) {
      instance = new GUI();
    }
    return instance;
  }

  private SpriteBatch batch;

  /**
   * спрайт хп бара
   */
  private Sprite hpSprite;
  private OrthographicCamera camera;

  /**
   * текстуры хп бара
   */
  private final List<Texture> hpTextures = new ArrayList<>();

  private Cursor cursor;
  private Sprite damageScreen;
  private Float damageScreenAlpha;
  private Long damageScreenTransparentCounter;

  private Joystick joystick;

  /**
   * Значение максимального хп, для подсчёта оставшегося процента хп
   */
  private double fullHp;

  public GUI() {
    for (int i = 0; i < 51; i++) {
      hpTextures.add(LoaderManager.getInstance().get("gui/hpBar/" + i + ".png"));
    }
    hpTextures.forEach(t -> t.setFilter(TextureFilter.MipMapLinearLinear, TextureFilter.MipMapLinearLinear));

    //init camera
    camera = new OrthographicCamera();
    camera.zoom = MAIN_ZOOM * GUI_ZOOM;
    camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0f);

    batch = new SpriteBatch();
    batch.setProjectionMatrix(camera.combined);

    //init hp sprite
    hpSprite = new Sprite(getActualTexture(null));
    hpSprite.setPosition(camera.viewportWidth / 2 * MAIN_ZOOM * GUI_ZOOM - hpSprite.getWidth() / 2,
        camera.viewportHeight * MAIN_ZOOM * GUI_ZOOM - hpSprite.getHeight());
    hpSprite.flip(false, true);

    //init damage red screen sprite
    damageScreen = new Sprite((Texture) LoaderManager.getInstance().get("gui/damageScreen.png"));
    damageScreen.setScale(Gdx.graphics.getWidth() / damageScreen.getWidth() * MAIN_ZOOM * GUI_ZOOM,
        Gdx.graphics.getHeight() / damageScreen.getHeight() * MAIN_ZOOM * GUI_ZOOM);
    damageScreen.setPosition(camera.viewportWidth / 2 * MAIN_ZOOM * GUI_ZOOM - damageScreen.getWidth() / 2,
        camera.viewportHeight / 2 * MAIN_ZOOM * GUI_ZOOM - damageScreen.getHeight() / 2);
    damageScreenAlpha = 0f;
    damageScreen.setAlpha(damageScreenAlpha);
    if (damageScreenTransparentCounter == null) {
      damageScreenTransparentCounter = TimeUtils.millis();
    }

    //init левый джойстик
    if (MOBILE) {
      createJoysticks();
    }
  }

  /**
   * Создать левый и правый контроллеры джойстика
   */
  private void createJoysticks() {
    this.joystick = new Joystick(camera.viewportWidth * MAIN_ZOOM * GUI_ZOOM);
  }


  /**
   * Рисовать интерфейс, вызывается в рендере
   *
   * @param hp
   */
  public void draw(double hp) {
    handleDamageScreenTransparency();

    batch.begin();
    hpSprite.setTexture(getActualTexture(hp));
    hpSprite.draw(batch);
    damageScreen.draw(batch);
    if (MOBILE) {
      joystick.draw(batch);
    }
    batch.end();
  }

  /**
   * Обработать прозрачность красного экрана
   */
  private void handleDamageScreenTransparency() {
    if (damageScreenAlpha != 0 && TimeUtils.timeSinceMillis(damageScreenTransparentCounter) > 100) {
      damageScreenAlpha = damageScreenAlpha - 0.005f;
    }
    if (damageScreenAlpha < 0) {
      damageScreenAlpha = 0f;
    }
    damageScreen.setAlpha(damageScreenAlpha);
  }

  /**
   * Посчитать сколько процентов хп осталось и выдать нужную текстуру хпБара
   *
   * @param hp оставшиеся хп
   */
  private Texture getActualTexture(Double hp) {
    if (hp == null) {
      return hpTextures.get(0);
    }
    int i = Integer.parseInt(
        BigDecimal.valueOf((100 - hp / (fullHp / 100)) / 2).setScale(0, RoundingMode.HALF_DOWN).toString());
    if (i < hpTextures.size()) {
      return hpTextures.get(i);
    } else {
      return hpTextures.get(hpTextures.size() - 1);
    }
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
  }

  public void initFullHp(Double hp) {
    this.fullHp = hp;
  }

  /**
   * Обработать удар по игроку, сделать красным экран
   */
  public void handleHit() {
    damageScreenAlpha = damageScreenAlpha + 0.5f;
    if (damageScreenAlpha > 1) {
      damageScreenAlpha = 1f;
    }
  }

  public Float handleLeftJoystickTouch(int i) {
    return joystick.getAngleForLeftController(unprojectTouchPos(i));
  }

  public Float handleRightJoystickTouch(int i) {
    return joystick.getAngleForRightController(unprojectTouchPos(i));
  }

  /**
   * Является ли касание пальца на левом контроллере
   * @param i индекс касания
   * @return
   */
  public boolean isOnLeftJoystick(int i) {
    Vector3 touchPos = unprojectTouchPos(i);
    return joystick.isLeftControllerTouched(touchPos);
  }

  /**
   * Является ли касание пальца на правом контроллере
   * @param i индекс касания
   * @return
   */
  public boolean isOnRightJoystick(int i) {
    return joystick.isRightControllerTouched(unprojectTouchPos(i));
  }

  /**
   * Перевести координаты касания в координаты камеры гуи
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