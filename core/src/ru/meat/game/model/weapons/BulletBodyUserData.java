package ru.meat.game.model.weapons;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BulletBodyUserData {

  private String name;
  private int damage;
}
