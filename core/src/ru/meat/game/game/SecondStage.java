package ru.meat.game.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import lombok.Data;

@Data
public class SecondStage extends Stage {
  private final Group enemiesGroup = new Group();
  private final Group playerGroup = new Group();

  public SecondStage(OrthographicCamera camera) {
    super(new ScreenViewport(camera), new PolygonSpriteBatch());

    this.addActor(enemiesGroup);
    this.addActor(playerGroup);
  }



  public void addEnemy(Actor actor) {
    synchronized (this) {
      enemiesGroup.addActor(actor);
    }
  }

  public void addPlayer(Actor actor) {
    synchronized (this) {
      playerGroup.addActor(actor);
    }
  }
}
