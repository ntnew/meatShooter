package ru.meat.game.model.weapons;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
public class BulletBodyUserData {

  private String name;
  private float damage;
  private boolean isNeedDispose;

  public BulletBodyUserData(String name, float damage) {
    this.name = name;
    this.damage = damage;
    this.isNeedDispose = false;
  }
}
