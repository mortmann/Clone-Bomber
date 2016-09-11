package com.clone.bomber.screens;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.clone.bomber.GameClass;


public class Options implements Screen {
	private static final int[][]  resolution = {{ 800, 600, 1024, 768, 1280,
		960, 1600, 1200 }, { 960, 540, 1280, 720, 1376,
		768, 1600, 900, 1920, 1080 }};
	private boolean fullscreen = false;
	private int resMarker=1;
	//Ratio 4,3= 4:3 etc
	private static final int[][] selectedRatio = {{4,3 }, { 16,9  }};
	private int ratioMarker=1;
	private Stage stage;
	private Skin skin;
	private OrthographicCamera camera;

	private TextButton fullButton;
	private TextButton exitButton;
	private Slider soundSlider;
	private Slider musicSlider;
	private Label soundLabel;
	private Label musicLabel;
	private FitViewport viewPort;
	private GameClass gameClass;
	private SelectBox<String> resolutionBox;
	
	
	public Options(GameClass gameClass) {
		this.gameClass=gameClass;
	}

	@Override
	public void show() {
		Preferences prefs = Gdx.app.getPreferences("clonebomber");
		fullscreen=prefs.getBoolean("fullscreen", false);
		if(prefs.getInteger("ratio", 169)==169){
			ratioMarker=1;
		} else if(prefs.getInteger("ratio", 169)==43){
			ratioMarker=0;
		}
		for(int i=0;i<resolution[ratioMarker].length;i+=2){
			if(resolution[ratioMarker][i]==prefs.getInteger("resWidth", 960)){
				resMarker=i;
			}
		}
		
		
		
	    camera = new OrthographicCamera(GameClass.viewportWidth , GameClass.viewportHeight);
		viewPort=new FitViewport(GameClass.viewportWidth , GameClass.viewportHeight,camera);
		viewPort.apply();
		System.out.println("show");
		
		skin = new Skin(Gdx.files.internal("res/gui/uiskin.json"));
		

//		float x=viewPort.getWorldWidth()/2 - 100;
//		float y=viewPort.getWorldHeight() *1f;
//		
		float x=GameClass.viewportWidth/2 - 100;
		float y=GameClass.viewportHeight-100;
		//Resolution Button setup

		//fullscreen Button setup
		fullButton=new TextButton("Fullscreen", skin);
		fullButton.setBounds(x, y -325, 200, 50);
		if(fullscreen){
			fullButton.setText("Windowed");
		} else {
			fullButton.setText("Fullscreen");
		}
		//exit Button setup
		exitButton=new TextButton("Save & Back", skin);
		exitButton.setBounds(x, y -425, 200, 50);
		
		//SoundSlider setup
		soundSlider =new Slider(0, 1, 0.01f, false, skin);
		soundSlider.setBounds(x, y -225, 200, 50);
		soundSlider.setValue(prefs.getFloat("soundVolume", 0.5f));

		soundLabel=new Label("Sound Volume", skin);
		soundLabel.setBounds(x-110, y-225, 100, 50);
		//musicSlider setup
		musicSlider =new Slider(0, 1, 0.01f, false, skin);
		musicSlider.setBounds(x, y -275, 200, 50);
		musicSlider.setValue(prefs.getFloat("musicVolume", 0.5f));
		musicLabel=new Label("Music Volume", skin);
		musicLabel.setBounds(x-110, y-275, 100, 50);
		

		
		resolutionBox = new SelectBox<String>(skin);
		Array<String> temp = new Array<String>();
		for(int i = 0; i<selectedRatio.length;i++){
			for(int m = 0; m < resolution[i].length;m+=2){
				temp.add(resolution[i][m] + "x"+ resolution[i][m+1] +"  @    "+selectedRatio[i][0] + ":"+selectedRatio[i][1]);
			}
			
		}
		resolutionBox.setItems(temp);
		resolutionBox.setBounds(x, y -125, 200, 50);

		
		fullButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y)  {
				
				fullscreen=!fullscreen;	
				if(fullscreen){
					fullButton.setText("Set Windowed");
				} else {
					fullButton.setText("Set Fullscreen");
	
				}
			}
		
		});	
		
		exitButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y)  {
				Gdx.graphics.setDisplayMode(resolution[ratioMarker][resMarker], resolution[ratioMarker][resMarker+1], fullscreen); 
				camera.setToOrtho(false, resolution[ratioMarker][resMarker], resolution[ratioMarker][resMarker+1]);
				Preferences prefs = Gdx.app.getPreferences("clonebomber");
				String res = resolutionBox.getSelected();
				res.trim();
				//TODO make out stirng resolution!
				prefs.putInteger("resHeight", resolution[ratioMarker][resMarker+1]);
				prefs.putInteger("resWidth", resolution[ratioMarker][resMarker]);
				prefs.putInteger("ratio", ratioMarker);
				prefs.putBoolean("fullscreen", fullscreen);
				prefs.putFloat("musicVolume", musicSlider.getValue());
				prefs.putFloat("soundVolume", soundSlider.getValue());
				prefs.flush();
				gameClass.goBackOptions();
//				NEEDS TO GO BACK 
			}
		});		
		
		stage=new Stage();
		Gdx.input.setInputProcessor(stage);
		stage.addActor(soundSlider);
		stage.addActor(soundLabel);
		stage.addActor(musicSlider);
		stage.addActor(musicLabel);
		stage.addActor(resolutionBox);
		stage.addActor(fullButton);
		stage.addActor(exitButton);
		stage.setViewport(viewPort);
		
	}

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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		Gdx.input.setInputProcessor(null);
	}

	@Override
	public void dispose() {
		stage.dispose();
	}

}
