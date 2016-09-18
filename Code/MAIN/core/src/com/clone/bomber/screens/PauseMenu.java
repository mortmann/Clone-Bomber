package com.clone.bomber.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.clone.bomber.GameClass;
import com.clone.bomber.util.MyScreen;

public class PauseMenu extends MyScreen{
	public static final int ID = 7;
	private TextButton resumeButton;
	private TextButton exitButton;
	private TextButton optionButton;
	
	
	public PauseMenu(GameClass gameClass){
		this.gameClass=gameClass;
	}
	


	@Override
	public void OnShow(){
		float x=GameClass.viewportWidth/2 - 100;
		float y=GameClass.viewportHeight/1.25f *1f;
		resumeButton=new TextButton("Resume", skin);
		resumeButton.setBounds(x, y- 125, 200, 50);
		optionButton=new TextButton("Options", skin);
		optionButton.setBounds(x, y-175, 200, 50);
		exitButton=new TextButton("Exit to mainmenu", skin);
		exitButton.setBounds(x, y-225, 200, 50);
		stage.addActor(exitButton);
		stage.addActor(optionButton);
		stage.setViewport(viewPort);
		stage.addActor(resumeButton);
		stage.getCamera().update();
		stage.getViewport().apply();
		stage.getViewport().update(Gdx.graphics.getWidth(),Gdx.graphics.getHeight(),true); 
		Gdx.input.setInputProcessor(stage);
		resumeButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y)  {
				gameClass.goBack();
				
			}
		});	
		optionButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y)  {
				gameClass.openOptions();
			}
		});	
		exitButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y)  {
				gameClass.setScreenString("Mainmenu");
			}
		});	
		
	}

	@Override
	public void render(float delta) {
		stage.act();
		camera.update();
		stage.getCamera().update();
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width,height,true); 
	}
	@Override
	public void resume() {
		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void hide() {
		stage.dispose();
		Gdx.input.setInputProcessor(null);
	}


}
