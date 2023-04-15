package ru.meat.game.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class MainStage extends Stage {

  private final Group mapGroup = new Group();
  private final Group bloodGroup = new Group();

  private final Group bleedsGroup = new Group();

  private final Group bulletsGroup = new Group();

  public MainStage(OrthographicCamera camera) {
    this.setViewport(new ScreenViewport(camera));
    this.addActor(mapGroup);
    this.addActor(bloodGroup);
    this.addActor(bleedsGroup);
    this.addActor(bulletsGroup);
  }

  public void addMap(Actor actor) {
    synchronized (this) {
      mapGroup.addActor(actor);
    }
  }

  public void addBlood(Actor actor) {
    synchronized (this) {
      bloodGroup.addActor(actor);
    }
  }

  public void addBullet(Actor actor) {
    synchronized (this) {
      bulletsGroup.addActor(actor);
    }
  }

  public void addBleeding(Actor actor) {
    synchronized (this) {
      bleedsGroup.addActor(actor);
    }
  }
}
