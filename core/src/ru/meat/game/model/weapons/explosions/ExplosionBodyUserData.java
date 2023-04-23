package ru.meat.game.model.weapons.explosions;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.meat.game.model.bodyData.BodyUserData;

@Data
public class ExplosionBodyUserData extends BodyUserData {

  private UUID id;
  /**
   * Время создания взрыва
   */
  private Long bornDate;

  public ExplosionBodyUserData(String name, float damage, UUID id, Long bornDate) {
    super(name, damage);
    this.id = id;
    this.bornDate = bornDate;
  }
}
