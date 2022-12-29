package ru.meat.game.service;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import lombok.Data;
import ru.meat.game.model.Map;
import ru.meat.game.model.FloatPair;
import ru.meat.game.utils.GDXUtils;

@Data
public class MapService {

  private final String firstMap = "dirtMap2.png";

  private Map currentMap;


  public void initMap(){
    currentMap = new Map(FloatPair.create(0f,0f), GDXUtils.resizeTexture(Gdx.files.internal("./assets/" + firstMap),1f));
  }

  public void draw(Batch batch){
    batch.draw(currentMap.getMainTexture(), currentMap.getPos().getX(), currentMap.getPos().getY());
  }
}
