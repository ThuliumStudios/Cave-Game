package com.bejk.util;

import java.util.Arrays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class DataHandler {
	private AssetManager assets;

	public DataHandler() {
		assets = new AssetManager();
		Arrays.asList("background", "blob", "player").forEach(this::loadAsset);
	}

	public void loadAsset(String str) {
		String suffix = ".atlas";
		Class<?> clazz = TextureAtlas.class;
		if (!Gdx.files.internal("img/" + str + ".atlas").exists()) {
			suffix = ".png";
			clazz = Texture.class;
		}

		assets.load("img/" + str + suffix, clazz);
	}

	public <T> T getAsset(String path, Class<T> type) {
		return assets.get(path, type);
	}

	public boolean isDoneLoading() {
		return assets.update();
	}

	public void dispose() {
		assets.clear();
	}
}
