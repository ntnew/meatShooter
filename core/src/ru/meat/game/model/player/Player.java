package ru.meat.game.model.player;

import static ru.meat.game.settings.Constants.WORLD_TO_VIEW;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.AnimationStateData;
import com.esotericsoftware.spine.Skeleton;
import com.esotericsoftware.spine.SkeletonRenderer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.meat.game.MyGame;
import ru.meat.game.model.bodyData.BodyUserData;
import ru.meat.game.model.enemies.EnemiesAnimation;
import ru.meat.game.model.weapons.Weapon;
import ru.meat.game.model.weapons.WeaponEnum;
import ru.meat.game.service.AudioService;
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

  private float feetRotationAngle = 0;

  private volatile float modelFrontAngle = 0;

  public Player(float x, float y) {
    try {
      currentWeapon = WeaponEnum.SHOTGUN;
      topStatus = CharacterTopStatus.MOVE;
      feetStatus = CharacterFeetStatus.IDLE;

      this.hp = RpgStatsService.getInstance().getStats().getHp();

      topSkeleton = new Skeleton(PlayerAnimationFactory.getInstance().getPlayerTopSkeletonData());
      topSkeleton.setPosition(x, y);

      AnimationStateData stateData = new AnimationStateData(PlayerAnimationFactory.getInstance().getPlayerTopSkeletonData());

      this.topState = new AnimationState(stateData);
      this.topState.setTimeScale(0.8f);

      feetSkeleton = new Skeleton(PlayerAnimationFactory.getInstance().getPlayerFeetSkeletonData());
      feetSkeleton.setPosition(x, y);


      AnimationStateData feetStateData = new AnimationStateData(PlayerAnimationFactory.getInstance().getPlayerFeetSkeletonData());

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

  @Override
  public void act(float delta) {
    super.act(delta);
    if (!this.isDead()
        && getActualWeapon().isReloading()
        && ((!this.getTopState().getTracks().isEmpty()
        && !Objects.equals("reload_" + this.getCurrentWeapon().getAniTag(),
        this.getTopState().getTracks().get(this.getTopState().getTracks().size - 1).getAnimation().getName()))
        || this.getTopState().getTracks().isEmpty())) {
      this.getTopState()
          .setAnimation(0, "reload_" + this.getCurrentWeapon().getAniTag(), true);
    }

    if (this.isDead()
        && ((!Objects.equals("death_" + this.getCurrentWeapon().getAniTag(),
        this.getTopState().getTracks().get(0).getAnimation().getName()))
        || this.getTopState().getTracks().isEmpty())) {
      this.getTopState().setAnimation(0, "death_" + this.getCurrentWeapon().getAniTag(), false);
    }

    this.getTopState().update(Gdx.graphics.getDeltaTime());
    this.getTopState().apply(this.getTopSkeleton());
    this.getTopSkeleton().updateWorldTransform();

    // обновить нижнюю часть анимации
    if (!this.isDead() && CharacterFeetStatus.MOVE.equals(this.getFeetStatus())) {
      this.getFeetState().update(Gdx.graphics.getDeltaTime());
    }

    this.getFeetState().apply(this.getFeetSkeleton());
    this.getFeetSkeleton().updateWorldTransform();

    handlePlayerHp();
  }

  /**
   * Обработать значения хп игрока
   */
  private void handlePlayerHp() {
    BodyUserData userData = (BodyUserData) body.getFixtureList().get(0).getUserData();
    if (userData.getDamage() != 0) {
      this. hp = hp - userData.getDamage();
      userData.setDamage(0);
      AudioService.getInstance().playHit();
    }

    if (this.hp <= 0) {
      this.isDead = true;
    }
  }

  /**
   * Получить выбранное оружие у игрока
   */
  public Weapon getActualWeapon() {
    return this.weapons.stream().filter(x -> x.getName().equals(this.currentWeapon))
        .findFirst()
        .orElse(this.weapons.get(0));
  }



  @Override
  public void draw(Batch batch, float parentAlpha) {
    if (!this.isDead) {
      drawFeetSprite( batch);
    }
    drawTopSprite(batch);
  }

  private void drawFeetSprite(Batch polyBatch) {
    this.feetSkeleton.setPosition(body.getPosition().x * WORLD_TO_VIEW, body.getPosition().y * WORLD_TO_VIEW);
    this.feetSkeleton.getRootBone().setRotation(feetRotationAngle);
    MyGame.getInstance().getGameZone().getRenderer().draw(polyBatch, this.feetSkeleton);
  }

  private void drawTopSprite(Batch polyBatch) {
    this.topSkeleton.setPosition(body.getPosition().x * WORLD_TO_VIEW, body.getPosition().y * WORLD_TO_VIEW);
    this.topSkeleton.getRootBone().setRotation(modelFrontAngle);
    MyGame.getInstance().getGameZone().getRenderer().draw(polyBatch, this.topSkeleton);
  }
}
