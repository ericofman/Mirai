package com.org.game.listeners;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;

public class WindowListener implements InputProcessor {
	public int mouseX, mouseY;

	private long memoryInMB() {
		return memoryInKB() / 1024;
	}

	private long memoryInKB() {
		return (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024;
	}

	private long memoryInBytes() {
		return (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());
	}
	
	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		if (keycode == Keys.M) {
			System.out.println("Memory used: " + memoryInMB() + "." + (memoryInKB()%1000) +
					" MB (in bytes: " + memoryInBytes() + ")");
		}
		if(keycode == Keys.ESCAPE) {
			Gdx.app.exit();	
		}
		return false;
	}

	@Override
	public boolean keyTyped(char character) { 
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		this.mouseX = screenX;
		this.mouseY = screenY;
		
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}
}
