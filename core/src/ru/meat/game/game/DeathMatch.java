package ru.meat.game.game;

import static ru.meat.game.settings.Constants.MAIN_ZOOM;
import static ru.meat.game.settings.Constants.WORLD_TO_VIEW;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.SnapshotArray;
import com.badlogic.gdx.utils.TimeUtils;
import java.util.ArrayList;
import ru.meat.game.MyGame;
import ru.meat.game.model.EnemyStatus;
import ru.meat.game.model.enemies.Enemy;
import ru.meat.game.model.enemies.EnemyFactory;
import ru.meat.game.model.player.PlayerService;

public class DeathMatch extends GameZone {

  public DeathMatch(int map) {
    super(map);
  }

  private long complexityTimestamp = TimeUtils.millis();
  private int enemyCount = 30;
  private final int enemyCountAddStep = 15;
  private final int increaseComplexityEvery = 60000;
  @Override
  protected void renderSpec(float delta) {
    createMoreEnemies();
  }

  private boolean isEmpty(Object[] el){
    return el== null || el.length == 0;
  }

  private void createMoreEnemies() {
    ArrayList<Actor> objects = new ArrayList<>();
    SnapshotArray<Actor> children = getSecondStage().getEnemiesGroup().getChildren();

    children.forEach(objects::add);
    // Добавлять врагов каждые 1 минуты
    if (TimeUtils.timeSinceMillis(complexityTimestamp) > increaseComplexityEvery) {
      complexityTimestamp = TimeUtils.millis();
      enemyCount += enemyCountAddStep;

    }

    if (objects.stream().filter(x -> ((Enemy) x).getStatus() != EnemyStatus.DIED).count() < enemyCount) {
      // Инициализация начальной позиции
      OrthographicCamera camera = MyGame.getInstance().getGameZone().getCamera();

      float xBound1 = PlayerService.getInstance().getBodyPosX() * WORLD_TO_VIEW;
      float xBound2 = PlayerService.getInstance().getBodyPosX() * WORLD_TO_VIEW;
      float yBound1 = PlayerService.getInstance().getBodyPosY() * WORLD_TO_VIEW;
      float yBound2 = PlayerService.getInstance().getBodyPosY() * WORLD_TO_VIEW;
      float deltaByY = camera.viewportHeight * MAIN_ZOOM;
      float deltaByX = camera.viewportWidth * MAIN_ZOOM;

      //рандомизация позиции появления врагов
      int random = MathUtils.random(1, 4);
      if (random == 1) {
        xBound1 = xBound1 - deltaByX - 100;
        xBound2 = xBound2 - deltaByX  - 80;
        yBound1 = yBound1 - deltaByY;
        yBound2 = yBound2 + deltaByY;
      } else if (random == 2) {
        xBound1 = xBound1 + deltaByX  + 80;
        xBound2 = xBound2 + deltaByX + 100;
        yBound1 = yBound1 - deltaByY;
        yBound2 = yBound2 + deltaByY;
      } else if (random == 3) {
        xBound1 = xBound1 - deltaByX;
        xBound2 = xBound2 + deltaByX;
        yBound1 = yBound1 + deltaByY  + 80;
        yBound2 = yBound2 + deltaByY  + 100;
      } else if (random == 4) {
        xBound1 = xBound1 - deltaByX;
        xBound2 = xBound2 + deltaByX;
        yBound1 = yBound1 - deltaByY - 100;
        yBound2 = yBound2 - deltaByY  - 80;
      }
//      Создание врага
      int random1 = MathUtils.random(0, 100);

      if (random1 > 30) {
        enemyService.addEnemy(
            EnemyFactory.createLittleBug(
                MathUtils.random(xBound1, xBound2),
                MathUtils.random(yBound1, yBound2)));
      } else if (random1 > 15 && random1 < 30) {
        enemyService.addEnemy(
            EnemyFactory.createSpider(
                MathUtils.random(xBound1, xBound2),
                MathUtils.random(yBound1, yBound2)));
      } else {
        enemyService.addEnemy(
            EnemyFactory.createBlackWidow(
                MathUtils.random(xBound1, xBound2),
                MathUtils.random(yBound1, yBound2)));
      }
    }
  }
}
