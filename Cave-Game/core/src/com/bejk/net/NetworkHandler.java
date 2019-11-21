package com.bejk.net;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
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

	public void addNewPlayer(PlayerPacket packet) {
		OnlinePlayer newPlayer = new OnlinePlayer(game.getAsset("img/player.atlas", TextureAtlas.class));
		newPlayer.set(packet);
		players.put(packet.ID, newPlayer);
	}

	public void addNewMonster(MonsterPacket packet) {
		Monster m = new Monster(game.getAsset("img/blob.atlas", TextureAtlas.class));
		m.set(packet);
		monsters.put(packet.ID, m);
	}

	public void updatePlayer(PlayerPacket packet) {
		// TODO: Remove this mess
		if (players.containsKey(packet.ID)) {
			game.interpolatePlayer(players.get(packet.ID).getSprite(), packet.x, packet.y, 1/14f);
			players.get(packet.ID).animate(packet.anim);
		} else
			addNewPlayer(packet);

		// players.get(packet.ID).set(packet);
	}

	public void updateMonster(MonsterPacket packet) {
		System.out.println("Got monster at "+packet.x + ","+packet.y);
		if (monsters.containsKey(packet.ID)) {
			//monsters.get(packet.ID).set(packet);
			game.interpolatePlayer(monsters.get(packet.ID).getSprite(), packet.x, packet.y, 1);
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
