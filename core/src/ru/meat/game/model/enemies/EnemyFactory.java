package ru.meat.game.model.enemies;

import static ru.meat.game.settings.Constants.MAIN_ZOOM;
import static ru.meat.game.settings.Constants.WORLD_TO_VIEW;

import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.AnimationStateData;
import com.esotericsoftware.spine.Skeleton;
import ru.meat.game.model.EnemyStatus;
import ru.meat.game.settings.Filters;
import ru.meat.game.utils.GDXUtils;

public class EnemyFactory {

  public static Enemy createLittleBug(float x, float y) {
    float speedLowRange = 1.5f;
    float speedTopRange = 2.5f;
    float speed = MathUtils.random(speedLowRange, speedTopRange) * MAIN_ZOOM / WORLD_TO_VIEW;
    Enemy enemy = new Enemy(x, y, 1f, 100, speed, 300, null) {
      @Override
      public void doSomething(float posX, float posY) {
        EnemiesScripts.simpleActions(posX, posY, this);
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
    enemy.getBody().getFixtureList().get(0).setFilterData(Filters.getEnemyFilter());

    enemy.setRewardPoint(15);
    enemy.getState().setAnimation(0, "walk", true);

    return enemy;
  }

  public static Enemy createSpider(float x, float y) {
    float speedLowRange = 1.1f;
    float speedTopRange = 1.8f;
    float speed = MathUtils.random(speedLowRange, speedTopRange) * MAIN_ZOOM / WORLD_TO_VIEW;
    Enemy enemy = new Enemy(x, y, 1f, 500, speed, 300, null) {
      @Override
      public void doSomething(float posX, float posY) {
        EnemiesScripts.spiderActions(posX, posY, this);
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
    enemy.getBody().getFixtureList().get(0).setFilterData(Filters.getEnemyFilter());

    enemy.setRewardPoint(30);
    enemy.getState().setAnimation(0, "walk", true);

    return enemy;
  }

  public static Enemy createBlackWidow(float x, float y) {
    float speedLowRange = 1.1f;
    float speedTopRange = 1.8f;
    float speed = MathUtils.random(speedLowRange, speedTopRange) * MAIN_ZOOM / WORLD_TO_VIEW;
    Enemy enemy = new Enemy(x, y, 1f, 500, speed, 300, null) {
      @Override
      public void doSomething(float posX, float posY) {
        EnemiesScripts.blackWidowActions(this, posX, posY);
      }
    };

    Skeleton skeleton = new Skeleton(EnemiesAnimation.getInstance().getBlackWidowSkeletonData());
    skeleton.setPosition(50, 50);
    float random = MathUtils.random(0.8f, 1.2f);
    skeleton.setScale(random, random);
    enemy.setSkeleton(skeleton);

    AnimationStateData stateData = new AnimationStateData(EnemiesAnimation.getInstance().getBlackWidowSkeletonData());
    enemy.setState(new AnimationState(stateData));
    enemy.getState().setTimeScale(1.5f);

    enemy.setRadius(630 / MAIN_ZOOM);
    enemy.setAttack(25);
    enemy.setAttackSpeed(1.0);
    enemy.setBody(GDXUtils.createCircleForModel(enemy.getRadius() / WORLD_TO_VIEW, 80,
        new EnemyBodyUserData("blackWidow", 0, false, enemy.getAttack(), enemy.getAttackSpeed()), x, y, true));

    enemy.getBody().getFixtureList().get(0).setFilterData(Filters.getEnemyFilter());

    enemy.setRewardPoint(30);
    enemy.getState().setAnimation(0, "walk", true);
    enemy.setStatus(EnemyStatus.MOVE);
    return enemy;
  }
}
