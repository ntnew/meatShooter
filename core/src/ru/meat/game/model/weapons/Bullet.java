package ru.meat.game.model.weapons;


import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import lombok.Data;

@Data
public class Bullet extends Image {

  private Body body;

  private float speedMultiply;

  private Sprite sprite;

  private Long bornDate;

  private float stateTime;


}
