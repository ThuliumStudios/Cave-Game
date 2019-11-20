package com.bejk.player;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.bejk.net.packet.PlayerPacket;
import com.bejk.util.Units;

public class Player {
	private Animation<TextureRegion> animation;
	private Sprite sprite;
	private TextureAtlas atlas;
	private String animationName;
	private Body body;
	
	private boolean isLooping;
	private float stateTime;
	
	private PlayerPacket packet;
	
	public Player(TextureAtlas atlas, int ID) {
		this.atlas = atlas;
		sprite = new Sprite(atlas.findRegion("idle_s", 0));
		sprite.setBounds(4, 4, .7f, 1);
		
		packet = new PlayerPacket();
		packet.ID = ID;
		setPacketInfo();
		
		animationName = "idle_s";
		animation = new Animation<>(.125f, atlas.findRegions("idle_s"));
	}
	
	public void render(Batch batch, float delta) {
		sprite.setPosition(body.getPosition().x - (sprite.getWidth() / 2f), 
				body.getPosition().y - (.1f));
		sprite.setRegion(animation.getKeyFrame(stateTime, isLooping));
		sprite.draw(batch);
		
		stateTime += delta;
	}
	
	public void setVelocity(float x, float y) {
		body.setLinearVelocity(x, y);
	}
	
	public void stop() {
		body.setLinearVelocity(Vector2.Zero);
		animate("idle_" + animationName.split("_") [1], true);
	}
	
	public void animate(String animationName, boolean isLooping) {
		if (this.animationName.equalsIgnoreCase(animationName))
			return;
		animation = new Animation<>(.125f, atlas.findRegions(animationName));
		stateTime = 0f;
		this.isLooping = isLooping;
		this.animationName = animationName;
	}
	
	public float getX() {
		return sprite.getX();
	}
	
	public float getY() {
		return sprite.getY();
	}
	
	public boolean checkForUpdates() {
		if (changesDetected(sprite.getX(), packet.x) || changesDetected(sprite.getY(), packet.y) || !animationName.equals(packet.anim))
			return setPacketInfo();
		
		return false;
	}
	
	public boolean setPacketInfo() {
		packet.name = sprite.toString();
		packet.anim = animationName;
		packet.x = sprite.getX();
		packet.y = sprite.getY();
		return true;
	}
	
	public PlayerPacket getPacket() {
		return packet;
	}
	
	public boolean changesDetected(float val, float comp) {
		return !MathUtils.isEqual(val, comp, .1f);
	}
	
	public void dispose() {
		
	}
	
	// Less used methods
	public BodyDef getBodyDef(float x, float y) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(x, y);
		return bodyDef;
	}

	public void createBody(Body body, Object name, float width, float height) {
		this.body = body;

		PolygonShape box = new PolygonShape();
		box.setAsBox(width, height);
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = box;
		fixtureDef.filter.categoryBits = Units.ENTITY_FLAG;
		fixtureDef.filter.maskBits = Units.GROUND_FLAG;
		body.createFixture(fixtureDef).setUserData(name);

		box.dispose();
	}
}
