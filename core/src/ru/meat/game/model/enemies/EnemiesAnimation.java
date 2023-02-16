package ru.meat.game.model.enemies;

import static ru.meat.game.settings.Constants.MAIN_ZOOM;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.esotericsoftware.spine.SkeletonData;
import com.esotericsoftware.spine.SkeletonJson;
import lombok.Data;
import ru.meat.game.loader.LoaderManager;

@Data
public class EnemiesAnimation {

  private SpriteBatch batch;

  private TextureAtlas atlas;

  private SkeletonData littleBugSkeletonData;

  private static EnemiesAnimation instance;

  public static EnemiesAnimation getInstance() {
    if (instance == null) {
      instance = new EnemiesAnimation();
    }
    return instance;
  }

  public EnemiesAnimation() {
    atlas = LoaderManager.getInstance().get("ani/littleBug/bug.atlas");

    SkeletonJson json = new SkeletonJson(atlas); // This loads skeleton JSON data, which is stateless.
    json.setScale(1f/MAIN_ZOOM);
    littleBugSkeletonData = json.readSkeletonData(Gdx.files.internal("ani/littleBug/bug.json"));
  }
}
