package ru.meat.game.model.enemies;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.Data;
import ru.meat.game.model.bodyData.BodyUserData;

/**
 * Клас, который кладётся в body box2d для передачи информации контактов
 */
@Data
public class EnemyBodyUserData extends BodyUserData {

  /**
   * Нужно ли атаковать игрока
   */
  private boolean needAttack;
  /**
   * Урон, наносимый врагом
   */
  private int attack;
  /**
   * время предыдущей атаки
   */
  private Long previousAttackTime;

  /**
   * скорость атаки
   */
  private Double attackSpeed;

  /**
   * Идентификаторы пуль, скоторыми уже проконтактировало тело и получило урон
   */
  private List<UUID> idContactedBullets;

  public EnemyBodyUserData(String name, int damage, boolean needAttack, int attack, Double attackSpeed) {
    super(name, damage);
    this.needAttack = needAttack;
    this.attack = attack;
    this.attackSpeed = attackSpeed;
    this.idContactedBullets = new ArrayList<>();
  }
}
