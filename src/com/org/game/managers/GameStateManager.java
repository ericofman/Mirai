package com.org.game.managers;

import java.util.Stack;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.org.game.states.GameState;
import com.org.game.states.MainGame;
import com.org.game.states.MainMenu;

public class GameStateManager {

	private Stack<GameState> gameState;
	
	public static final int MENU_STATE = 0;
	public static final int PLAYING_STATE = 1; 
	
	public GameStateManager() { 
		
		gameState = new Stack<GameState>(); 
		
		setState(PLAYING_STATE);
	}
	
	public void setState(int state) {
		if(!gameState.isEmpty()) {
			gameState.pop();
		}
		switch(state) { 
			case MENU_STATE:
				gameState.push(new MainMenu());
				break;
			case PLAYING_STATE: 
				gameState.push(new MainGame());
				break;
			default:
				System.out.println("NO STATE LOADED");
				break;
		}
	}
	
	public void init() {
		gameState.peek().init();
	}
	
	public void update(float delta) {
		gameState.peek().update(delta);
	}
	
	public void render(SpriteBatch batch) {
		gameState.peek().render(batch);
	}
	
	public void dispose() {
		gameState.peek().dispose();
	} 
}
