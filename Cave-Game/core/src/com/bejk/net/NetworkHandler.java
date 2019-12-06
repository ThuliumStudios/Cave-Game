package com.bejk.net;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ObjectMap;
import com.bejk.entity.Monster;
import com.bejk.entity.OnlinePlayer;
import com.bejk.main.MainGame;
import com.bejk.net.packet.MonsterPacket;
import com.bejk.net.packet.PlayerPacket;
import com.bejk.player.Player;

public class NetworkHandler {
	private ObjectMap<Integer, OnlinePlayer> players = new ObjectMap<>();
	private ObjectMap<Integer, Monster> monsters = new ObjectMap<>();

	private MainGame game;

	public NetworkHandler(MainGame game) {
		this.game = game;
	}

	public void render(Batch batch, Player player, float delta) {
		players.values().forEach(p -> p.render(batch, delta));
		monsters.values().forEach(p -> p.render(batch, delta));
	}
	
	public void renderHP(ShapeRenderer shapes) {
		monsters.values().forEach(p -> p.renderHP(shapes));
	}

	public void addNewPlayer(PlayerPacket packet) {
		OnlinePlayer newPlayer = new OnlinePlayer(game.getAsset("img/player.atlas", TextureAtlas.class));
		newPlayer.set(packet);
		players.put(packet.ID, newPlayer);
	}

	public void addNewMonster(MonsterPacket packet) {
		Monster m = new Monster(game.getAsset("img/blob.atlas", TextureAtlas.class));
		m.set(packet);
		monsters.put(packet.eID, m);
	}

	public void updatePlayer(PlayerPacket packet) {
		// TODO: Remove this mess
		if (players.containsKey(packet.ID)) {
			game.interpolatePlayer(players.get(packet.ID).getSprite(), packet.x, packet.y, 1/9f);
			players.get(packet.ID).animate(packet.anim);
		} else
			addNewPlayer(packet);

		// players.get(packet.ID).set(packet);
	}

	public void updateMonster(MonsterPacket packet) {
		if (monsters.containsKey(packet.eID)) {
			game.interpolatePlayer(monsters.get(packet.eID).getSprite(), packet.x, packet.y, 1);
			monsters.get(packet.eID).update(packet);
			//TODO: Tidy code up
			if (packet.hp <= 0) {
				monsters.remove(packet.eID);
			}
		} else
			addNewMonster(packet);
	}

	public void removePlayer(int index) {
		players.remove(index);
	}

	public void dispose() {
		players.clear();
	}
}
