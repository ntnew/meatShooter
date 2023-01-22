package ru.meat.game.service;

import static ru.meat.game.utils.FilesUtils.initAnimationFrames;

import lombok.Data;
import ru.meat.game.model.weapons.Rifle;
import ru.meat.game.model.weapons.Shotgun;
import ru.meat.game.model.weapons.Weapon;
import ru.meat.game.model.weapons.WeaponEnum;

@Data
public class WeaponService {
  private final static float pistolBulletSpeed = 10f;

  private final String assetsDir = "./assets/Top_Down_survivor/";

//  public Weapon handgunWeapon(float zoom, float frameDuration) {
//    return Weapon.builder()
//        .name(WeaponEnum.PISTOL)
//        .idleAnimation(initAnimationFrames(assetsDir + "handgun/idle/", zoom, frameDuration))
//        .reloadAnimation(initAnimationFrames(assetsDir + "handgun/reload/", zoom, frameDuration))
//        .shootAnimation(initAnimationFrames(assetsDir + "handgun/shoot/", zoom, frameDuration))
//        .moveAnimation(initAnimationFrames(assetsDir + "handgun/move/", zoom, frameDuration))
//        .meleeAttackAnimation(initAnimationFrames(assetsDir + "handgun/meleeattack/", zoom, frameDuration))
//        .shootSound("glockShoot.mp3")
//        .reloadSound("sound/weapons/ak47reload.mp3")
//        .speed(pistolBulletSpeed)
//        .fireRate(1000)
//        .clipSize(12)
//        .fireCount(0)
//        .reloadCounter(0)
//        .reloadDuration(3)
//        .currentLockCounter(0)
//        .damage(20)
//        .build();
//  }

  public Weapon rifleWeapon(float zoom, float frameDuration) {
    return Rifle.builder()
        .name(WeaponEnum.RIFLE)
        .idleAnimation(initAnimationFrames(assetsDir + "rifle/idle/", zoom, frameDuration))
        .reloadAnimation(initAnimationFrames(assetsDir + "rifle/reload/", zoom, frameDuration))
        .shootAnimation(initAnimationFrames(assetsDir + "rifle/shoot/", zoom, frameDuration))
        .moveAnimation(initAnimationFrames(assetsDir + "rifle/move/", zoom, frameDuration))
        .meleeAttackAnimation(initAnimationFrames(assetsDir + "handgun/meleeattack/", zoom, frameDuration))
        .speed(pistolBulletSpeed)
        .fireRate(100)
        .shootSound("sound/weapons/ak47.mp3")
        .reloadSound("sound/weapons/ak47reload.mp3")
        .clipSize(30)
        .fireCount(0)
        .reloadDuration(3)
        .reloadCounter(0)
        .damage(35)
        .bulletDeflection(1)
        .currentLockCounter(0)
        .build();
  }

  public Weapon shotgunWeapon(float zoom, float frameDuration) {
    return Shotgun.builder()
        .name(WeaponEnum.SHOTGUN)
        .idleAnimation(initAnimationFrames(assetsDir + "rifle/idle/", zoom, frameDuration))
        .reloadAnimation(initAnimationFrames(assetsDir + "rifle/reload/", zoom, frameDuration))
        .shootAnimation(initAnimationFrames(assetsDir + "rifle/shoot/", zoom, frameDuration))
        .moveAnimation(initAnimationFrames(assetsDir + "rifle/move/", zoom, frameDuration))
        .meleeAttackAnimation(initAnimationFrames(assetsDir + "handgun/meleeattack/", zoom, frameDuration))
        .speed(pistolBulletSpeed)
        .fireRate(800)
        .shootSound("sound/weapons/shotgun.mp3")
        .reloadSound("sound/weapons/shotgunReload.mp3")
        .clipSize(3)
        .fireCount(0)
        .reloadDuration(1f)
        .reloadCounter(0)
        .damage(35)
        .shotInOneBullet(10)
        .bulletDeflection(2)
        .currentLockCounter(0)
        .build();
  }
}
