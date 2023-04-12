package ru.meat.game.model.maps;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.meat.game.model.FloatPair;

@Data
@NoArgsConstructor
public class Map extends Image {

  private int mapPos;

  public Map(FloatPair coords, Texture texture) {
    super(texture);
    texture.setFilter(TextureFilter.MipMapLinearLinear, TextureFilter.MipMapLinearLinear);
//    this.setSize(0.3f,0.3f);
    this.setPosition(coords.getX(), coords.getY());
    this.setOrigin(this.getWidth() / 2, this.getHeight() / 2);
  }

  @Override
  public void draw(Batch batch, float parentAlpha) {
    super.draw(batch, parentAlpha);
  }

  @Override
  public void act(float delta) {
    super.act(delta);
  }
}
