package ru.meat.game.service;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import lombok.Data;
import ru.meat.game.loader.LoaderManager;
import ru.meat.game.model.maps.Map;
import ru.meat.game.model.FloatPair;
import ru.meat.game.model.maps.Maps;
import ru.meat.game.utils.GDXUtils;

@Data
public class MapService {

  private Map currentMap;

  private SpriteBatch batch;

  public void initMap(int mapPos){
    Maps map = Maps.getNameByPos(mapPos);
    currentMap = new Map(FloatPair.create(0f,0f), LoaderManager.getInstance().get(map.getName()));
    batch = new SpriteBatch();

//    currentMap = new Map(FloatPair.create(0f,0f), GDXUtils.resizeTexture((Texture) LoaderManager.getInstance().get(map.getName()), map.getScale()));
// изменение размера медленно работает TODO переделать масштабирование
  }

  public void draw(OrthographicCamera camera){
    batch.begin();
    batch.setProjectionMatrix(camera.combined);
    batch.draw(currentMap.getMainTexture(), currentMap.getPos().getX(), currentMap.getPos().getY());
    batch.end();
  }
}
