package ru.meat.game.model.player;

import static ru.meat.game.utils.FilesUtils.initAnimationFrames;
import static ru.meat.game.settings.Constants.WORLD_TO_VIEW;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.Actor;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.meat.game.model.bodyData.BodyUserData;
import ru.meat.game.model.weapons.Weapon;
import ru.meat.game.model.weapons.WeaponEnum;
import ru.meat.game.service.RpgStatsService;
import ru.meat.game.model.weapons.WeaponFactory;
import ru.meat.game.utils.GDXUtils;

@EqualsAndHashCode(callSuper = true)
@Data
public class Player extends Actor {

  /**
   * Тело игрока в box2d
   */
  private Body body;

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

  private Animation<Texture> walkAnimation;
  private Animation<Texture> idle;
  private Animation<Texture> runAnimation;
  private Animation<Texture> strafeLeftAnimation;
  private Animation<Texture> strafeRightAnimation;


  private Animation<Texture> rifleIdleAnimation;
  private Animation<Texture> rifleSnootAnimation;
  private Animation<Texture> rifleMoveAnimation;
  private Animation<Texture> rifleReloadAnimation;

  private Animation<Texture> diedAnimation;

  private WeaponEnum currentWeapon;

  private List<Weapon> weapons = new ArrayList<>();

  private CharacterTopStatus topStatus;
  private CharacterFeetStatus feetStatus;

  public Player(float x, float y) {
    try {
      currentWeapon = WeaponEnum.SHOTGUN;
      topStatus = CharacterTopStatus.IDLE;
      feetStatus = CharacterFeetStatus.IDLE;

      this.hp = RpgStatsService.getInstance().getStats().getHp();

      this.walkAnimation = initAnimationFrames("./assets/Top_Down_survivor/feet/walk/", zoomMultiplier, frameDuration);
      this.idle = initAnimationFrames("./assets/Top_Down_survivor/feet/idle/", zoomMultiplier, frameDuration);
      this.runAnimation = initAnimationFrames("./assets/Top_Down_survivor/feet/run/", zoomMultiplier, frameDuration);
      this.strafeLeftAnimation = initAnimationFrames("./assets/Top_Down_survivor/feet/strafe_left/", zoomMultiplier, frameDuration);
      this.strafeRightAnimation = initAnimationFrames("./assets/Top_Down_survivor/feet/strafe_right/", zoomMultiplier,frameDuration);
      this.diedAnimation = initAnimationFrames("./assets/Top_Down_survivor/feet/strafe_right/", zoomMultiplier,frameDuration);

      weapons.add(WeaponFactory.shotgunWeapon(zoomMultiplier, frameDuration));
      weapons.add(WeaponFactory.rifleWeapon(zoomMultiplier, frameDuration));
      weapons.add(WeaponFactory.doubleBarrelShotgunWeapon(zoomMultiplier, frameDuration));
      weapons.add(WeaponFactory.machineGun(zoomMultiplier, frameDuration));
      weapons.add(WeaponFactory.m79(zoomMultiplier, frameDuration));
      weapons.add(WeaponFactory.m32(zoomMultiplier, frameDuration));
      weapons.add(WeaponFactory.aa12(zoomMultiplier, frameDuration));


      body = GDXUtils.createCircleForModel(90/WORLD_TO_VIEW, 100, new BodyUserData("player",0), x,y,true);
      body.getFixtureList().get(0).setFilterData(GDXUtils.getFilter());

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}