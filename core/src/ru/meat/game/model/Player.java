package ru.meat.game.model;

import static ru.meat.game.utils.FilesUtils.compareTwoFilenames;
import static ru.meat.game.utils.FilesUtils.convertBoolToInt;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Player {

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


  public Player() {
    try {
      this.walkAnimation = initAnimationFrames("./assets/Top_Down_survivor/feet/walk/");
      this.idle = initAnimationFrames("./assets/Top_Down_survivor/feet/idle/");
      this.runAnimation = initAnimationFrames("./assets/Top_Down_survivor/feet/run/");
      this.strafeLeftAnimation = initAnimationFrames("./assets/Top_Down_survivor/feet/strafe_left/");
      this.strafeRightAnimation = initAnimationFrames("./assets/Top_Down_survivor/feet/strafe_right/");

      this.handgunIdleAnimation = initAnimationFrames("./assets/Top_Down_survivor/handgun/idle/");
      this.handgunMoveAnimation = initAnimationFrames("./assets/Top_Down_survivor/handgun/move/");
      this.handgunMeleeAttackAnimation = initAnimationFrames("./assets/Top_Down_survivor/handgun/meleeattack/");
      this.handgunShootAnimation = initAnimationFrames("./assets/Top_Down_survivor/handgun/shoot/");
      this.handgunReloadAnimation = initAnimationFrames("./assets/Top_Down_survivor/handgun/reload/");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private Animation<Texture> initAnimationFrames(String animationFilesPath) throws IOException {
    Texture[] collect = Files.walk(Paths.get(animationFilesPath))
        .filter(Files::isRegularFile)
        .sorted((x, y) -> convertBoolToInt(compareTwoFilenames(x, y)))
        .map(file -> new Texture(Gdx.files.internal(file.toAbsolutePath().toString())))
        .toArray(Texture[]::new);

    return new Animation<>(0.025f, collect);
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
}