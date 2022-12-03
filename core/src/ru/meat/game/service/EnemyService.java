package ru.meat.game.service;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import ru.meat.game.model.Enemy;
import ru.meat.game.model.EnemyStatus;

public class EnemyService {


  public Enemy createZombieEnemy(float x, float y) {
    return new Enemy(x, y, 40, 3, 100, 0.7f,
        "./assets/export/move/",
        "./assets/export/idle/",
        "./assets/export/attack/",
        null,
        0, 0);
  }

  public void updateStateTime() {
    stateTime += Gdx.graphics.getDeltaTime();
  }

  ;

  private float stateTime;

  public EnemyService() {

  }

  public void drawEnemySprite(SpriteBatch batch, Enemy enemy, float stateTime) {
    Sprite sprite = new Sprite(getActualFrame(stateTime, enemy));
    sprite.setX(enemy.getPosX());
    sprite.setY(enemy.getPosY());
    sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
    sprite.setRotation(enemy.getAnimationAngle());
    sprite.draw(batch);
  }

  private Texture getActualFrame(float stateTime, Enemy enemy) {
    if (enemy.getStatus().equals(EnemyStatus.IDLE)) {
      return enemy.getIdleAnimation().getKeyFrame(stateTime);
    } else if (enemy.getStatus().equals(EnemyStatus.MOVE)) {
      return enemy.getWalkAnimation().getKeyFrame(stateTime);
    } else if (enemy.getStatus().equals(EnemyStatus.ATTACK)) {
      return enemy.getAttackAnimation().getKeyFrame(stateTime, true);
    }
    return enemy.getIdleAnimation().getKeyFrame(stateTime);
  }

  public void move(float x, float y, Enemy enemy) {
    if (Math.sqrt((x - enemy.getPosX()) * (x - enemy.getPosX()) + (y - enemy.getPosY()) * (y - enemy.getPosY()))
        < enemy.getAttackRange()) {
      enemy.setStatus(EnemyStatus.ATTACK);
    } else {
      defineSpeedXandY(x, y, enemy);
      enemy.setStatus(EnemyStatus.MOVE);

      rotateModel(x - enemy.getPosX(), y - enemy.getPosY(), enemy);

      enemy.setPosX(enemy.getPosX() + enemy.getSpeedX());
      enemy.setPosY(enemy.getPosY() + enemy.getSpeedY());
    }
  }


  public void rotateModel(float x, float y, Enemy enemy) {
    enemy.setAnimationAngle(MathUtils.radiansToDegrees * MathUtils.atan2(y, x) + 10);
  }


  public void defineSpeedXandY(float x, float y, Enemy enemy) {
    float catetPrilezjaschiy = x - enemy.getPosX();
    float catetProtivo = y - enemy.getPosY();
    float gip = (float) Math.sqrt(catetPrilezjaschiy * catetPrilezjaschiy + catetProtivo * catetProtivo);
    float sin = catetProtivo / gip;
    float cos = catetPrilezjaschiy / gip;
    enemy.setSpeedY(sin * enemy.getSpeed());
    enemy.setSpeedX(cos * enemy.getSpeed());
  }


  public void moveToPlayer(float x, float y, Enemy enemy) {
    float v1 = enemy.getPosY();
    float v2 = enemy.getPosX();
    if (enemy.getPosX() < x - 20) {
      enemy.setPosX(enemy.getPosX() + enemy.getSpeed());
    } else {
      enemy.setPosX(enemy.getPosX() - enemy.getSpeed());
    }
    if (enemy.getPosY() < y - 20) {
      enemy.setPosY(enemy.getPosY() + enemy.getSpeed());
    } else {
      enemy.setPosY(enemy.getPosY() - enemy.getSpeed());
    }

    rotateModel(x, y, enemy);
  }
}

