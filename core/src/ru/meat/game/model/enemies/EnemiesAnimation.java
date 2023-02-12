package ru.meat.game.model.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.async.AsyncExecutor;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.Skeleton;
import com.esotericsoftware.spine.SkeletonData;
import com.esotericsoftware.spine.SkeletonJson;
import com.esotericsoftware.spine.SkeletonRenderer;
import com.esotericsoftware.spine.SkeletonRendererDebug;
import lombok.Data;
import ru.meat.game.utils.FilesUtils;

@Data
public class EnemiesAnimation {

  private SpriteBatch batch;

  private TextureAtlas atlas;

  private SkeletonData skeletonData;

  private boolean loading = false;

  private static EnemiesAnimation instance;

  public static EnemiesAnimation getInstance() {
    if (instance == null) {
      instance = new EnemiesAnimation();
    }
    return instance;
  }

  public EnemiesAnimation() {
    loading = true;

    atlas = new TextureAtlas(Gdx.files.internal("./assets/ani/littleBug/bug.atlas"));

    SkeletonJson json = new SkeletonJson(atlas); // This loads skeleton JSON data, which is stateless.
    json.setScale(0.3f);
    skeletonData = json.readSkeletonData(Gdx.files.internal("./assets/ani/littleBug/bug.json"));


    this.loading = false;
  }
}
