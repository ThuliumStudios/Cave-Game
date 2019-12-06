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
import com.badlogic.gdx.utils.Array;
import com.bejk.net.packet.AttackRequest;
import com.bejk.net.packet.PlayerPacket;
import com.bejk.util.Units;

public class Player {
	private Array<Object> updates = new Array<>();
	private Animation<TextureRegion> animation;
	private Sprite sprite;
	private TextureAtlas atlas;
	private String animationName;
	private Body body;

	private boolean isLooping;
	private char direction;
	private float hitRecharge;
	private float rechargeTime;
	private float stateTime;

	private PlayerPacket packet;
	private AttackRequest attackRequest;

	public Player(TextureAtlas atlas, int ID) {
		this.atlas = atlas;
		direction = 's';
		sprite = new Sprite(atlas.findRegion("idle" + getDirection(), 0));
		sprite.setBounds(4, 4, .7f, 1);

		packet = new PlayerPacket();
		attackRequest = new AttackRequest();

		packet.ID = ID;
		setPacketInfo();

		animationName = "idle";
		setDirection('s');
		animation = new Animation<>(.125f, atlas.findRegions("idle" + getDirection()));
	}

	public void render(Batch batch, float delta) {
		sprite.setPosition(body.getPosition().x - (sprite.getWidth() / 2f), body.getPosition().y - (.1f));
		sprite.setRegion(animation.getKeyFrame(stateTime, isLooping));
		sprite.draw(batch);

		stateTime += delta;
		if (hitRecharge < rechargeTime)
			hitRecharge += delta;
	}

	public void setVelocity(float x, float y) {
		body.setLinearVelocity(x, y);
	}

	public void attack() {
		System.out.println("Ok, attack here");
		attackRequest.attackerID = packet.ID;
	}

	public void stop() {
		body.setLinearVelocity(Vector2.Zero);
		animate("idle", true);
	}

	public void animate(String animationName, boolean isLooping) {
		if (this.animationName.equalsIgnoreCase(animationName))
			return;
		animation = new Animation<>(.125f, atlas.findRegions(animationName + getDirection()));
		stateTime = 0f;
		packet.anim = animationName + getDirection();
		this.isLooping = isLooping;
		this.animationName = animationName;
	}

	public float getX() {
		return sprite.getX();
	}

	public float getY() {
		return sprite.getY();
	}

	public String getDirection() {
		return "_" + direction;
	}
	
	public void setDirection(char direction) {
		this.direction = direction;
	}
	
	public boolean hasUpdates() {
		if (changesDetected(sprite.getX(), packet.x) || changesDetected(sprite.getY(), packet.y)
				|| !packet.anim.equals(animationName + getDirection()))
			return setPacketInfo();
		if (attackRequest.attackerID == packet.ID)
			return sendAttackRequest();
		return false;
	}

	public boolean setPacketInfo() {
		packet.name = sprite.toString();
		packet.x = sprite.getX();
		packet.y = sprite.getY();
		packet.anim = animationName + getDirection();
		updates.add(packet);
		return true;
	}

	public boolean sendAttackRequest() {
		Vector2 pos = new Vector2();
		switch (direction) {
		case 'n':
			pos.set(sprite.getX(), sprite.getY() + .25f);
			break;
		case 'e':
			pos.set(sprite.getX() + .25f, sprite.getY());
			break;
		case 's':
			pos.set(sprite.getX(), sprite.getY() - .75f);
			break;
		case 'w':
			pos.set(sprite.getX() - .75f, sprite.getY());
			break;
		}
		attackRequest.x = pos.x;
		attackRequest.y = pos.y;
		updates.add(attackRequest);
		System.out.println("Should add attack here");
		return true;
	}

	public Array<Object> sendUpdates() {
		return updates;
	}

	public void clearUpdates() {
		System.out.println("Clearing updates");
		attackRequest.attackerID = -1;
		updates.clear();
	}

//	public PlayerPacket getPacket() {
//		return packet;
//	}

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
