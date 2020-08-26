package com.org.game.entities;
 
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.AnimationStateData;
import com.esotericsoftware.spine.Skeleton;
import com.esotericsoftware.spine.SkeletonData;
import com.esotericsoftware.spine.SkeletonJson;
import com.esotericsoftware.spine.SkeletonRenderer;
import com.esotericsoftware.spine.SkeletonRendererDebug;
import com.esotericsoftware.spine.Skin;
import com.esotericsoftware.spine.attachments.AtlasAttachmentLoader;
import com.esotericsoftware.spine.attachments.RegionAttachment;
import com.org.game.Constant;
import com.org.game.handlers.Animation;
import com.org.game.level.Level;

public abstract class Entity {    
	public int width, height;
	
	// entity direction
	protected boolean facingRight;
	
	protected float SPEED, MAX_SPEED;
	protected float JUMP_FORCE;
	
	// input  
	protected boolean down, left, right; 
	protected boolean attack;        
	
	protected boolean jumping;
	protected boolean grounded;
	
	protected Level level;
	
	// animation
	protected Animation animation;

	protected int currentAction;
	protected int previousAction;  
	
	// camera shake
	protected Random random;
	protected float shakeX;
	protected float shakeY;
	protected float shakeTime = 0;
	protected float currentShakeTime = 1;
	protected float shakePower = 0;
	protected float currentShakePower = 0; 
	
	// box2D
	protected Body body; 
	private long lastGroundTime = 0;
	protected Fixture entityFixture;
	protected Fixture entitySensorFixture;

	// Spine
	protected TextureAtlas atlas;
	protected SkeletonJson json;
	protected SkeletonData playerSkeletonData;
	protected AnimationStateData stateData;
	protected SkeletonRenderer skeletonRenderer;
	protected Skeleton skeleton;
	protected AnimationState animationState;
	protected SkeletonRendererDebug debugRenderer;
	protected Box2dAttachment bodyAttachment;
	
	public Entity(Level level, boolean rotatable, Vector2 pos) {
		this.level = level;
		
		random = new Random();
		animation = new Animation(); 
		
		atlas = new TextureAtlas(Gdx.files.internal("Spine/mainPlayer.atlas"));
		
		AtlasAttachmentLoader atlasLoader = new AtlasAttachmentLoader(atlas) {
			public RegionAttachment newRegionAttachment(Skin skin, String name, String path) {
				Box2dAttachment attachment = new Box2dAttachment(name);
				AtlasRegion region = atlas.findRegion(attachment.getName());
				if (region == null) throw new RuntimeException("Region not found in atlas: " + attachment);
				attachment.setRegion(region);
				return attachment;
			}
		};
		
		json = new SkeletonJson(atlasLoader);
		json.setScale((1 / Constant.PPM) / 5);
		playerSkeletonData = json.readSkeletonData(Gdx.files.internal("Spine/mainPlayer.json"));
		stateData = new AnimationStateData(playerSkeletonData);

		skeletonRenderer = new SkeletonRenderer();
		skeleton = new Skeleton(playerSkeletonData);
		debugRenderer = new SkeletonRendererDebug();
		debugRenderer.setScale((1 / Constant.PPM) / 5);
		animationState = new AnimationState(stateData);
		animationState.setAnimation(0, "animation", true);
 
		bodyAttachment = (Box2dAttachment)skeleton.getAttachment("Body", "Body");  
		createBodyOnAttachment(bodyAttachment, BodyType.DynamicBody); 
	} 
	
	private void createBodyOnAttachment(Box2dAttachment attachment, BodyType bodytype) {
		PolygonShape boxPoly = new PolygonShape(); 
		
		boxPoly.setAsBox((attachment.getWidth() / 2) * attachment.getScaleX(),
				(attachment.getHeight() * 2) * attachment.getScaleY(),
				new Vector2(0, (attachment.getHeight() * 2) * attachment.getScaleY()),
				0 * MathUtils.degRad);
	  
		BodyDef bdef = new BodyDef();
		bdef.type = bodytype;  
		
		skeleton.setX(4);
		skeleton.setY(3);
		
		bdef.position.x = skeleton.getX();
		bdef.position.y = skeleton.getY();
		//boxBodyDef.angle = attachment.getRotation() * MathUtils.degRad;
		attachment.body = level.physics.world.createBody(bdef); 
		attachment.body.setFixedRotation(true);
		
		FixtureDef fdef = new FixtureDef();
		fdef.shape = boxPoly;
		fdef.density = 1f;
		fdef.restitution = 0.1f;
		fdef.friction= 0.3f;
		entitySensorFixture = attachment.body.createFixture(fdef);
		 
		boxPoly.dispose();
	}  
	
	protected abstract void hit(int damage);  
	
	protected abstract void update(float delta); 
	  
	public void render(SpriteBatch batch) {
		batch.begin();
		skeletonRenderer.draw(batch, skeleton);
		debugRenderer.getShapeRenderer().setProjectionMatrix(this.level.getCamera().combined);
		batch.end();
		debugRenderer.draw(skeleton);
	}
 
	protected void cameraShake(float power, float time) {
		shakePower = power;
		shakeTime = time;
		currentShakeTime = 0;
	}

	protected void getNextPosition() {  
		Vector2 pos = this.bodyAttachment.body.getPosition();

		grounded = isPlayerGrounded(Gdx.graphics.getDeltaTime());
		if (grounded) { 
			lastGroundTime = System.nanoTime();
		} else {
			if (System.nanoTime() - lastGroundTime < 100000000) {
				grounded = true; 
			}
		} 
		
		if (left) {
			skeleton.setFlipX(true);
			if (Math.abs(this.bodyAttachment.body.getLinearVelocity().x) < MAX_SPEED) {
				this.getBody().applyForceToCenter(-SPEED, 0, true);
			}
		} else if (right) {
			skeleton.setFlipX(false);
			if (Math.abs(this.bodyAttachment.body.getLinearVelocity().x) < MAX_SPEED) {
				this.getBody().applyForceToCenter(SPEED, 0, true);
			}
		} 
		if (jumping) {  
			jumping = false;
			if (grounded) {
				bodyAttachment.body.applyLinearImpulse(0, JUMP_FORCE, pos.x, pos.y, true); 
			}
		}
	}
	
	private boolean isPlayerGrounded(float deltaTime) {		 
		Array<Contact> contactList =  level.physics.world.getContactList();
		for(int i = 0; i < contactList.size; i++) {
			Contact contact = contactList.get(i);
			if(contact.isTouching() && (contact.getFixtureA() == entitySensorFixture ||
			   contact.getFixtureB() == entitySensorFixture)) {		 
				return true;
			}
		}
		return false;
	}
	
	public SkeletonJson getSkeletonJson() {
		return json;
	}
	
	public Box2dAttachment getAttachment() {
		return bodyAttachment;
	}
	 
	public Skeleton getSkeleton() {
		return skeleton;
	}
	
	public Body getBody() {
		return bodyAttachment.body;
	} 
	
	public boolean isJumping() {
		return jumping;
	}
 
	public boolean isGrounded() {
		return grounded;
	}
	
	public static class Box2dAttachment extends RegionAttachment {
		Body body;

		public float getWorldX(){
			return body.getPosition().x;
		}
		public float getWorldY(){
			return body.getPosition().y;
		}
		
		public Box2dAttachment(String name) {
			super(name);
		}
	}
}
