package ru.meat.game.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.SnapshotArray;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import java.util.stream.Stream;
import lombok.Data;
import ru.meat.game.model.enemies.Enemy;

/**
 * Сцена с онимацией спайна
 */
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
    enemiesGroup.addActor(actor);
  }

  public void addPlayer(Actor actor) {
    playerGroup.addActor(actor);
  }

  public void changeGroups(){
    this.getActors().reverse();
  }
}
