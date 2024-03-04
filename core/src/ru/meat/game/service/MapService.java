package ru.meat.game.service;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import lombok.Data;
import ru.meat.game.loader.LoaderManager;
import ru.meat.game.model.FloatPair;
import ru.meat.game.model.maps.Map;
import ru.meat.game.model.maps.Maps;

@Data
public class MapService {

  private static Texture map;

  private static Texture getMap(){
    if (map == null){
      Maps mapInfo = Maps.getNameByPos(1);
      map = LoaderManager.getInstance().get(mapInfo.getName());
      map.setFilter(TextureFilter.MipMapLinearLinear, TextureFilter.MipMapLinearLinear);
    }
    return map;
  }

  public static Map initMap(int mapPos) {
    Maps mapInfo = Maps.getNameByPos(mapPos);
    return new Map(FloatPair.create(0f, 0f), getMap());
  }

  public static Map initMap(float x, float y) {
    return new Map(FloatPair.create(x, y), getMap());
  }
}
