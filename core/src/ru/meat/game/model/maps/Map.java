package ru.meat.game.model.maps;

import com.badlogic.gdx.graphics.Texture;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import ru.meat.game.model.FloatPair;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Map {

  private FloatPair pos;

  private Texture mainTexture;

}
