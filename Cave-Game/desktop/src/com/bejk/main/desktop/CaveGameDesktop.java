package com.bejk.main.desktop;

import java.util.Arrays;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.bejk.main.MainGame;
import com.bejk.util.Units;

public class CaveGameDesktop {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "C A V E   G A M E   (Development Mode)";
		config.width = 1280;
		config.height = Math.round(config.width * Units.RATIO);
		Arrays.asList(128, 32, 16).forEach(size -> config.addIcon("ico/icon" + size + ".png", FileType.Internal));
		new LwjglApplication(new MainGame(), config);
	}
}
