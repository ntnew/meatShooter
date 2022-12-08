package ru.meat.game.service;

import static ru.meat.game.utils.FilesUtils.initAnimationFrames;

import lombok.Data;
import ru.meat.game.model.weapons.Weapon;
import ru.meat.game.model.weapons.WeaponEnum;

@Data
public class WeaponService {

  private final String assetsDir = "./assets/Top_Down_survivor/";

  public Weapon handgunWeapon(float zoom, float frameDuration) {
    return Weapon.builder()
        .name(WeaponEnum.PISTOL)
        .idleAnimation(initAnimationFrames(assetsDir + "handgun/idle/", zoom, frameDuration))
        .reloadAnimation(initAnimationFrames(assetsDir + "handgun/reload/", zoom, frameDuration))
        .shootAnimation(initAnimationFrames(assetsDir + "handgun/shoot/", zoom, frameDuration))
        .moveAnimation(initAnimationFrames(assetsDir + "handgun/move/", zoom, frameDuration))
        .meleeAttackAnimation(initAnimationFrames(assetsDir + "handgun/meleeattack/", zoom, frameDuration))
        .build();
  }

  public Weapon rifleWeapon(float zoom, float frameDuration) {
    return Weapon.builder()
        .name(WeaponEnum.RIFLE)
        .idleAnimation(initAnimationFrames(assetsDir + "rifle/idle/", zoom, frameDuration))
        .reloadAnimation(initAnimationFrames(assetsDir + "rifle/reload/", zoom, frameDuration))
        .shootAnimation(initAnimationFrames(assetsDir + "rifle/shoot/", zoom, frameDuration))
        .moveAnimation(initAnimationFrames(assetsDir + "rifle/move/", zoom, frameDuration))
        .meleeAttackAnimation(initAnimationFrames(assetsDir + "handgun/meleeattack/", zoom, frameDuration))
        .build();
  }

}
