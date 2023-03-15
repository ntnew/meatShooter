package ru.meat.game.model.weapons;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import lombok.Data;

@Data
public class Bullet {

  private Body body;

  private float speedMultiply;

  private Sprite sprite;

  private Long bornDate;

  private float stateTime;
}
