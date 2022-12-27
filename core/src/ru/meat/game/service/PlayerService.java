package ru.meat.game.service;

import static ru.meat.game.utils.Settings.MAIN_ZOOM;
import static ru.meat.game.utils.Settings.WORLD_TO_VIEW;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.TimeUtils;
import lombok.Data;
import ru.meat.game.model.CharacterFeetStatus;
import ru.meat.game.model.CharacterTopStatus;
import ru.meat.game.model.Player;
import ru.meat.game.model.weapons.Weapon;
import ru.meat.game.model.weapons.WeaponEnum;

@Data
public class PlayerService {

  private final AudioService audioService;


  private Player player;

  private float moveMultiplier = 1f;

  private float feetStateTime;

  private float topStateTime;

  private float speed = 3f*MAIN_ZOOM;

  private float moveDirectionAngle = 0;

  private float modelFrontAngle = 0;

  public PlayerService(float x, float y, World world) {
    player = new Player(world, x, y);
    audioService = new AudioService();
  }

  public void updateState() {
    if (CharacterFeetStatus.RUN.equals(player.getFeetStatus())) {
      feetStateTime += Gdx.graphics.getDeltaTime();
    }
    topStateTime += Gdx.graphics.getDeltaTime();

    getActualWeapon().updateState();
  }

  public void rotateModel(OrthographicCamera camera) {
    float xInput = Gdx.input.getX();
    float yInput = Gdx.input.getY();

    Vector3 tmpVec3 = new Vector3();
    tmpVec3.set(xInput, yInput, 0);
    tmpVec3 = camera.unproject(tmpVec3);

    modelFrontAngle = MathUtils.radiansToDegrees * MathUtils.atan2(tmpVec3.y - player.getBody().getPosition().y, tmpVec3.x -  player.getBody().getPosition().x) + 20;
  }

  public void drawPlayer(SpriteBatch batch) {
    drawFeetSprite(batch);
    drawTopSprite(batch);
  }

  private void drawFeetSprite(SpriteBatch batch) {
    Sprite sprite = new Sprite(getActualFrame(feetStateTime));
    sprite.setX(
        player.getBody().getPosition().x * WORLD_TO_VIEW + (20f / player.getZoomMultiplier()) - sprite.getWidth() / 2);
    sprite.setY(
        player.getBody().getPosition().y * WORLD_TO_VIEW + (30f / player.getZoomMultiplier()) - sprite.getHeight() / 2);
    sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
    sprite.setRotation(modelFrontAngle - 20);
    sprite.draw(batch);
  }

  private void drawTopSprite(SpriteBatch batch) {
    Sprite sprite = new Sprite(getActualFrameTop(topStateTime));
    sprite.setX(player.getBody().getPosition().x * WORLD_TO_VIEW - sprite.getWidth() / 2);
    sprite.setY(player.getBody().getPosition().y * WORLD_TO_VIEW - sprite.getHeight() / 2);
    sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
    sprite.setRotation(modelFrontAngle - 20);
    sprite.draw(batch);
  }

  private Texture getActualFrame(float stateTime) {
    float v = modelFrontAngle + 360 + 20;
    v = angleMathAdd(v);
    if (angleMathAdd(45 + v) > moveDirectionAngle && moveDirectionAngle >= angleMathAdd(v - 45)) {
      return player.getRunAnimation().getKeyFrame(stateTime, true);
    } else if (angleMathAdd(135 + v) > moveDirectionAngle && moveDirectionAngle >= angleMathAdd(v + 45)) {
      return player.getStrafeRightAnimation().getKeyFrame(stateTime, true);
    } else if (angleMathAdd(v - 45) > moveDirectionAngle && moveDirectionAngle >= angleMathAdd(v - 135)) {
      return player.getStrafeLeftAnimation().getKeyFrame(stateTime, true);
    } else {
      return player.getRunAnimation().getKeyFrame(stateTime, true);
    }
  }

  private float angleMathAdd(float v) {
    if (v < 0) {
      return v + 360;
    } else if (v > 360) {
      return v - 360;
    } else if (v == 360) {
      return 0;
    }
    return v;
  }

