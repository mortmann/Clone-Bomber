package com.clonebomber.controller.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.clonebomber.controller.Controller;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.height=540;
		config.width=960;
		config.resizable = false;
		new LwjglApplication(new Controller(), config);
		
	}
}
