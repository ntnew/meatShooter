package ru.meat.game.model.enemies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.utils.async.AsyncExecutor;
import lombok.Data;
import ru.meat.game.utils.FilesUtils;

@Data
public class EnemiesAnimation {

  private Animation<Texture> walkAnimation;
  private Animation<Texture> idleAnimation;
  private Animation<Texture> attackAnimation;
  private Animation<Texture> dieAnimation;

  private static EnemiesAnimation instance;

  public static EnemiesAnimation getInstance() {
    if (instance == null) {
      instance = new EnemiesAnimation();
    }
    return instance;
  }

  public EnemiesAnimation() {
    this.attackAnimation = doAsync("./assets/export/attack/", 1, 0.1f);
    this.walkAnimation = doAsync("./assets/export/move/", 1, 0.05f);
    this.idleAnimation = doAsync("./assets/export/idle/", 1, 0.05f);
    this.dieAnimation = doAsync("./assets/export/died", 1, 0.05f);

    this.idleAnimation.setPlayMode(PlayMode.LOOP);
    this.walkAnimation.setPlayMode(PlayMode.LOOP);
    this.attackAnimation.setPlayMode(PlayMode.NORMAL);
  }

  private static Animation<Texture> doAsync(String path, float zoom, float frameDuration) {
    return FilesUtils.initAnimationFrames(path, zoom, frameDuration);
  }
}
