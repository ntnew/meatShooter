package ru.meat.game.model.weapons;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.TimeUtils;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.meat.game.interfaces.Shootable;
import ru.meat.game.service.AudioService;
import ru.meat.game.service.BulletService;
import ru.meat.game.service.RpgStatsService;

@Data
@AllArgsConstructor
@Builder
public class Weapon implements Shootable {

  private final BulletService bulletService;

  private Texture bulletTexture;

  private WeaponEnum name;

  private float speed;

  private Animation<Texture> idleAnimation;
  private Animation<Texture> moveAnimation;
  private Animation<Texture> meleeAttackAnimation;
  private Animation<Texture> shootAnimation;
  private Animation<Texture> reloadAnimation;
  private String shootSound;
  private String reloadSound;

  private long currentLockCounter;
  /**
   * Скорострельность, сколько длится один выстрел, в миллисекундах
   */
  private long fireRate;

  private int damage;

  private int clipSize;

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

  @Override
  public void shoot(float fromX, float fromY, float screenX, float screenY, boolean playerRunning) {
    if (fireCount < clipSize) {
      fireCount += 1;
      float deflection = bulletDeflection * (playerRunning ? 2 : 1);
      AudioService.getInstance().playSound(shootSound);
      bulletService.createBullet(fromX, fromY, MathUtils.random(screenX - deflection, screenX + deflection),
          MathUtils.random(screenY - deflection, screenY + deflection), speed, damage);
    } else {
      reload();
    }
  }

  /**
   * произвести перезарядку оружия
   */
  private void reload() {
    if (reloadCounter == 0) {
      reloadCounter = TimeUtils.millis();
      AudioService.getInstance().playSound(reloadSound);
    }
    if (TimeUtils.timeSinceMillis(reloadCounter) > reloadDuration * 1000L
        / RpgStatsService.getInstance().getStats().getReloadSpeed()) {
      fireCount = 0;
      reloadCounter = 0;
    }
  }

  @Override
  public void updateState() {
    bulletService.updateBullets();
  }
}
