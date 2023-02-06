package ru.meat.game.model.weapons;

import static ru.meat.game.settings.Constants.MAIN_ZOOM;
import static ru.meat.game.utils.FilesUtils.initAnimationFrames;

import com.badlogic.gdx.graphics.Texture;
import lombok.Data;
import ru.meat.game.loader.LoaderManager;
import ru.meat.game.model.weapons.Rifle;
import ru.meat.game.model.weapons.Shotgun;
import ru.meat.game.model.weapons.Weapon;
import ru.meat.game.model.weapons.WeaponEnum;
import ru.meat.game.utils.GDXUtils;

@Data
public class WeaponFactory {
  private final static float pistolBulletSpeed = 12f;

  private final static String assetsDir = "./assets/Top_Down_survivor/";

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

  public static Weapon rifleWeapon(float zoom, float frameDuration) {
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
        .bulletTexture(GDXUtils.resizeTexture((Texture) LoaderManager.getInstance().get("Bullet1.png"), 1 / MAIN_ZOOM))
        .box2dRadius(4)
        .damage(35)
        .bulletDeflection(1)
        .currentLockCounter(0)
        .build();
  }

  public static Weapon shotgunWeapon(float zoom, float frameDuration) {
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
        .clipSize(8)
        .fireCount(0)
        .reloadDuration(0.7f)
        .reloadCounter(0)
        .damage(20)
        .bulletTexture(GDXUtils.resizeTexture((Texture) LoaderManager.getInstance().get("Bullet1.png"), 2 / MAIN_ZOOM))
        .box2dRadius(2)
        .shotInOneBullet(10)
        .bulletDeflection(2)
        .currentLockCounter(0)
        .build();
  }
}
