package ru.meat.game.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.esotericsoftware.spine.utils.TwoColorPolygonBatch;
import lombok.Data;

/**
 * Сцена с онимацией спайна
 */
@Data
public class SecondStage extends Stage {

  private final Group enemiesGroup = new Group();
  private final Group playerGroup = new Group();
  private final Group bleedsGroup = new Group();
  private final Group explosionsGroup = new Group();
  private final Group highBulletsGroup = new Group();


  public SecondStage(OrthographicCamera camera) {
    super(new ScreenViewport(camera), new TwoColorPolygonBatch());
    getBatch().enableBlending();
    this.addActor(enemiesGroup);
    this.addActor(playerGroup);
    this.addActor(highBulletsGroup);
    this.addActor(bleedsGroup);
    this.addActor(explosionsGroup);
  }


  public void addEnemy(Actor actor) {
    enemiesGroup.addActor(actor);
  }

  public void addPlayer(Actor actor) {
    playerGroup.addActor(actor);
  }

  public void addBullet(Actor actor) {
    highBulletsGroup.addActor(actor);
  }

  public void addBleeding(Actor actor) {
    bleedsGroup.addActor(actor);
  }

  public void addExplosion(Actor actor) {
    explosionsGroup.addActor(actor);
  }

  public void changeGroups(){
    this.getActors().reverse();
  }
}
