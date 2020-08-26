package com.org.game.physics;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

public class WorldContactListener implements ContactListener { 
	@Override
	public void beginContact(Contact c) {  
	}

	@Override
	public void endContact(Contact c) { 
	}

	@Override
	public void preSolve(Contact c, Manifold Manifold) { 
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) { 
	}

	public void update() { 
	} 
}
