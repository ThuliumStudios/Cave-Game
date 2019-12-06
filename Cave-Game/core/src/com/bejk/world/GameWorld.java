package com.bejk.world;

import java.util.Arrays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.bejk.entity.Monster;
import com.bejk.main.MainGame;
import com.bejk.net.NetworkHandler;
import com.bejk.player.Player;
import com.bejk.player.PlayerInput;
import com.bejk.player.PlayerOverlay;
import com.bejk.util.Units;

public class GameWorld {
	private Array<Object> toBeUpdated = new Array<>();
	private OrthographicCamera camera;
	private Viewport viewport;

	private NetworkHandler network;
	private PlayerOverlay overlay;
	private Player player;
	private GameMap map;
	private World world;

	// TODO: Delete
	private Box2DDebugRenderer debug;
	private ShapeRenderer shapes;
	private boolean isDebugging = false;

	public GameWorld(MainGame game, int ID) {
		camera = new OrthographicCamera(Units.WORLD_WIDTH, Units.WORLD_HEIGHT);
		viewport = new StretchViewport(Units.WORLD_WIDTH, Units.WORLD_HEIGHT, camera);
		viewport.apply();
		camera.update();

		map = new GameMap(game.getBatch());
		player = new Player(game.getAsset("img/player.atlas", TextureAtlas.class), ID);
		network = game.getNetworkHandler();

		world = new World(Vector2.Zero, true);
		player.createBody(world.createBody(player.getBodyDef(3, 3)), player, .3f, .1f);
		
		overlay = new PlayerOverlay(game.getSkin());
		
		shapes = new ShapeRenderer();

		map.createBox2dObjects(world, new BodyDef(), new FixtureDef());
		debug = new Box2DDebugRenderer();
		Gdx.input.setInputProcessor(new InputMultiplexer(new PlayerInput(player), overlay));
	}

	public void render(Batch batch, float delta) {
		map.render(camera);

		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		network.render(batch, player, delta);
		player.render(batch, delta);
		batch.end();
		
		shapes.setProjectionMatrix(camera.combined);
		shapes.begin(ShapeType.Filled);
		network.renderHP(shapes);
		shapes.end();
		
		overlay.render(delta);

		camera.position.set(player.getX(), player.getY(), 0);
		camera.update();

		if (Gdx.input.isKeyJustPressed(Keys.F12))
			isDebugging = !isDebugging;

		world.step(1 / 60f, 6, 2);
		if (isDebugging)
			debug.render(world, camera.combined);
		if (player.hasUpdates()) {
			Arrays.asList(player.sendUpdates().shrink()).forEach(toBeUpdated::add);
			player.clearUpdates();
		}
	}
	
	public Array<Object> getUpdates() {
		return toBeUpdated;
	}

	public void resize(int width, int height) {
		viewport.update(width, height);
	}

	public void dispose() {
		debug.dispose();
		world.dispose();
		map.dispose();

		shapes.dispose();
	}
}
