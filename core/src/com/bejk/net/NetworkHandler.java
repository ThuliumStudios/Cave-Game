package com.bejk.net;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.ObjectMap;
import com.bejk.entity.OnlinePlayer;
import com.bejk.main.MainGame;
import com.bejk.net.packet.PlayerPacket;

public class NetworkHandler {
	private ObjectMap<Integer, OnlinePlayer> players = new ObjectMap<>();

	private MainGame game;

	public NetworkHandler(MainGame game) {
		this.game = game;
	}

	public void render(Batch batch, float delta) {
		players.values().forEach(p -> p.render(batch, delta));
	}

	public void addNewPlayer(PlayerPacket packet) {
		OnlinePlayer newPlayer = new OnlinePlayer(game.getAsset("img/player.atlas", TextureAtlas.class));
		newPlayer.set(packet);
		players.put(packet.ID, newPlayer);
	}

	public void updatePlayer(PlayerPacket packet) {
		// TODO: Remove this mess
		if (players.containsKey(packet.ID)) {
			game.interpolatePlayer(players.get(packet.ID), packet);
			players.get(packet.ID).animate(packet.anim);
		}
		else
			addNewPlayer(packet);

		// players.get(packet.ID).set(packet);
	}

	public void removePlayer(int index) {
		players.remove(index);
	}

	public void dispose() {
		players.clear();
	}
}
