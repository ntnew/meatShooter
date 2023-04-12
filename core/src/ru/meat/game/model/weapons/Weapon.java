package ru.meat.game.model.weapons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.SneakyThrows;
import ru.meat.game.service.AudioService;
import ru.meat.game.service.BulletService;
import ru.meat.game.service.RpgStatsService;
import ru.meat.game.utils.GDXUtils;

@Data
@AllArgsConstructor
@Builder
public class Weapon {

  private WeaponEnum name;

  private BulletType bulletType;
  /**
   * текстура пули
   */
  private Texture bulletTexture;
  /**
   * размер текстуры пули
   */
  private float textureScale;


  /**
   * Скорость полёта пули
   */
  private float speed;

  private String shootSound;
  private String reloadSound;
  private String preReloadSound;
  private Long preReloadDuration;
  private String postReloadSound;

  private long currentLockCounter;
  /**
   * Скорострельность, сколько длится один выстрел, в миллисекундах
   */
  private long fireRate;

  /**
   * урон 1 пули
   */
  private int damage;

  /**
   * размер магазина
   */
  private int clipSize;

  /**
   * число сделанных выстрелов
   */
  private int fireCount;

  /**
   * сколько длится перезарядка в секундах
   */
  private float reloadDuration;

  /**
   * разброс пули +- от точки куда выстрелил
   */
  private float bulletDeflection;

  /**
   * радиус пули
   */
  private float box2dRadius;

  /**
   * Пуль в одном выстреле
   */
  private int shotInOneBullet;
  /**
   * Перезаряжает пуль за раз
   */
  private int reloadBulletPerTick;

  /**
   * Флаг, идёт ли перезарядка
   */
  private volatile boolean reloading;

  /**
   * множитель скорости передвижения
   */
  private float moveSpeedMultiplier;

  /**
   * Сделать выстрел
   *
   * @param fromX         из координаты Х
   * @param fromY         из координаты У
   * @param screenX       в точку на экране Х
   * @param screenY       в точку на экране У
   * @param playerRunning флаг двигается ли игрок
   */
  public void shoot(float fromX, float fromY, float screenX, float screenY, boolean playerRunning) {
    if (!reloading && fireCount < clipSize) {

      fireCount += 1;
      AudioService.getInstance().playShootSound(shootSound);

      //найти  новую точку на экране с новоой гипотенузой для одинаковости выстрелов
      float catetPrilezjaschiy = (screenX - fromX);
      float newGip = 20;
      float catetProtivo = (screenY - fromY);
      float gip = GDXUtils.calcGipotenuza(catetPrilezjaschiy, catetProtivo);
      float sin = catetProtivo / gip;
      float cos = catetPrilezjaschiy / gip;
      float newCatetForY = sin * newGip;
      float newCatetForX = cos * newGip;

      createShoot(fromX, fromY, fromX + newCatetForX, fromY + newCatetForY, playerRunning);
    } else if (!reloading) {
      reloading = true;
      new ThreadForReload().start();
    }
  }

  private void createShoot(float fromX, float fromY, float screenX, float screenY, boolean playerRunning) {
    float deflection = bulletDeflection * (playerRunning ? 2 : 1);
    for (int i = 0; i < shotInOneBullet; i++) {
      BulletService.getInstance()
          .createBullet(fromX, fromY,
              MathUtils.random(screenX - deflection, screenX + deflection),
              MathUtils.random(screenY - deflection, screenY + deflection),
              speed, damage, bulletTexture,
              box2dRadius, bulletType, textureScale);
    }
  }

  /**
   * Сделать выстрел для мобилки
   *
   * @param fromX         из координаты Х
   * @param fromY         из координаты У
   * @param screenX       в точку на экране Х
   * @param screenY       в точку на экране У
   * @param playerRunning флаг двигается ли игрок
   */
  public void shootMobile(float fromX, float fromY, float screenX, float screenY, boolean playerRunning) {
    if (!reloading && fireCount < clipSize) {
      fireCount += 1;
      Gdx.app.postRunnable(() -> AudioService.getInstance().playShootSound(shootSound));
      createShoot(fromX, fromY, screenX, screenY, playerRunning);
    } else if (!reloading) {
      //произвести перезарядку оружия
      reloading = true;
      new ThreadForReload().start();
    }
  }

  class ThreadForReload extends Thread {

    @SneakyThrows
    @Override
    public void run() {
      if (preReloadSound != null) {
        Gdx.app.postRunnable(() -> AudioService.getInstance().playReloadSound(preReloadSound));
        Thread.sleep(preReloadDuration);
      }

      int sleepDur = (int) Math.ceil(
          reloadDuration * 1000L / RpgStatsService.getInstance().getStats().getReloadSpeed());

      while (fireCount > 0) {
        Gdx.app.postRunnable(() -> AudioService.getInstance().playReloadSound(reloadSound));
        Thread.sleep(sleepDur);
        fireCount -= reloadBulletPerTick;
      }

      fireCount = Math.max(fireCount, 0);
      if (postReloadSound != null) {
        Gdx.app.postRunnable(() -> AudioService.getInstance().playReloadSound(postReloadSound));
        Thread.sleep(50);
      }
      reloading = false;
    }
  }
}
