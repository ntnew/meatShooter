package ru.meat.game.model.weapons.explosions;


import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import lombok.Data;
import ru.meat.game.model.FloatPair;

@Data
public class Explosion extends Image {

  /**
   * Позиция в мире бокс2д
   */
  protected FloatPair pos;

  /**
   * угол анимации
   */
  protected float angle;

  /**
   * Время создания взрыва
   */
  protected Long bornDate;

  /**
   * Множитель размера текстуры
   */
  protected float scale;

  /**
   * Время анимации
   */
  protected float stateTime;

  protected Animation animation;

  public Explosion(FloatPair pos, float angle, Long bornDate, float scale, Animation<TextureRegion> animation) {
    super(animation.getKeyFrame(0));
    this.pos = pos;
    this.angle = angle;
    this.bornDate = bornDate;
    this.scale = scale;

    setPosition(pos.getX() - this.getDrawable().getMinWidth() / 2, pos.getY() - this.getDrawable().getMinHeight() / 2);
    scaleBy(scale);
    setOrigin(this.getDrawable().getMinWidth() / 2, this.getDrawable().getMinHeight() / 2);
    setRotation(angle);
  }
}
