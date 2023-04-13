package ru.meat.game.game;

import static ru.meat.game.settings.Constants.MAIN_ZOOM;
import static ru.meat.game.settings.Constants.WORLD_TO_VIEW;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import ru.meat.game.model.EnemyStatus;
import ru.meat.game.model.enemies.EnemyFactory;
import ru.meat.game.model.player.PlayerService;

public class DeathMatch extends GameZone {

  public DeathMatch(int map) {
    super(map);
  }

  @Override
  protected void renderSpec(float delta) {
    createMoreEnemies();

  }

  private void createMoreEnemies() {
    if (enemyService.getEnemies().stream().filter(x -> x.getStatus() != EnemyStatus.DIED).count() < 30) {
      // Инициализация начальной позиции
      float xBound1 = PlayerService.getInstance().getBodyPosX() * WORLD_TO_VIEW;
      float xBound2 = PlayerService.getInstance().getBodyPosX() * WORLD_TO_VIEW;
      float yBound1 = PlayerService.getInstance().getBodyPosY() * WORLD_TO_VIEW;
      float yBound2 = PlayerService.getInstance().getBodyPosY() * WORLD_TO_VIEW;
      float deltaByY = Gdx.graphics.getHeight() * MAIN_ZOOM;
      float deltaByX = Gdx.graphics.getWidth() * MAIN_ZOOM;

      //рандомизация позиции появления врагов
      int random = MathUtils.random(1, 4);
      if (random == 1) {
        xBound1 = xBound1 - deltaByX / 2 - 100;
        xBound2 = xBound2 - deltaByX / 2 - 80;
        yBound1 = yBound1 - deltaByY;
        yBound2 = yBound2 + deltaByY;
      } else if (random == 2) {
        xBound1 = xBound1 + deltaByX / 2 + 80;
        xBound2 = xBound2 + deltaByX / 2 + 100;
        yBound1 = yBound1 - deltaByY;
        yBound2 = yBound2 + deltaByY;
      } else if (random == 3) {
        xBound1 = xBound1 - deltaByX;
        xBound2 = xBound2 + deltaByX;
        yBound1 = yBound1 + deltaByY / 2 + 80;
        yBound2 = yBound2 + deltaByY / 2 + 100;
      } else if (random == 4) {
        xBound1 = xBound1 - deltaByX;
        xBound2 = xBound2 + deltaByX;
        yBound1 = yBound1 - deltaByY / 2 - 100;
        yBound2 = yBound2 - deltaByY / 2 - 80;
      }
//      Создание врага
      int random1 = MathUtils.random(0, 100);

//      enemyService.getEnemies().add(
//            EnemyFactory.createScorpionBoss(
//                500,
//               500));

      if (random1 > 30) {
        enemyService.addEnemy(
            EnemyFactory.createLittleBug(
                MathUtils.random(xBound1, xBound2),
                MathUtils.random(yBound1, yBound2)));
      } else  if (random1 > 15 && random1 <30){
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
