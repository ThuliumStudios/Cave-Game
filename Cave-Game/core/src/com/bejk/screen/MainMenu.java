package com.bejk.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.utils.Align;
import com.bejk.main.MainGame;

public class MainMenu implements Screen {
	private Batch batch;
	private Stage stage;
	private Skin skin;
	
	private Texture background;
	
	private MainGame game;

	public MainMenu(MainGame game) {
		this.game = game;
	}

	@Override
	public void show() {
		background = game.getAsset("img/background.png", Texture.class);
		batch = game.getBatch();
		stage = new Stage();
		skin = game.getSkin();
		
		Table table = new Table(skin);
		Label title = new Label("Cave Game", skin);
		
		title.setAlignment(Align.center);
		title.setFontScale(5);
		
		TextButton continueGame = new TextButton("Continue", skin);
		TextButton newGame = new TextButton("New Game", skin);
		TextButton settings = new TextButton("Settings", skin);
		TextButton bestiary = new TextButton("Bestiary (?)", skin);
		TextButton credits = new TextButton("Credits", skin);
		continueGame.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				new SavedGames("Select Saved Profile", skin).show(stage);
			}
		});
		
		table.defaults().expand().width(Value.percentWidth(.5f, table)).growY();
		table.setFillParent(true);
		table.add(title).height(Value.percentHeight(.2f, table)).row();
		table.add(continueGame).row();
		table.add(newGame).row();
		table.add(settings).row();
		table.add(bestiary).row();
		table.add(credits).row();
		
		stage.addActor(table);
		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		batch.begin();
		batch.draw(background, 0, 0, stage.getWidth(), stage.getHeight());
		batch.end();
		
		stage.act(delta);
		stage.draw();
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

	@Override
	public void dispose() {
		stage.dispose();
	}

	private class SavedGames extends Dialog {
		public SavedGames(String title, Skin skin) {
			super(title, skin);
			
			String[] str = {};
			for (int i = 0; i < 3; i++) {
				button("Profile " + (i+1), i);
				getButtonTable().row();
			}
		}
		
		@Override
		protected void result(Object object) {
			super.result(object);
			game.connect();
		}
	}
}
