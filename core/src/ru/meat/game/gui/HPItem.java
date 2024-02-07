package ru.meat.game.gui;


import static ru.meat.game.settings.Constants.MAIN_ZOOM;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import ru.meat.game.loader.LoaderManager;
import ru.meat.game.model.player.PlayerService;

public class HPItem extends Image {


  /**
   * Значение максимального хп, для подсчёта оставшегося процента хп
   */
  private static Double fullHp;


  /**
   * текстуры хп бара
   */
  private static final List<TextureRegion> hpTextures = new ArrayList<>();

  public HPItem(Stage stage) {
    super(getActualTexture());
    fullHp = PlayerService.getInstance().getPlayer().getHp();
    setPosition(getWidth()/2, stage.getHeight() * MAIN_ZOOM/ 1.65f - getHeight());
  }

  @Override
  public void act(float delta) {
    ((TextureRegionDrawable) getDrawable()).setRegion(new TextureRegion(getActualTexture()));
    super.act(delta);
  }

  /**
   * Посчитать сколько процентов хп осталось и выдать нужную текстуру хпБара
   */
  public static TextureRegion getActualTexture() {
    if (hpTextures.isEmpty()) {
      initTextures();
    }

    if (fullHp == null) {
      return hpTextures.get(0);
    }
    int i = Integer.parseInt(
        BigDecimal.valueOf((100 - PlayerService.getInstance().getPlayer().getHp() / (fullHp / 100)) / 2)
            .setScale(0, RoundingMode.HALF_DOWN).toString());
    if (i < hpTextures.size()) {
      return hpTextures.get(i);
    } else {
      return hpTextures.get(hpTextures.size() - 1);
    }
  }

  private static void initTextures() {
    for (int i = 0; i < 51; i++) {
      Texture o = LoaderManager.getInstance().get("gui/hpBar/" + i + ".png");
      o.setFilter(TextureFilter.MipMapLinearLinear, TextureFilter.MipMapLinearLinear);
      hpTextures.add(new TextureRegion(o));
    }
  }
}
