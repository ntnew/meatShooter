package ru.meat.game.gui;

import static ru.meat.game.settings.Constants.GUI_ZOOM;
import static ru.meat.game.settings.Constants.MAIN_ZOOM;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.math.BigDecimal;
import java.math.RoundingMode;
import lombok.Data;
import ru.meat.game.loader.LoaderManager;
import ru.meat.game.utils.FilesUtils;

@Data
public class GUI {

  private SpriteBatch batch;
  private OrthographicCamera camera;
  private final Texture[] textures;

  private Cursor cursor;

  /**
   * Значение максимального хп, для подсчёта оставшегося процента хп
   */
  private double fullHp;

  public GUI(double fullHp) {
    this.textures = FilesUtils.collectTextures("./assets/gui/hpBar/", 1);
    for (int i = 0; i < this.textures.length; i++) {
      textures[i].setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
    }

    this.fullHp = fullHp;

    camera = new OrthographicCamera();
    camera.zoom = MAIN_ZOOM * GUI_ZOOM;
    camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0f);
    batch = new SpriteBatch();
    batch.setProjectionMatrix(camera.combined);
  }


  public void draw(double hp) {
    batch.begin();

    Sprite sprite = new Sprite(getActualTexture(hp));
    sprite.setPosition(camera.viewportWidth / 2 * MAIN_ZOOM * GUI_ZOOM - sprite.getWidth() / 2,
        camera.viewportHeight * MAIN_ZOOM * GUI_ZOOM - sprite.getHeight());
    sprite.flip(false, true);
    sprite.draw(batch);

    batch.end();
  }

  /**
   * Посчитать сколько процентов хп осталось и выдать нужную текстуру хпБара
   * @param hp оставшиеся хп
   */
  private Texture getActualTexture(double hp) {
    int i = Integer.parseInt(
        BigDecimal.valueOf((100 - hp / (fullHp / 100)) / 2).setScale(0, RoundingMode.HALF_DOWN).toString());
    if (i < textures.length) {
      return textures[i];
    } else {
      return textures[textures.length - 1];
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
}