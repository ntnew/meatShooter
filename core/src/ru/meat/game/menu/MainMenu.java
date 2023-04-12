package ru.meat.game.menu;

import static ru.meat.game.settings.Constants.DEBUG;
import static ru.meat.game.utils.GDXUtils.createButton;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import ru.meat.game.MyGame;
import ru.meat.game.service.AudioService;

public class MainMenu implements Screen {

  private Table table;
  private Button newGameButton;
  private Button upgradeButton;

  private Button optionsButton;

  private Button exitButton;

  public MainMenu() {

    MyGame.getInstance().initStage();

    createButtons();

    table = new Table();
    table.setSize(300, 300);
    table.setPosition(Gdx.graphics.getWidth() / 3, Gdx.graphics.getHeight() / 3);
    table.setDebug(DEBUG);
    table.add(newGameButton).width(100).height(30);
    table.row();
    table.add(upgradeButton).width(100).height(30);
    table.row();
    table.add(optionsButton).width(100).height(30);
    table.row();
    table.add(exitButton).width(100).height(30);

    MyGame.getInstance().addActor(table);
  }

  private void createButtons() {
    newGameButton = createButton(MyGame.getInstance().getTextButtonStyle(), "New Game", new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        MyGame.getInstance().setScreen(new MapSelectorMenu());
      }
    });

    upgradeButton = createButton(MyGame.getInstance().getTextButtonStyle(), "Upgrade", new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        MyGame.getInstance().setScreen(new UpgradeMenu());
      }
    });

    optionsButton = createButton(MyGame.getInstance().getTextButtonStyle(), "Options", new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        MyGame.getInstance().setScreen(new SettingsMenu(false));
      }
    });

    exitButton = createButton(MyGame.getInstance().getTextButtonStyle(), "Exit", new ClickListener() {
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

    MyGame.getInstance().drawStage();
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
