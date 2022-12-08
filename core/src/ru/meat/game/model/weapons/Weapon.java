package ru.meat.game.model.weapons;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.meat.game.model.FloatPair;

@Data
@AllArgsConstructor
@Builder
public class Weapon {

  private Texture bulletTexture;

  private WeaponEnum name;

  private FloatPair speed;

  private Animation<Texture> idleAnimation;
  private Animation<Texture> moveAnimation;
  private Animation<Texture> meleeAttackAnimation;
  private Animation<Texture> shootAnimation;
  private Animation<Texture> reloadAnimation;
}
