package com.hyacinth;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.hyacinth.entities.Constants;
import com.hyacinth.entities.DynamicEntity;
import com.hyacinth.entities.Player;
import com.hyacinth.scenes.PlayingLevel;
import com.hyacinth.scenes.Title;

import java.util.ArrayList;

public class Game extends ApplicationAdapter {
	private SpriteBatch batch;
	private OrthographicCamera camera;
	private AssetManager assetManager;
	private int tileWidth, tileHeight, mapWidthInTiles, mapHeightInTiles, mapWidthInPixels, mapHeightInPixels;
	private Audio audio;
	private int level;
	private GameState state;
	private FreeTypeFontGenerator generator;
	private Title title;
	private Texture cursor;
	private PlayingLevel[] levels;

	@Override
	public void create () {
		state = GameState.TITLE;
		generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/pixeboy.ttf"));
		batch = new SpriteBatch();
		camera = new OrthographicCamera(1280, 720);//changing these values is just zooming in/out

		audio = Gdx.audio;


		title = new Title(generator);
		cursor = new Texture(Gdx.files.internal("textures/cursor.png"));
		loadMap("levels/test_level.tmx");

	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(.3f, 0.3f, 0.3f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.update();
		if(state == GameState.TITLE) {
			if (title.draw(camera)) {
				state = GameState.GAME;
			}
		} else {

		}
		batch.begin();
		batch.draw(cursor, Gdx.input.getX() - 7, 1080 - Gdx.input.getY() - 7);
		batch.end();
	}

	private TiledMap loadMap(String map){//TODO chage this to load all maps in /levels
		assetManager = new AssetManager();
		assetManager.setLoader(TiledMap.class, new TmxMapLoader());
		assetManager.load(map, TiledMap.class);
		assetManager.finishLoading();
		return assetManager.get(map, TiledMap.class);
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}