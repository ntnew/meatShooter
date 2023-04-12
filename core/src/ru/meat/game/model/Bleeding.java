package ru.meat.game.model;

import com.badlogic.gdx.graphics.g2d.Sprite;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Bleeding {

  private Sprite sprite;

  private float stateTime;
}
