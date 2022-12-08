package ru.meat.game.model;

public class FloatPair {

  private Float x;
  private Float y;

  public FloatPair(Float x, Float y) {
    this.x = x;
    this.y = y;
  }

  public static FloatPair create(float x, float y){
    return new FloatPair(x, y);
  }

  public Float getX() {
    return x;
  }

  public void setX(Float x) {
    this.x = x;
  }

  public Float getY() {
    return y;
  }

  public void setY(Float y) {
    this.y = y;
  }
}
