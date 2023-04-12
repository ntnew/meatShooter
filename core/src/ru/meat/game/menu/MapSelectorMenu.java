package ru.meat.game.menu;

import static ru.meat.game.settings.Constants.DEBUG;
import static ru.meat.game.settings.Constants.TEXTURE_PARAMETERS;
import static ru.meat.game.utils.GDXUtils.createButton;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import ru.meat.game.MyGame;
import ru.meat.game.game.DeathMatch;
import ru.meat.game.loader.LoaderManager;
import ru.meat.game.model.enemies.EnemiesAnimation;
import ru.meat.game.model.maps.Maps;
import ru.meat.game.model.weapons.explosions.ExplosionsService;
import ru.meat.game.service.AudioService;
import ru.meat.game.service.BulletService;

public class MapSelectorMenu implements Screen {

  private Button firstMapButton;

  private Button backButton;
  private int selectedMap = 0;

  private final Table table;

  public MapSelectorMenu() {

    MyGame.getInstance().initStage();
    createButtons();

    table = new Table();
    table.setSize(300, 300);
    table.setPosition(Gdx.graphics.getWidth() / 3f, Gdx.graphics.getHeight() / 3f);
    table.setDebug(DEBUG);
    table.add(firstMapButton).width(100).height(40);
    table.row();
    table.add(backButton).width(100).height(40);
    MyGame.getInstance().addActor(table);
  }


  @Override
  public void show() {

    LoaderManager.getInstance().load(Maps.getNameByPos(selectedMap).getName(), Texture.class, TEXTURE_PARAMETERS);
    LoaderManager.getInstance().load("Bullet1.png", Texture.class, TEXTURE_PARAMETERS);
    LoaderManager.getInstance().load("GBullet.png", Texture.class, TEXTURE_PARAMETERS);
    ExplosionsService.initResources();
    BulletService.initResources();
    EnemiesAnimation.loadResources();
  }

  @Override
  public void render(float delta) {
    ScreenUtils.clear(0, 0, 0, 1);

    if (LoaderManager.getInstance().update() && selectedMap != 0) {
      MyGame.getInstance().setScreen(new DeathMatch(selectedMap));
      AudioService.getInstance().initSteps();
      AudioService.getInstance().smoothStopMusic();
    }

    MyGame.getInstance().drawStage();
  }

  private void createButtons() {
    firstMapButton = createButton(MyGame.getInstance().getTextButtonStyle(), "Death match", new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        selectedMap = 1;
        firstMapButton.setDisabled(true);
        backButton.setDisabled(true);
      }
    });
    backButton = createButton(MyGame.getInstance().getTextButtonStyle(), "Back", new ChangeListener() {
      @Override
      public void changed(ChangeEvent event, Actor actor) {
        MyGame.getInstance().setScreen(new MainMenu());
      }
    });
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
