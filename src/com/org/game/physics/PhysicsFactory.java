package com.org.game.physics;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.org.game.Constant;

public class PhysicsFactory {

	private PhysicsWorld physicsWorld;
	
	public PhysicsFactory(PhysicsWorld physics) {
		this.physicsWorld = physics;
	}
	
	public Body createRectangle(float x, float y, float w, float h, boolean dynamic, float friction, float restitution) {
		BodyDef bdef = new BodyDef();
	 
		PolygonShape shape = new PolygonShape();
		FixtureDef fd = new FixtureDef();
		
		shape.setAsBox((w/2) / Constant.PPM, (h/2) / Constant.PPM);	
	
		bdef.position.set(x / Constant.PPM, y / Constant.PPM);
		Body body = this.physicsWorld.world.createBody(bdef);
	
		fd.friction = 1f;
		fd.shape = shape; 
	 
		body.createFixture(fd);
		return body; 
	}
	
	public Body createPoly(float x, float y, float[] verts, boolean dynamic) {
		BodyDef bodyDef = new BodyDef();
		if (dynamic) {
			bodyDef.type = BodyDef.BodyType.DynamicBody;
		}
		bodyDef.position.set(x / Constant.PPM, y / Constant.PPM);
		Body body = this.physicsWorld.world.createBody(bodyDef);
		PolygonShape shape = new PolygonShape();
		shape.set(verts);
		if (dynamic) {
			FixtureDef fixtureDef = new FixtureDef();
			fixtureDef.shape = shape;
			fixtureDef.density = 0.5F;
			fixtureDef.friction = 0.5F;
			fixtureDef.restitution = 0.6F;
			body.createFixture(fixtureDef);
			return body;
		}
		body.createFixture(shape, 0.0F);
		return body;
	}
}
