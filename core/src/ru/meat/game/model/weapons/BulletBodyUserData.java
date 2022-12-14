package ru.meat.game.model.weapons;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
public class BulletBodyUserData {

  private String name;
  private int damage;
  private boolean isNeedDispose;

  public BulletBodyUserData(String name, int damage) {
    this.name = name;
    this.damage = damage;
    this.isNeedDispose = false;
  }
}
