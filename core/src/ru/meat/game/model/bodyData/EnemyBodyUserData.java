package ru.meat.game.model.bodyData;

import lombok.Data;

@Data
public class EnemyBodyUserData extends BodyUserData{

  private boolean needAttack;

  public EnemyBodyUserData(String name, int damage, boolean needAttack) {
    super(name, damage);
    this.needAttack = needAttack;
  }
}
