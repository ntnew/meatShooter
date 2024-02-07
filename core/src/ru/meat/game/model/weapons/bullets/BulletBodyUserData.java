package ru.meat.game.model.weapons.bullets;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.meat.game.model.weapons.BulletType;

@Data
@AllArgsConstructor
public class BulletBodyUserData {

  private UUID id;
  private String name;
  private float damage;
  private boolean isNeedDispose;
  private BulletType type;
}
