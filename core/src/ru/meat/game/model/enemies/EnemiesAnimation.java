package ru.meat.game.model.enemies;

import static ru.meat.game.settings.Constants.MAIN_ZOOM;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
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

  private SkeletonData blackWidowSkeletonData;

  private SkeletonData scorpionSkeletonData;

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

    SkeletonJson blackWidowJson = new SkeletonJson((TextureAtlas) LoaderManager.getInstance().get("ani/blackWidow/spider_mini_boss.atlas"));
    blackWidowJson.setScale(3f/MAIN_ZOOM);
    blackWidowSkeletonData = blackWidowJson.readSkeletonData(Gdx.files.internal("ani/blackWidow/spider_mini_boss.json"));

    SkeletonJson scorpJson = new SkeletonJson((TextureAtlas) LoaderManager.getInstance().get("ani/scorpion/boss.atlas"));
    scorpJson.setScale(5f/MAIN_ZOOM);
    scorpionSkeletonData = scorpJson.readSkeletonData(Gdx.files.internal("ani/scorpion/boss.json"));
  }


  public static void loadResources() {
    LoaderManager.getInstance().load("ani/littleBug/bug.atlas", TextureAtlas.class);
    LoaderManager.getInstance().load("ani/littleBug/bug.png", Texture.class);

    LoaderManager.getInstance().load("ani/spider/spider.atlas", TextureAtlas.class);
    LoaderManager.getInstance().load("ani/spider/spider.png", Texture.class);

    LoaderManager.getInstance().load("ani/blackWidow/spider_mini_boss.atlas", TextureAtlas.class);
    LoaderManager.getInstance().load("ani/blackWidow/spider_mini_boss.png", Texture.class);

    LoaderManager.getInstance().load("ani/scorpion/boss.atlas", TextureAtlas.class);
    LoaderManager.getInstance().load("ani/scorpion/boss.png", Texture.class);
  }
}
