package ru.meat.game.model.weapons;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.TimeUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import ru.meat.game.service.AudioService;
import ru.meat.game.service.BulletService;
import ru.meat.game.service.RpgStatsService;
import ru.meat.game.utils.GDXUtils;

@Data
@AllArgsConstructor
@Builder
public class Weapon {

  private WeaponEnum name;
  /**
   * текстура пули
   */
  protected Texture bulletTexture;


  /**
   * Скорость полёта пули
   */
  protected float speed;

  private Animation<Texture> idleAnimation;
  private Animation<Texture> moveAnimation;
  private Animation<Texture> meleeAttackAnimation;
  private Animation<Texture> shootAnimation;
  private Animation<Texture> reloadAnimation;

  protected String shootSound;
  protected String reloadSound;

  private long currentLockCounter;
  /**
   * Скорострельность, сколько длится один выстрел, в миллисекундах
   */
  private long fireRate;

  /**
   * урон 1 пули
   */
  protected int damage;

  /**
   * размер магазина
   */
  protected int clipSize;

  /**
   * число сделанных выстрелов
   */
  protected int fireCount;

  /**
   * сколько длится перезарядка в секундах
   */
  protected float reloadDuration;

  protected long reloadCounter;

  /**
   * разброс пули +- от точки куда выстрелил
   */
  protected float bulletDeflection;

  /**
   * радиус пули
   */
  protected float box2dRadius;

  private volatile boolean wantShoot = false;

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
  private boolean reloading = false;

  /**
   * множитель скорости передвижения
   */
  private float moveSpeedMultiplier = 1;

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

      //найти  новую точку на экране с новоой гипотенузой
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

      wantShoot = true;
      float deflection = bulletDeflection * (playerRunning ? 2 : 1);
      for (int i = 0; i < shotInOneBullet; i++) {
        BulletService.getInstance()
            .createBullet(fromX, fromY, MathUtils.random(screenX - deflection, screenX + deflection),
                MathUtils.random(screenY - deflection, screenY + deflection), speed, damage, bulletTexture,
                box2dRadius);
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

    @Override
    public void run() {
      while (fireCount > 0) {
        AudioService.getInstance().playSound(reloadSound);
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
    }
  }
}
