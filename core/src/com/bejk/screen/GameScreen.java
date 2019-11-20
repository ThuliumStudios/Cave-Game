package com.bejk.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.bejk.main.MainGame;
import com.bejk.net.NetworkHandler;
import com.bejk.world.GameWorld;

public class GameScreen implements Screen {
	private Batch batch;
	
	private GameWorld world;
	
	private MainGame game;
	
	public GameScreen(MainGame game, int ID) {
		this.game = game;
		
		world = new GameWorld(game, ID);
	}

	@Override
	public void show() {
		batch = game.getBatch();
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		world.render(batch, delta);
		if (!world.getUpdates().isEmpty())
			sendUpdates();
	}
	
	public void sendUpdates() {
		world.getUpdates().forEach(game::sendData);
		world.getUpdates().clear();
	}

	@Override
	public void resize(int width, int height) {
		world.resize(width, height);
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void hide() {
	}

	@Override
	public void dispose() {
		world.dispose();
	}

}
