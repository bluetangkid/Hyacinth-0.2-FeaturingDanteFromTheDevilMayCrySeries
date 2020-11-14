package com.hyacinth;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.hyacinth.entities.Constants;
import com.hyacinth.entities.DynamicEntity;
import com.hyacinth.entities.Player;

import java.util.ArrayList;

public class Game extends ApplicationAdapter {
	private SpriteBatch batch;
	private World world;
	private float accumulator = 0;
	private Box2DDebugRenderer debugRenderer;
	private OrthographicCamera camera;
	private AssetManager assetManager;
	private TiledMap map;
	private MapProperties mapProperties;
	private OrthogonalTiledMapRenderer mapRenderer;
	private long time;
	private int tileWidth, tileHeight, mapWidthInTiles, mapHeightInTiles, mapWidthInPixels, mapHeightInPixels;
	private Audio audio;
	private int level;
	private float timeStep;
	private Player player;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		world = new World(new Vector2(0, -Constants.GRAVITY), true);
		debugRenderer = new Box2DDebugRenderer();
		camera = new OrthographicCamera(1920, 1080);
		player = new Player(world);
		audio = Gdx.audio;
		timeStep = 1f/Gdx.graphics.getDisplayMode().refreshRate;
		BodyDef groundBodyDef = new BodyDef();
		groundBodyDef.position.set(new Vector2(0, -70));
		Body groundBody = world.createBody(groundBodyDef);
		PolygonShape groundBox = new PolygonShape();
		groundBox.setAsBox(camera.viewportWidth, 20.0f);
		groundBody.createFixture(groundBox, 0.0f);
		groundBox.dispose();
		world.setContactListener(new GroundListener(this));
	}

	@Override
	public void render () {
		Array<Body> bodies = new Array<Body>();
		world.getBodies(bodies);
		for(Body b : bodies) {
			DynamicEntity entity = (DynamicEntity) b.getUserData();
			if(entity != null){
				System.out.println(b.getPosition());
				entity.update();
			}
		}
		Gdx.gl.glClearColor(.3f, 0.3f, 0.3f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		//batch.begin();
		camera.update();
		debugRenderer.render(world, camera.combined);
		//batch.end();
		doPhysicsStep(System.currentTimeMillis() - time);
		time = System.currentTimeMillis();
	}

	private void doPhysicsStep(float deltaTime) {
		float frameTime = Math.min(deltaTime, 0.25f);
		accumulator += frameTime;
		while (accumulator >= timeStep) {
			world.step(timeStep, 6, 2);
			accumulator -= timeStep;
		}
	}

	private void loadMap(String map){
		assetManager = new AssetManager();
		assetManager.setLoader(TiledMap.class, new TmxMapLoader());
		assetManager.load(map, TiledMap.class);
		assetManager.finishLoading();
		this.map = assetManager.get(map, TiledMap.class);
		mapProperties = this.map.getProperties();
		MapProperties properties = this.map.getProperties();
		tileWidth         = properties.get("tilewidth", Integer.class);
		tileHeight        = properties.get("tileheight", Integer.class);
		mapWidthInTiles   = properties.get("width", Integer.class);
		mapHeightInTiles  = properties.get("height", Integer.class);
		mapWidthInPixels  = mapWidthInTiles  * tileWidth;
		mapHeightInPixels = mapHeightInTiles * tileHeight;

		camera.position.x = mapWidthInPixels * .5f;
		camera.position.y = mapHeightInPixels * .3f;
	}

	public void setPlayerGround(boolean bruh){
		player.setGround(bruh);
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}

class GroundListener implements ContactListener {
	Game world;

	public GroundListener(Game w){
		this.world = w;
	}

	@Override
	public void beginContact(Contact contact) {
		if(contact.getFixtureA().isSensor() || contact.getFixtureB().isSensor()){
			world.setPlayerGround(false);
		}
	}

	@Override
	public void endContact(Contact contact) {
		if((contact.getFixtureA().isSensor() || contact.getFixtureB().isSensor()) && !contact.isTouching()){
			world.setPlayerGround(true);
		}
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {

	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {

	}
}