package ru.meat.game.game;

import static ru.meat.game.settings.Constants.MAIN_ZOOM;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import lombok.Data;
import ru.meat.game.MyGame;
import ru.meat.game.service.MapService;

@Data
public class MainStage extends Stage {

  private volatile Group mapGroup = new Group();

  private final Group bloodGroup = new Group();

  private final Group bulletsGroup = new Group();


  public MainStage(OrthographicCamera camera) {
    super(new ScreenViewport(camera));

    this.addActor(mapGroup);
    this.addActor(bloodGroup);
    this.addActor(bulletsGroup);
  }

  @Override
  public void act() {
    isMapOverlapScreen();

    isBloodSpotIsCulled();
    super.act();
  }

  private void isBloodSpotIsCulled() {
    bloodGroup.getChildren().forEach(b -> {
      Rectangle actorRect = new Rectangle();
      Rectangle camRect = new Rectangle();
      boolean visible;
      float stageX = b.getX();
      float stageY = b.getY();

      OrthographicCamera camera = MyGame.getInstance().getGameZone().getCamera();
      actorRect.set(stageX, stageY, getWidth(), getHeight());
      camRect.set(camera.position.x - camera.viewportWidth / 2.0f * MAIN_ZOOM,
          camera.position.y - camera.viewportHeight / 2.0f * MAIN_ZOOM,
          camera.viewportWidth * MAIN_ZOOM, camera.viewportHeight * MAIN_ZOOM);
      visible = (camRect.overlaps(actorRect));
      if (!visible) {
        b.remove();
      }
    });
  }

  private void isMapOverlapScreen() {
    Rectangle camRect = getCameraRectangle();

    mapGroup.getChildren().forEach(m -> {
      Rectangle actorRect = new Rectangle();
      boolean visible;
      float stageX = m.getX();
      float stageY = m.getY();

      actorRect.set(stageX, stageY, m.getWidth(), m.getHeight());

      visible = (camRect.overlaps(actorRect));
      if (!visible) {
        m.remove();
      }
    });

    if (!mapGroup.getChildren().isEmpty()) {
      Actor actor1 = mapGroup.getChildren().get(0);
      Rectangle rightRect = new Rectangle(actor1.getX(), actor1.getY(), actor1.getWidth(), actor1.getHeight());
      Rectangle topRect = new Rectangle(actor1.getX(), actor1.getY(), actor1.getWidth(), actor1.getHeight());
      Rectangle leftRect = new Rectangle(actor1.getX(), actor1.getY(), actor1.getWidth(), actor1.getHeight());
      Rectangle downRect = new Rectangle(actor1.getX(), actor1.getY(), actor1.getWidth(), actor1.getHeight());

      for (int i = 0; i < mapGroup.getChildren().size; i++) {
        Actor actor = mapGroup.getChildren().get(i);
        float stageX = actor.getX();
        float stageY = actor.getY();
        if (stageX + actor.getWidth() > rightRect.getX() + actor.getWidth()) {
          rightRect.set(stageX, stageY, actor.getWidth(), actor.getHeight());
        }
        if (stageY + actor.getHeight() > topRect.getY() + actor.getHeight()) {
          topRect.set(stageX, stageY, actor.getWidth(), actor.getHeight());
        }
        if (stageX < leftRect.getX()) {
          leftRect.set(stageX, stageY, actor.getWidth(), actor.getHeight());
        }
        if (stageY < downRect.getY()) {
          downRect.set(stageX, stageY, actor.getWidth(), actor.getHeight());
        }
      }
      if (rightRect.x + rightRect.getWidth() < camRect.getX() + camRect.getWidth()) {
        mapGroup.addActor(MapService.initMap(rightRect.getX() + rightRect.getWidth(), rightRect.getY()));
        mapGroup.addActor(
            MapService.initMap(rightRect.getX() + rightRect.getWidth(), rightRect.getY() + rightRect.getHeight()));
        mapGroup.addActor(
            MapService.initMap(rightRect.getX() + rightRect.getWidth(), rightRect.getY() - rightRect.getHeight()));
      }

      if (topRect.y + topRect.getHeight() < camRect.getY() + camRect.getHeight()) {
        mapGroup.addActor(MapService.initMap(topRect.getX(), topRect.getY() + topRect.getHeight()));
        mapGroup.addActor(
            MapService.initMap(topRect.getX() + topRect.getWidth(), topRect.getY() + topRect.getHeight()));
        mapGroup.addActor(
            MapService.initMap(topRect.getX() - topRect.getWidth(), topRect.getY() + topRect.getHeight()));
      }

      if (leftRect.x > camRect.getX()) {
        mapGroup.addActor(MapService.initMap(leftRect.getX() - leftRect.getWidth(), leftRect.getY()));
        mapGroup.addActor(
            MapService.initMap(leftRect.getX() - leftRect.getWidth(), leftRect.getY() + leftRect.getHeight()));
        mapGroup.addActor(
            MapService.initMap(leftRect.getX() - leftRect.getWidth(), leftRect.getY() - leftRect.getHeight()));
      }

      if (downRect.y > camRect.getY()) {
        mapGroup.addActor(MapService.initMap(downRect.getX(), downRect.getY() - downRect.getHeight()));
        mapGroup.addActor(
            MapService.initMap(downRect.getX() + downRect.getWidth(), downRect.getY() - downRect.getHeight()));
        mapGroup.addActor(
            MapService.initMap(downRect.getX() - downRect.getWidth(), downRect.getY() - downRect.getHeight()));
      }
    }
  }

  private Rectangle getCameraRectangle() {
    OrthographicCamera camera = MyGame.getInstance().getGameZone().getCamera();
    Rectangle camRect = new Rectangle();
    camRect.set(camera.position.x - camera.viewportWidth / 2.0f * MAIN_ZOOM,
        camera.position.y - camera.viewportHeight / 2.0f * MAIN_ZOOM,
        camera.viewportWidth * MAIN_ZOOM, camera.viewportHeight * MAIN_ZOOM);
    return camRect;
  }

  @Override
  public void draw() {
    super.draw();
  }

  public void addMap(Actor actor) {
    mapGroup.addActor(actor);
  }

  public void addBlood(Actor actor) {
    bloodGroup.addActor(actor);
  }

  public void addBullet(Actor actor) {
    bulletsGroup.addActor(actor);
  }
}
