package com.clone.bomber.util;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.math.MathUtils;

public class MyMusic {
	private static final String path = "res/music/";

	private Music music;
	//TODO CHANGE MAYBE TO SELF READING 
	//create new radios here 
	private String[] radioOne = {"ElMagicia.mp3"};
	private int radio=0;
	private ArrayList<String[]> radios;
	private ArrayList<String> radionames;
	private float volume=0.5f;

	private int radioMarker=0;
	
	public MyMusic() {
		radios= new ArrayList<String[]>();
		radionames= new ArrayList<String>();
		//add radios here
		radios.add(radioOne);
		radionames.add("radioOne");
	}
	
	public void playRadio(){
			music = Gdx.audio.newMusic(Gdx.files.internal(path + radios.get(radio)[MathUtils.random(radioOne.length-1)]));
			volume = Gdx.app.getPreferences("clonebomber").getInteger("musicVolume");
		if(radio!=-1){
			music.setVolume(volume);
			music.play();
		}
	}
	
	public void changeRadio(){
		radio++;
		if(radio>radios.size()-1){
			radio=-1;
		}
	}
	public String getRadioName(){
		return radionames.get(radioMarker);
		
	}
}
