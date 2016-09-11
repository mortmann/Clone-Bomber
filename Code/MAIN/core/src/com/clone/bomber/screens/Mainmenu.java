package com.clone.bomber.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.clone.bomber.GameClass;

public class Mainmenu implements Screen{
	private Stage stage;
	private Skin skin;
	private TextButton playButton;
	private TextButton optionButton;
	private TextButton editorButton;
	private TextButton exitButton;
	private FitViewport viewPort;
	private OrthographicCamera camera;
	private GameClass gameClass;
	private Image background;
	private ImageButton clanbomberButton;
	private TextButton testButton;
	private Dialog notBindDialog;
	public Mainmenu(GameClass gameClass) {
		this.gameClass=gameClass;
	}

	@Override
	public void show() {
		gameClass.getNet().endRound();
	    camera = new OrthographicCamera(GameClass.viewportWidth , GameClass.viewportHeight);
		viewPort=new FitViewport(GameClass.viewportWidth , GameClass.viewportHeight,camera);
		viewPort.apply();
		stage=new Stage();
		Gdx.input.setInputProcessor(stage);
		skin = new Skin(Gdx.files.internal("res/gui/uiskin.json"));
		float x=1280/2 - 95;
		float y=720-180;
		background = new Image(new TextureRegion(new Texture("res/gui/mainmenu.png")));
		background.setBounds(0, 0, GameClass.viewportWidth , GameClass.viewportHeight);
		
		clanbomberButton=new ImageButton(new TextureRegionDrawable((new TextureRegion(new Texture("res/gui/clear.png")))));
		clanbomberButton.setBounds(987, y-475, 154, 50);
		clanbomberButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y)  {
				Gdx.net.openURI("http://clanbomber.sourceforge.net/");
			}
		});	
		
		playButton=new TextButton("Play", skin);
		playButton.setBounds(x, y-125, 190, 50);

		optionButton=new TextButton("Options", skin);
		optionButton.setBounds(x,y-200, 190, 50);
		
		editorButton=new TextButton("Editor", skin);
		editorButton.setBounds(x, y-275, 190, 50);
		
		exitButton=new TextButton("Exit", skin);
		exitButton.setBounds(x,y-475, 190, 50);
// TODO WHEN DONE REMOVE
		testButton=new TextButton("drdrrd", skin);
		testButton.setBounds(x, y-375, 190, 50);
		
        // Wire up a click listener to our button
        testButton.addListener(new ClickListener(){
            @Override 
            public void clicked(InputEvent event, float x, float y){
            	
            }
        });
// 		adding to Stage       
		stage.addActor(background);
//		stage.addActor(testButton);
		stage.addActor(clanbomberButton);
		stage.addActor(playButton);
		stage.addActor(optionButton);
		stage.addActor(editorButton);
		stage.addActor(exitButton);	
		stage.setViewport(viewPort);
		//Button handling
		playButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y)  {
				gameClass.setScreenString("Gamesetup");
			}
		});	
		optionButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y)  {
				gameClass.openOptions();
			}
		});	
		editorButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y)  {
				gameClass.setScreenString("Editor");
			}
		});	

		exitButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y)  {
				Gdx.app.exit();
			}
		});	

		if(!gameClass.getNet().isBind()){
			notBindDialog=new Dialog("Warning! Server couldn�t bind the Port!" , skin);
			notBindDialog.add("Please close all other Version�s of this Game or make sure").row();
			notBindDialog.add(" no other Programm uses the Port 54555!").colspan(2);
			notBindDialog.show(stage);
			notBindDialog.addListener(new ClickListener() {
				@Override
				public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
					notBindDialog.hide();
					return false;
				}
			});	
		}
	}

	@Override
	public void render(float delta) {
		
		stage.draw();
		stage.act();
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
		
	}

}
