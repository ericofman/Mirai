package com.org.game;
 
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.org.game.listeners.WindowListener;
import com.org.game.managers.AssetManager;
import com.org.game.managers.GameStateManager;
import com.org.game.sound.MusicStream;
import com.org.game.sound.Sound;
import com.org.game.sound.SoundStream;

public class Mirai extends ApplicationAdapter  {

	public static int V_HEIGHT = 600;
	public static int V_WIDTH = 800; 
	
	private SpriteBatch batch;  
	
	private GameStateManager gsm;  
	
	public static AssetManager manager;   
	
	public static InputMultiplexer imp; 
	
	@Override
	public void create() {  
		V_WIDTH = Gdx.graphics.getWidth();
		V_HEIGHT = Gdx.graphics.getHeight();
		
		batch = new SpriteBatch(); 
		
		// load assets 
		manager = new AssetManager();   
		manager.loadTexture("Soccerball.png"); 
		manager.loadTexture("mapTest_bg.png");  
		
		// game states
		gsm = new GameStateManager();    
		
		// register input
		imp = new InputMultiplexer();
		imp.addProcessor(new WindowListener()); 
		Gdx.input.setInputProcessor(imp);
		
		gsm.init(); 
	}  

	@Override
	public void render() {
		Gdx.gl.glClearColor(0.3f, 0.3f, 0.3f, 1);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); 
		 
		gsm.update(Gdx.graphics.getDeltaTime());
		gsm.render(batch);    
	}  
	
	public void dispose() { 
		batch.dispose();
		gsm.dispose();
	}     
}