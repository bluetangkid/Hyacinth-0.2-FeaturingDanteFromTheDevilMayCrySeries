package com.hyacinth;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.hyacinth.entities.Constants;
import com.hyacinth.entities.DynamicEntity;
import com.hyacinth.entities.Player;
import com.hyacinth.scenes.LevelSelect;
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
	private TiledRenderer renderer;
	private LevelSelect levelSelect;
	private Music mainMusic;

	@Override
	public void create () {
		state = GameState.TITLE;
		generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/pixeboy.ttf"));
		batch = new SpriteBatch();
		camera = new OrthographicCamera(1280/1.2f, 720/1.2f);//changing these values is just zooming in/out

		audio = Gdx.audio;
		mainMusic = audio.newMusic(Gdx.files.internal("sound/Chucky Chease Beats.mp3"));
		mainMusic.setLooping(true);
		mainMusic.setVolume(.2f);
		mainMusic.play();

		title = new Title(generator);
		cursor = new Texture(Gdx.files.internal("textures/cursor.png"));
		levels = new PlayingLevel[(int)Gdx.files.internal("core/assets/levels/").list().length];
		for (int i = 0; i < levels.length; i++){
			levels[i] = new PlayingLevel(loadMap("levels/level_" + i + ".tmx"));
		}
		renderer = new TiledRenderer();
		levelSelect = new LevelSelect(generator, levels);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(.3f, 0.3f, 0.3f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();
		if(state == GameState.TITLE) {
			if (title.draw(camera)) {
				state = GameState.LEVEL_SELECT;
			}
		} else if(state == GameState.LEVEL_SELECT){
			int selection = levelSelect.draw(camera);
			if (selection > 0){
				level = selection;
				state = GameState.GAME;
			}
		} else if(state == GameState.GAME){
			levels[0].render(camera, renderer);
		}
		batch.begin();
		batch.draw(cursor, Gdx.input.getX() - 7, 1080 - Gdx.input.getY() - 7);
		batch.end();
	}

	private TiledMap loadMap(String map){//TODO change this to load all maps in /levels
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
class TiledRenderer implements TiledMapRenderer {

	@Override
	public void renderObjects(MapLayer layer) {

	}

	@Override
	public void renderObject(MapObject object) {

	}

	@Override
	public void renderTileLayer(TiledMapTileLayer layer) {

	}

	@Override
	public void renderImageLayer(TiledMapImageLayer layer) {

	}

	@Override
	public void setView(OrthographicCamera camera) {

	}

	@Override
	public void setView(Matrix4 projectionMatrix, float viewboundsX, float viewboundsY, float viewboundsWidth, float viewboundsHeight) {

	}

	@Override
	public void render() {

	}

	@Override
	public void render(int[] layers) {

	}
}