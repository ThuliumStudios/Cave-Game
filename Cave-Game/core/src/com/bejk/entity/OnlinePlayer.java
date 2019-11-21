package com.bejk.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.bejk.net.packet.PlayerPacket;

public class OnlinePlayer {
	private Animation<TextureRegion> animation;
	private TextureAtlas atlas;
	private Sprite sprite;
	private String anim;
	private float stateTime;
	
	public OnlinePlayer(TextureAtlas atlas) {
		this.atlas = atlas;
		sprite = new Sprite(atlas.findRegion("idle_s", 0));
		sprite.setBounds(0, 0, .7f, 1);
		sprite.setColor(new Color(0xafafafff));
		stateTime = 0f;
		anim = "idle_s";
		animation = new Animation<>(.125f, atlas.findRegions("idle_s"));
	}
	
	public void render(Batch batch, float delta) {
		sprite.setRegion(animation.getKeyFrame(stateTime, true));
		sprite.draw(batch);
		stateTime += delta;
	}
	
	public void animate(String anim) {
		if (this.anim.equalsIgnoreCase(anim))
			return;
		animation = new Animation<>(.125f, atlas.findRegions(anim));
		stateTime = 0f;
		this.anim = anim;
	}
	
	public void set(PlayerPacket packet) {
		sprite.setPosition(packet.x, packet.y);
	}
	
	public Sprite getSprite() {
		return sprite;
	}
}
