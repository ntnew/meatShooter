package ru.meat.game.model;

import static ru.meat.game.utils.FilesUtils.initAnimationFrames;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;

public class Player {

  private final int zoomMultiplier = 3;
  private final float frameDuration = 0.03f;

  private Animation<Texture> walkAnimation;
  private Animation<Texture> idle;
  private Animation<Texture> runAnimation;
  private Animation<Texture> strafeLeftAnimation;
  private Animation<Texture> strafeRightAnimation;

  private Animation<Texture> handgunIdleAnimation;
  private Animation<Texture> handgunMoveAnimation;
  private Animation<Texture> handgunMeleeAttackAnimation;
  private Animation<Texture> handgunShootAnimation;
  private Animation<Texture> handgunReloadAnimation;


  private CharacterTopStatus topStatus;
  private CharacterFeetStatus feetStatus;

  public Player() {
    try {
      topStatus = CharacterTopStatus.IDLE;
      feetStatus = CharacterFeetStatus.IDLE;

      this.walkAnimation = initAnimationFrames("./assets/Top_Down_survivor/feet/walk/", zoomMultiplier, frameDuration);
      this.idle = initAnimationFrames("./assets/Top_Down_survivor/feet/idle/", zoomMultiplier, frameDuration);
      this.runAnimation = initAnimationFrames("./assets/Top_Down_survivor/feet/run/", zoomMultiplier, frameDuration);
      this.strafeLeftAnimation = initAnimationFrames("./assets/Top_Down_survivor/feet/strafe_left/", zoomMultiplier, frameDuration);
      this.strafeRightAnimation = initAnimationFrames("./assets/Top_Down_survivor/feet/strafe_right/", zoomMultiplier,frameDuration);

      this.handgunIdleAnimation = initAnimationFrames("./assets/Top_Down_survivor/handgun/idle/", zoomMultiplier,frameDuration);
      this.handgunMoveAnimation = initAnimationFrames("./assets/Top_Down_survivor/handgun/move/", zoomMultiplier,frameDuration);
      this.handgunMeleeAttackAnimation = initAnimationFrames("./assets/Top_Down_survivor/handgun/meleeattack/",
          zoomMultiplier,frameDuration);
      this.handgunShootAnimation = initAnimationFrames("./assets/Top_Down_survivor/handgun/shoot/", zoomMultiplier,frameDuration);
      this.handgunReloadAnimation = initAnimationFrames("./assets/Top_Down_survivor/handgun/reload/", zoomMultiplier,frameDuration);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }


  public Animation<Texture> getWalkAnimation() {
    return walkAnimation;
  }

  public void setWalkAnimation(Animation<Texture> walkAnimation) {
    this.walkAnimation = walkAnimation;
  }

  public Animation<Texture> getIdle() {
    return idle;
  }

  public void setIdle(Animation<Texture> idle) {
    this.idle = idle;
  }

  public Animation<Texture> getRunAnimation() {
    return runAnimation;
  }

  public void setRunAnimation(Animation<Texture> runAnimation) {
    this.runAnimation = runAnimation;
  }

  public Animation<Texture> getStrafeLeftAnimation() {
    return strafeLeftAnimation;
  }

  public void setStrafeLeftAnimation(
      Animation<Texture> strafeLeftAnimation) {
    this.strafeLeftAnimation = strafeLeftAnimation;
  }

  public Animation<Texture> getStrafeRightAnimation() {
    return strafeRightAnimation;
  }

  public void setStrafeRightAnimation(
      Animation<Texture> strafeRightAnimation) {
    this.strafeRightAnimation = strafeRightAnimation;
  }

  public Animation<Texture> getHandgunIdleAnimation() {
    return handgunIdleAnimation;
  }

  public void setHandgunIdleAnimation(
      Animation<Texture> handgunIdleAnimation) {
    this.handgunIdleAnimation = handgunIdleAnimation;
  }

  public Animation<Texture> getHandgunMoveAnimation() {
    return handgunMoveAnimation;
  }

  public void setHandgunMoveAnimation(
      Animation<Texture> handgunMoveAnimation) {
    this.handgunMoveAnimation = handgunMoveAnimation;
  }

  public Animation<Texture> getHandgunMeleeAttackAnimation() {
    return handgunMeleeAttackAnimation;
  }

  public void setHandgunMeleeAttackAnimation(
      Animation<Texture> handgunMeleeAttackAnimation) {
    this.handgunMeleeAttackAnimation = handgunMeleeAttackAnimation;
  }

  public Animation<Texture> getHandgunShootAnimation() {
    return handgunShootAnimation;
  }

  public void setHandgunShootAnimation(
      Animation<Texture> handgunShootAnimation) {
    this.handgunShootAnimation = handgunShootAnimation;
  }

  public Animation<Texture> getHandgunReloadAnimation() {
    return handgunReloadAnimation;
  }

  public void setHandgunReloadAnimation(
      Animation<Texture> handgunReloadAnimation) {
    this.handgunReloadAnimation = handgunReloadAnimation;
  }

  public CharacterTopStatus getTopStatus() {
    return topStatus;
  }

  public void setTopStatus(CharacterTopStatus topStatus) {
    this.topStatus = topStatus;
  }

  public CharacterFeetStatus getFeetStatus() {
    return feetStatus;
  }

  public void setFeetStatus(CharacterFeetStatus feetStatus) {
    this.feetStatus = feetStatus;
  }

  public int getZoomMultiplier() {
    return zoomMultiplier;
  }
}