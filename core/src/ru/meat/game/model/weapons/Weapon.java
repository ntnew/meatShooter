package ru.meat.game.model.weapons;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.TimeUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import ru.meat.game.service.AudioService;
import ru.meat.game.service.BulletService;
import ru.meat.game.service.RpgStatsService;

@Data
@AllArgsConstructor
@SuperBuilder
public abstract class Weapon {

  private Texture bulletTexture;

  private WeaponEnum name;

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

  protected int damage;

  protected int clipSize;

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
   * Сделать выстрел
   * @param fromX из координаты Х
   * @param fromY из координаты У
   * @param screenX в точку на экране Х
   * @param screenY в точку на экране У
   * @param playerRunning флаг двигается ли игрок
   */
  public void shoot(float fromX, float fromY, float screenX, float screenY, boolean playerRunning) {
    if (fireCount < clipSize) {
      fireCount += 1;
      AudioService.getInstance().playSound(shootSound);
      implementShoot(fromX, fromY, screenX, screenY, playerRunning);
    } else {
      implementReload();
    }
  }

  protected abstract void implementShoot(float fromX, float fromY, float screenX, float screenY, boolean playerRunning);

  /**
   * произвести перезарядку оружия
   */
  protected abstract void implementReload();
}
