package ru.meat.game.model.weapons;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import java.util.concurrent.atomic.AtomicBoolean;
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

  private float speed;

  private Animation<Texture> idleAnimation;
  private Animation<Texture> moveAnimation;
  private Animation<Texture> meleeAttackAnimation;
  private Animation<Texture> shootAnimation;
  private Animation<Texture> reloadAnimation;
  private String shootSound;

  private long currentLockCounter;
  /**
   * Скорострельность, сколько длится один выстрел, в миллисекундах
   */
  private long fireRate;

}
