package com.bejk.net;

import com.bejk.net.packet.DisconnectionPacket;
import com.bejk.net.packet.PlayerPacket;
import com.esotericsoftware.kryo.Kryo;

public class Registration {

	public static void register(Kryo kryo) {
		kryo.register(PlayerPacket.class);
		kryo.register(DisconnectionPacket.class);
	}
}
