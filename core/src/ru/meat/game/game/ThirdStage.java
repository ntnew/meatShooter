package ru.meat.game.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class ThirdStage extends Stage {

  private final Group bulletsGroup = new Group();
  private final Group bleedsGroup = new Group();
  private final Group explosionsGroup = new Group();

  private final Group guiGroup = new Group();

  public ThirdStage(OrthographicCamera camera) {
    super(new ScreenViewport(camera));

    this.addActor(bulletsGroup);
    this.addActor(bleedsGroup);
    this.addActor(explosionsGroup);
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

  public void addExplosion(Actor actor) {
    synchronized (this) {
      explosionsGroup.addActor(actor);
    }
  }
}
