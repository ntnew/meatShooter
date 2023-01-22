package ru.meat.game.model.weapons;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor

public enum WeaponEnum {
  PISTOL(1),
  RIFLE(2),
  SHOTGUN(3);

  @Getter
  private final int pos;


  public static WeaponEnum getByPos(int pos){
    return Arrays.stream(WeaponEnum.values()).filter(x -> x.getPos() == pos).findFirst().orElse(PISTOL);
  }
}
