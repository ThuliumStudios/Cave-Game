package com.bejk.combat;

import java.util.Random;

import com.bejk.net.packet.MonsterPacket;
import com.bejk.net.packet.PlayerPacket;

public class CombatHandler {
	private static final Random rand = new Random();
	
	// Constant values
	public static final float MIN_ACCURACY = Float.MIN_VALUE;

	public static float rollAccuracy() { 
		float baseAccuracy = 1;
		
		/*
		 * Put some magic formula here
		 */
		
		return Math.max(MIN_ACCURACY, baseAccuracy);
	}
	
	public static int rollDamage() {
		return 1;
	}
	
	public static int damageEnemy(MonsterPacket monster, PlayerPacket player) {
		if (rollAccuracy() >= rand.nextFloat())
			return rollDamage();
		return 0;
	}
}
