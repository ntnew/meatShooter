package ru.meat.game.service;

import static ru.meat.game.settings.Constants.MAIN_ZOOM;
import static ru.meat.game.settings.Constants.WORLD_TO_VIEW;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.meat.game.Box2dWorld;
import ru.meat.game.model.enemies.Enemy;
import ru.meat.game.model.EnemyStatus;
import ru.meat.game.model.FloatPair;
import ru.meat.game.model.bodyData.BodyUserData;
import ru.meat.game.model.enemies.EnemyBodyUserData;
import ru.meat.game.model.enemies.EnemiesAnimation;
import ru.meat.game.utils.GDXUtils;

@NoArgsConstructor
public class EnemyService {

  @Getter
  private final List<Enemy> enemies = new ArrayList<>();
  @Getter
  private final AtomicInteger rewardPointCount = new AtomicInteger(0);


  public Enemy createZombieEnemy(float x, float y) {
    float zombieSpeedLowRange = 1f;
    float zombieSpeedTopRange = 2f;
    float speed = MathUtils.random(zombieSpeedLowRange, zombieSpeedTopRange) * MAIN_ZOOM / WORLD_TO_VIEW;
    Enemy enemy = new Enemy(x, y, 1f, 100, speed,0, 300, null);
    enemy.setRadius(80);
    enemy.setAttack(10);
    enemy.setAttackSpeed(1.5);
    enemy.setCenterMultip(FloatPair.create(2.9f, 1.78f));
    enemy.setBody(GDXUtils.createCircleForModel(enemy.getRadius() / WORLD_TO_VIEW, 80,
        new EnemyBodyUserData("zombie", 0, false, enemy.getAttack(), enemy.getAttackSpeed()), x, y));
    enemy.setRewardPoint(5);
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
      Texture keyFrame = EnemiesAnimation.getInstance().getAttackAnimation().getKeyFrame(stateTime, true);
      Texture lastKeyframe = EnemiesAnimation.getInstance().getAttackAnimation().getKeyFrames()[
          EnemiesAnimation.getInstance().getAttackAnimation().getKeyFrames().length - 1];
      if (keyFrame.equals(lastKeyframe)) {
        enemy.setStatus(EnemyStatus.MOVE);
        EnemyBodyUserData userData = (EnemyBodyUserData) enemy.getBody().getFixtureList().get(0).getUserData();
        userData.setNeedAttack(false);
      }
      return keyFrame;
    } else if (enemy.getStatus().equals(EnemyStatus.IDLE)) {
      return EnemiesAnimation.getInstance().getIdleAnimation().getKeyFrame(stateTime);
    } else if (enemy.getStatus().equals(EnemyStatus.MOVE)) {
      return EnemiesAnimation.getInstance().getWalkAnimation().getKeyFrame(stateTime);
    } else if (enemy.getStatus().equals(EnemyStatus.DIED)) {
      return EnemiesAnimation.getInstance().getDieAnimation().getKeyFrame(stateTime, true);
    }
    return EnemiesAnimation.getInstance().getIdleAnimation().getKeyFrame(stateTime);
  }

  /**
   * Метод действия врага
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

  public void createEnemies() {
    enemies.add(createZombieEnemy(50f, 50f));
  }

  /**
   * отработать действия врагов
   *
   * @param posX x координата игрока
   * @param posY y координата игрока
   */
  public void actionEnemies(float posX, float posY) {
    enemies.forEach(enemy -> {
      if (!enemy.getStatus().equals(EnemyStatus.DIED)) {
        doSomething(posX, posY, enemy);
      } else if (enemy.getBody() != null && !enemy.getBody().getFixtureList().isEmpty()) {
        Box2dWorld.getInstance().getWorld().destroyBody(enemy.getBody());
        enemy.setBody(null);
        AudioService.getInstance().playEnemyDie();
        rewardPointCount.set(rewardPointCount.get() + enemy.getRewardPoint());
      }
    });
  }

  public void drawEnemies(SpriteBatch spriteBatch, float stateTime) {
    enemies.forEach(enemy -> drawEnemySprite(spriteBatch, enemy, stateTime));
  }
}

