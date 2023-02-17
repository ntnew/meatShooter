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

  private SkeletonData littleBugSkeletonData;

  private SkeletonData spiderSkeletonData;

  private static EnemiesAnimation instance;

  public static EnemiesAnimation getInstance() {
    if (instance == null) {
      instance = new EnemiesAnimation();
    }
    return instance;
  }

  public EnemiesAnimation() {

    SkeletonJson bugJson = new SkeletonJson((TextureAtlas) LoaderManager.getInstance().get("ani/littleBug/bug.atlas"));
    bugJson.setScale(1f/MAIN_ZOOM);
    littleBugSkeletonData = bugJson.readSkeletonData(Gdx.files.internal("ani/littleBug/bug.json"));

    SkeletonJson spiderJson = new SkeletonJson((TextureAtlas) LoaderManager.getInstance().get("ani/spider/spider.atlas"));
    spiderJson.setScale(2f/MAIN_ZOOM);
    spiderSkeletonData = spiderJson.readSkeletonData(Gdx.files.internal("ani/spider/spider.json"));
  }
}
