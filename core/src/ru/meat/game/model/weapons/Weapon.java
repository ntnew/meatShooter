package ru.meat.game.model.weapons;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.TimeUtils;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.meat.game.interfaces.Shootable;
import ru.meat.game.service.AudioService;
import ru.meat.game.service.BulletService;

@Data
@AllArgsConstructor
@Builder
public class Weapon implements Shootable {

  private final BulletService bulletService;

  private final AudioService audioService;

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
  private int reloadDuration;

  private long reloadCounter;

  @Override
  public void shoot(float fromX, float fromY, float screenX, float screenY) {
    if (fireCount < clipSize) {
      fireCount += 1;
      AudioService.getInstance().playSound(shootSound);
      bulletService.createBullet(fromX, fromY, screenX, screenY, speed, damage);
    } else {
      if (reloadCounter == 0) {
        reloadCounter = TimeUtils.millis();
        AudioService.getInstance().playSound(reloadSound);
      }
      if (TimeUtils.timeSinceMillis(reloadCounter) > reloadDuration*1000L){
        fireCount = 0;
        reloadCounter = 0;
      }
    }
  }

  @Override
  public void updateState() {
    bulletService.updateBullets();
  }
}
