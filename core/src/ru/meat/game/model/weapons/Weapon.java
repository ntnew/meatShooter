package ru.meat.game.model.weapons;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.TimeUtils;
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

  private Animation<Texture> idleAnimation;
  private Animation<Texture> moveAnimation;
  private Animation<Texture> meleeAttackAnimation;
  private Animation<Texture> shootAnimation;
  private Animation<Texture> reloadAnimation;

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

  private long reloadCounter;

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
  private boolean reloading;

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

      screenX = fromX + newCatetForX;
      screenY = fromY + newCatetForY;

      float deflection = bulletDeflection * (playerRunning ? 2 : 1);
      for (int i = 0; i < shotInOneBullet; i++) {
        BulletService.getInstance()
            .createBullet(fromX, fromY,
                MathUtils.random(screenX - deflection, screenX + deflection),
                MathUtils.random(screenY - deflection, screenY + deflection),
                speed, damage, bulletTexture,
                box2dRadius, bulletType, textureScale);
      }

    } else if (!reloading) {
      reloading = true;
      implementReload();
    }
  }

  /**
   * произвести перезарядку оружия
   */
  private void implementReload() {
    if (reloadCounter == 0) {
      new ThreadForReload().start();
    }
  }

  class ThreadForReload extends Thread {

    @SneakyThrows
    @Override
    public void run() {
      if (preReloadSound != null) {
        AudioService.getInstance().playReloadSound(preReloadSound);
        Thread.sleep(preReloadDuration);
      }
      while (fireCount > 0) {
        AudioService.getInstance().playReloadSound(reloadSound);
        reloadCounter = TimeUtils.millis();
        while (true) {
          if (TimeUtils.timeSinceMillis(reloadCounter) > reloadDuration * 1000L
              / RpgStatsService.getInstance().getStats().getReloadSpeed()) {
            fireCount -= reloadBulletPerTick;
            reloadCounter = 0;
            break;
          }
        }
      }
      reloading = false;
      fireCount = Math.max(fireCount, 0);
      if (postReloadSound != null) {
        AudioService.getInstance().playReloadSound(postReloadSound);
      }
    }
  }
}
