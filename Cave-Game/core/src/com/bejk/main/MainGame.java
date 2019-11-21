package com.bejk.main;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.bejk.entity.OnlinePlayer;
import com.bejk.net.GameClient;
import com.bejk.net.NetworkHandler;
import com.bejk.net.packet.PlayerPacket;
import com.bejk.screen.SplashScreen;
import com.bejk.util.DataHandler;
import com.bejk.util.SpriteAccessor;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
import aurelienribon.tweenengine.equations.Linear;

public class MainGame extends Game {
	private Batch batch;
	private Skin skin;

	private GameClient client;
	private NetworkHandler handler;
	private DataHandler dataHandler;
	private TweenManager tweenManager;

	@Override
	public void create() {
		batch = new SpriteBatch();
		tweenManager = new TweenManager();
		dataHandler = new DataHandler();
		handler = new NetworkHandler(this);
		skin = new Skin(Gdx.files.internal("skin/rusty-robot-ui.json"));
		
		Tween.registerAccessor(Sprite.class, new SpriteAccessor());
		setScreen(new SplashScreen(this));
	}

	public void connect() {
		handler = new NetworkHandler(this);
		client = new GameClient(handler, this);
		client.connect();
	}

	@Override
	public void render() {
		super.render();
		tweenManager.update(Gdx.graphics.getDeltaTime());
	}

	public NetworkHandler getNetworkHandler() {
		return handler;
	}

	public Batch getBatch() {
		return batch;
	}
	
	public Skin getSkin() {
		return skin;
	}
	
	public TweenManager getTween() {
		return tweenManager;
	}

	public <T> T getAsset(String path, Class<T> type) {
		return dataHandler.getAsset(path, type);
	}

	public void sendData(Object o) {
		client.sendPlayer((PlayerPacket) o);
	}

	public void interpolatePlayer(Sprite sprite, float x, float y, float duration) {
		Tween.to(sprite, SpriteAccessor.POS_XY, duration).target(x, y).ease(Linear.INOUT).start(tweenManager);
	}
	
	public boolean isDoneLoading() {
		return dataHandler.isDoneLoading();
	}

	@Override
	public void dispose() {
		getScreen().dispose();
		dataHandler.dispose();
		skin.dispose();
		if (client != null)
			client.dispose();
	}
}
