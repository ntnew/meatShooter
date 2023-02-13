package ru.meat.game.menu;

import static ru.meat.game.settings.Constants.DEBUG;
import static ru.meat.game.utils.GDXUtils.createButton;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.loaders.TextureLoader.TextureParameter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import ru.meat.game.game.DeathMatch;
import ru.meat.game.MyGame;
import ru.meat.game.loader.LoaderManager;
import ru.meat.game.model.enemies.EnemiesAnimation;
import ru.meat.game.model.maps.Maps;
import ru.meat.game.model.weapons.explosions.Explosions;
import ru.meat.game.service.AudioService;
import ru.meat.game.service.BloodService;

public class MapSelectorMenu implements Screen {
  private final MyGame game;
  private Button firstMapButton;

  private Button backButton;

  private boolean loading = false;

  private boolean startedLoad = false;

  private int selectedMap = 0;

  private Table table;

  public MapSelectorMenu(final MyGame game) {
    this.game = game;
    game.initStage();
    createButtons();

    table = new Table();
    table.setSize(300, 300);
    table.setPosition(Gdx.graphics.getWidth() / 3, Gdx.graphics.getHeight() / 3);
    table.setDebug(DEBUG);
    table.add(firstMapButton).width(100).height(40);
    table.row();
    table.add(backButton).width(100).height(40);
    game.getStage().addActor(table);
  }


  @Override
  public void show() {

  }

  @Override
  public void render(float delta) {
    ScreenUtils.clear(0, 0, 0, 1);

    if (selectedMap != 0 && loading && !startedLoad) {
      startedLoad = true;
      firstMapButton.setDisabled(true);
      backButton.setDisabled(true);
      TextureParameter param = new TextureParameter();
      param.genMipMaps = true;
      LoaderManager.getInstance().load(Maps.getNameByPos(selectedMap).getName(), Texture.class, param);
      LoaderManager.getInstance().load("Bullet1.png", Texture.class, param);
      LoaderManager.getInstance().load("GBullet.png", Texture.class, param);
      LoaderManager.getInstance().load("ani/explosion.png", Texture.class, param);
      LoaderManager.getInstance().load("ani/littleBug/bug.atlas", TextureAtlas.class);
      LoaderManager.getInstance().load("ani/littleBug/bug.json", TextureAtlas.class);
      LoaderManager.getInstance().load("ani/littleBug/bug.png", Texture.class);
      BloodService.getInstance();
    }

    if (loading && startedLoad && LoaderManager.getInstance().update() && !EnemiesAnimation.getInstance().isLoading()) {
      game.setScreen(new DeathMatch(selectedMap, game));
      AudioService.getInstance().initSteps();
      AudioService.getInstance().smoothStopMusic();
    }

    game.drawStage();
  }

  private void createButtons() {
    firstMapButton = createButton(game.getTextButtonStyle(), "Death match", new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        selectedMap = 1;
        loading = true;
      }
    });
    backButton = createButton(game.getTextButtonStyle(), "Back", new ChangeListener() {
      @Override
      public void changed(ChangeEvent event, Actor actor) {
        game.setScreen(new MainMenu(game));
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
