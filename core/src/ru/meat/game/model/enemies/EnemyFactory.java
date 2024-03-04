package ru.meat.game.model.enemies;

import static ru.meat.game.settings.Constants.MAIN_ZOOM;
import static ru.meat.game.settings.Constants.WORLD_TO_VIEW;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.AnimationStateData;
import com.esotericsoftware.spine.Skeleton;
import ru.meat.game.model.EnemyStatus;
import ru.meat.game.model.enemies.scripts.BlackWidowScripts;
import ru.meat.game.model.enemies.scripts.LittleBugScripts;
import ru.meat.game.model.enemies.scripts.ScorpionBossScript;
import ru.meat.game.model.enemies.scripts.SpiderScripts;
import ru.meat.game.settings.Filters;
import ru.meat.game.utils.GDXUtils;

public class EnemyFactory {

  public static Enemy createLittleBug(float x, float y) {
    float speedLowRange = 1.5f;
    float speedTopRange = 2.5f;
    float speed = MathUtils.random(speedLowRange, speedTopRange) * MAIN_ZOOM / WORLD_TO_VIEW;

    float bodyRadius = 280 / MAIN_ZOOM;
    Body body = GDXUtils.createCircleForModel(bodyRadius / WORLD_TO_VIEW, 80,
        new EnemyBodyUserData("bug", 0, false, 10, 1.1), x, y, true);
    body.getFixtureList().get(0).setFilterData(Filters.getEnemyFilter());
    body.getPosition().set(x, y);
    Enemy enemy = new Enemy(100, speed, 300, body);

    enemy.setEnemyScript(LittleBugScripts.littleBugActions());

    Skeleton skeleton = new Skeleton(EnemiesAnimation.getInstance().getLittleBugSkeletonData());
    float random = MathUtils.random(0.8f, 1.2f);
    skeleton.setScale(random, random);
    enemy.setSkeleton(skeleton);

    AnimationStateData stateData = new AnimationStateData(
        EnemiesAnimation.getInstance().getLittleBugSkeletonData()); // Defines mixing (crossfading) between animations.
    stateData.setMix("walk", "attack", 0.2f);
    stateData.setMix("attack", "walk", 0.2f);
    enemy.setAnimationState(new AnimationState(stateData));
    enemy.getAnimationState().setTimeScale(2.8f);
    enemy.setRewardPoint(15);
    enemy.getAnimationState().setAnimation(0, "walk", true);

    return enemy;
  }

  public static Enemy createSpider(float x, float y) {
    float speedLowRange = 1.1f;
    float speedTopRange = 1.8f;
    float speed = MathUtils.random(speedLowRange, speedTopRange) * MAIN_ZOOM / WORLD_TO_VIEW;

    Body spider = GDXUtils.createCircleForModel(530 / MAIN_ZOOM / WORLD_TO_VIEW, 80,
        new EnemyBodyUserData("spider", 0, false, 25, 1.0), x, y, true);
    spider.getFixtureList().get(0).setFilterData(Filters.getEnemyFilter());
    spider.getPosition().set(x, y);

    Enemy enemy = new Enemy(500, speed, 300, spider);

    enemy.setEnemyScript(SpiderScripts.spiderActions());

    Skeleton skeleton = new Skeleton(EnemiesAnimation.getInstance().getSpiderSkeletonData());
    skeleton.setPosition(50, 50);
    float random = MathUtils.random(0.8f, 1.2f);
    skeleton.setScale(random, random);
    enemy.setSkeleton(skeleton);

    AnimationStateData stateData = new AnimationStateData(
        EnemiesAnimation.getInstance().getSpiderSkeletonData());
    stateData.setMix("walk", "attack", 0.1f);
    stateData.setMix("attack", "walk", 0.1f);
    enemy.setAnimationState(new AnimationState(stateData));
    enemy.getAnimationState().setTimeScale(1.5f);
    enemy.setRewardPoint(30);
    enemy.getAnimationState().setAnimation(0, "walk", true);

    return enemy;
  }

  public static Enemy createBlackWidow(float x, float y) {
    float speedLowRange = 1.1f;
    float speedTopRange = 1.8f;
    float speed = MathUtils.random(speedLowRange, speedTopRange) * MAIN_ZOOM / WORLD_TO_VIEW;
    Body body = GDXUtils.createCircleForModel(630 / MAIN_ZOOM / WORLD_TO_VIEW, 80,
        new EnemyBodyUserData("blackWidow", 0, false, 25, 1.0), x, y, true);
    body.getPosition().set(x, y);
    body.getFixtureList().get(0).setFilterData(Filters.getEnemyFilter());

    Enemy enemy = new Enemy(500, speed, 300, body);

    enemy.setEnemyScript(BlackWidowScripts.blackWidow());

    Skeleton skeleton = new Skeleton(EnemiesAnimation.getInstance().getBlackWidowSkeletonData());
    skeleton.setPosition(50, 50);
    float random = MathUtils.random(0.8f, 1.2f);
    skeleton.setScale(random, random);
    enemy.setSkeleton(skeleton);

    AnimationStateData stateData = new AnimationStateData(EnemiesAnimation.getInstance().getBlackWidowSkeletonData());
    enemy.setAnimationState(new AnimationState(stateData));
    enemy.getAnimationState().setTimeScale(1.5f);
    enemy.getBody().getFixtureList().get(0).setFilterData(Filters.getEnemyFilter());
    enemy.setRewardPoint(30);
    enemy.getAnimationState().setAnimation(0, "walk", true);
    enemy.setStatus(EnemyStatus.MOVE);
    return enemy;
  }

  public static Enemy createScorpionBoss(float x, float y) {
//    float speedLowRange = 0.8f;
//    float speedTopRange = 1f;
//    float speed = MathUtils.random(speedLowRange, speedTopRange) * MAIN_ZOOM / WORLD_TO_VIEW;
//    Enemy enemy = new Enemy(x, y, 10000, speed, 300);
//
//    enemy.setEnemyScript(ScorpionBossScript.scorpionBoss());
//
//    Skeleton skeleton = new Skeleton(EnemiesAnimation.getInstance().getScorpionSkeletonData());
//    skeleton.setPosition(500, 500);
//    float random = MathUtils.random(0.8f, 1.2f);
//    skeleton.setScale(random, random);
//    enemy.setSkeleton(skeleton);
//
//    AnimationStateData stateData = new AnimationStateData(EnemiesAnimation.getInstance().getScorpionSkeletonData());
//    enemy.setAnimationState(new AnimationState(stateData));
//    enemy.getAnimationState().setTimeScale(1.5f);
//
//    enemy.setRadius(630 / MAIN_ZOOM);
//    enemy.setAttack(25);
//    enemy.setAttackSpeed(1.0);
//    enemy.setBody(GDXUtils.createCircleForModel(enemy.getRadius() / WORLD_TO_VIEW, 80,
//        new EnemyBodyUserData("scorpion", 0, false, enemy.getAttack(), enemy.getAttackSpeed()), x, y, true));
//
//    enemy.getBody().getFixtureList().get(0).setFilterData(Filters.getEnemyFilter());
//
//    enemy.setRewardPoint(30);
//    enemy.getAnimationState().setAnimation(0, "walk", true);
//    enemy.setStatus(EnemyStatus.MOVE);
//    return enemy;
    return null;
  }
}
