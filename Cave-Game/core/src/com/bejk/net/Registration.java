package com.bejk.net;

import java.util.Arrays;

import com.bejk.net.packet.AttackRequest;
import com.bejk.net.packet.DisconnectionPacket;
import com.bejk.net.packet.MonsterPacket;
import com.bejk.net.packet.PlayerPacket;
import com.esotericsoftware.kryo.Kryo;

public class Registration {

	public static void register(Kryo kryo) {
		Arrays.asList(PlayerPacket.class, MonsterPacket.class, DisconnectionPacket.class, AttackRequest.class)
				.forEach(kryo::register);
	}
}
