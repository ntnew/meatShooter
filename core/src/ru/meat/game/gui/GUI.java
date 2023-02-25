package ru.meat.game.gui;

import static ru.meat.game.settings.Constants.GUI_ZOOM;
import static ru.meat.game.settings.Constants.MAIN_ZOOM;
import static ru.meat.game.settings.Constants.TEXTURE_PARAMETERS;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import ru.meat.game.loader.LoaderManager;

@Data
public class GUI {

  private SpriteBatch batch;

  /**
   * спрайт хп бара
   */
  private Sprite hpSprite;
  private OrthographicCamera camera;

  /**
   * текстуры хп бара
   */
  private final List<Texture> hpTextures = new ArrayList<>();

  private Cursor cursor;

  /**
   * Значение максимального хп, для подсчёта оставшегося процента хп
   */
  private double fullHp;

  public GUI(double fullHp) {
    for (int i = 0; i < 51; i++) {
      hpTextures.add(LoaderManager.getInstance().get("gui/hpBar/" + i + ".png"));
    }
    hpTextures.forEach(t -> t.setFilter(TextureFilter.MipMapLinearLinear, TextureFilter.MipMapLinearLinear));

    this.fullHp = fullHp;

    //init camera
    camera = new OrthographicCamera();
    camera.zoom = MAIN_ZOOM * GUI_ZOOM;
    camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0f);

    batch = new SpriteBatch();
    batch.setProjectionMatrix(camera.combined);

    //init hp sprite
    hpSprite = new Sprite(getActualTexture(fullHp));
    hpSprite.setPosition(camera.viewportWidth / 2 * MAIN_ZOOM * GUI_ZOOM - hpSprite.getWidth() / 2,
        camera.viewportHeight * MAIN_ZOOM * GUI_ZOOM - hpSprite.getHeight());
    hpSprite.flip(false, true);
  }


  public void draw(double hp) {
    batch.begin();
    hpSprite.setTexture(getActualTexture(hp));
    hpSprite.draw(batch);
    batch.end();
  }

  /**
   * Посчитать сколько процентов хп осталось и выдать нужную текстуру хпБара
   *
   * @param hp оставшиеся хп
   */
  private Texture getActualTexture(double hp) {
    int i = Integer.parseInt(
        BigDecimal.valueOf((100 - hp / (fullHp / 100)) / 2).setScale(0, RoundingMode.HALF_DOWN).toString());
    if (i < hpTextures.size()) {
      return hpTextures.get(i);
    } else {
      return hpTextures.get(hpTextures.size() - 1);
    }
  }

  /**
   * Установить курсор "цель"
   */
  public void setAimCursor() {
    if (cursor == null) {
      createCursor();
    }

    Gdx.graphics.setCursor(cursor);
  }

  private void createCursor() {
    Pixmap pm = new Pixmap(Gdx.files.internal("gui/cursorAim.png"));

    Pixmap pixmap100 = new Pixmap(
        BigDecimal.valueOf(pm.getWidth() / 8).intValue(),
        BigDecimal.valueOf(pm.getHeight() / 8).intValue(),
        pm.getFormat());
    pixmap100.drawPixmap(pm,
        0, 0, pm.getWidth(), pm.getHeight(),
        0, 0, pixmap100.getWidth(), pixmap100.getHeight()
    );
    cursor = Gdx.graphics.newCursor(pixmap100, pixmap100.getWidth() / 2, pixmap100.getHeight() / 2);
    pixmap100.dispose();
    pm.dispose();
  }

  public static void loadHpBarTextures() {
    for (int i = 0; i < 51; i++) {
      LoaderManager.getInstance().load("gui/hpBar/" + i + ".png", Texture.class, TEXTURE_PARAMETERS);
    }
  }
}