package com.bejk.player;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.bejk.util.Units;

public class PlayerInput implements InputProcessor {
	
	
	private Player player;

	public PlayerInput(Player player) {
		this.player = player;
	}

	@Override
	public boolean keyDown(int keycode) {
		switch (keycode) {
		case Keys.W:
			player.setDirection('n');
			player.setVelocity(0, Units.SPEED);
			player.animate("walk", true);
			break;
		case Keys.A:
			player.setDirection('w');
			player.setVelocity(-Units.SPEED, 0);
			player.animate("walk", true);
			break;
		case Keys.S:
			player.setDirection('s');
			player.setVelocity(0, -Units.SPEED);
			player.animate("walk", true);
			break;
		case Keys.D:
			player.setDirection('e');
			player.setVelocity(Units.SPEED, 0);
			player.animate("walk", true);
			break;
		case Keys.SPACE:
			player.attack();
			break;
		default:
			return false;
		}
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		switch (keycode) {
		case Keys.W:
		case Keys.S:
			player.stop();
			break;
		case Keys.A:
		case Keys.D:
			player.stop();
			break;
		}
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

}
