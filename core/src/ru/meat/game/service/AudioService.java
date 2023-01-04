package ru.meat.game.service;

import static ru.meat.game.utils.Settings.VOLUME;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.meat.game.utils.Settings;

@Data
@NoArgsConstructor
public class AudioService {

  public void playShoot(String shootSound) {
    Music music = Gdx.audio.newMusic(Gdx.files.internal(shootSound));
    music.setOnCompletionListener(Music::dispose);
    music.setVolume(VOLUME);
    music.play();
  }


  public static void playMainMenuMusic() {
    Music music = Gdx.audio.newMusic(Gdx.files.internal("sound/main_menu.mp3"));
    music.setOnCompletionListener(Music::dispose);
    music.setVolume(VOLUME);
    music.play();
  }
}
