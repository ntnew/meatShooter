package ru.meat.game.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.esotericsoftware.spine.utils.TwoColorPolygonBatch;
import lombok.Data;

@Data
public class MainStage extends Stage {

  private final Group mapGroup = new Group();
  private final Group bloodGroup = new Group();

  public MainStage(OrthographicCamera camera) {
    super(new ScreenViewport(camera));
    this.addActor(mapGroup);
    this.addActor(bloodGroup);
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
}
