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
  private BulletType type;
}
