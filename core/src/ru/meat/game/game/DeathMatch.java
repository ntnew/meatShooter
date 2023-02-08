package ru.meat.game.game;

import static ru.meat.game.settings.Constants.MAIN_ZOOM;
import static ru.meat.game.settings.Constants.WORLD_TO_VIEW;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Cursor.SystemCursor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import ru.meat.game.Box2dWorld;
import ru.meat.game.MyGame;
import ru.meat.game.gui.GUI;
import ru.meat.game.menu.MainMenu;
import ru.meat.game.menu.PauseMenu;
import ru.meat.game.model.EnemyStatus;
import ru.meat.game.service.AudioService;
import ru.meat.game.service.BloodService;
import ru.meat.game.service.BulletService;
import ru.meat.game.service.EnemyService;
import ru.meat.game.service.MapService;
import ru.meat.game.service.PlayerService;
import ru.meat.game.service.RpgStatsService;

public class DeathMatch extends GameZone {

  private float stateTime;
  private SpriteBatch spriteBatch;




  public DeathMatch(int map, MyGame game) {
    super(game);
    enemyService = new EnemyService();

    this.mapService = new MapService();
    mapService.initMap(map);
    spriteBatch = new SpriteBatch();


    enemyService.createEnemies();

  }

  @Override
  protected void renderSpec(float delta) {
    createMoreEnemies();

    enemyService.actionEnemies(playerService.getBodyPosX(), playerService.getBodyPosY());


    //рисовать текстуры
    spriteBatch.setProjectionMatrix(camera.combined);

    BloodService.getInstance().drawBloodSpots(camera);

    BulletService.getInstance().drawBullets(camera);

    spriteBatch.begin();
    playerService.drawPlayer(spriteBatch);
    stateTime += Gdx.graphics.getDeltaTime();
    enemyService.drawEnemies(spriteBatch, stateTime);
    spriteBatch.end();


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
      //Создание врага
      enemyService.getEnemies().add(
          enemyService.createZombieEnemy(
              MathUtils.random(xBound1, xBound2),
              MathUtils.random(yBound1, yBound2)));
    }
  }
}
