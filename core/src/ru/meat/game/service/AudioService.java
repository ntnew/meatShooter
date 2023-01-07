package ru.meat.game.service;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.async.AsyncExecutor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.meat.game.loader.LoaderManager;
import ru.meat.game.settings.Settings;

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

  private Music currentMusic;

  public void playShoot(String shootSound) {
    Sound sound = LoaderManager.getInstance().get(shootSound);
    sound.play(Settings.getInstance().EFFECT_VOLUME);
  }


  public void playMainMenuMusic() {
    Music music = Gdx.audio.newMusic(Gdx.files.internal("sound/main_menu.mp3"));
    music.setOnCompletionListener(Music::dispose);
    music.setVolume(Settings.getInstance().MUSIC_VOLUME);
    music.play();
    this.currentMusic = music;
  }

  /**
   * плавно выключить музыку
   */
  public void smoothStopMusic() {
    if (currentMusic != null) {
      new AsyncExecutor(4, "turn-off-music").submit(() -> {
        float volume = currentMusic.getVolume();
        long timeCount = 0;
        while (volume > 0.01) {
          volume = currentMusic.getVolume();
          if (TimeUtils.timeSinceMillis(timeCount) > 300) {
            timeCount = TimeUtils.millis();
            currentMusic.setVolume(volume - 0.01f);
            volume = volume - 0.01f;
          }
        }
        currentMusic.stop();
        currentMusic = null;
        return true;
      });
    }
  }

  public void playTestShoot(float volume) {
    Sound sound = Gdx.audio.newSound(Gdx.files.internal("glockShoot.mp3"));
    sound.play(volume);
  }
}
