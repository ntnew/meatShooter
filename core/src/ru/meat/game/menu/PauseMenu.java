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
import ru.meat.game.MeatShooterClass;
import ru.meat.game.MyGame;

public class PauseMenu implements Screen {

  private final MyGame game;

  private Button toMenuButton;

  private Button settingsButton;
  private Button resumeButton;

  private Table table;

  public PauseMenu(final MyGame game, MeatShooterClass currentGameScreen) {
    this.game = game;
    this.game.setMeatShooterClass(currentGameScreen);

    game.initStage();
    createButtons();

    game.initStage();

    createButtons();

    table = new Table();
    table.setSize(300, 300);
    table.setPosition(Gdx.graphics.getWidth() / 3, Gdx.graphics.getHeight() / 3);
    table.setDebug(DEBUG);
    table.add(toMenuButton).width(100).height(30);
    table.row();
    table.add(settingsButton).width(100).height(30);
    table.row();
    table.add(resumeButton).width(100).height(30);
    table.row();

    game.getStage().addActor(table);
  }


  @Override
  public void show() {
    createButtons();
  }

  private void createButtons() {

    toMenuButton = createButton(game.getTextButtonStyle(), "Surrender", new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        super.clicked(event, x, y);
        game.getMeatShooterClass().endGameSession();
      }
    });

    settingsButton = createButton(game.getTextButtonStyle(), "Settings", new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        super.clicked(event, x, y);
        game.setScreen(new SettingsMenu(game, true));
      }
    });

    resumeButton = createButton(game.getTextButtonStyle(), "Resume", new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        super.clicked(event, x, y);
        game.setScreen(game.getMeatShooterClass());
        game.getMeatShooterClass().resumeGame();
      }
    });
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
