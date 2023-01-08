package ru.meat.game.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import java.util.Arrays;
import java.util.List;
import ru.meat.game.MyGame;
import ru.meat.game.service.AudioService;

public class SettingsMenu implements Screen {

  final MyGame game;
  private final Stage stage;
  private OrthographicCamera camera;

  private List<String> resolutions = Arrays.asList("800x600", "1280x720", "1920x1080");

  private Table table;

  private Label resolutionBox;
  private Button nextButton;
  private Button prevButton;


  private Label effectVolumeBox;
  private Button nextEffVolButton;
  private Button prevEffVolButton;


  private Label musicVolumeBox;
  private Button addMusicVolButton;
  private Button decreaseMusicVolButton;


  private Button backButton;
  private Button saveButton;
  private Preferences preferences;

  public SettingsMenu(final MyGame game) {
    this.game = game;
    preferences = Gdx.app.getPreferences("My Preferences");

    stage = new Stage(new ScreenViewport());
    Gdx.input.setInputProcessor(stage);

    initCam();

    createBackButton();
    createResolutionBox();
    createNextButton();
    createPreviousButton();
    createSaveButton();

    createEffVolumeBox();
    createDecreaseEffVolButton();
    createAddEffVolButton();

    createAddMusicVolButton();
    createDecreaseMusicVolButton();
    createMusicVolumeBox();

    table = new Table();
    table.setSize(300, 300);
    table.setPosition(10, 10);
    table.setDebug(false);
    Label resolution = new Label("resolution", game.getLabelStyle());
    resolution.setAlignment(Align.center);
    table.add(resolution).width(120);
    table.add(prevButton);
    table.add(resolutionBox).width(75);
    table.add(nextButton);
    table.row();
    table.add(new Label("effect volume", game.getLabelStyle()));
    table.add(prevEffVolButton);
    table.add(effectVolumeBox);
    table.add(nextEffVolButton);
    table.row();
    table.add(new Label("music volume", game.getLabelStyle()));
    table.add(decreaseMusicVolButton);
    table.add(musicVolumeBox);
    table.add(addMusicVolButton);
    table.row();
    table.add(saveButton);
    table.add();
    table.add(backButton);

    stage.addActor(table);


  }

  private void initCam() {
    camera = new OrthographicCamera();
    camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
  }

  private void createBackButton() {
    backButton = new TextButton("Back", game.getTextButtonStyle());
    backButton.addListener(new ChangeListener() {
      @Override
      public void changed(ChangeEvent event, Actor actor) {
        game.setScreen(new MainMenu(game));
        AudioService.getInstance().getCurrentMusic().setVolume(preferences.getFloat("MUSIC_VOLUME"));
      }
    });
  }

  private void createResolutionBox() {
    int screen_width = preferences.getInteger("SCREEN_WIDTH");
    int screen_height = preferences.getInteger("SCREEN_HEIGHT");
    resolutionBox = new Label(screen_width + "x" + screen_height, game.getLabelStyle());
    resolutionBox.setAlignment(Align.center);
  }

  private void createNextButton() {
    nextButton = new TextButton(">>", game.getTextButtonStyle());
    nextButton.addListener(new ChangeListener() {
      @Override
      public void changed(ChangeEvent event, Actor actor) {
        String s = resolutions.stream().filter(x -> x.equals(resolutionBox.getText().toString())).findFirst()
            .orElse("800x600");
        int i = resolutions.indexOf(s);
        if (i < resolutions.size() - 1) {
          i += 1;
        }
        resolutionBox.setText(resolutions.get(i));
      }
    });
  }

  private void createPreviousButton() {
    prevButton = new TextButton("<<", game.getTextButtonStyle());
    prevButton.addListener(new ChangeListener() {
      @Override
      public void changed(ChangeEvent event, Actor actor) {
        String s = resolutions.stream().filter(x -> x.equals(resolutionBox.getText().toString())).findFirst()
            .orElse("800x600");
        int i = resolutions.indexOf(s);
        if (i > 0) {
          i -= 1;
        }
        resolutionBox.setText(resolutions.get(i));
      }
    });
  }

  private void createEffVolumeBox() {
    float effect_volume = preferences.getFloat("EFFECT_VOLUME");
    effectVolumeBox = new Label(String.format("%.0f", effect_volume * 100), game.getLabelStyle());
  }

