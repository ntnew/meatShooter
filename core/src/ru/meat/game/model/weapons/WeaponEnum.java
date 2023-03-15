package ru.meat.game.model.weapons;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor

public enum WeaponEnum {
  PISTOL(1,"shotgun"),
  RIFLE(2,"rifle"),
  SHOTGUN(3, "shotgun"),
  DOUBLE_BARREL(4,"double_barrel"),
  MACHINE_GUN(5, "machine_gun"),

  //GrenadeLaunchers
  M79(6, "m79"),
  M32(7,"m32"),

  AA12(8,"shotgun_new");

  @Getter
  private final int pos;

  @Getter
  private final String aniTag;


  public static WeaponEnum getByPos(int pos) {
    return Arrays.stream(WeaponEnum.values()).filter(x -> x.getPos() == pos).findFirst().orElse(PISTOL);
  }
}
