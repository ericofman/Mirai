package com.org.game.states;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class GameState {
	public abstract void init();

	public abstract void update(float delta);

	public abstract void render(SpriteBatch batch);

	public abstract void dispose();
}
