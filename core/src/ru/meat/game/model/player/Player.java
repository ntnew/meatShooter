package ru.meat.game.model.player;

import static ru.meat.game.settings.Constants.WORLD_TO_VIEW;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.AnimationStateData;
import com.esotericsoftware.spine.Skeleton;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.meat.game.model.bodyData.BodyUserData;
import ru.meat.game.model.enemies.EnemiesAnimation;
import ru.meat.game.model.weapons.Weapon;
import ru.meat.game.model.weapons.WeaponEnum;
import ru.meat.game.service.RpgStatsService;
import ru.meat.game.model.weapons.WeaponFactory;
import ru.meat.game.settings.Filters;
import ru.meat.game.utils.GDXUtils;

@EqualsAndHashCode(callSuper = true)
@Data
public class Player extends Actor {

  /**
   * Тело игрока в box2d
   */
  private Body body;


  private AnimationState topState;

  private Skeleton topSkeleton;



  private AnimationState feetState;

  private Skeleton feetSkeleton;

  /**
   * Текущие жизни игрока
   */
  private Double hp;

  /**
   * делитель зума модельки
   */
  private final float zoomMultiplier = 1f;

  /**
   * Длительность кадров в анимации
   */
  private final float frameDuration = 0.03f;

  /**
   * Флаг того, что игрок умер
   */
  private boolean isDead = false;

  private WeaponEnum currentWeapon;

  private List<Weapon> weapons = new ArrayList<>();

  private CharacterTopStatus topStatus;
  private CharacterFeetStatus feetStatus;

  public Player(float x, float y) {
    try {
      currentWeapon = WeaponEnum.SHOTGUN;
      topStatus = CharacterTopStatus.MOVE;
      feetStatus = CharacterFeetStatus.IDLE;

      this.hp = RpgStatsService.getInstance().getStats().getHp();


      topSkeleton = new Skeleton(PlayerAnimationFactory.getInstance().getPlayerTopSkeletonData());
      topSkeleton.setPosition(x, y);
//      float random = MathUtils.random(0.8f, 1.2f);
//      skeleton.setScale(random, random);

      AnimationStateData stateData = new AnimationStateData(PlayerAnimationFactory.getInstance().getPlayerTopSkeletonData());
//      stateData.setMix("walk", "attack", 0.2f);
//      stateData.setMix("attack", "walk", 0.2f);
      this.topState = new AnimationState(stateData);
      this.topState.setTimeScale(0.8f);

      feetSkeleton = new Skeleton(PlayerAnimationFactory.getInstance().getPlayerFeetSkeletonData());
      feetSkeleton.setPosition(x, y);
//      float random = MathUtils.random(0.8f, 1.2f);
//      skeleton.setScale(random, random);

      AnimationStateData feetStateData = new AnimationStateData(PlayerAnimationFactory.getInstance().getPlayerFeetSkeletonData());
//      stateData.setMix("walk", "attack", 0.2f);
//      stateData.setMix("attack", "walk", 0.2f);
      this.feetState = new AnimationState(feetStateData);
      this.feetState.setTimeScale(1.5f);

      topState.setAnimation(0, "move_" + getCurrentWeapon().getAniTag(), true);

      this.feetState.setAnimation(0, "run", true);



      weapons.add(WeaponFactory.shotgunWeapon());
      weapons.add(WeaponFactory.rifleWeapon());
      weapons.add(WeaponFactory.doubleBarrelShotgunWeapon());
      weapons.add(WeaponFactory.machineGun());
      weapons.add(WeaponFactory.m79());
      weapons.add(WeaponFactory.m32());
      weapons.add(WeaponFactory.aa12());

      body = GDXUtils.createCircleForModel(90 / WORLD_TO_VIEW, 100, new BodyUserData("player", 0), x, y, true);
      body.getFixtureList().get(0).setFilterData(Filters.getPlayerFilter());

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}