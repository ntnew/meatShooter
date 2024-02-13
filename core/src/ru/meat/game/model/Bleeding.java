package ru.meat.game.model;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.TimeUtils;
import java.util.Objects;
import lombok.Data;
import lombok.SneakyThrows;
import ru.meat.game.service.BloodService;

@Data
public class Bleeding extends Image {

  private float stateTime = 0;

  private Long bornDate = TimeUtils.millis();

  private boolean needDispose = false;
  private boolean transparencyStarted = false;

  public Bleeding(Animation<TextureRegion> animation, FloatPair coord) {
    super(animation.getKeyFrame(0));
    this.setPosition(coord.getX() - this.getDrawable().getMinWidth() / 2,
        coord.getY() - this.getDrawable().getMinHeight() / 2);
    this.scaleBy(2f);
    this.setOrigin(this.getDrawable().getMinWidth() / 2, this.getDrawable().getMinHeight() / 2);
    this.setRotation(MathUtils.random(0, 359));
  }

  @Override
  public void act(float delta) {
    if (needDispose || TimeUtils.timeSinceMillis(getBornDate()) > 6000) {
      this.remove();
    }
    if (Objects.equals(((TextureRegionDrawable) getDrawable()).getRegion().getTexture(),
        BloodService.getInstance().getBleedAnimation()
            .getKeyFrames()[BloodService.getInstance().getBleedAnimation().getKeyFrames().length - 1].getTexture())
        && !transparencyStarted) {
      transparencyStarted = true;
      new ThreadForTransparency().start();
    }

    ((TextureRegionDrawable) getDrawable()).setRegion(
        new TextureRegion(BloodService.getInstance().getBleedAnimation().getKeyFrame(stateTime += delta, false)));
    super.act(delta);
  }

  class ThreadForTransparency extends Thread {


    ThreadForTransparency() {
      setDaemon(true);
    }

    @SneakyThrows
    @Override
    public void run() {
      while (getColor().a > 0.05f) {
        getColor().a = getColor().a - 0.05f;
        Thread.sleep(10);
      }
      needDispose = true;
    }
  }
}
