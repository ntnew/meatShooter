package ru.meat.game.menu;

import static ru.meat.game.utils.GDXUtils.createButton;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import ru.meat.game.MyGame;

public class MainMenu implements Screen {

  final MyGame game;
  private final Stage stage;
  private OrthographicCamera camera;

  private Table table;
  private Button newGameButton;
  private Button upgradeButton;

  private Button optionsButton;

  private Button exitButton;

  public MainMenu(final MyGame game) {
    this.game = game;

    initCam();

    stage = new Stage(new ScreenViewport());
    Gdx.input.setInputProcessor(stage);

    createButtons();

    table = new Table();
    table.setSize(300, 300);
    table.setPosition(10, 10);
    table.setDebug(false);
    table.add(newGameButton);
    table.row();
    table.add(upgradeButton);
    table.row();
    table.add(optionsButton);
    table.row();
    table.add(exitButton);

    stage.addActor(table);
  }

  private void initCam() {
    camera = new OrthographicCamera();
    camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
  }

  private void createButtons() {
    newGameButton = createButton(game.getTextButtonStyle(), "New Game", new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        game.setScreen(new MapSelectorMenu(game));
      }
    });

    upgradeButton = createButton(game.getTextButtonStyle(), "Upgrade", new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        game.setScreen(new UpgradeMenu(game));
      }
    });

    optionsButton = createButton(game.getTextButtonStyle(), "Options", new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        game.setScreen(new SettingsMenu(game));
      }
    });

    exitButton = createButton(game.getTextButtonStyle(), "Exit", new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        Gdx.app.exit();
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

  //...Rest of class omitted for succinctness.

}
