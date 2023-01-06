package ru.meat.game.service;

import static ru.meat.game.utils.Settings.VOLUME;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.async.AsyncExecutor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.meat.game.loader.LoaderManager;
import ru.meat.game.model.enemies.EnemiesAnimation;

@Data
@NoArgsConstructor
public class AudioService {

  private static AudioService instance;

  public static AudioService getInstance() {
    if (instance == null) {
      instance = new AudioService();
    }
    return instance;
  }

  public void playShoot(String shootSound) {
    Sound sound = LoaderManager.getInstance().get(shootSound);
    sound.play(VOLUME);
  }


  public static Music playMainMenuMusic() {
    Music music = Gdx.audio.newMusic(Gdx.files.internal("sound/main_menu.mp3"));
    music.setOnCompletionListener(Music::dispose);
    music.setVolume(VOLUME);
    music.play();
    return music;
  }

  /**
   * плавно выключить музыку
   *
   * @param music
   */
  public static void smoothStopMusic(Music music) {
    new AsyncExecutor(4, "turn-off-music").submit(() -> {
      float volume = music.getVolume();
      long timeCount = 0;
      while (volume > 0.01) {
        volume = music.getVolume();
        if (TimeUtils.timeSinceMillis(timeCount) > 300) {
          timeCount = TimeUtils.millis();
          music.setVolume(volume - 0.01f);
          volume = volume - 0.01f;
        }
      }
      music.stop();
      return true;
    });
  }
}
