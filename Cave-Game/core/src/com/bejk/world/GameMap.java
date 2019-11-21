package com.bejk.world;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.bejk.util.Units;

public class GameMap {
	private TiledMap map;
	private OrthogonalTiledMapRenderer mapRenderer;
	
	public GameMap(Batch batch) {
		map = new TmxMapLoader().load("maps/map0x0.tmx");
		mapRenderer = new OrthogonalTiledMapRenderer(map, 1/64f, batch);
	}
	
	public void render(OrthographicCamera camera) {
		mapRenderer.setView(camera);
		mapRenderer.render();
	}
	
	public void dispose() {
		map.dispose();
		mapRenderer.dispose();
	}
	
	/*
	 * 
	 */
	public void createBox2dObjects(World world, BodyDef bodyDef, FixtureDef fixtureDef) {
		TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get("collision");

		for (int y = 0; y < layer.getHeight(); y++) {
			for (int x = 0; x < layer.getWidth(); x++) {
				Cell cell = layer.getCell(x, y);

				if (cell == null || cell.getTile() == null)
					continue;
				bodyDef.type = BodyType.StaticBody;
				bodyDef.position.set(((x + .5f) ), ((y + .5f)));

				ChainShape cs = new ChainShape();
				Vector2[] v = new Vector2[5];
				v[0] = new Vector2(-.5f, -.5f);
				v[1] = new Vector2(-.5f, .5f);
				v[2] = new Vector2(.5f, .5f);
				v[3] = new Vector2(.5f, -.5f);
				v[4] = new Vector2(v[0]);
				cs.createChain(v);
				
				fixtureDef.friction = 0;
				fixtureDef.shape = cs;
				fixtureDef.filter.categoryBits = Units.GROUND_FLAG;
				fixtureDef.filter.maskBits = Units.ENTITY_FLAG;
				fixtureDef.isSensor = false;
				world.createBody(bodyDef).createFixture(fixtureDef);
				cs.dispose();
			}
		}
	}
}
