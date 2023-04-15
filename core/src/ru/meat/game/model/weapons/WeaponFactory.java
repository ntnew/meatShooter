package ru.meat.game.model.weapons;

import static ru.meat.game.settings.Constants.MAIN_ZOOM;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import lombok.Data;
import ru.meat.game.loader.LoaderManager;

@Data
public class WeaponFactory {

  private final static float pistolBulletSpeed = 12f;
  private final static float grenadeSpeed = 0.7f;

  private final static String assetsDir = "./assets/Top_Down_survivor/";

  public static Weapon rifleWeapon() {
    return Weapon.builder()
        .name(WeaponEnum.RIFLE)
        .speed(pistolBulletSpeed)
        .bulletType(BulletType.COMMON)
        .fireRate(100)
        .shootSound("ak47.mp3")
        .reloadSound("ak47reload.mp3")
        .clipSize(30)
        .reloadBulletPerTick(30)
        .fireCount(0)
        .reloadDuration(3)
        .shotInOneBullet(1)
        .bulletTexture(LoaderManager.getInstance().get("Bullet1.png"))
        .textureScale(16/MAIN_ZOOM)
        .box2dRadius(16/MAIN_ZOOM)
        .damage(35)
        .bulletDeflection(1)
        .currentLockCounter(0)
        .moveSpeedMultiplier(1f)
        .reloading(false)
        .build();
  }

  public static Weapon shotgunWeapon() {
    return Weapon.builder()
        .name(WeaponEnum.SHOTGUN)
        .speed(pistolBulletSpeed)
        .bulletType(BulletType.COMMON)
        .fireRate(800)
        .shootSound("shotgun.mp3")
        .reloadSound("shotgunReload.mp3")
        .clipSize(8)
        .reloadBulletPerTick(1)
        .fireCount(0)
        .reloadDuration(0.7f)
        .damage(20)
        .bulletTexture(LoaderManager.getInstance().get("Bullet1.png"))
        .textureScale(11f/MAIN_ZOOM)
        .box2dRadius(8/MAIN_ZOOM)
        .shotInOneBullet(10)
        .bulletDeflection(2)
        .currentLockCounter(0)
        .moveSpeedMultiplier(1f)
        .reloading(false)
        .build();
  }

  public static Weapon doubleBarrelShotgunWeapon() {
    return Weapon.builder()
        .name(WeaponEnum.DOUBLE_BARREL)
        .speed(pistolBulletSpeed)
        .bulletType(BulletType.COMMON)
        .fireRate(300)
        .shootSound("2barrelShot.mp3")
        .reloadSound("2barrelReload.mp3")
        .clipSize(2)
        .fireCount(0)
        .reloadDuration(1.2f)
        .damage(25)
        .bulletTexture(LoaderManager.getInstance().get("Bullet1.png"))
        .textureScale(11f/MAIN_ZOOM)
        .box2dRadius(8/MAIN_ZOOM)
        .shotInOneBullet(12)
        .reloadBulletPerTick(2)
        .bulletDeflection(2)
        .currentLockCounter(0)
        .moveSpeedMultiplier(1f)
        .reloading(false)
        .build();
  }

  public static Weapon machineGun() {
    return Weapon.builder()
        .name(WeaponEnum.MACHINE_GUN)
        .speed(pistolBulletSpeed)
        .bulletType(BulletType.COMMON)
        .fireRate(90)
        .shootSound("m249.mp3")
        .reloadSound("m249reload.mp3")
        .clipSize(150)
        .reloadBulletPerTick(150)
        .fireCount(0)
        .reloadDuration(5)
        .bulletTexture(LoaderManager.getInstance().get("Bullet1.png"))
        .textureScale(16/MAIN_ZOOM)
        .box2dRadius(16/MAIN_ZOOM)
        .damage(35)
        .shotInOneBullet(1)
        .bulletDeflection(2)
        .currentLockCounter(0)
        .moveSpeedMultiplier(0.7f)
        .reloading(false)
        .build();
  }

  public static Weapon m79() {
    return Weapon.builder()
        .name(WeaponEnum.M79)
        .speed(grenadeSpeed)
        .bulletType(BulletType.EXPLOSIVE)
        .fireRate(100)
        .shootSound("m79shoot.mp3")
        .preReloadSound("m79open.mp3")
        .preReloadDuration(200L)
        .reloadSound("m79reload.mp3")
        .clipSize(1)
        .reloadBulletPerTick(1)
        .fireCount(0)
        .reloadDuration(1)
        .bulletTexture(LoaderManager.getInstance().get("GBullet.png"))
        .textureScale(1f/MAIN_ZOOM)
        .box2dRadius(32/MAIN_ZOOM)
        .damage(150)
        .shotInOneBullet(1)
        .bulletDeflection(2)
        .currentLockCounter(0)
        .moveSpeedMultiplier(1f)
        .reloading(false)
        .build();
  }

  public static Weapon m32() {
    return Weapon.builder()
        .name(WeaponEnum.M32)
        .speed(grenadeSpeed)
        .bulletType(BulletType.EXPLOSIVE)
        .fireRate(500)
        .shootSound("m79shoot.mp3")
        .preReloadSound("m32open.mp3")
        .preReloadDuration(200L)
        .reloadSound("m32reload.mp3")
        .postReloadSound("m32close.mp3")
        .clipSize(6)
        .reloadBulletPerTick(1)
        .fireCount(0)
        .reloadDuration(0.5f)
        .bulletTexture(LoaderManager.getInstance().get("GBullet.png"))
        .textureScale(1f/MAIN_ZOOM)
        .box2dRadius(32/MAIN_ZOOM)
        .damage(150)
        .shotInOneBullet(1)
        .bulletDeflection(2)
        .currentLockCounter(0)
        .moveSpeedMultiplier(0.9f)
        .reloading(false)
        .build();
  }

  public static Weapon aa12() {
    return Weapon.builder()
        .name(WeaponEnum.AA12)
        .speed(pistolBulletSpeed)
        .bulletType(BulletType.COMMON)
        .fireRate(300)
        .shootSound("aa12shoot.mp3")
        .preReloadSound("aa12open.mp3")
        .preReloadDuration(200L)
        .reloadSound("aa12reload.mp3")
        .postReloadSound("aa12close.mp3")
        .clipSize(20)
        .fireCount(0)
        .reloadDuration(1.8f)
        .damage(25)
        .bulletTexture(LoaderManager.getInstance().get("Bullet1.png"))
        .textureScale(11f/MAIN_ZOOM)
        .box2dRadius(8/MAIN_ZOOM)
        .shotInOneBullet(12)
        .reloadBulletPerTick(20)
        .bulletDeflection(2)
        .currentLockCounter(0)
        .moveSpeedMultiplier(0.9f)
        .reloading(false)
        .build();
  }
}
