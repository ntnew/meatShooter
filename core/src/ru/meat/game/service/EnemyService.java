package ru.meat.game.service;

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


  public Enemy createZombieEnemy(float x, float y, World world) {
    Enemy enemy =  new Enemy(x, y, 40, 3, 100, 0.7f,
        "./assets/export/move/",
        "./assets/export/idle/",
        "./assets/export/attack/",
        null,
        0, 100, null);
    enemy.setBox(GDXUtils.createCircleForEnemy(world,40, 100));
    return enemy;
  }

  public void correctDistanceBetweenEnemies(List<Enemy> enemies) {
//    for (int i = 0; i < enemies.size(); i++) {
//      Enemy enemy = enemies.get(i);
//      for (int i1 = 0; i1 < enemies.size(); i1++) {
//        if (i != i1) {
//          Enemy enemy2 = enemies.get(i1);
//          float cat1 = enemy.getPosX() - enemy2.getPosX();
//          float cat2 = enemy.getPosY() - enemy2.getPosY();
//          float gip = (float) Math.sqrt(cat1 * cat1 + cat2 + cat2);
//          if (gip < 20) {
//            enemy2.setPosX(cat1 < 0 ? enemy2.getPosX() - 1 : enemy2.getPosX() + 1);
//            enemy2.setPosY(cat1 < 0 ? enemy2.getPosY() - 1 : enemy2.getPosY() + 1);
//
//          }
//        }
//      }
//    }
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
    if (Math.sqrt((x - enemy.getPosX()) * (x - enemy.getPosX()) + (y - enemy.getPosY()) * (y - enemy.getPosY()))
        < enemy.getAttackRange()) {
      enemy.setStatus(EnemyStatus.ATTACK);
    } else {
      enemy.setStatus(EnemyStatus.MOVE);
      enemy.setEnemyPingCounter(enemy.getEnemyPingCounter() + 1);
      if (GDXUtils.findGipotenuza(enemy.getPosX()-x, enemy.getPosY()-y) < 400) {
        enemy.setDestination(new PairOfFloat(x,y));
      } else if (enemy.getEnemyPingCounter() > enemy.getEnemyPing()){
        enemy.setEnemyPingCounter(0);
        enemy.setDestination(new PairOfFloat(MathUtils.random(x - 500, x + 500),MathUtils.random(y - 500, y + 500)));
      } else {

      }
      defineSpeedXandY(enemy);
      rotateModel(enemy.getFloatDestination().getX() - enemy.getPosX(), enemy.getFloatDestination().getY() - enemy.getPosY(), enemy);
      enemy.setPosX(enemy.getPosX() + enemy.getSpeedX());
      enemy.setPosY(enemy.getPosY() + enemy.getSpeedY());

    }
  }


  public void rotateModel(float x, float y, Enemy enemy) {
    enemy.setAnimationAngle(MathUtils.radiansToDegrees * MathUtils.atan2(y, x) + 10);
  }


  public void defineSpeedXandY( Enemy enemy) {
    float x;
    float y;
    if (enemy.getFloatDestination().getX()>enemy.getDestination().getX()) {
      x = enemy.getFloatDestination().getX()  - enemy.getTurnSpeed().getX();
    } else if (enemy.getFloatDestination().getX()  < enemy.getDestination().getX()){
      x = enemy.getFloatDestination().getX() + enemy.getTurnSpeed().getX();
    } else {
      x = enemy.getDestination().getX();
    }

    if (enemy.getFloatDestination().getY() > enemy.getDestination().getY()) {
      y = enemy.getFloatDestination().getY()  - enemy.getTurnSpeed().getY();
    } else if ( enemy.getFloatDestination().getY() < enemy.getDestination().getY()){
      y = enemy.getFloatDestination().getY() + enemy.getTurnSpeed().getY();
    } else {
      y = enemy.getDestination().getY();
    }

    enemy.setFloatDestination(new PairOfFloat(x,y));
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

