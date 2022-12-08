package ru.meat.game.model;

import com.badlogic.gdx.graphics.Texture;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Map {

  private FloatPair pos;

  private Texture mainTexture;

}
