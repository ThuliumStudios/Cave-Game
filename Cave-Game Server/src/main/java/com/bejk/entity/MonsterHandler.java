package com.bejk.entity;

import java.util.Timer;
import java.util.TimerTask;

import com.bejk.net.packet.MonsterPacket;

public class MonsterHandler {
	private static Timer timer = new Timer();

	public static void queueSpawn(MonsterPacket packet) {
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				packet.hp = 5;
				packet.x = 15;
				packet.y = 15;
			}
		}, 5000);
	}
}
