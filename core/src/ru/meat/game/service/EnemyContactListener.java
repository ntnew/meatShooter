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

  }

  @Override
  public void preSolve(Contact contact, Manifold oldManifold) {

    WorldManifold manifold = contact.getWorldManifold();
    for(int j = 0; j < manifold.getNumberOfContactPoints(); j++){
      if(contact.getFixtureA().getUserData() != null && contact.getFixtureA().getUserData().equals("p"))
        contact.setEnabled(false);
      if(contact.getFixtureB().getUserData() != null && contact.getFixtureB().getUserData().equals("p"))
        contact.setEnabled(false);
    }
  }

  @Override
  public void postSolve(Contact contact, ContactImpulse impulse){
  }

}