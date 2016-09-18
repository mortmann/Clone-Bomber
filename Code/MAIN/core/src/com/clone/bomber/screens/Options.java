package com.clone.bomber.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.clone.bomber.GameClass;
import com.clone.bomber.util.MyScreen;


public class Options extends MyScreen {
	private static final int[][]  resolution = {{800,600,1024,768,1280,
		960,1600,1200}, {960,540,1280,720,1376,
		768,1600,900,1920,1080}};
	private boolean fullscreen = false;
	//Ratio 4,3= 4:3 etc
	private static final int[][] selectedRatio = {{4,3 }, { 16,9  }};
	private TextButton fullButton;
	private TextButton exitButton;
	private Slider soundSlider;
	private Slider musicSlider;
	private Label soundLabel;
	private Label musicLabel;
	private SelectBox<String> resolutionBox;
	
	
	public Options(GameClass gameClass) {
		this.gameClass=gameClass;
	}

	@Override
	public void OnShow(){
		Preferences prefs = Gdx.app.getPreferences("clonebomber");
		fullscreen=prefs.getBoolean("fullscreen", false);
		
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
		
		int currentWidht = prefs.getInteger("resWidth",GameClass.viewportWidth);
		int currentHeight = prefs.getInteger("resHeight",GameClass.viewportHeight);
		resolutionBox = new SelectBox<String>(skin);
		int selected = -1;
		Array<String> temp = new Array<String>();
		for(int i = 0; i<selectedRatio.length;i++){
			for(int m = 0; m < resolution[i].length;m+=2){
				if(currentWidht==resolution[i][m] && resolution[i][m+1] == currentHeight){
					selected = temp.size;
				}
				temp.add(resolution[i][m] + "x"+ resolution[i][m+1] +"  @    "+selectedRatio[i][0] + ":"+selectedRatio[i][1]);
			}
			
		}
		resolutionBox.setItems(temp);
		resolutionBox.setSelectedIndex(selected);
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
				Preferences prefs = Gdx.app.getPreferences("clonebomber");
				String res = resolutionBox.getSelected();
				res = res.trim();
				res = res.split("@")[0].trim();
				String[] ps = res.split("x");
				prefs.putInteger("resHeight", Integer.parseInt(ps[1]));
				prefs.putInteger("resWidth",Integer.parseInt(ps[0]) );
				prefs.putBoolean("fullscreen", fullscreen);
				prefs.putFloat("musicVolume", musicSlider.getValue());
				prefs.putFloat("soundVolume", soundSlider.getValue());
				prefs.flush();
				Gdx.graphics.setDisplayMode(Integer.parseInt(ps[0]),Integer.parseInt(ps[1]), fullscreen); 
				gameClass.goBackOptions();
			}
		});		
		
		stage.addActor(soundSlider);
		stage.addActor(soundLabel);
		stage.addActor(musicSlider);
		stage.addActor(musicLabel);
		stage.addActor(resolutionBox);
		stage.addActor(fullButton);
		stage.addActor(exitButton);
		stage.setViewport(viewPort);
		
	}

	



}
