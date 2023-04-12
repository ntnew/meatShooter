package ru.meat.game.service;

import lombok.Data;
import ru.meat.game.loader.LoaderManager;
import ru.meat.game.model.FloatPair;
import ru.meat.game.model.maps.Map;
import ru.meat.game.model.maps.Maps;

@Data
public class MapService {

  public static Map initMap(int mapPos) {
    Maps mapInfo = Maps.getNameByPos(mapPos);
    return new Map(FloatPair.create(0f, 0f), LoaderManager.getInstance().get(mapInfo.getName()));
  }
}