  private void createAddEffVolButton() {
    nextEffVolButton = new TextButton(">>", game.getTextButtonStyle());
    nextEffVolButton.addListener(new ChangeListener() {
      @Override
      public void changed(ChangeEvent event, Actor actor) {
        double v = Double.parseDouble(effectVolumeBox.getText().toString());
        v = v + 5;
        v = Math.min(v, 100);
        effectVolumeBox.setText(String.format("%.0f", v));
        AudioService.getInstance().playTestShoot(Float.parseFloat(String.valueOf(v / 100)));
      }
    });
  }

  private void createDecreaseEffVolButton() {
    prevEffVolButton = new TextButton("<<", game.getTextButtonStyle());
    prevEffVolButton.addListener(new ChangeListener() {
      @Override
      public void changed(ChangeEvent event, Actor actor) {
        double v = Double.parseDouble(effectVolumeBox.getText().toString());
        v = v - 5;
        v = Math.max(v, 0);
        effectVolumeBox.setText(String.format("%.0f", v));
        AudioService.getInstance().playTestShoot(Float.parseFloat(String.valueOf(v / 100)));
      }
    });
  }

  private void createMusicVolumeBox() {
    float effect_volume = preferences.getFloat("MUSIC_VOLUME");
    musicVolumeBox = new Label(String.format("%.0f", effect_volume * 100), game.getLabelStyle());
  }

  private void createAddMusicVolButton() {
    addMusicVolButton = new TextButton(">>", game.getTextButtonStyle());
    addMusicVolButton.addListener(new ChangeListener() {
      @Override
      public void changed(ChangeEvent event, Actor actor) {
        double v = Double.parseDouble(musicVolumeBox.getText().toString());
        v = v + 5;
        v = Math.min(v, 100);
        musicVolumeBox.setText(String.format("%.0f", v));
        AudioService.getInstance().getCurrentMusic().setVolume(Float.parseFloat(String.valueOf(v / 100)));
      }
    });
  }

  private void createDecreaseMusicVolButton() {
    decreaseMusicVolButton = new TextButton("<<", game.getTextButtonStyle());
    decreaseMusicVolButton.addListener(new ChangeListener() {
      @Override
      public void changed(ChangeEvent event, Actor actor) {
        double v = Double.parseDouble(musicVolumeBox.getText().toString());
        v = v - 5;
        v = Math.max(v, 0);
        musicVolumeBox.setText(String.format("%.0f", v));
        AudioService.getInstance().getCurrentMusic().setVolume(Float.parseFloat(String.valueOf(v / 100)));
      }
    });
  }

  private void createSaveButton() {
    saveButton = new TextButton("Save", game.getTextButtonStyle());
    saveButton.addListener(new ChangeListener() {
      @Override
      public void changed(ChangeEvent event, Actor actor) {

        int screenWidth = preferences.getInteger("SCREEN_WIDTH");

        String[] xes = resolutionBox.getText().toString().split("x");
        int newWidth = Integer.parseInt(xes[0]);
        int newHeight = Integer.parseInt(xes[1]);

        if (newWidth != screenWidth) {
          Gdx.graphics.setWindowedMode(newWidth, newHeight);
          preferences.putInteger("SCREEN_WIDTH", newWidth);
          preferences.putInteger("SCREEN_HEIGHT", newHeight);
        }

        preferences.putFloat("EFFECT_VOLUME", Float.parseFloat(effectVolumeBox.getText().toString()) / 100);
        preferences.putFloat("MUSIC_VOLUME", Float.parseFloat(musicVolumeBox.getText().toString()) / 100);

        preferences.flush();
        game.setScreen(new MainMenu(game));
      }
    });
  }

  @Override
  public void show() {

  }

  @Override
  public void render(float delta) {
    ScreenUtils.clear(0, 0, 0, 1);
    camera.update();
    game.getBatch().setProjectionMatrix(camera.combined);

    game.getBatch().begin();

    stage.act();
    stage.draw();
    game.getBatch().end();
  }

  @Override
  public void resize(int width, int height) {

  }

  @Override
  public void pause() {

  }

  @Override
  public void resume() {

  }

  @Override
  public void hide() {

  }

  @Override
  public void dispose() {

  }
}
