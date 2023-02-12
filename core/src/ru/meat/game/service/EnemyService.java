package ru.meat.game.service;

import static ru.meat.game.settings.Constants.WORLD_TO_VIEW;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.SkeletonRenderer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.meat.game.Box2dWorld;
import ru.meat.game.model.enemies.Enemy;
import ru.meat.game.model.EnemyStatus;
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
      enemy.getState().setAnimation(0, "dead", false);
    } else {
      enemy.getBody().setAwake(true);
      enemy.setPosX(enemy.getBody().getPosition().x);
      enemy.setPosY(enemy.getBody().getPosition().y);
      updateEnemyHp(enemy);
      //если расстояние меньше расстояния атаки, то атаковать
      EnemyBodyUserData userData = (EnemyBodyUserData) enemy.getBody().getFixtureList().get(0).getUserData();
      if (userData.isNeedAttack()) {
        if (!enemy.getStatus().equals(EnemyStatus.ATTACK)) {
          enemy.setStatus(EnemyStatus.ATTACK);
          enemy.getState().setAnimation(0, "attack", false);
          enemy.getState().addAnimation(0, "walk", true, 0);
        }
        userData.setNeedAttack(false);
      } else {
        if (!enemy.getStatus().equals(EnemyStatus.MOVE) && enemy.getState().getTracks().isEmpty()){
          enemy.getState().setAnimation(0, "walk", true);
        }
        enemy.setStatus(EnemyStatus.MOVE);
//        if (enemy.getState().)

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
    float v = MathUtils.radiansToDegrees * MathUtils.atan2(y, x);
    enemy.setAnimationAngle(v + 10);
    enemy.getSkeleton().getRootBone().setRotation(v-90);
  }

  /**
   * отработать действия врагов
   *
   * @param posX x координата игрока
   * @param posY y координата игрока
   */
  public void actionEnemies(float posX, float posY) {
    enemies.forEach(enemy -> {
      enemy.getState().update(Gdx.graphics.getDeltaTime()); // Update the animation time.
      enemy.getState().apply(enemy.getSkeleton()); // Poses skeleton using current animations. This sets the bones' local SRT.
      enemy.getSkeleton().updateWorldTransform();
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

  public void drawEnemies(Batch spriteBatch,  SkeletonRenderer renderer) {
    enemies.forEach(enemy -> drawSpineAni(spriteBatch, enemy, renderer));
  }

  private void drawSpineAni(Batch batch, Enemy enemy, SkeletonRenderer renderer){
    enemy.getSkeleton().setPosition(enemy.getPosX() * WORLD_TO_VIEW, enemy.getPosY() * WORLD_TO_VIEW );
    renderer.draw(batch, enemy.getSkeleton());
  }
}

