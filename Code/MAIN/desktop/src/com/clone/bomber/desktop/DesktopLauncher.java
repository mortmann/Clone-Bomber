package com.clone.bomber.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.clone.bomber.Bomber;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.height=720;
		config.width=1280;
		config.resizable = false;
		config.foregroundFPS=10000;
		config.fullscreen = false;  
		config.vSyncEnabled = false;
		new LwjglApplication(new Bomber(), config);
	}
}
