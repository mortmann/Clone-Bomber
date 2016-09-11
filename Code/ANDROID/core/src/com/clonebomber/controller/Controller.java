package com.clonebomber.controller;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Controller extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	Main main;
	@Override
	public void create () {
		 main = new Main();
	}

	@Override
	public void render () {
		main.render();
	}
	
}
