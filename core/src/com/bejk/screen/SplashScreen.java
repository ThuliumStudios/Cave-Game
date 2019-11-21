package com.bejk.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.bejk.main.MainGame;
import com.bejk.util.SpriteAccessor;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;

public class SplashScreen implements Screen {
	private Batch batch;
	private Texture logoTexture;
	private Sprite logo;

	private MainGame game;

	public SplashScreen(MainGame game) {
		this.game = game;
	}

	@Override
	public void show() {
		logoTexture = new Texture(Gdx.files.internal("img/logo.png"));
		logo = new Sprite(logoTexture);
		logo.setBounds(Gdx.graphics.getWidth() / 4f, Gdx.graphics.getHeight() / 4f, 
				Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f);
		batch = game.getBatch();
		
		Tween.set(logo, SpriteAccessor.OPACITY).target(0).start(game.getTween());
		Tween.to(logo, SpriteAccessor.OPACITY, 1).target(1).repeatYoyo(1, 1)
				.setCallback(new TweenCallback() {
					@Override
					public void onEvent(int type, BaseTween<?> source) {
						unsplash();
					}
				}).start(game.getTween());
		game.getTween().update(Float.MIN_VALUE);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		batch.begin();
		logo.draw(batch);
		batch.end();
		
		game.isDoneLoading();
	}

	@Override
	public void resize(int width, int height) {
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

	public void unsplash() {
		logoTexture.dispose();
		game.setScreen(new MainMenu(game));
	}

	@Override
	public void dispose() {
		logoTexture.dispose();
		batch.dispose();
	}

}
