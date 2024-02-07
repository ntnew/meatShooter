package ru.meat.game.gui;

import static ru.meat.game.settings.Constants.GUI_ZOOM;
import static ru.meat.game.settings.Constants.MAIN_ZOOM;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.TimeUtils;
import ru.meat.game.loader.LoaderManager;

public class DamageScreen extends Image {

  private Long damageScreenTransparentCounter;
  private Float damageScreenAlpha = 0f;

  public DamageScreen() {
    super(new TextureRegion(getDamageScreenTexture()));
    damageScreenTransparentCounter = 0L;
    setScale(
        Gdx.graphics.getWidth() / getDrawable().getMinWidth() * MAIN_ZOOM * GUI_ZOOM,
        Gdx.graphics.getHeight() / getDrawable().getMinHeight() * MAIN_ZOOM * GUI_ZOOM);
    setPosition(-Gdx.graphics.getWidth()*MAIN_ZOOM/2,-Gdx.graphics.getHeight()*MAIN_ZOOM/2);
    getColor().a = 0;
  }

  private static Texture getDamageScreenTexture() {
    Texture texture = LoaderManager.getInstance().get("gui/damageScreen.png");
    texture.setFilter(TextureFilter.MipMapLinearLinear, TextureFilter.MipMapLinearLinear);
    return texture;
  }

  public void handleHit(){
    damageScreenAlpha += 0.5f;
    if (damageScreenAlpha > 1) {
      damageScreenAlpha = 1f;
    }
    getColor().a = damageScreenAlpha;
  }

  @Override
  public void act(float delta) {
    handleDamageScreenTransparency();
    super.act(delta);
  }

  /**
   * Обработать прозрачность красного экрана
   */
  private void handleDamageScreenTransparency() {
    if (getColor().a != 0 && TimeUtils.timeSinceMillis(damageScreenTransparentCounter) > 15) {
      damageScreenTransparentCounter = TimeUtils.millis();
      damageScreenAlpha = damageScreenAlpha - 0.005f;
      if (damageScreenAlpha < 0) {
        damageScreenAlpha = 0f;
      }
      getColor().a = damageScreenAlpha;
    }
  }
}