  public Texture getActualFrameTop(float stateTime) {
    Weapon animationStack = getActualWeapon();
    if (player.getTopStatus() == CharacterTopStatus.MOVE) {
      return animationStack.getMoveAnimation().getKeyFrame(stateTime, true);
    } else if (player.getTopStatus() == CharacterTopStatus.RELOAD) {
      return animationStack.getReloadAnimation().getKeyFrame(stateTime, true);
    } else if (player.getTopStatus() == CharacterTopStatus.SHOOT) {
      return animationStack.getShootAnimation().getKeyFrame(stateTime, true);
    } else if (player.getTopStatus() == CharacterTopStatus.MELEE_ATTACK) {
      return animationStack.getMeleeAttackAnimation().getKeyFrame(stateTime, true);
    }
    return animationStack.getIdleAnimation().getKeyFrame(stateTime, true);
  }

  public void changeTopStatus(CharacterTopStatus status) {
    player.setTopStatus(status);
  }


  public void changeFeetStatus(CharacterFeetStatus status) {
    player.setFeetStatus(status);
  }

  public void shoot(OrthographicCamera cameraBox2D, float screenX, float screenY) {
    Weapon weapon = getActualWeapon();
    if (weapon.getCurrentLockCounter() == 0
        || TimeUtils.timeSinceMillis(weapon.getCurrentLockCounter()) > weapon.getFireRate()) {
      weapon.setCurrentLockCounter(TimeUtils.millis());
      Vector3 point = new Vector3();
      point.set(screenX, screenY, 0);
      cameraBox2D.unproject(point);

      weapon.shoot(player.getBody().getPosition().x, player.getBody().getPosition().y, point.x, point.y);
      audioService.playShoot(weapon.getShootSound());
    }
  }


  /**
   * Получить выбранное оружие у игрока
   */
  public Weapon getActualWeapon() {
    return player.getWeapons().stream().filter(x -> x.getName().equals(player.getCurrentWeapon())).findFirst()
        .orElse(player.getWeapons().get(0));
  }

  public void changeWeapon(int i) {
    player.setCurrentWeapon(WeaponEnum.getByPos(i));
  }

  public void handleKey(OrthographicCamera camera, OrthographicCamera cameraBox2D) {
    if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(
        Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.D)) {
      changeFeetStatus(CharacterFeetStatus.RUN);
      changeTopStatus(CharacterTopStatus.MOVE);
    } else {
      changeFeetStatus(CharacterFeetStatus.IDLE);
      changeTopStatus(CharacterTopStatus.IDLE);
    }

    float x = 0;
    float y = 0;

    if (Gdx.input.isKeyPressed(Input.Keys.A)) {
      x = -getSpeed();
    }

    if (Gdx.input.isKeyPressed(Input.Keys.W)) {
      y = getSpeed();
    }

    if (Gdx.input.isKeyPressed(Input.Keys.S)) {
      y = -getSpeed();
    }

    if (Gdx.input.isKeyPressed(Input.Keys.D)) {
      x = getSpeed();
    }

    player.getBody().setTransform(player.getBody().getPosition().x + x / WORLD_TO_VIEW, player.getBody().getPosition().y +y/WORLD_TO_VIEW, 0);

    Vector2 position = player.getBody().getPosition();

    if (position.x > cameraBox2D.position.x && x < 0){
      x=0;
    }
    if (position.x < cameraBox2D.position.x && x > 0){
      x=0;
    }

    if (position.y > cameraBox2D.position.y && y < 0){
      y=0;
    }
    if (position.y < cameraBox2D.position.y && y > 0){
      y=0;
    }
    camera.translate(x,y);
    cameraBox2D.translate(x/WORLD_TO_VIEW,y/WORLD_TO_VIEW);

    camera.update();
    cameraBox2D.update();
  }

  private float getSpeed() {
    return (speed * moveMultiplier);
  }

  public void drawBullets(SpriteBatch spriteBatch) {
    getActualWeapon().getBulletService()
        .drawBullets(spriteBatch, player.getBody().getPosition().x, player.getBody().getPosition().y);
  }
}
