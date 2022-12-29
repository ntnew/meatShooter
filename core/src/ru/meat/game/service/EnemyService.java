package ru.meat.game.service;

import static ru.meat.game.utils.GDXUtils.calcGipotenuza;
import static ru.meat.game.utils.Settings.*;

import com.badlogic.gdx.Gdx;
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
import ru.meat.game.model.bodyData.BodyUserData;
import ru.meat.game.model.EnemyStatus;
import ru.meat.game.model.FloatPair;
import ru.meat.game.model.bodyData.EnemyBodyUserData;
import ru.meat.game.utils.GDXUtils;

@NoArgsConstructor
public class EnemyService {

  private List<Enemy> enemies = new ArrayList<>();

  public Enemy createZombieEnemy(float x, float y, World world) {
    Enemy enemy = new Enemy(x, y, 1f, 100, 0.01f * MAIN_ZOOM,
        "./assets/export/move/",
        "./assets/export/idle/",
        "./assets/export/attack/",
        "./assets/export/died",
        0, 300, null);
    enemy.setRadius(80);
    enemy.setAttack(10);
    enemy.setAttackSpeed(1.5);
    enemy.setCenterMultip(FloatPair.create(2.9f, 1.78f));
    enemy.setBody(GDXUtils.createCircleForEnemy(world, enemy.getRadius() / WORLD_TO_VIEW, 80,
        new EnemyBodyUserData("zombie", 0, false, enemy.getAttack(), enemy.getAttackSpeed()), x, y));
    return enemy;
  }

  public void drawEnemySprite(SpriteBatch batch, Enemy enemy, float stateTime) {
    Sprite sprite = new Sprite(getActualFrame(stateTime, enemy));
    sprite.setX(enemy.getPosX() * WORLD_TO_VIEW - (sprite.getWidth() / enemy.getCenterMultip().getX()));
    sprite.setY(
        enemy.getPosY() * WORLD_TO_VIEW - (sprite.getHeight() - sprite.getHeight() / enemy.getCenterMultip().getY()));

    sprite.setOrigin(sprite.getWidth() / enemy.getCenterMultip().getX(),
        sprite.getHeight() - sprite.getHeight() / enemy.getCenterMultip().getY());
    sprite.setRotation(enemy.getAnimationAngle());
    sprite.draw(batch);
  }

  private Texture getActualFrame(float stateTime, Enemy enemy) {
    if (enemy.getStatus().equals(EnemyStatus.ATTACK)) {
      Texture keyFrame = enemy.getAttackAnimation().getKeyFrame(stateTime, true);
      Texture lastKeyframe = enemy.getAttackAnimation().getKeyFrames()[enemy.getAttackAnimation().getKeyFrames().length- 1];
      if (keyFrame.equals(lastKeyframe)) {
        enemy.setStatus(EnemyStatus.MOVE);
        EnemyBodyUserData userData = (EnemyBodyUserData) enemy.getBody().getFixtureList().get(0).getUserData();
        userData.setNeedAttack(false);
      }
      return keyFrame;
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
      enemy.getBody().setAwake(true);
      enemy.setPosX(enemy.getBody().getPosition().x);
      enemy.setPosY(enemy.getBody().getPosition().y);
      updateEnemyHp(enemy);
      //если расстояние меньше расстояния атаки, то атаковать
      EnemyBodyUserData userData = (EnemyBodyUserData) enemy.getBody().getFixtureList().get(0).getUserData();
      if (userData.isNeedAttack()) {
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
      rotateModel(x - enemy.getBody().getPosition().x, y - enemy.getBody().getPosition().y, enemy);
//      }
//      else {
//        enemy.setSpeedY(0);
//        enemy.setSpeedX(0);
//      }

      float catetPrilezjaschiy = x - enemy.getBody().getPosition().x;
      float catetProtivo = y - enemy.getBody().getPosition().y;
      float gip = GDXUtils.calcGipotenuza(catetPrilezjaschiy, catetProtivo);
      float sin = catetProtivo / gip;
      float cos = catetPrilezjaschiy / gip;
      enemy.setSpeedY(sin * enemy.getSpeed());
      enemy.setSpeedX(cos * enemy.getSpeed());
      enemy.getBody().setTransform(enemy.getBody().getPosition().x + enemy.getSpeedX(),
          enemy.getSpeedY() + enemy.getBody().getPosition().y, 0);
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

  /**
   * Установить угол поворота для модельки врага на точку
   *
   * @param x координата
   * @param y координата добавлено 10 градусов, чтобы можелька чуть более на ццентр игрока смотрела
   */
  public void rotateModel(float x, float y, Enemy enemy) {
    enemy.setAnimationAngle(MathUtils.radiansToDegrees * MathUtils.atan2(y, x) + 10);
  }

//  public void defineSpeedXandY(Enemy enemy) {
//    float x;
//    float y;
//    if (enemy.getFloatDestination().getX() > enemy.getDestination().getX()) {
//      x = enemy.getFloatDestination().getX() - enemy.getTurnSpeed().getX();
//    } else if (enemy.getFloatDestination().getX() < enemy.getDestination().getX()) {
//      x = enemy.getFloatDestination().getX() + enemy.getTurnSpeed().getX();
//    } else {
//      x = enemy.getDestination().getX();
//    }
//
//    if (enemy.getFloatDestination().getY() > enemy.getDestination().getY()) {
//      y = enemy.getFloatDestination().getY() - enemy.getTurnSpeed().getY();
//    } else if (enemy.getFloatDestination().getY() < enemy.getDestination().getY()) {
//      y = enemy.getFloatDestination().getY() + enemy.getTurnSpeed().getY();
//    } else {
//      y = enemy.getDestination().getY();
//    }
//
//    enemy.setFloatDestination(FloatPair.create(x, y));
//    float catetPrilezjaschiy = x - enemy.getPosX();
//    float catetProtivo = y - enemy.getPosY();
//    float gip = GDXUtils.calcGipotenuza(catetPrilezjaschiy, catetProtivo);
//    float sin = catetProtivo / gip;
//    float cos = catetPrilezjaschiy / gip;
//    enemy.setSpeedY(sin * enemy.getSpeed());
//    enemy.setSpeedX(cos * enemy.getSpeed());
//  }

  public void createEnemies(World world) {
    enemies.add(createZombieEnemy(50f, 50f, world));
    enemies.add(createZombieEnemy(100f, 100f, world));
    enemies.add(createZombieEnemy(MathUtils.random(0, Gdx.graphics.getWidth()),
        MathUtils.random(0, Gdx.graphics.getHeight()), world));
    enemies.add(createZombieEnemy(MathUtils.random(0, Gdx.graphics.getWidth()),
        MathUtils.random(0, Gdx.graphics.getHeight()), world));
    enemies.add(createZombieEnemy(MathUtils.random(0, Gdx.graphics.getWidth()),
        MathUtils.random(0, Gdx.graphics.getHeight()), world));
  }

  /**
   * отработать действия врагов
   *
   * @param posX  x координата игрока
   * @param posY  y координата игрока
   * @param world мир box2d
   */
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
    });
  }
}

