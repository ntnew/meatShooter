package ru.meat.game.model.enemies;

import static ru.meat.game.settings.Constants.MAIN_ZOOM;
import static ru.meat.game.settings.Constants.WORLD_TO_VIEW;

import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.AnimationStateData;
import com.esotericsoftware.spine.Skeleton;
import ru.meat.game.model.FloatPair;
import ru.meat.game.utils.GDXUtils;

public class EnemyFactory {

  public static Enemy createLittleBug(float x, float y){
    float zombieSpeedLowRange = 1.5f;
    float zombieSpeedTopRange = 2.5f;
    float speed = MathUtils.random(zombieSpeedLowRange, zombieSpeedTopRange) * MAIN_ZOOM / WORLD_TO_VIEW;
    Enemy enemy = new Enemy(x, y, 1f, 100, speed,0, 300, null);

    Skeleton skeleton = new Skeleton(EnemiesAnimation.getInstance().getSkeletonData());
    skeleton.setPosition(50, 50);
    enemy.setSkeleton(skeleton);

    AnimationStateData stateData = new AnimationStateData(EnemiesAnimation.getInstance().getSkeletonData()); // Defines mixing (crossfading) between animations.
    stateData.setMix("walk", "attack", 0.2f);
    stateData.setMix("attack", "walk", 0.2f);
    enemy.setState(new AnimationState(stateData));
    enemy.getState().setTimeScale(2.8f);

    enemy.setRadius(80);
    enemy.setAttack(10);
    enemy.setAttackSpeed(1.5);
    enemy.setCenterMultip(FloatPair.create(2.9f, 1.78f));
    enemy.setBody(GDXUtils.createCircleForModel(enemy.getRadius() / WORLD_TO_VIEW, 80,
        new EnemyBodyUserData("bug", 0, false, enemy.getAttack(), enemy.getAttackSpeed()), x, y, true));
    enemy.setRewardPoint(5);
    enemy.getState().setAnimation(0, "walk", true);


    return enemy;
  }

  public static Enemy createZombie(float x, float y){
    float zombieSpeedLowRange = 1f;
    float zombieSpeedTopRange = 2f;
    float speed = MathUtils.random(zombieSpeedLowRange, zombieSpeedTopRange) * MAIN_ZOOM / WORLD_TO_VIEW;
    Enemy enemy = new Enemy(x, y, 1f, 100, speed,0, 300, null);
    enemy.setRadius(80);
    enemy.setAttack(10);
    enemy.setAttackSpeed(1.5);
    enemy.setCenterMultip(FloatPair.create(2.9f, 1.78f));
    enemy.setBody(GDXUtils.createCircleForModel(enemy.getRadius() / WORLD_TO_VIEW, 80,
        new EnemyBodyUserData("zombie", 0, false, enemy.getAttack(), enemy.getAttackSpeed()), x, y,true));
    enemy.setRewardPoint(5);
    return enemy;
  }
}
