package ru.meat.game.menu;

import static ru.meat.game.settings.Constants.DEBUG;
import static ru.meat.game.utils.GDXUtils.createButton;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Table.Debug;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import ru.meat.game.MyGame;
import ru.meat.game.service.AudioService;

public class MainMenu implements Screen {

  final MyGame game;
  private Table table;
  private Button newGameButton;
  private Button upgradeButton;

  private Button optionsButton;

  private Button exitButton;

  public MainMenu(final MyGame game) {
    this.game = game;

    game.initStage();

    createButtons();

    table = new Table();
    table.setSize(300, 300);
    table.setPosition(Gdx.graphics.getWidth()/3, Gdx.graphics.getHeight()/3);
    table.setDebug(DEBUG);
    table.add(newGameButton).width(100).height(30);
    table.row();
    table.add(upgradeButton).width(100).height(30);
    table.row();
    table.add(optionsButton).width(100).height(30);
    table.row();
    table.add(exitButton).width(100).height(30);

    game.getStage().addActor(table);
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
    AudioService.getInstance().playMainMenuMusic();
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

  //...Rest of class omitted for succinctness.

}
