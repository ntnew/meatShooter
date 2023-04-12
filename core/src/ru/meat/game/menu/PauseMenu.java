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
import ru.meat.game.game.GameZone;

public class PauseMenu implements Screen {

  private Button toMenuButton;

  private Button settingsButton;
  private Button resumeButton;

  private Table table;

  public PauseMenu(GameZone currentGameScreen) {
    MyGame.getInstance().setGameZone(currentGameScreen);

    MyGame.getInstance().initStage();
    createButtons();

    MyGame.getInstance().initStage();

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

    MyGame.getInstance().addActor(table);
  }


  @Override
  public void show() {
    createButtons();
  }

  private void createButtons() {

    toMenuButton = createButton(MyGame.getInstance().getTextButtonStyle(), "Surrender", new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        super.clicked(event, x, y);
        MyGame.getInstance().getGameZone().endGameSession();
      }
    });

    settingsButton = createButton(MyGame.getInstance().getTextButtonStyle(), "Settings", new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        super.clicked(event, x, y);
        MyGame.getInstance().setScreen(new SettingsMenu(true));
      }
    });

    resumeButton = createButton(MyGame.getInstance().getTextButtonStyle(), "Resume", new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        super.clicked(event, x, y);
        MyGame.getInstance().setScreen(MyGame.getInstance().getGameZone());
        MyGame.getInstance().getGameZone().resumeGame();
        MyGame.getInstance().getStage().clear();
      }
    });
  }

  @Override
  public void render(float delta) {
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
