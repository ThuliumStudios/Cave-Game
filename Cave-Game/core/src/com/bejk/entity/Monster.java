package com.bejk.entity;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.bejk.net.packet.MonsterPacket;

public class Monster {
	private Animation<TextureRegion> animation;
	private TextureAtlas atlas;
	private Sprite sprite;

	private String animationName;
	private float stateTime;
	
	public Monster(TextureAtlas atlas) {
		this.atlas = atlas;
		sprite = new Sprite(atlas.findRegion("idle_s"));
		sprite.setBounds(2, 2, 1, 1);
		animation = new Animation<>(.6f, atlas.findRegions("idle_s"));
		animationName = "idle_s";
	}
	
	public void render(Batch batch, float delta) {
		sprite.setRegion(animation.getKeyFrame(stateTime, true));
		sprite.draw(batch);
		stateTime += delta;
	}
	
	public void animate(String animationName, float speed) {
		if (this.animationName.equals(animationName))
			return;
		this.animationName = animationName;
		animation = new Animation<>(speed, atlas.findRegions(animationName));
		stateTime = 0f;
	}
	
	public Sprite getSprite() {
		return sprite;
	}
	
	public float getY() {
		return sprite.getY();
	}
	
	public void set(MonsterPacket packet) {
		sprite.setPosition(packet.x, packet.y);
	}
	
	public void dispose() {
		
	}
}
