package ru.meat.game;

import static ru.meat.game.service.AudioService.playMainMenuMusic;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import ru.meat.game.menu.MainMenu;

public class MyGame extends Game {

  public SpriteBatch batch;
  public BitmapFont font;

  public Music music;

  public void create() {

//   Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
// fullscreen
    batch = new SpriteBatch();
    font = new BitmapFont();
    music = playMainMenuMusic();

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