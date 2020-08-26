package com.org.game.level;
 
import box2dLight.RayHandler;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.org.game.Constant;
import com.org.game.Mirai;
import com.org.game.physics.PhysicsWorld;
import com.org.game.states.MainGame;

public class Level {  
	private OrthogonalTiledMapRenderer tiledMapRenderer;  
	private OrthographicCamera camera;

	private TiledMap tiledMap; 
	
	// box2d debug
	private Box2DDebugRenderer b2dr;  

	// map layers
	private int[] topLayers = {1};  
	private int[] bottomLayers = {0};  
	
	public PhysicsWorld physics;
	
	private Texture ball;
	private TextureRegion ballRegion;
	public Level(OrthographicCamera camera, MainGame game) {  
		this.camera = camera; 
		
		physics = new PhysicsWorld(game);
		ball = Mirai.manager.getTexture("Soccerball.png"); 
		ballRegion = new TextureRegion();
		ballRegion.setRegion(ball);
		setupLevel();  
		
		b2dr = new Box2DDebugRenderer();   
		
		physics.rayHandler.setShadows(true); 
		RayHandler.setGammaCorrection(true);
		RayHandler.useDiffuseLight(true);  
		physics.rayHandler.setAmbientLight(0f, 0f, 0f, 0.5f);
		physics.rayHandler.setBlurNum(3);
 
		physics.rayHandler.diffuseBlendFunc.set(GL20.GL_DST_COLOR, GL20.GL_SRC_COLOR);
	} 

	private Body createCircle(BodyType type, float radius, float density) {
		BodyDef bdef = new BodyDef();
		bdef.type = type;  
		Body box = physics.world.createBody(bdef);
		
		CircleShape poly = new CircleShape();
		poly.setRadius(radius);
		box.createFixture(poly, density).setUserData("circle");
		poly.dispose();
		
		return box;
	}  
	 
	public void setupLevel() {   
		tiledMap = new TmxMapLoader().load("map2.tmx"); 
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, 1 / Constant.PPM);  
        
        this.physics.Generate(this.tiledMap.getLayers().get("physics"), 0.0F, 0.0F); 
        this.physics.createLights(this.tiledMap.getLayers().get("lights")); 
        
        for(int i = 0; i < 30; i++) {
			Body circle = createCircle(BodyType.DynamicBody,  0.299f, 0.1f);
			circle.setTransform((float)Math.random() * 40f , (float)Math.random() * 10 + 6, (float)(Math.random() * 2 * Math.PI));
		} 
	}   

	public void update(float delta) {   
		physics.update();
	}
	
	private	Array<Fixture> fixtures = new Array<Fixture>();
   
	public void render(SpriteBatch batch) {    
	    tiledMapRenderer.setView(camera);
	    tiledMapRenderer.render(bottomLayers);    
	    
		// Box2D debug 
	    //b2dr.render(physics.world, camera.combined);
		
		physics.world.getFixtures(fixtures);  
		batch.begin();
		for(int i = 0; i < fixtures.size; i++) {
			if(fixtures.get(i).getUserData() != null && fixtures.get(i).getUserData().equals("circle")) { 
				batch.draw(ballRegion,  fixtures.get(i).getBody().getPosition().x - 0.5f / 2, fixtures.get(i).getBody().getPosition().y - 0.5f /2,
						0.5f/2, 0.5f/2,
						0.5f, 0.5f, 1, 1, fixtures.get(i).getBody().getAngle() * MathUtils.radiansToDegrees); 
			}
		} 
		batch.end();
		this.physics.world.getFixtures(fixtures);
		
		physics.render(batch); 
	}   
	
	public void postRender(SpriteBatch batch) {
		tiledMapRenderer.render(topLayers);    
	}
	
	public OrthographicCamera getCamera() {
		return camera;
	}  
}
