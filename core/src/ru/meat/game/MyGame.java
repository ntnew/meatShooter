package ru.meat.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import ru.meat.game.menu.MainMenu;
import ru.meat.game.service.AudioService;
import ru.meat.game.settings.Settings;

public class MyGame extends Game {

  public SpriteBatch batch;
  public BitmapFont font;

  public void create() {
   Gdx.graphics.setWindowedMode(Settings.getInstance().SCREEN_WIDTH, Settings.getInstance().SCREEN_HEIGHT);

//   Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
// fullscreen
    batch = new SpriteBatch();
    font = new BitmapFont();
    AudioService.getInstance().playMainMenuMusic();

    this.setScreen(new MainMenu(this));
  }

  public void render() {
    super.render(); // important!
  }

  public void dispose() {
    batch.dispose();
    font.dispose();
  }

}