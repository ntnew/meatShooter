package ru.meat.game.menu;

import static ru.meat.game.settings.Constants.DEBUG;
import static ru.meat.game.utils.GDXUtils.createButton;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import java.util.Arrays;
import java.util.List;
import ru.meat.game.MyGame;
import ru.meat.game.service.AudioService;

public class SettingsMenu implements Screen {

  final MyGame game;

  /**
   * Флаг того, что игра настройки открыты из паузы
   */
  private final boolean pause;

  private final List<String> resolutions = Arrays.asList("800x600", "1280x720", "1920x1080");

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

  public SettingsMenu(final MyGame game, boolean pause) {
    this.game = game;
    this.pause = pause;
    preferences = Gdx.app.getPreferences("My Preferences");

    game.initStage();

    createResolutionBox();
    createButtons();
    createSaveButton();

    createDecreaseEffVolButton();
    createAddEffVolButton();

    createAddMusicVolButton();
    createDecreaseMusicVolButton();
    createMusicVolumeBox();

    table = new Table();
    table.setSize(300, 300);
    table.setPosition(Gdx.graphics.getWidth() / 3, Gdx.graphics.getHeight() / 3);
    table.setDebug(DEBUG);
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

    game.getStage().addActor(table);
  }

  private void createResolutionBox() {
    int screen_width = preferences.getInteger("SCREEN_WIDTH");
    int screen_height = preferences.getInteger("SCREEN_HEIGHT");
    resolutionBox = new Label(screen_width + "x" + screen_height, game.getLabelStyle());
    resolutionBox.setAlignment(Align.center);
  }

  private void createButtons() {
    nextButton = createButton(game.getTextButtonStyle(), ">>", new ChangeListener() {
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

    prevButton = createButton(game.getTextButtonStyle(), "<<", new ChangeListener() {
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

    effectVolumeBox = new Label(String.format("%.0f", preferences.getFloat("EFFECT_VOLUME") * 100),
        game.getLabelStyle());
  }

  private void createAddEffVolButton() {
    nextEffVolButton = createButton(game.getTextButtonStyle(), ">>", new ChangeListener() {
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
    prevEffVolButton = createButton(game.getTextButtonStyle(), "<<", new ChangeListener() {
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
    addMusicVolButton = createButton(game.getTextButtonStyle(), ">>", new ChangeListener() {
      @Override
      public void changed(ChangeEvent event, Actor actor) {
        double v = Double.parseDouble(musicVolumeBox.getText().toString());
        v = v + 5;
        v = Math.min(v, 100);
        musicVolumeBox.setText(String.format("%.0f", v));
        if (AudioService.getInstance().getCurrentMusic() != null) {
          AudioService.getInstance().getCurrentMusic().setVolume(Float.parseFloat(String.valueOf(v / 100)));
        }
      }
    });
  }

  private void createDecreaseMusicVolButton() {
    decreaseMusicVolButton = createButton(game.getTextButtonStyle(), "<<", new ChangeListener() {
      @Override
      public void changed(ChangeEvent event, Actor actor) {
        double v = Double.parseDouble(musicVolumeBox.getText().toString());
        v = v - 5;
        v = Math.max(v, 0);
        musicVolumeBox.setText(String.format("%.0f", v));
        if (AudioService.getInstance().getCurrentMusic() != null) {
          AudioService.getInstance().getCurrentMusic().setVolume(Float.parseFloat(String.valueOf(v / 100)));
        }
      }
    });
  }

  private void createSaveButton() {
    saveButton = createButton(game.getTextButtonStyle(), "Save", new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        String[] xes = resolutionBox.getText().toString().split("x");
        int newWidth = Integer.parseInt(xes[0]);
        int newHeight = Integer.parseInt(xes[1]);

        if (newWidth != preferences.getInteger("SCREEN_WIDTH")) {
          Gdx.graphics.setWindowedMode(newWidth, newHeight);
          preferences.putInteger("SCREEN_WIDTH", newWidth);
          preferences.putInteger("SCREEN_HEIGHT", newHeight);
        }

        preferences.putFloat("EFFECT_VOLUME", Float.parseFloat(effectVolumeBox.getText().toString()) / 100);
        preferences.putFloat("MUSIC_VOLUME", Float.parseFloat(musicVolumeBox.getText().toString()) / 100);

        if (AudioService.getInstance().getCurrentMusic() != null) {
          AudioService.getInstance().getCurrentMusic().setVolume(preferences.getFloat("MUSIC_VOLUME"));
        }

        preferences.flush();
        if (pause) {
          game.setScreen(new PauseMenu(game, game.getGameZone()));
        } else {
          game.setScreen(new MainMenu(game));
        }
      }
    });

    backButton = createButton(game.getTextButtonStyle(), "Back", new ChangeListener() {
      @Override
      public void changed(ChangeEvent event, Actor actor) {
        if (AudioService.getInstance().getCurrentMusic() != null) {
          AudioService.getInstance().getCurrentMusic().setVolume(preferences.getFloat("MUSIC_VOLUME"));
        }
        if (pause) {
          game.setScreen(new PauseMenu(game, game.getGameZone()));
        } else {
          game.setScreen(new MainMenu(game));
        }
      }
    });
  }

  @Override
  public void show() {

  }

  @Override
  public void render(float delta) {
    ScreenUtils.clear(0, 0, 0, 1);
    game.drawStage();
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
