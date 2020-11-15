package com.hyacinth.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.hyacinth.Game;

public class DesktopLauncher {
	static Lwjgl3Application application;
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(144);
		config.setDecorated(false);
		config.setWindowedMode(1920, 1080);
		config.setResizable(false);
		application = new Lwjgl3Application(new Game(), config);
	}
}