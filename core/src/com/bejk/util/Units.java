package com.bejk.util;

public class Units {
	public static final int WORLD_WIDTH = 20;
	public static final int WORLD_HEIGHT = 12;
	public static final float RATIO = (WORLD_HEIGHT / (float) WORLD_WIDTH);
	public static final float SPEED = 5;

	public static final short GROUND_FLAG = 1 << 2;
	public static final short ENTITY_FLAG = 1 << 3;
}
