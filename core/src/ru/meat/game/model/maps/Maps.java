package ru.meat.game.model.maps;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Maps {
  FIRST_MAP("dirtMap2.png", 1, 1f);

  private final String name;
  private final int position;
  private final float scale;

  public static Maps getNameByPos(int position){
    return Arrays.stream(Maps.values()).filter(x -> x.getPosition() == position).findFirst().orElse(FIRST_MAP);
  }
}
