package ru.meat.game.service;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.WorldManifold;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class EnemyContactListener implements ContactListener {

  @Override
  public void endContact(Contact contact) {

  }

  @Override
  public void beginContact(Contact contact) {
    System.out.println("shiieeeet");
  }

  @Override
  public void preSolve(Contact contact, Manifold oldManifold) {

  }

  @Override
  public void postSolve(Contact contact, ContactImpulse impulse){
  }

}