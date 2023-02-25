package ru.meat.game.model.weapons;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
public class BulletBodyUserData {

  private UUID id;
  private String name;
  private float damage;
  private boolean isNeedDispose;
  private BulletType type;
}
