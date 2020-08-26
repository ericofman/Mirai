package com.org.game.entities;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.org.game.level.Level;

public class Player extends Entity implements InputProcessor { 
	private OrthographicCamera camera;  
	private int health; 
	
	public Player(OrthographicCamera cam, Level level, int x, int y) {
		super(level, true, new Vector2(x, y));
		this.camera = cam; 
 
		health = 50;

		grounded = false;
		facingRight = true;

		SPEED = 15;
		MAX_SPEED = 5;
		JUMP_FORCE = 7;

		width = 32;
		height = 32;
	}  
	
	@Override
	public void update(float delta) { 
		if (currentShakeTime <= shakeTime) {
			currentShakePower = shakePower
					* ((shakeTime - currentShakeTime) / shakeTime);

			shakeX = (random.nextFloat() - 0.5f) * 2 * currentShakePower;
			shakeY = (random.nextFloat() - 0.5f) * 2 * currentShakePower;

			this.camera.translate(-shakeX, -shakeY);
			currentShakeTime += delta;
		}

		skeleton.setX(bodyAttachment.getWorldX());
		skeleton.setY(bodyAttachment.getWorldY()); 

		skeleton.updateWorldTransform();
		animationState.update(delta);
		animationState.apply(skeleton);  

		getNextPosition();
	}  

	@Override
	public void render(SpriteBatch batch) {
		batch.begin();
		skeletonRenderer.draw(batch, skeleton);
		//debugRenderer.getShapeRenderer().setProjectionMatrix(this.level.getCamera().combined);
		batch.end();
		//debugRenderer.draw(skeleton);
	}
	
	@Override
	protected void hit(int damage) {
		health -= damage;
	}

	@Override
	public boolean keyDown(int keycode) {
		if (keycode == Input.Keys.W) {
			jumping = true;
		}
		if (keycode == Input.Keys.A) {
			left = true;
		}
		if (keycode == Input.Keys.S) {
			down = true;
		}
		if (keycode == Input.Keys.D) {
			right = true;
		}
		if (keycode == Input.Keys.SPACE) {
			attack = true;
		}
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		if (keycode == Input.Keys.W) {
			jumping = false;
		}
		if (keycode == Input.Keys.A) {
			left = false;
		}
		if (keycode == Input.Keys.S) {
			down = false;
		}
		if (keycode == Input.Keys.D) {
			right = false;
		}
		if (keycode == Input.Keys.SPACE) {
			attack = false;
		}
		return false;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
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
		return false;
	}

	@Override
	public boolean scrolled(int amount) { 
		return false;
	}
}
