package ru.meat.game.model.bodyData;

import lombok.Data;

/**
 * Клас, который кладётся в body box2d для передачи информации контактов
 */
@Data
public class EnemyBodyUserData extends BodyUserData{

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

  public EnemyBodyUserData(String name, int damage, boolean needAttack, int attack, Double attackSpeed) {
    super(name, damage);
    this.needAttack = needAttack;
    this.attack = attack;
    this.attackSpeed = attackSpeed;
  }
}
