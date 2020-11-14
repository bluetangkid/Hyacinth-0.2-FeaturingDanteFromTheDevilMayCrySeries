package com.hyacinth.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.hyacinth.Game;

public class DesktopLauncher {
	static LwjglApplication application;
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.backgroundFPS = 30;
		config.foregroundFPS = 144;
		config.width = 1920;
		config.height = 1080;
		config.fullscreen = true;
		application = new LwjglApplication(new Game(), config);
	}
}