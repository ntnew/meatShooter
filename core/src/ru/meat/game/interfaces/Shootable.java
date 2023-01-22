package ru.meat.game.interfaces;

import ru.meat.game.model.weapons.Bullet;

public interface Shootable {

  void shoot(float fromX, float fromY, float screenX, float screenY, boolean playerRuning);

//  /**
//   * Использовать для удаления пуль
//   */
//  void updateState();
}
