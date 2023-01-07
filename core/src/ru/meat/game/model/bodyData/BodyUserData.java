package ru.meat.game.model.bodyData;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * Клас, который кладётся в body box2d для передачи информации контактов
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BodyUserData {

  private String name;
  private float damage;
}
