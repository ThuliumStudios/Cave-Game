package com.bejk.main;

import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;

import com.bejk.combat.CombatHandler;
import com.bejk.entity.MonsterHandler;
import com.bejk.net.Registration;
import com.bejk.net.packet.AttackRequest;
import com.bejk.net.packet.DisconnectionPacket;
import com.bejk.net.packet.MonsterPacket;
import com.bejk.net.packet.PlayerPacket;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

public class CaveGameServer {
	private HashMap<Integer, PlayerPacket> players = new HashMap<>();
	private HashMap<Integer, MonsterPacket> monsters = new HashMap<>();
	private HashMap<Integer, Object> updatedTCP = new HashMap<>();
	private HashMap<Integer, Object> updatedUDP = new HashMap<>();
	private Random random;
	private boolean isRunning = true;

	// Tick values
	private long delta, lastTime;

	private Server server;

	public CaveGameServer() {
		System.out.println("Beginning server creation. . .");
		server = new Server();
		Registration.register(server.getKryo());
		try {
			server.bind(55668, 55667);
		} catch (IOException e) {
			e.printStackTrace();
		}
		server.start();
		server.addListener(serverListener);

		long seed = System.currentTimeMillis();
		System.out.println("Seed: " + seed);
		random = new Random(seed);

//		while (isRunning) {
//			tick();
//		}

		// TODO: Replace with 'tick' method
		Timer tickTimer = new Timer();
		tickTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				if (isRunning)
					sendServerData();
			}
		}, 3000, 1000 / 10L);
		Timer monsterMovement = new Timer();
		monsterMovement.schedule(new TimerTask() {
			@Override
			public void run() {
				if (isRunning)
					calculate();
			}
		}, 10000, 6000);

		// TODO: Delete ------------------------------>
		MonsterPacket mp = new MonsterPacket();
		mp.eID = 0;
		mp.mID = 0;
		mp.hp = 5;
		mp.x = 15;
		mp.y = 15;
		monsters.put(mp.eID, mp);
		// ------------------------------------------->
	}

	public void sendServerData() {
		updatedUDP.forEach((k, v) -> server.sendToAllExceptUDP(k, v));
		updatedTCP.forEach((k, v) -> server.sendToAllExceptTCP(k, v));
		updatedUDP.clear();
		updatedTCP.clear();
	}

	public void calculate() {
		monsters.values().forEach(m -> {
			if (m.hp <= 0)
				return;
			m.x = (float) Math.max((m.x - 5) + random.nextInt(10), 1);
			m.y = (float) Math.max((m.y - 5) + random.nextInt(10), 1);
			queueUpdate(-m.eID, m, true);
		});
	}

	public void processAttack(AttackRequest attack) {
		System.out.println("Attacked");
		Rectangle2D attackBox = new Rectangle2D.Float(attack.x, attack.y, 1, 1);
		Rectangle2D monsterHitbox = new Rectangle2D.Float();
		monsters.forEach((k, v) -> {
			if (v.hp <= 0) {
				System.out.println("Hims dead");
				return;
			}
			monsterHitbox.setRect(v.x, v.y, 1, 1);
			System.out.println("Atacked at " + attackBox.getX() + "," + attackBox.getY());
			System.out.println("Monster is at " + monsterHitbox.getX() + "," + monsterHitbox.getY());
			if (attackBox.intersects(monsterHitbox)) {
				attackMonster(v, attack);
			}
			queueUpdate(-v.eID, v, true);
		});
	}

	public void attackMonster(MonsterPacket monster, AttackRequest attacker) {
		monster.hp -= CombatHandler.damageEnemy(monster, players.get(attacker.attackerID));
		if (monster.hp <= 0) {
			MonsterHandler.queueSpawn(monster);
			queueUpdate(-monster.eID, monster, true);
		}
	}

	public boolean isDead(MonsterPacket monster) {
		return monster.hp <= 0;
	}

	public void killMonster(MonsterPacket packet) {
		System.out.println("Killing monster");
	}

	public void killPlayer() {

	}

	public void queueUpdate(int ID, Object object, boolean tcp) {
		Object o = tcp ? updatedTCP.put(ID, object) : updatedUDP.put(ID, object);
	}

	public void updatePlayer(PlayerPacket newPacket) {
		int ID = newPacket.ID;
		if (!players.containsKey(ID))
			players.put(ID, new PlayerPacket());
		players.put(ID, newPacket);
		queueUpdate(ID, newPacket, false);
	}

	/**
	 * Server listener -> handles packets received
	 */
	private Listener serverListener = new Listener() {
		@Override
		public void received(Connection connection, Object object) {
			switchType(object, caze(PlayerPacket.class, packet -> updatePlayer(packet)),
					caze(AttackRequest.class, packet -> processAttack(packet)));
		};

		@Override
		public void connected(Connection connection) {
			super.connected(connection);
		}

		@Override
		public void disconnected(Connection connection) {
			DisconnectionPacket packet = new DisconnectionPacket();
			packet.ID = connection.getID();
			server.sendToAllExceptTCP(connection.getID(), packet);
		};
	};

	/**
	 * Consumer classes. Allows for a semblance of "switch case" statements with
	 * class types and packet reception.
	 */
	public static <T> void switchType(Object o, Consumer<?>... a) {
		for (Consumer consumer : a)
			consumer.accept(o);
	}

	public static <T> Consumer<?> caze(Class<T> cls, Consumer<T> c) {
		return obj -> Optional.of(obj).filter(cls::isInstance).map(cls::cast).ifPresent(c);
	}

	/**
	 * Main method
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		new CaveGameServer();
	}

	public void tick() {
		long now = System.nanoTime();

		delta += (now - lastTime) / (1000000000.0 / 15.0);
		lastTime = now;

		while (delta >= 1) {
			sendServerData();
			delta--;
		}
	}
}
