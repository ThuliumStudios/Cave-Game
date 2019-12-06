package com.bejk.net;

import java.io.IOException;
import java.util.Optional;
import java.util.function.Consumer;

import com.badlogic.gdx.Gdx;
import com.bejk.main.MainGame;
import com.bejk.net.packet.DisconnectionPacket;
import com.bejk.net.packet.MonsterPacket;
import com.bejk.net.packet.PlayerPacket;
import com.bejk.screen.GameScreen;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

public class GameClient {
	// private String IP = "192.168.1.2";
	private String IP = "173.26.38.149";

	private Client client;
	private NetworkHandler handler;
	
	private MainGame game;

	public GameClient(NetworkHandler handler, MainGame game) {
		this.handler = handler;
		this.game = game;
		client = new Client();
		client.start();
		Registration.register(client.getKryo());

		client.addListener(clientListener);
	}

	public boolean connect() {
		try {
			client.connect(5000, IP, 55668, 55667);
			game.setScreen(new GameScreen(game, getID()));
			return true;
		} catch (IOException e) {
			Gdx.app.log("Connection", "Unable to connect. \n" + e);
			return false;
		}
	}
	
	public void sendData(Object packet, boolean TCP) {
		if (TCP) 
			client.sendTCP(packet);
		else 
			client.sendUDP(packet);
	}

	private Listener clientListener = new Listener() {
		public void received(Connection connection, Object object) {
			switchType(object, caze(PlayerPacket.class, packet -> handler.updatePlayer(packet)),
					caze(MonsterPacket.class, packet -> handler.updateMonster(packet)),
					caze(DisconnectionPacket.class, packet -> handler.removePlayer(packet.ID)));
		};

		public void connected(Connection connection) {
		};

		public void disconnected(Connection connection) {
			
		};

	};
	
	public int getID() {
		return client.getID();
	}

	public void dispose() {
		client.close();
	}

	public static <T> void switchType(Object o, Consumer... a) {
		for (Consumer consumer : a)
			consumer.accept(o);
	}

	public static <T> Consumer<T> caze(Class<T> cls, Consumer<T> c) {
		return obj -> Optional.of(obj).filter(cls::isInstance).map(cls::cast).ifPresent(c);
	}
}
