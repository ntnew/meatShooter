package ru.meat.game.model;

import static ru.meat.game.utils.FilesUtils.initAnimationFrames;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import ru.meat.game.model.weapons.Weapon;
import ru.meat.game.model.weapons.WeaponEnum;
import ru.meat.game.service.BulletService;
import ru.meat.game.service.WeaponService;

@Data
public class Player extends Actor {

  private final float zoomMultiplier = 4.5f;
  private final float frameDuration = 0.03f;

  private Animation<Texture> walkAnimation;
  private Animation<Texture> idle;
  private Animation<Texture> runAnimation;
  private Animation<Texture> strafeLeftAnimation;
  private Animation<Texture> strafeRightAnimation;


  private Animation<Texture> rifleIdleAnimation;
  private Animation<Texture> rifleSnootAnimation;
  private Animation<Texture> rifleMoveAnimation;
  private Animation<Texture> rifleReloadAnimation;

  private WeaponEnum currentWeapon;

  private List<Weapon> weapons = new ArrayList<>();

  private WeaponService weaponService;

  private CharacterTopStatus topStatus;
  private CharacterFeetStatus feetStatus;

  public Player(World world) {
    try {
      currentWeapon = WeaponEnum.PISTOL;
      topStatus = CharacterTopStatus.IDLE;
      feetStatus = CharacterFeetStatus.IDLE;

      this.walkAnimation = initAnimationFrames("./assets/Top_Down_survivor/feet/walk/", zoomMultiplier, frameDuration);
      this.idle = initAnimationFrames("./assets/Top_Down_survivor/feet/idle/", zoomMultiplier, frameDuration);
      this.runAnimation = initAnimationFrames("./assets/Top_Down_survivor/feet/run/", zoomMultiplier, frameDuration);
      this.strafeLeftAnimation = initAnimationFrames("./assets/Top_Down_survivor/feet/strafe_left/", zoomMultiplier, frameDuration);
      this.strafeRightAnimation = initAnimationFrames("./assets/Top_Down_survivor/feet/strafe_right/", zoomMultiplier,frameDuration);

      weaponService = new WeaponService(new BulletService(world));
      weapons.add(weaponService.handgunWeapon(zoomMultiplier, frameDuration));
      weapons.add(weaponService.rifleWeapon(zoomMultiplier, frameDuration));


    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}