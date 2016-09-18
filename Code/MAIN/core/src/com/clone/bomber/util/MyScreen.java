package com.clone.bomber.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.clone.bomber.GameClass;

public abstract class MyScreen implements Screen, InputProcessor{
	protected Vector3 normalCameraPos;
	protected OrthographicCamera camera;
	protected Viewport viewPort;
	protected Stage stage;
	protected Skin skin;
	protected SpriteBatch spriteBatch;
	protected GameClass gameClass;

	@Override
	public void show(){
		spriteBatch = new SpriteBatch();
		camera = new OrthographicCamera(GameClass.viewportWidth,
				GameClass.viewportHeight);
		normalCameraPos=new Vector3(GameClass.viewportWidth / 2,
				GameClass.viewportHeight / 2, 0);
		camera.translate(normalCameraPos);
		viewPort = new FitViewport(GameClass.viewportWidth,
				GameClass.viewportHeight, camera);
		viewPort.apply();	
		stage = new Stage();
		stage.setViewport(viewPort);
		skin = new Skin(Gdx.files.internal("res/gui/uiskin.json"));		
		spriteBatch.setProjectionMatrix(viewPort.getCamera().combined);

		//input stuff
		InputMultiplexer inputMultiplexer = new InputMultiplexer();
		inputMultiplexer.addProcessor(stage);
		inputMultiplexer.addProcessor(this);
		Gdx.input.setInputProcessor(inputMultiplexer);
		OnShow();
	}
	
	public abstract void OnShow();

	@Override
	public void render(float delta) {
		stage.draw();
		stage.act(delta);
	}

	@Override
	public void resize(int width, int height) {
		viewPort.update(width, height);
		stage.getViewport().update(width, height,true); 		
	}

	@Override
	public void pause() {
		
	}

	@Override
	public void resume() {
		
	}

	@Override
	public void hide() {
		Gdx.input.setInputProcessor(null);
	}

	@Override
	public void dispose() {
		stage.dispose();
	}
	
	@Override
	public boolean keyDown(int keycode){
		return false;
	}
	
	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}
}
