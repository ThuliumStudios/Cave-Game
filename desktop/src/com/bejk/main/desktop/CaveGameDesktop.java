package com.bejk.main.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.bejk.main.MainGame;
import com.bejk.util.Units;

public class CaveGameDesktop {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "The Magnificent Brandin/Ethan/Joel/Kyle Cave Game (Trademarked)";
		config.width = 1280;
		config.height = Math.round(config.width * Units.RATIO);
		new LwjglApplication(new MainGame(), config);
	}
}
