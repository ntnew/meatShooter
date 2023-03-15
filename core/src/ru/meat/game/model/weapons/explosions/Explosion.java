package ru.meat.game.model.weapons.explosions;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.meat.game.model.FloatPair;

@Data
@AllArgsConstructor
public class Explosion {

  /**
   * Позиция в мире бокс2д
   */
  private FloatPair pos;

  /**
   * Время анимации
   */
  private float stateTime;

  /**
   * угол анимации
   */
  private float angle;

  private ExplosionType type;

  /**
   * Время создания взрыва
   */
  private Long bornDate;

  private float scale;
}
