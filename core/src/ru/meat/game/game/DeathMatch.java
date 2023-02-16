package ru.meat.game.game;

import static ru.meat.game.settings.Constants.MAIN_ZOOM;
import static ru.meat.game.settings.Constants.WORLD_TO_VIEW;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.SkeletonRenderer;
import com.esotericsoftware.spine.SkeletonRendererDebug;
import ru.meat.game.MyGame;
import ru.meat.game.model.EnemyStatus;
import ru.meat.game.model.enemies.EnemyFactory;
import ru.meat.game.service.BloodService;
import ru.meat.game.service.BulletService;
import ru.meat.game.service.EnemyService;

public class DeathMatch extends GameZone {

  public DeathMatch(int map, MyGame game) {
    super(game, map);

    enemyService.getEnemies().add(EnemyFactory.createLittleBug(100, 100));
  }

  @Override
  protected void renderSpec(float delta) {
    createMoreEnemies();

    enemyService.actionEnemies(playerService.getBodyPosX(), playerService.getBodyPosY());

    //рисовать текстуры
    spriteBatch.setProjectionMatrix(camera.combined);
    polyBatch.setProjectionMatrix(camera.combined);

    polyBatch.begin();
    BloodService.getInstance().drawBloodSpots(camera);
    BulletService.getInstance().drawBullets(camera);

    spriteBatch.begin();

    playerService.drawPlayer(spriteBatch);
    enemyService.drawEnemies(polyBatch, renderer);
    spriteBatch.end();
    polyBatch.end();
  }

  private void createMoreEnemies() {
    if (enemyService.getEnemies().stream().filter(x -> x.getStatus() != EnemyStatus.DIED).count() < 30) {
      // Инициализация начальной позиции
      float xBound1 = playerService.getBodyPosX() * WORLD_TO_VIEW;
      float xBound2 = playerService.getBodyPosX() * WORLD_TO_VIEW;
      float yBound1 = playerService.getBodyPosY() * WORLD_TO_VIEW;
      float yBound2 = playerService.getBodyPosY() * WORLD_TO_VIEW;
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
      enemyService.getEnemies().add(
          EnemyFactory.createLittleBug(
              MathUtils.random(xBound1, xBound2),
              MathUtils.random(yBound1, yBound2)));
    }
  }
}
