package com.bejk.player;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class PlayerOverlay extends Stage {
	private Table inventory;

	public PlayerOverlay(Skin skin) {
		super();
		
		inventory = new Table(skin);
		inventory.setBounds(getWidth() * (3/4f), 0, getWidth() / 4f, getHeight() / 4f);
		inventory.debug();
		
		addActor(inventory);
	}
	
	public void render(float delta) {
		act(delta);
		draw();
	}
	
	public void dispose() {
		super.dispose();
	};
}
