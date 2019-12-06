package com.bejk.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.bejk.net.packet.MonsterPacket;

public class Monster {
	private Animation<TextureRegion> animation;
	private TextureAtlas atlas;
	private Sprite sprite;

	private String animationName;
	private float stateTime;
	private int hp;
	
	// TODO: Replace with actual max hp
	private int maxHp = 5;
	
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
	
	public void renderHP(ShapeRenderer shapes) {
		shapes.setColor(Color.RED);
		shapes.rect(sprite.getX(), sprite.getY() - (1/8f), sprite.getWidth(), 1/16f);
		shapes.setColor(Color.GREEN);
		shapes.rect(sprite.getX(), sprite.getY() - (1/8f), sprite.getWidth() * ((float) hp / maxHp), 1/16f);
	}
	
	public void update(MonsterPacket packet) {
		hp = packet.hp;
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
		update(packet);
	}
	
	public void dispose() {
		
	}
}
