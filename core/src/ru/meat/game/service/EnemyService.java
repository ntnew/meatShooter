package ru.meat.game.service;

import static ru.meat.game.utils.GDXUtils.calcGipotenuza;
import static ru.meat.game.utils.GDXUtils.createCircleForEnemy;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import java.util.List;
import lombok.NoArgsConstructor;
import ru.meat.game.model.Enemy;
import ru.meat.game.model.EnemyStatus;
import ru.meat.game.model.FloatPair;
import ru.meat.game.utils.GDXUtils;

@NoArgsConstructor
public class EnemyService {


  public Enemy createZombieEnemy(float x, float y) {
    Enemy enemy = new Enemy(x, y, 50, 4.5f, 100, 0.7f,
        "./assets/export/move/",
        "./assets/export/idle/",
        "./assets/export/attack/",
        null,
        0, 300, null);
    enemy.setRadius(30);
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
            enemy2.setPosX(cat1 < 0 ? enemy2.getPosX() - 0.5f : enemy2.getPosX() + 0.5f);
            enemy2.setPosY(cat2 < 0 ? enemy2.getPosY() - 0.5f : enemy2.getPosY() + 0.5f);
          }
        }
      }
    }
  }

  public void drawEnemySprite(SpriteBatch batch, Enemy enemy, float stateTime) {
    Sprite sprite = new Sprite(getActualFrame(stateTime, enemy));
    sprite.setX(enemy.getPosX());
    sprite.setY(enemy.getPosY());
    sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
    sprite.setRotation(enemy.getAnimationAngle());
    sprite.draw(batch);
    enemy.setCenter(FloatPair.create(enemy.getPosX() + sprite.getWidth() / 2, enemy.getPosY() + sprite.getHeight() / 2 - 10));
  }

  private Texture getActualFrame(float stateTime, Enemy enemy) {
    if (enemy.getStatus().equals(EnemyStatus.ATTACK)) {
      return enemy.getAttackAnimation().getKeyFrame(stateTime, true);
    } else if (enemy.getStatus().equals(EnemyStatus.IDLE)) {
      return enemy.getIdleAnimation().getKeyFrame(stateTime);
    } else if (enemy.getStatus().equals(EnemyStatus.MOVE)) {
      return enemy.getWalkAnimation().getKeyFrame(stateTime);
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
    enemy.setEnemyPingCounter(enemy.getEnemyPingCounter() + 1);

    //если расстояние меньше расстояния атаки, то атаковать
    if (calcGipotenuza(x, y, enemy.getPosX(), enemy.getPosY()) < enemy.getAttackRange()) {
      enemy.setStatus(EnemyStatus.ATTACK);
    } else {
      enemy.setStatus(EnemyStatus.MOVE);
      //если расстояние меньше 10х расстояний атаки, то идти напрямик к игроку
      if (isEnemyTooCloseToPlayer(x, y, enemy) && !enemy.getPreviousStatus().equals(EnemyStatus.ATTACK)) {
        enemy.setDestination(FloatPair.create(x, y));
      } else if (enemy.getEnemyPingCounter() > enemy.getEnemyPing()) {
        enemy.setEnemyPingCounter(0);
        if (MathUtils.random(0, 100) > 50) { //ставит рандомную точку, чтобы идти в непонятном направлении в 50% случаев
          enemy.setDestination(
              FloatPair.create(MathUtils.random(x - 2000, x + 2000), MathUtils.random(y - 2000, y + 2000)));
        } else {
          enemy.setDestination(
              FloatPair.create(MathUtils.random(x - 500, x + 500), MathUtils.random(y - 500, y + 500)));
        }
      }
    }

    defineSpeedXandY(enemy);
    if (calcGipotenuza(x, y, enemy.getPosX(), enemy.getPosY()) > enemy.getAttackRange() / 1.5) {
      rotateModel(enemy.getFloatDestination().getX() - enemy.getPosX(),
          enemy.getFloatDestination().getY() - enemy.getPosY(), enemy);
    } else {
      enemy.setSpeedY(0);
      enemy.setSpeedX(0);
    }

    enemy.setPosX(enemy.getPosX() + enemy.getSpeedX());
    enemy.setPosY(enemy.getPosY() + enemy.getSpeedY());
  }

  private boolean isEnemyTooCloseToPlayer(float x, float y, Enemy enemy) {
    return calcGipotenuza(enemy.getPosX() - x, enemy.getPosY() - y) < enemy.getAttackRange() * 8;
  }

  /**
   * Установить угол поворота для модельки врага на точку
   *
   * @param x координата
   * @param y координата добавлено 10 градусов, чтобы можелька чуть более на ццентр игрока смотрела
   */
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

    enemy.setFloatDestination(FloatPair.create(x, y));
    float catetPrilezjaschiy = x - enemy.getPosX();
    float catetProtivo = y - enemy.getPosY();
    float gip = GDXUtils.calcGipotenuza(catetPrilezjaschiy, catetProtivo);
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

