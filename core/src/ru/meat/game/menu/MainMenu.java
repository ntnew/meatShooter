package ru.meat.game.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
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

  private Button newGameButton;
  private Button exitButton;
  private Button optionsButton;

  public MainMenu(final MyGame game) {
    this.game = game;

    initCam();

    stage = new Stage(new ScreenViewport());
    Gdx.input.setInputProcessor(stage);

    createButtons();
  }

  private void initCam() {
    camera = new OrthographicCamera();
    camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
  }

  private void createButtons() {
    TextButtonStyle textButtonStyle = new TextButtonStyle();
    textButtonStyle.font = this.game.font;
    textButtonStyle.fontColor = Color.WHITE;

    createStartButton(textButtonStyle);

    createOptionsButton(textButtonStyle);

    createExitButton(textButtonStyle);
  }

  private void createExitButton(TextButtonStyle textButtonStyle) {
    exitButton = new TextButton("Exit", textButtonStyle);
    exitButton.setPosition(Gdx.graphics.getWidth() / 3, Gdx.graphics.getHeight() / 2 - 100);
    exitButton.setSize(200, 70);
    exitButton.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        Gdx.app.exit();
      }
    });
    stage.addActor(exitButton);
  }

  private void createOptionsButton(TextButtonStyle textButtonStyle) {
    optionsButton = new TextButton("Settings", textButtonStyle);
    optionsButton.setPosition(Gdx.graphics.getWidth() / 3, Gdx.graphics.getHeight() / 2 - 50);
    optionsButton.setSize(200, 70);
    optionsButton.addListener(new ChangeListener() {
      @Override
      public void changed(ChangeEvent event, Actor actor) {
        System.out.println("Settings");
      }
    });
    stage.addActor(optionsButton);
  }

  private void createStartButton(TextButtonStyle textButtonStyle) {
    newGameButton = new TextButton("New Game", textButtonStyle);
    newGameButton.setSize(200, 50);
    newGameButton.setPosition(Gdx.graphics.getWidth() / 3, Gdx.graphics.getHeight() / 2);
    newGameButton.addListener(new ChangeListener() {
      @Override
      public void changed(ChangeEvent event, Actor actor) {
        game.setScreen(new MapSelectorMenu(game));
        dispose();
      }
    });
    stage.addActor(newGameButton);
  }

  @Override
  public void show() {

  }

  @Override
  public void render(float delta) {
    ScreenUtils.clear(0, 0, 0, 1);

    camera.update();
    game.batch.setProjectionMatrix(camera.combined);

    game.batch.begin();
    newGameButton.draw(game.batch, 1);
    optionsButton.draw(game.batch, 1);
    exitButton.draw(game.batch, 1);

    game.batch.end();
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
