package ru.meat.game.loader;

import com.badlogic.gdx.assets.AssetManager;

public class LoaderManager extends AssetManager {

  private static LoaderManager instance;

  public static LoaderManager getInstance() {
    if (instance == null) {
      instance = new LoaderManager();
    }
    return instance;
  }

  @Override
  public synchronized void dispose() {
    super.dispose();
    instance = null;
  }
}
