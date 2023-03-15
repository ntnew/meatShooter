package ru.meat.game.settings;

import com.badlogic.gdx.physics.box2d.Filter;

public class Filters {
  // 0000000000000001 in binary
  final public static short CATEGORY_PLAYER = 0x0001;

  // 0000000000000010 in binary
  final public static short CATEGORY_PLAYER_BULLETS = 0x0002;

  // 0000000000000100 in binary
  final public static short CATEGORY_ENEMY = 0x0004;

  // 0000000000001000 in binary
  final public static short CATEGORY_ENEMY_BULLETS = 0x0008;


  final public static short MASK_PLAYER = CATEGORY_ENEMY | CATEGORY_ENEMY_BULLETS;
  final public static short MASK_PLAYER_BULLET =  CATEGORY_ENEMY ;
  final public static short MASK_ENEMY = CATEGORY_PLAYER | CATEGORY_PLAYER_BULLETS | CATEGORY_ENEMY;
  final public static short MASK_ENEMY_BULLETS = CATEGORY_PLAYER;

  public static Filter getPlayerFilter() {
    Filter f = new Filter();
    f.categoryBits = CATEGORY_PLAYER;
    f.maskBits = MASK_PLAYER;
    return  f;
  }

  public static Filter getPlayerBulletFilter() {
    Filter f = new Filter();
    f.categoryBits = CATEGORY_PLAYER_BULLETS;
    f.maskBits = MASK_PLAYER_BULLET;
    return  f;
  }

  public static Filter getEnemyFilter() {
    Filter f = new Filter();
    f.categoryBits = CATEGORY_ENEMY;
    f.maskBits = MASK_ENEMY;
    return  f;
  }

  public static Filter getFilterForEnemiesBullets() {
    Filter f = new Filter();
    f.categoryBits = CATEGORY_ENEMY_BULLETS;
    f.maskBits = MASK_ENEMY_BULLETS;
    return  f;
  }
}
