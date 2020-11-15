package com.hyacinth;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.math.Matrix4;
import com.hyacinth.scenes.LevelSelect;
import com.hyacinth.scenes.PlayingLevel;
import com.hyacinth.scenes.Title;

public class Game extends ApplicationAdapter {
	private SpriteBatch batch, bgbatch;
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
	private Texture bg, controlScheme;

	@Override
	public void create () {
		Gdx.graphics.setUndecorated(true);
		Gdx.graphics.setWindowedMode(Gdx.graphics.getDisplayMode().width, Gdx.graphics.getDisplayMode().height);
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getBackBufferWidth(), Gdx.graphics.getBackBufferHeight());
		Gdx.graphics.setTitle("Hyacinth 0.2");
		state = GameState.TITLE;
		generator = new FreeTypeFontGenerator(Gdx.files.internal("data/fonts/pixeboy.ttf"));
		batch = new SpriteBatch();
		camera = new OrthographicCamera(1280/1.2f, 720/1.2f);//changing these values is just zooming in/out
		audio = Gdx.audio;
		mainMusic = audio.newMusic(Gdx.files.internal("data/sound/Chucky Chease Beats.mp3"));
		mainMusic.setLooping(true);
		mainMusic.setVolume(.2f);

		title = new Title(generator);
		cursor = new Texture(Gdx.files.internal("data/textures/cursor.png"));
		levels = new PlayingLevel[(int)Gdx.files.internal("core/assets/data/levels/").list().length];
		for (int i = 0; i < levels.length; i++){
			levels[i] = new PlayingLevel(loadMap("data/levels/level_" + i + ".tmx"), camera, generator);
		}
		renderer = new TiledRenderer();
		levelSelect = new LevelSelect(generator, levels);
		bg = new Texture(Gdx.files.internal("data/textures/bg.png"));
		bgbatch = new SpriteBatch();
		controlScheme = new Texture(Gdx.files.internal("data/textures/controls.png"));
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(.1f, 0.35f, 0.43f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.update();
		bgbatch.setProjectionMatrix(camera.combined);
		if(state == GameState.TITLE) {
			bgbatch.begin();
			bgbatch.draw(bg, -1280/(1.2f*2), -720/(1.2f*2), 1280/1.2f, 720/1.2f);
			bgbatch.end();
			GameState status = title.draw(camera);
			if (status != GameState.DEFAULT) {
				state = status;
			}
		} else if(state == GameState.LEVEL_SELECT){
			bgbatch.begin();
			bgbatch.draw(bg, -1280/(1.2f*2), -720/(1.2f*2), 1280/1.2f, 720/1.2f);
			bgbatch.end();
			int selection = levelSelect.draw(camera);
			if (selection >= 0){
				level = selection;
				levels[level].reset();
				state = GameState.GAME;
				mainMusic.play();
			}
		} else if(state == GameState.GAME){
			bgbatch.begin();
			bgbatch.draw(bg, 0, 0, 1024, 576);
			bgbatch.end();
			int status = levels[level].render(camera, renderer);
			if(status > 0){
				state = GameState.COMPLETE;
			}else if(status < 0){
				state = GameState.DEATH;
			}
		} else if(state == GameState.DEATH) {
			//TODO: death screen
			levels[level].reset();
			state = GameState.GAME;
		} else if (state == GameState.COMPLETE) {
			//TODO: complete screen
			if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) state = GameState.LEVEL_SELECT;
		} else if (state == GameState.CONTROLS) {
			batch.begin();
			batch.draw(controlScheme, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			batch.end();
		}

		//at any point,
		if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
			state = GameState.TITLE;
			camera.position.x = 0;
			camera.position.y = 0;
			camera.position.z = 0;
		}

		batch.begin();
		batch.draw(cursor, Gdx.input.getX() - 7, 1080 - Gdx.input.getY() - 7);
		batch.end();
	}

	private TiledMap loadMap(String map){
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