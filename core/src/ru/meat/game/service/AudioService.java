package ru.meat.game.service;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.async.AsyncExecutor;
import java.util.Arrays;
import java.util.List;
import lombok.Data;
import ru.meat.game.loader.LoaderManager;
import ru.meat.game.settings.Settings;

@Data
public class AudioService {

  private static AudioService instance;

  public static AudioService getInstance() {
    if (instance == null) {
      instance = new AudioService();
    }
    return instance;
  }

  private List<String> enemyDies = Arrays.asList("sound/enemy/die2.mp3", "sound/enemy/die1.mp3", "sound/enemy/die3.mp3",
      "sound/enemy/die4.mp3");


  private List<String> gameMusic = Arrays.asList("sound/track1.mp3", "sound/track2.mp3");

  private boolean stepSoundLock = false;

  private boolean firstStepPlaying = false;

  private Music step1;
  private Music step2;

  public AudioService() {
    LoaderManager.getInstance().load("sound/player/step1.mp3", Music.class);
    LoaderManager.getInstance().load("sound/player/step2.mp3", Music.class);
    LoaderManager.getInstance().load("glockShoot.mp3", Sound.class);
    LoaderManager.getInstance().load("ak47.mp3", Sound.class);
    LoaderManager.getInstance().load("sound/weapons/ak47reload.mp3", Sound.class);
    LoaderManager.getInstance().load("sound/track1.mp3", Music.class);
    LoaderManager.getInstance().load("sound/track2.mp3", Music.class);
    LoaderManager.getInstance().load("sound/player/hit1.mp3", Sound.class);
    enemyDies.forEach(x -> LoaderManager.getInstance().load(x, Sound.class));
  }

  private Music currentMusic;

  public void playSound(String shootSound) {
    Sound sound = LoaderManager.getInstance().get(shootSound);
    sound.play(Settings.getInstance().EFFECT_VOLUME);
  }

  public void playEnemyDie() {
    int random = MathUtils.random(0, enemyDies.size() - 1);
    Sound sound = LoaderManager.getInstance().get(enemyDies.get(random));
    sound.play(Settings.getInstance().EFFECT_VOLUME);
  }

  public void initSteps() {
    step1 = LoaderManager.getInstance().get("sound/player/step1.mp3");
    step2 = LoaderManager.getInstance().get("sound/player/step2.mp3");
  }

  public void playStep() {
    if (!firstStepPlaying && !stepSoundLock) {
      step1.setVolume(Settings.getInstance().EFFECT_VOLUME);
      step1.setOnCompletionListener(music -> stepSoundLock = false);
      step1.play();
      stepSoundLock = true;
      firstStepPlaying = true;
    }
    if (!stepSoundLock) {
      step2.setVolume(Settings.getInstance().EFFECT_VOLUME);
      step2.setOnCompletionListener(x -> stepSoundLock = false);
      step2.play();
      stepSoundLock = true;
      firstStepPlaying = false;
    }
  }

  public void playHit() {
    playSound("sound/player/hit1.mp3");
  }


  public void playGameMusic() {
    if (currentMusic == null) {
      int random = MathUtils.random(0, gameMusic.size() - 1);
      Music music = LoaderManager.getInstance().get(gameMusic.get(random));
      music.setOnCompletionListener(music1 -> {
        music1.dispose();
        currentMusic = null;
      });
      music.setVolume(Settings.getInstance().MUSIC_VOLUME);
      music.play();
      this.currentMusic = music;
    }
  }

  public void playMainMenuMusic() {
    if (currentMusic == null) {
      Music music = Gdx.audio.newMusic(Gdx.files.internal("sound/main_menu.mp3"));
      music.setOnCompletionListener(Music::dispose);
      music.setVolume(Settings.getInstance().MUSIC_VOLUME);
      music.play();
      this.currentMusic = music;
    }
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
          if (TimeUtils.timeSinceMillis(timeCount) > 100) {
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
