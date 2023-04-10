package ru.meat.game.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import ru.meat.game.model.player.PlayerService;

public class PlayerControlHandlerThread extends Thread {

  public PlayerControlHandlerThread() {
    this.setDaemon(true);
  }

  @Override
  public void run() {
    while (!PlayerService.getInstance().getPlayer().isDead()) {
      sleep();
      PlayerService.getInstance().handleMoveKey();
      handleMouse();
    }
    System.out.println("ВЫШЕЛ");
  }

  private void sleep() {
    try {
      Thread.sleep(16);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Обработать нажатия мыши, если персонаж ещё жив
   */
  private void handleMouse() {
    PlayerService.getInstance().rotateModel();
    if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
      Gdx.app.postRunnable(() -> PlayerService.getInstance().shoot());
    }
  }
}
