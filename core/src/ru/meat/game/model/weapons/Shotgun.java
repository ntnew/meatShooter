package ru.meat.game.model.weapons;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.utils.TimeUtils;
import lombok.AllArgsConstructor;
import lombok.experimental.SuperBuilder;
import ru.meat.game.model.weapons.Rifle.MMM;
import ru.meat.game.service.AudioService;
import ru.meat.game.service.BulletService;
import ru.meat.game.service.RpgStatsService;

@SuperBuilder
public class Shotgun extends Weapon {

  private int fullClip;

  private volatile boolean wantShoot = false;

  private int shotInOneBullet;


  @Override
  protected void implementShoot(float fromX, float fromY, float screenX, float screenY, boolean playerRunning) {
    wantShoot = true;
    float deflection = bulletDeflection * (playerRunning ? 2 : 1);
    for (int i = 0; i < shotInOneBullet; i++) {
      BulletService.getInstance()
          .createBullet(fromX, fromY, MathUtils.random(screenX - deflection, screenX + deflection),
              MathUtils.random(screenY - deflection, screenY + deflection), speed, damage, bulletTexture, box2dRadius);
    }
  }

  @Override
  protected void implementReload() {
    if (reloadCounter == 0) {

      new MMM().start();
    }
  }

  class MMM extends Thread {
    @Override
    public void run() {
      for (int i = 0; i < clipSize; i++) {
        AudioService.getInstance().playSound(reloadSound);
        reloadCounter = TimeUtils.millis();
        while (true) {
          if (TimeUtils.timeSinceMillis(reloadCounter) > reloadDuration * 1000L
              / RpgStatsService.getInstance().getStats().getReloadSpeed()) {
            fireCount -= 1;
            reloadCounter = 0;
            break;
          }
        }
      }
      reloading = false;
    }
  }
}
