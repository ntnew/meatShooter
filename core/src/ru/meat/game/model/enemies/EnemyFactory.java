package ru.meat.game.model.enemies;

import static ru.meat.game.settings.Constants.MAIN_ZOOM;
import static ru.meat.game.settings.Constants.WORLD_TO_VIEW;

import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.AnimationStateData;
import com.esotericsoftware.spine.Skeleton;
import ru.meat.game.model.EnemyStatus;
import ru.meat.game.model.FloatPair;
import ru.meat.game.service.EnemyService;
import ru.meat.game.utils.GDXUtils;

public class EnemyFactory {

  public static Enemy createLittleBug(float x, float y) {
    float speedLowRange = 1.5f;
    float speedTopRange = 2.5f;
    float speed = MathUtils.random(speedLowRange, speedTopRange) * MAIN_ZOOM / WORLD_TO_VIEW;
    Enemy enemy = new Enemy(x, y, 1f, 100, speed, 0, 300, null) {
      @Override
      public void doSomething(float posX, float posY) {
        simpleActions(posX, posY, this);
      }
    };

    Skeleton skeleton = new Skeleton(EnemiesAnimation.getInstance().getLittleBugSkeletonData());
    skeleton.setPosition(50, 50);
    float random = MathUtils.random(0.8f, 1.2f);
    skeleton.setScale(random, random);
    enemy.setSkeleton(skeleton);

    AnimationStateData stateData = new AnimationStateData(
        EnemiesAnimation.getInstance().getLittleBugSkeletonData()); // Defines mixing (crossfading) between animations.
    stateData.setMix("walk", "attack", 0.2f);
    stateData.setMix("attack", "walk", 0.2f);
    enemy.setState(new AnimationState(stateData));
    enemy.getState().setTimeScale(2.8f);

    enemy.setRadius(280 / MAIN_ZOOM);
    enemy.setAttack(10);
    enemy.setAttackSpeed(1.1);
    enemy.setBody(GDXUtils.createCircleForModel(enemy.getRadius() / WORLD_TO_VIEW, 80,
        new EnemyBodyUserData("bug", 0, false, enemy.getAttack(), enemy.getAttackSpeed()), x, y, true));
    enemy.setRewardPoint(15);
    enemy.getState().setAnimation(0, "walk", true);

    return enemy;
  }

  public static Enemy createSpider(float x, float y) {
    float speedLowRange = 1.1f;
    float speedTopRange = 1.8f;
    float speed = MathUtils.random(speedLowRange, speedTopRange) * MAIN_ZOOM / WORLD_TO_VIEW;
    Enemy enemy = new Enemy(x, y, 1f, 500, speed, 0, 300, null) {
      @Override
      public void doSomething(float posX, float posY) {
        simpleActions(posX, posY, this);
      }
    };

    Skeleton skeleton = new Skeleton(EnemiesAnimation.getInstance().getSpiderSkeletonData());
    skeleton.setPosition(50, 50);
    float random = MathUtils.random(0.8f, 1.2f);
    skeleton.setScale(random, random);
    enemy.setSkeleton(skeleton);

    AnimationStateData stateData = new AnimationStateData(
        EnemiesAnimation.getInstance().getSpiderSkeletonData());
    stateData.setMix("walk", "attack", 0.1f);
    stateData.setMix("attack", "walk", 0.1f);
    enemy.setState(new AnimationState(stateData));
    enemy.getState().setTimeScale(1.5f);

    enemy.setRadius(530 / MAIN_ZOOM);
    enemy.setAttack(25);
    enemy.setAttackSpeed(1.0);
    enemy.setBody(GDXUtils.createCircleForModel(enemy.getRadius() / WORLD_TO_VIEW, 80,
        new EnemyBodyUserData("spider", 0, false, enemy.getAttack(), enemy.getAttackSpeed()), x, y, true));
    enemy.setRewardPoint(30);
    enemy.getState().setAnimation(0, "walk", true);

    return enemy;
  }

  public static Enemy createZombie(float x, float y) {
    float zombieSpeedLowRange = 1f;
    float zombieSpeedTopRange = 2f;
    float speed = MathUtils.random(zombieSpeedLowRange, zombieSpeedTopRange) * MAIN_ZOOM / WORLD_TO_VIEW;
    Enemy enemy = new Enemy(x, y, 1f, 100, speed, 0, 300, null);
    enemy.setRadius(80);
    enemy.setAttack(10);
    enemy.setAttackSpeed(1.5);
    enemy.setBody(GDXUtils.createCircleForModel(enemy.getRadius() / WORLD_TO_VIEW, 80,
        new EnemyBodyUserData("zombie", 0, false, enemy.getAttack(), enemy.getAttackSpeed()), x, y, true));
    enemy.setRewardPoint(45);
    return enemy;
  }

  /**
   * Метод действия врага простой идти на врага, при касании атаковать
   * <p>
   * для пауков и маленького жучка
   *
   * @param x     x координата игрока
   * @param y     у координата игрока
   * @param enemy враг
   */
  public static void simpleActions(float x, float y, Enemy enemy) {
    enemy.setEnemyPingCounter(enemy.getEnemyPingCounter() + 1);

    if (enemy.getHp() <= 0) {
      enemy.setStatus(EnemyStatus.DIED);
      enemy.getBody().setActive(false);
      enemy.getState().setAnimation(0, "dead", false);
    } else {
      enemy.getBody().setAwake(true);
      enemy.setPosX(enemy.getBody().getPosition().x);
      enemy.setPosY(enemy.getBody().getPosition().y);
      EnemyService.updateEnemyHp(enemy);
      EnemyBodyUserData userData = (EnemyBodyUserData) enemy.getBody().getFixtureList().get(0).getUserData();
      if (userData.isNeedAttack()) {
        if (!enemy.getStatus().equals(EnemyStatus.ATTACK)) {
          enemy.setStatus(EnemyStatus.ATTACK);
          enemy.getState().setAnimation(0, "attack", false);
          enemy.getState().addAnimation(0, "walk", true, 0);
        }
        userData.setNeedAttack(false);
      } else {
        if (!enemy.getStatus().equals(EnemyStatus.MOVE) && enemy.getState().getTracks().isEmpty()) {
          enemy.getState().setAnimation(0, "walk", true);
        }
        enemy.setStatus(EnemyStatus.MOVE);
      }
      EnemyService.rotateModel(x - enemy.getBody().getPosition().x, y - enemy.getBody().getPosition().y, enemy);

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
}
