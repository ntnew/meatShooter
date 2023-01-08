package ru.meat.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import lombok.Data;
import ru.meat.game.menu.MainMenu;
import ru.meat.game.service.AudioService;
import ru.meat.game.settings.Settings;

@Data
public class MyGame extends Game {

  private SpriteBatch batch;
  private BitmapFont font;
  private LabelStyle labelStyle;
  private TextButtonStyle textButtonStyle;

  public void create() {
    Gdx.graphics.setWindowedMode(Settings.getInstance().SCREEN_WIDTH, Settings.getInstance().SCREEN_HEIGHT);

//   Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
// fullscreen
    batch = new SpriteBatch();
    font = new BitmapFont();

    textButtonStyle = new TextButtonStyle();
    textButtonStyle.font = font;
    textButtonStyle.fontColor = Color.WHITE;

    labelStyle = new LabelStyle();
    labelStyle.font = font;
    labelStyle.fontColor = Color.WHITE;

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