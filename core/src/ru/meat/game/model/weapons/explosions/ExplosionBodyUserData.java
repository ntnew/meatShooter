package ru.meat.game.model.weapons.explosions;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.meat.game.model.bodyData.BodyUserData;

@Data
@AllArgsConstructor
public class ExplosionBodyUserData extends BodyUserData {

  private UUID id;
  /**
   * Время создания взрыва
   */
  private Long bornDate;

}
