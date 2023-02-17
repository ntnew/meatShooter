package ru.meat.game.service;

import static ru.meat.game.settings.Constants.EXPLODE_SOUND_MULTIPLY;
import static ru.meat.game.settings.Constants.PATH_WEAPON_SOUND;
import static ru.meat.game.settings.Constants.SHOOT_SOUND_MULTIPLY;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.async.AsyncExecutor;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
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

  private List<String> enemyBugDies = Arrays.asList(
      "sound/enemy/dieBug2.mp3",
      "sound/enemy/dieBug1.mp3",
      "sound/enemy/dieBug3.mp3",
      "sound/enemy/dieBug4.mp3",
      "sound/enemy/dieBug5.mp3",
      "sound/enemy/dieBug6.mp3",
      "sound/enemy/dieBug7.mp3");

  private List<String> weaponSounds = Arrays.asList(
      "glockShoot.mp3",
      "ak47.mp3", "ak47reload.mp3",
      "shotgun.mp3", "shotgunReload.mp3",
      "2barrelShot.mp3", "2barrelReload.mp3",
      "m249.mp3", "m249reload.mp3",
      "m79reload.mp3","m79open.mp3", "m79shoot.mp3",
      "m32open.mp3", "m32reload.mp3", "m32close.mp3",
      "aa12shoot.mp3", "aa12open.mp3", "aa12reload.mp3", "aa12close.mp3"
  );

  private List<String> explosionsSounds = Arrays.asList(
      "exp1.mp3",
      "exp2.mp3",
      "exp3.mp3"
  );


  private List<String> gameMusic = Arrays.asList("sound/track1.mp3", "sound/track2.mp3");
  private int currentMusicPos = 0;

  private boolean stepSoundLock = false;

  private boolean firstStepPlaying = false;

  private Music step1;
  private Music step2;

  public AudioService() {
    LoaderManager.getInstance().load("sound/player/step1.mp3", Music.class);
    LoaderManager.getInstance().load("sound/player/step2.mp3", Music.class);

    weaponSounds.forEach(x -> LoaderManager.getInstance().load(PATH_WEAPON_SOUND + x, Sound.class));
    explosionsSounds.forEach(x -> LoaderManager.getInstance().load(PATH_WEAPON_SOUND + x, Sound.class));

    LoaderManager.getInstance().load("sound/track1.mp3", Music.class);
    LoaderManager.getInstance().load("sound/track2.mp3", Music.class);
    LoaderManager.getInstance().load("sound/player/hit1.mp3", Sound.class);
    LoaderManager.getInstance().load("sound/select-click.mp3", Sound.class);
    enemyBugDies.forEach(x -> LoaderManager.getInstance().load(x, Sound.class));
  }

  private Music currentMusic;

  public void playClick() {
    playSound("sound/select-click.mp3");
  }

  public void playSound(String soundPath) {
    Sound sound = LoaderManager.getInstance().get(soundPath);
    sound.play(Settings.getInstance().EFFECT_VOLUME);
  }

  public void playReloadSound(String soundPath) {
    playSound(PATH_WEAPON_SOUND + soundPath);
  }

  public void playShootSound(String soundPath) {
    Sound sound = LoaderManager.getInstance().get(PATH_WEAPON_SOUND + soundPath);
    sound.play(Settings.getInstance().EFFECT_VOLUME * SHOOT_SOUND_MULTIPLY);
  }

  public void playExplosionSound() {
    int random = MathUtils.random(0, explosionsSounds.size() - 1);
    Sound sound = LoaderManager.getInstance().get(PATH_WEAPON_SOUND + explosionsSounds.get(random));
    sound.play(Settings.getInstance().EFFECT_VOLUME * EXPLODE_SOUND_MULTIPLY);
  }

  public void playEnemyDie() {
    int random = MathUtils.random(0, enemyBugDies.size() - 1);
    Sound sound = LoaderManager.getInstance().get(enemyBugDies.get(random));
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

  /**
   * Играть игровую музыку, если кончилась, включать следующую
   */
  public void playGameMusic() {
    if (currentMusic == null) {
      Music music = LoaderManager.getInstance().get(gameMusic.get(getRandomMusic()));
      music.setOnCompletionListener(music1 -> {
        music1.dispose();
        currentMusic = null;
      });
      music.setVolume(Settings.getInstance().MUSIC_VOLUME);
      music.play();
      this.currentMusic = music;
    }
  }

  private int getRandomMusic() {
    int random = MathUtils.random(0, gameMusic.size() - 1);
    if (random == currentMusicPos) {
      getRandomMusic();
    }
    currentMusicPos = random;
    return random;
  }

  public void playMainMenuMusic() {
    if (currentMusic == null) {
      Music music = Gdx.audio.newMusic(Gdx.files.internal("sound/main_menu.mp3"));
      music.setOnCompletionListener(music1 -> {
        music1.dispose();
        currentMusic = null;
      });
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
    Sound sound = LoaderManager.getInstance().get(PATH_WEAPON_SOUND + "glockShoot.mp3");
    sound.play(volume);
  }
}
