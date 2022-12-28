package ru.meat.game.model.bodyData;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BodyUserData {

  private String name;
  private int damage;

}
