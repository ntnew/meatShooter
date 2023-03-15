package ru.meat.game.model.player;

import static ru.meat.game.settings.Constants.MAIN_ZOOM;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.esotericsoftware.spine.SkeletonData;
import com.esotericsoftware.spine.SkeletonJson;
import lombok.Data;
import ru.meat.game.loader.LoaderManager;

@Data
public class PlayerAnimationFactory {

  private static final String pathTopAtlas = "char/top/survivor.atlas";

  private static final String pathTopPng = "char/top/survivor.png";

  private static final String pathTopJson = "char/top/survivor.json";

  private static final String pathFeetAtlas = "char/feet/survivor.atlas";

  private static final String pathFeetPng = "char/feet/survivor.png";

  private static final String pathFeetJson = "char/feet/survivor.json";


  private SkeletonData playerTopSkeletonData;

  private SkeletonData playerFeetSkeletonData;


  private static PlayerAnimationFactory instance;

  public static PlayerAnimationFactory getInstance() {
    if (instance == null) {
      instance = new PlayerAnimationFactory();
    }
    return instance;
  }

  public static void loadAni() {
    LoaderManager.getInstance().load(pathTopAtlas, TextureAtlas.class);
    LoaderManager.getInstance().load(pathTopPng, Texture.class);

    LoaderManager.getInstance().load(pathFeetAtlas, TextureAtlas.class);
    LoaderManager.getInstance().load(pathFeetPng, Texture.class);
  }

  public PlayerAnimationFactory() {
    SkeletonJson top = new SkeletonJson((TextureAtlas) LoaderManager.getInstance().get(pathTopAtlas));
    top.setScale(1f);
    playerTopSkeletonData = top.readSkeletonData(Gdx.files.internal(pathTopJson));

    SkeletonJson bot = new SkeletonJson((TextureAtlas) LoaderManager.getInstance().get(pathFeetAtlas));
    bot.setScale(1f);
    playerFeetSkeletonData = bot.readSkeletonData(Gdx.files.internal(pathFeetJson));
  }
}
