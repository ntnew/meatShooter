package ru.meat.game.model.weapons;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.TimeUtils;
import lombok.Builder;
import lombok.experimental.SuperBuilder;
import ru.meat.game.service.AudioService;
import ru.meat.game.service.BulletService;
import ru.meat.game.service.RpgStatsService;

@SuperBuilder
public class Rifle extends Weapon {

  @Override
  protected void implementShoot(float fromX, float fromY, float screenX, float screenY, boolean playerRunning) {
    float deflection = bulletDeflection * (playerRunning ? 2 : 1);
    BulletService.getInstance().createBullet(fromX, fromY, MathUtils.random(screenX - deflection, screenX + deflection),
        MathUtils.random(screenY - deflection, screenY + deflection), speed, damage);
  }

  @Override
  protected void implementReload() {
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
}
