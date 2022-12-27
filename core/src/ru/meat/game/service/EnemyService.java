package ru.meat.game.service;

import static ru.meat.game.utils.GDXUtils.calcGipotenuza;
import static ru.meat.game.utils.Settings.*;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import java.util.ArrayList;
import java.util.List;
import lombok.NoArgsConstructor;
import ru.meat.game.model.Enemy;
import ru.meat.game.model.BodyUserData;
import ru.meat.game.model.EnemyStatus;
import ru.meat.game.model.FloatPair;
import ru.meat.game.utils.GDXUtils;

@NoArgsConstructor
public class EnemyService {

  private List<Enemy> enemies = new ArrayList<>();

  public Enemy createZombieEnemy(float x, float y) {
    Enemy enemy = new Enemy(x, y, 50, 1f, 100, 0.7f * MAIN_ZOOM,
        "./assets/export/move/",
        "./assets/export/idle/",
        "./assets/export/attack/",
        "./assets/export/died",
        0, 300, null);
    enemy.setRadius(50);
    enemy.setCenterMultip(FloatPair.create(2.9f, 1.78f));
    return enemy;
  }

  /**
   * скорректировать дистанцию между врагами
   */
  public void correctDistanceBetweenEnemies() {
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
    if (enemy.getBody() != null) {
      sprite.setX(
          enemy.getBody().getPosition().x * WORLD_TO_VIEW - (sprite.getWidth() / enemy.getCenterMultip().getX()));
      sprite.setY(enemy.getBody().getPosition().y * WORLD_TO_VIEW - (sprite.getHeight()
          - sprite.getHeight() / enemy.getCenterMultip().getY()));
    }
    sprite.setOrigin(sprite.getWidth() / enemy.getCenterMultip().getX(),
        sprite.getHeight() - sprite.getHeight() / enemy.getCenterMultip().getY());
    sprite.setRotation(enemy.getAnimationAngle());
    sprite.draw(batch);
  }

  private Texture getActualFrame(float stateTime, Enemy enemy) {
    if (enemy.getStatus().equals(EnemyStatus.ATTACK)) {
      return enemy.getAttackAnimation().getKeyFrame(stateTime, true);
    } else if (enemy.getStatus().equals(EnemyStatus.IDLE)) {
      return enemy.getIdleAnimation().getKeyFrame(stateTime);
    } else if (enemy.getStatus().equals(EnemyStatus.MOVE)) {
      return enemy.getWalkAnimation().getKeyFrame(stateTime);
    } else if (enemy.getStatus().equals(EnemyStatus.DIED)) {
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

    if (enemy.getHp() <= 0) {
      enemy.setStatus(EnemyStatus.DIED);
      enemy.getBody().setActive(false);
    } else {
      updateEnemyHp(enemy);
      //если расстояние меньше расстояния атаки, то атаковать
      if (calcGipotenuza(x, y, enemy.getPosX(), enemy.getPosY()) < enemy.getAttackRange()) {
        enemy.setStatus(EnemyStatus.ATTACK);
      } else {
        enemy.setStatus(EnemyStatus.MOVE);
        //если расстояние меньше 10х расстояний атаки, то идти напрямик к игроку
//        if (isEnemyTooCloseToPlayer(x, y, enemy)) {
//          enemy.setDestination(FloatPair.create(x, y));
//        } else if (enemy.getEnemyPingCounter() > enemy.getEnemyPing()) {
//          enemy.setEnemyPingCounter(0);
//          if (MathUtils.random(0, 100) < 30) {
//            //ставит рандомную точку, чтобы идти в непонятном направлении в 30% случаев
//            enemy.setDestination(
//                FloatPair.create(MathUtils.random(x - 2000, x + 2000), MathUtils.random(y - 2000, y + 2000)));
//          } else {
//            enemy.setDestination(
//                FloatPair.create(MathUtils.random(x - 500, x + 500), MathUtils.random(y - 500, y + 500)));
//          }
//        }
      }

//      defineSpeedXandY(enemy);
//      if (calcGipotenuza(x, y, enemy.getPosX(), enemy.getPosY()) > enemy.getAttackRange() / 1.5) {
//        rotateModel(enemy.getFloatDestination().getX() - enemy.getPosX(),
//            enemy.getFloatDestination().getY() - enemy.getPosY(), enemy);
//      } else {
//        enemy.setSpeedY(0);
//        enemy.setSpeedX(0);
//      }
//
//      enemy.setPosX(enemy.getPosX() + enemy.getSpeedX());
//      enemy.setPosY(enemy.getPosY() + enemy.getSpeedY());
//      enemy.setSpeedX(100);
//      enemy.setSpeedY(100);
      enemy.getBody().applyForceToCenter(new Vector2(enemy.getSpeedX(), enemy.getSpeedY()), true);
    }
  }

  private void updateEnemyHp(Enemy enemy) {
    BodyUserData userData = (BodyUserData) enemy.getBody().getFixtureList().get(0).getUserData();
    if (userData != null && userData.getDamage() != 0) {
      enemy.setHp(enemy.getHp() - userData.getDamage());
      userData.setDamage(0);
      System.out.println(enemy.getHp());
    }
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

  public void createEnemies(World world) {
    enemies.add(createZombieEnemy(50f, 50f));
//    enemies.add(createZombieEnemy(100f, 100f));
//    enemies.add(createZombieEnemy(MathUtils.random(0, Gdx.graphics.getWidth()),
//        MathUtils.random(0, Gdx.graphics.getHeight())));
//    enemies.add(createZombieEnemy(MathUtils.random(0, Gdx.graphics.getWidth()),
//        MathUtils.random(0, Gdx.graphics.getHeight())));
//    enemies.add(createZombieEnemy(MathUtils.random(0, Gdx.graphics.getWidth()),
//        MathUtils.random(0, Gdx.graphics.getHeight())));

    enemies.forEach(
        x -> x.setBody(GDXUtils.createCircleForEnemy(world, x.getRadius() / WORLD_TO_VIEW, 80,
            new BodyUserData("z1", 0), x.getPosX(), x.getPosY())));
  }

  public void actionEnemies(float posX, float posY, World world) {
    enemies.forEach(enemy -> {
      if (!enemy.getStatus().equals(EnemyStatus.DIED)) {
        doSomething(posX, posY, enemy);
      } else if (enemy.getBody() != null && !enemy.getBody().getFixtureList().isEmpty()) {
        world.destroyBody(enemy.getBody());
        enemy.setBody(null);
      }
      //TODO сделать максимально облегчённую версию врага
    });
  }

  public void drawEnemies(SpriteBatch spriteBatch, float stateTime) {
    enemies.forEach(enemy -> {
      drawEnemySprite(spriteBatch, enemy, stateTime);
      enemy.setPreviousStatus(enemy.getStatus());
    });
  }
}

