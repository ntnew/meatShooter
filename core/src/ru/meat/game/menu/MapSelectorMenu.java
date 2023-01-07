package ru.meat.game.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import ru.meat.game.MeatShooterClass;
import ru.meat.game.MyGame;
import ru.meat.game.loader.LoaderManager;
import ru.meat.game.model.enemies.EnemiesAnimation;
import ru.meat.game.model.maps.Maps;
import ru.meat.game.service.AudioService;

public class MapSelectorMenu implements Screen {

  private final Stage stage;
  private final MyGame game;

  private OrthographicCamera camera;

  private Button firstMapButton;

  private Button backButton;

  private boolean loading = false;

  private boolean startedLoad = false;

  private int selectedMap = 0;

  public MapSelectorMenu(final MyGame game) {
    this.game = game;
    initCam();
    stage = new Stage(new ScreenViewport());
    Gdx.input.setInputProcessor(stage);

    TextButtonStyle textButtonStyle = new TextButtonStyle();
    textButtonStyle.font = this.game.font;
    textButtonStyle.fontColor = Color.WHITE;
    createDeathMatchButton(textButtonStyle);
    createBackButton(textButtonStyle);

  }

  private void createBackButton(TextButtonStyle textButtonStyle) {
    backButton = new TextButton("Back", textButtonStyle);
    backButton.setSize(200, 50);
    backButton.setPosition(Gdx.graphics.getWidth() / 3, Gdx.graphics.getHeight() / 2-50);
    backButton.addListener(new ChangeListener() {
      @Override
      public void changed(ChangeEvent event, Actor actor) {
        game.setScreen(new MainMenu(game));
      }
    });
    stage.addActor(backButton);
  }

  private void initCam() {
    camera = new OrthographicCamera();
    camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
  }

  @Override
  public void show() {

  }

  @Override
  public void render(float delta) {
    ScreenUtils.clear(0, 0, 0, 1);
    if (selectedMap != 0 && loading && !startedLoad) {
      startedLoad = true;
      LoaderManager.getInstance().load(Maps.getNameByPos(selectedMap).getName(), Texture.class);
      LoaderManager.getInstance().load("glockShoot.mp3", Sound.class);
      LoaderManager.getInstance().load("ak47.mp3", Sound.class);
      EnemiesAnimation.getInstance();

    }
    if (loading && startedLoad && LoaderManager.getInstance().update() && !EnemiesAnimation.getInstance().isLoading()) {
      game.setScreen(new MeatShooterClass(selectedMap));
      AudioService.getInstance().smoothStopMusic();
    }

    camera.update();
    game.batch.setProjectionMatrix(camera.combined);

    game.batch.begin();
    if (!loading) {
      firstMapButton.draw(game.batch, 1);
      backButton.draw(game.batch,1);
    }

    game.batch.end();
  }

  private void createDeathMatchButton(TextButtonStyle textButtonStyle) {
    firstMapButton = new TextButton("Death match", textButtonStyle);
    firstMapButton.setSize(200, 50);
    firstMapButton.setPosition(Gdx.graphics.getWidth() / 3, Gdx.graphics.getHeight() / 2);
    firstMapButton.addListener(new ChangeListener() {
      @Override
      public void changed(ChangeEvent event, Actor actor) {
        selectedMap = 1;
        loading = true;
      }
    });
    stage.addActor(firstMapButton);
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
