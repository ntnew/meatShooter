package ru.meat.game.model.weapons;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.utils.TimeUtils;
import lombok.AllArgsConstructor;
import lombok.experimental.SuperBuilder;
import ru.meat.game.service.AudioService;
import ru.meat.game.service.BulletService;
import ru.meat.game.service.RpgStatsService;

@SuperBuilder
public class Shotgun extends Weapon {

  private int fullClip;

  private volatile boolean wantShoot = false;

  private int shotInOneBullet;

  private boolean reloading;


  @Override
  protected void implementShoot(float fromX, float fromY, float screenX, float screenY, boolean playerRunning) {
    wantShoot = true;
    float deflection = bulletDeflection * (playerRunning ? 2 : 1);
    for (int i = 0; i < shotInOneBullet; i++) {
      BulletService.getInstance()
          .createBullet(fromX, fromY, MathUtils.random(screenX - deflection, screenX + deflection),
              MathUtils.random(screenY - deflection, screenY + deflection), speed, damage);
    }
  }

  @Override
  protected void implementReload() {
    if (!reloading) {
      reloading = true;
      new MMM().start();
    }
  }

  class MMM extends Thread {

    @Override
    public void run() {
      for (; fireCount > 0; ) {
        fireCount = fireCount -1 ;
        System.out.println(fireCount);
        if (reloadCounter == 0) {
          reloadCounter = TimeUtils.millis();
          AudioService.getInstance().playSound(reloadSound);
          while (TimeUtils.timeSinceMillis(reloadCounter)
              < reloadDuration * 1000L / RpgStatsService.getInstance().getStats().getReloadSpeed()) {

          }
          reloadCounter = 0;
        }
      }
      reloading = false;
    }
  }
}
