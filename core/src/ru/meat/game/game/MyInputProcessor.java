package ru.meat.game.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Cursor.SystemCursor;
import ru.meat.game.MyGame;
import ru.meat.game.menu.PauseMenu;
import ru.meat.game.model.player.PlayerService;

public class MyInputProcessor implements InputProcessor {

  private final GameZone gameZone;

  public MyInputProcessor(GameZone gameZone) {
    this.gameZone = gameZone;
  }

  @Override
  public boolean keyDown(int keycode) {
    if (keycode == Keys.NUM_1) {
      PlayerService.getInstance().changeWeapon(1);
    }
    if (keycode == Keys.NUM_2) {
      PlayerService.getInstance().changeWeapon(2);
    }
    if (keycode == Keys.NUM_3) {
      PlayerService.getInstance().changeWeapon(3);
    }
    if (keycode == Keys.NUM_4) {
      PlayerService.getInstance().changeWeapon(4);
    }
    if (keycode == Keys.NUM_5) {
      PlayerService.getInstance().changeWeapon(5);
    }
    if (keycode == Keys.NUM_6) {
      PlayerService.getInstance().changeWeapon(6);
    }
    if (keycode == Keys.NUM_7) {
      PlayerService.getInstance().changeWeapon(7);
    }
    if (keycode == Keys.NUM_8) {
      PlayerService.getInstance().changeWeapon(8);
    }

    if (keycode == Keys.TAB) {
      PlayerService.getInstance().changeToNextWeapon();
    }
    if (keycode == Keys.ESCAPE) {
      Gdx.graphics.setSystemCursor(SystemCursor.Arrow);
      MyGame.getInstance().setScreen(new PauseMenu(gameZone));
    }
    return false;
  }

  @Override
  public boolean keyUp(int keycode) {
    return false;
  }

  @Override
  public boolean keyTyped(char character) {
    return false;
  }

  @Override
  public boolean touchDown(int screenX, int screenY, int pointer, int button) {

    return false;
  }

  @Override
  public boolean touchUp(int screenX, int screenY, int pointer, int button) {

    return false;
  }

  @Override
  public boolean touchDragged(int screenX, int screenY, int pointer) {
    return false;
  }

  @Override
  public boolean mouseMoved(int screenX, int screenY) {
    return false;
  }

  @Override
  public boolean scrolled(float amountX, float amountY) {
    return false;
  }
}
