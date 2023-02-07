package ru.meat.game;

import static ru.meat.game.settings.Constants.MENU_ZOOM;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import lombok.Data;
import ru.meat.game.game.GameZone;
import ru.meat.game.loader.LoaderManager;
import ru.meat.game.menu.MainMenu;
import ru.meat.game.service.AudioService;
import ru.meat.game.service.BloodService;
import ru.meat.game.settings.Settings;

@Data
public class MyGame extends Game {

  private SpriteBatch batch;
  private BitmapFont font;
  private LabelStyle labelStyle;
  private TextButtonStyle textButtonStyle;

  private OrthographicCamera menuCamera;

  private Stage stage;

  private GameZone gameZone;

  private boolean loaded = false;

  public void create() {
    Gdx.graphics.setWindowedMode(Settings.getInstance().SCREEN_WIDTH, Settings.getInstance().SCREEN_HEIGHT);

//   Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
// fullscreen
    batch = new SpriteBatch();
    font = new BitmapFont();
    initCam();

    textButtonStyle = new TextButtonStyle();
    textButtonStyle.font = font;
    textButtonStyle.fontColor = Color.WHITE;

    labelStyle = new LabelStyle();
    labelStyle.font = font;
    labelStyle.fontColor = Color.WHITE;

    AudioService.getInstance();
    BloodService.getInstance();
  }

  public void render() {
    super.render(); // important!
    if (!loaded && LoaderManager.getInstance().update()) {
      this.setScreen(new MainMenu(this));
      loaded = true;
    }
  }

  public void dispose() {
    batch.dispose();
    font.dispose();
  }

  private void initCam() {
    menuCamera = new OrthographicCamera();
    menuCamera.zoom = MENU_ZOOM;
    menuCamera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    menuCamera.update();
  }

  private void updateCamera() {
    menuCamera.update();
    batch.setProjectionMatrix(menuCamera.combined);
  }

  public void initStage() {
    stage = new Stage(new ScreenViewport(menuCamera));
    Gdx.input.setInputProcessor(stage);
  }

  public void drawStage() {
    updateCamera();
    batch.begin();
    stage.act();
    stage.draw();
    batch.end();
  }
}