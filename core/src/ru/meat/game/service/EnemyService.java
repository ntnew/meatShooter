package ru.meat.game.service;

import static ru.meat.game.utils.GDXUtils.calcGipotenuza;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.World;
import java.util.List;
import ru.meat.game.model.Enemy;
import ru.meat.game.model.EnemyStatus;
import ru.meat.game.model.PairOfFloat;
import ru.meat.game.utils.GDXUtils;

public class EnemyService {


  public Enemy createZombieEnemy(float x, float y, World world, String name) {
    Enemy enemy = new Enemy(x, y, 50, 4.5f, 100, 0.7f,
        "./assets/export/move/",
        "./assets/export/idle/",
        "./assets/export/attack/",
        null,
        0, 200, null);
    enemy.setBox(GDXUtils.createCircleForEnemy(world, 30, 100, name, x, y));
    enemy.setRadius(50);
    return enemy;
  }

  /**
   * скорректировать дистанцию между врагами
   *
   * @param enemies список врагов на карте
   */
  public void correctDistanceBetweenEnemies(List<Enemy> enemies) {
    for (int i = 0; i < enemies.size(); i++) {
      Enemy enemy = enemies.get(i);
      for (int i1 = 0; i1 < enemies.size(); i1++) {
        if (i != i1) {
          Enemy enemy2 = enemies.get(i1);
          float cat1 = enemy2.getPosX() - enemy.getPosX();
          float cat2 = enemy2.getPosY() - enemy.getPosY();
          float gip = GDXUtils.calcGipotenuza(cat1, cat2);

          if (gip < enemy.getRadius()) {
            enemy2.setInterference(true);
            enemy2.setPosX(cat1 < 0 ? enemy2.getPosX() - 0.5f : enemy2.getPosX() + 0.5f);
            enemy2.setPosY(cat2 < 0 ? enemy2.getPosY() - 0.5f : enemy2.getPosY() + 0.5f);
          } else {
            enemy2.setInterference(false);
          }
        }
      }
    }
  }

  public EnemyService() {
  }

  public void drawEnemySprite(SpriteBatch batch, Enemy enemy, float stateTime) {
    Sprite sprite = new Sprite(getActualFrame(stateTime, enemy));
    sprite.setX(enemy.getPosX());
    sprite.setY(enemy.getPosY());
    sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
    sprite.setRotation(enemy.getAnimationAngle());
    sprite.draw(batch);
    enemy.setCenter(new PairOfFloat(enemy.getPosX() + sprite.getWidth() / 2, enemy.getPosY() + sprite.getHeight() / 2));
  }

  private Texture getActualFrame(float stateTime, Enemy enemy) {
    if (enemy.getStatus().equals(EnemyStatus.IDLE)) {
      return enemy.getIdleAnimation().getKeyFrame(stateTime);
    } else if (enemy.getStatus().equals(EnemyStatus.MOVE)) {
      return enemy.getWalkAnimation().getKeyFrame(stateTime);
    } else if (enemy.getStatus().equals(EnemyStatus.ATTACK)) {
      return enemy.getAttackAnimation().getKeyFrame(stateTime, true);
    } else if (enemy.getStatus().equals(EnemyStatus.DIE)) {
      return enemy.getDieAnimation().getKeyFrame(stateTime, true);
    }
    return enemy.getIdleAnimation().getKeyFrame(stateTime);
  }

  /**
   * Метод действия врага, если расстояние до игрока меньше расстояния атаки, то атакует, else идёт
   *
   * @param x     x координата игрока
   * @param y     у координата игрока
   * @param enemy враг
   */
  public void doSomething(float x, float y, Enemy enemy) {

    //если расстояние меньше расстояния атаки, то атаковать
    if (calcGipotenuza(x, y, enemy.getPosX(), enemy.getPosY()) < enemy.getAttackRange() && !enemy.isInterference()) {
      enemy.setStatus(EnemyStatus.ATTACK);
    } else {
      enemy.setStatus(EnemyStatus.MOVE);
      enemy.setEnemyPingCounter(enemy.getEnemyPingCounter() + 1);
      //если расстояние меньше 10х расстояний атаки, то идти напрямик к игрок
      if (calcGipotenuza(enemy.getPosX() - x, enemy.getPosY() - y) < enemy.getAttackRange() * 10) {
        enemy.setDestination(new PairOfFloat(x, y));
      } else if (enemy.getEnemyPingCounter() > enemy.getEnemyPing()) {
        enemy.setEnemyPingCounter(0);
        if (MathUtils.random(0, 100) > 50) { //ставит рандомную точку, чтобы идти в непонятном направлении в 50% случаев
          enemy.setDestination(
              new PairOfFloat(MathUtils.random(x - 2000, x + 2000), MathUtils.random(y - 2000, y + 2000)));
        } else {
          enemy.setDestination(new PairOfFloat(MathUtils.random(x - 500, x + 500), MathUtils.random(y - 500, y + 500)));
        }
      } else {

      }
      defineSpeedXandY(enemy);
      rotateModel(enemy.getFloatDestination().getX() - enemy.getPosX(),
          enemy.getFloatDestination().getY() - enemy.getPosY(), enemy);
      enemy.setPosX(enemy.getPosX() + enemy.getSpeedX());
      enemy.setPosY(enemy.getPosY() + enemy.getSpeedY());
    }
  }


  public void rotateModel(float x, float y, Enemy enemy) {
    enemy.setAnimationAngle(MathUtils.radiansToDegrees * MathUtils.atan2(y, x) + 10);
  }


  public void defineSpeedXandY(Enemy enemy) {
    float x;
    float y;
    if (enemy.getFloatDestination().getX() > enemy.getDestination().getX()) {
      x = enemy.getFloatDestination().getX() - enemy.getTurnSpeed().getX();
    } else if (enemy.getFloatDestination().getX() < enemy.getDestination().getX()) {
      x = enemy.getFloatDestination().getX() + enemy.getTurnSpeed().getX();
    } else {
      x = enemy.getDestination().getX();
    }

    if (enemy.getFloatDestination().getY() > enemy.getDestination().getY()) {
      y = enemy.getFloatDestination().getY() - enemy.getTurnSpeed().getY();
    } else if (enemy.getFloatDestination().getY() < enemy.getDestination().getY()) {
      y = enemy.getFloatDestination().getY() + enemy.getTurnSpeed().getY();
    } else {
      y = enemy.getDestination().getY();
    }

    enemy.setFloatDestination(new PairOfFloat(x, y));
    float catetPrilezjaschiy = x - enemy.getPosX();
    float catetProtivo = y - enemy.getPosY();
    float gip = (float) Math.sqrt(catetPrilezjaschiy * catetPrilezjaschiy + catetProtivo * catetProtivo);
    float sin = catetProtivo / gip;
    float cos = catetPrilezjaschiy / gip;
    enemy.setSpeedY(sin * enemy.getSpeed());
    enemy.setSpeedX(cos * enemy.getSpeed());
  }


  public void moveToPlayer(float x, float y, Enemy enemy) {
    float v1 = enemy.getPosY();
    float v2 = enemy.getPosX();
    if (enemy.getPosX() < x - 20) {
      enemy.setPosX(enemy.getPosX() + enemy.getSpeed());
    } else {
      enemy.setPosX(enemy.getPosX() - enemy.getSpeed());
    }
    if (enemy.getPosY() < y - 20) {
      enemy.setPosY(enemy.getPosY() + enemy.getSpeed());
    } else {
      enemy.setPosY(enemy.getPosY() - enemy.getSpeed());
    }

    rotateModel(x, y, enemy);
  }
}

