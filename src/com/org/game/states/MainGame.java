package com.org.game.states;

import box2dLight.PointLight;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.org.game.Constant;
import com.org.game.Mirai;
import com.org.game.entities.Player;
import com.org.game.handlers.ParallaxBackground;
import com.org.game.handlers.ParallaxLayer;
import com.org.game.level.Level;

public class MainGame extends GameState { 
	
	private Player player;  
	 
	public Level level; 
	public OrthographicCamera camera; 
	private PointLight light; 
	private ParallaxBackground bg;
	private ParallaxLayer[] pl;
	private Texture background;
	
	public MainGame() {   		
		camera = new OrthographicCamera();
		camera.setToOrtho(false, Mirai.V_WIDTH / Constant.PPM, Mirai.V_HEIGHT / Constant.PPM);
		camera.update();  
	     
		level = new Level(camera, this);
		
		player = new Player(camera, level, 200, 300);   
		
		light= new PointLight(level.physics.rayHandler, 100, null, 2f, 0, 0);
		
		camera.zoom = 0.8f;  
	}
	
	public void init() { 
		Mirai.imp.addProcessor(player);    
	}   
	 
	@Override
	public void update(float delta) { 
		camera.update();
		 
		player.update(delta);  
		 
		camera.position.set(player.getSkeleton().getX(), player.getSkeleton().getY() + player.getAttachment().getHeight(), 0);
		level.update(delta);    
		
		light.setPosition(player.getBody().getPosition().x, player.getBody().getPosition().y);   
	}
	
	@Override
	public void render(SpriteBatch batch) {  
		batch.setProjectionMatrix(camera.combined); 
		level.render(batch);   
		player.render(batch);  
		level.postRender(batch); 
	}

	@Override
	public void dispose() { 
		light.dispose(); 
		level.physics.dispose();
	}  
}
