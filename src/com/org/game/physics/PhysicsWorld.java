package com.org.game.physics;

import java.util.HashMap;

import box2dLight.ConeLight;
import box2dLight.PointLight;
import box2dLight.RayHandler;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.org.game.Constant;
import com.org.game.states.MainGame;

public class PhysicsWorld { 
	private MainGame game;
	
	public World world;
	private Vector2 gravity = new Vector2(0.0F, -9.81F);
	public PhysicsFactory factory;
	public WorldContactListener worldContactListener;
	
	private Box2DDebugRenderer debugRenderer;
	
	public RayHandler rayHandler;  

	public HashMap<Integer, PointLight> lights = new HashMap<Integer, PointLight>();
	
	public PhysicsWorld(MainGame game) {
		this.game = game;
		this.world = new World(gravity, true);
		
		this.worldContactListener = new WorldContactListener();
		this.world.setContactListener(worldContactListener);
		this.factory = new PhysicsFactory(this);
		
		this.debugRenderer = new Box2DDebugRenderer();
		
		this.rayHandler = new RayHandler(world); 
	}
	
	public void Generate(MapLayer physics, float xOffset, float yOffset) {
		for (MapObject phys : physics.getObjects()) {
			if (phys instanceof RectangleMapObject) {
				Rectangle rect = ((RectangleMapObject) phys).getRectangle();
				float x = rect.getX() + rect.getWidth() / 2;
				float y = rect.getY() + rect.getHeight() / 2;
				this.factory.createRectangle(x, y, rect.getWidth(), rect.getHeight(), false, 0.8F, 0.6F).setUserData(this);
			} else if ((phys instanceof PolygonMapObject)) {
				float[] verts = ((PolygonMapObject) phys).getPolygon().getVertices();
				for (int i = 0; i < verts.length; i++) {
					verts[i] = (verts[i] / Constant.PPM);
				}
				float x = ((PolygonMapObject) phys).getPolygon().getX();
				float y = ((PolygonMapObject) phys).getPolygon().getY();
				this.factory.createPoly(x, y, verts, false).setUserData(this);
			}
		}
	}

	public void createLights(MapLayer physics) { 
		for(MapObject phys : physics.getObjects()) {
			if(phys instanceof RectangleMapObject) {
				Rectangle rect = ((RectangleMapObject)phys).getRectangle();
				float x = rect.getX() + rect.getWidth() / 2;
				float y = rect.getY() + rect.getHeight() / 2; 
				ConeLight cl = new ConeLight(rayHandler, 400, null, 500f, x / Constant.PPM, y / Constant.PPM, -90.0F, 60.0F);
				cl.setSoftnessLength(0); 
			}
		}
	}
	
	public void update() {
		this.world.step(1/60F, 6, 2);
	    this.worldContactListener.update();
	} 
	
	public void render(SpriteBatch batch) {  
		 rayHandler.setCombinedMatrix(game.camera);
		 this.rayHandler.updateAndRender();  
	}
	
	public void dispose() {
		world.dispose();
		debugRenderer.dispose();
	}
	
	public void remove(Body body) {
		this.world.destroyBody(body);
	}
}
