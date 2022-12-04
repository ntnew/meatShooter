package ru.meat.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import lombok.Data;
import ru.meat.game.model.Player;
@Data
public class MyWorld {

  World world;
  //персонаж
  Player player;


  public MyWorld(){
    //вектор указывает направление силы, действующей в мире (гравитация)
    world = new World(new Vector2(0, 0), true);
    createWorld();
  }

  private void createWorld(){

  }
}
