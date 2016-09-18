package com.clone.bomber.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Sound;
import com.clone.bomber.entity.PowerUPEffects;

public class MySound {
	private static final String pathSound = "res/sound/";
	private Sound[] sound;
	private float volume=0.1f;
	
	public MySound() {
		sound=new Sound[24];
		Preferences prefs = Gdx.app.getPreferences("clonebomber");
		this.volume=prefs.getFloat("musicVolume",0.1f);
		//player
		sound[0]= Gdx.audio.newSound(Gdx.files.internal(pathSound + "die.wav"));
		sound[1]= Gdx.audio.newSound(Gdx.files.internal(pathSound + "corpse_explode.mp3"));
		//bomb interactions
		sound[2]= Gdx.audio.newSound(Gdx.files.internal(pathSound + "putbomb.wav"));
		sound[3]= Gdx.audio.newSound(Gdx.files.internal(pathSound + "explode.mp3"));
		//disease - negative powerUP
		sound[4]= Gdx.audio.newSound(Gdx.files.internal(pathSound + "joint.mp3"));
		sound[5]= Gdx.audio.newSound(Gdx.files.internal(pathSound + "horny.mp3"));
		sound[6]= Gdx.audio.newSound(Gdx.files.internal(pathSound + "schnief.mp3"));
		//positive powerUP
		sound[7]= Gdx.audio.newSound(Gdx.files.internal(pathSound + "wow.mp3"));
		//suddendeath
		sound[8]= Gdx.audio.newSound(Gdx.files.internal(pathSound + "crunch.wav"));
		sound[9]= Gdx.audio.newSound(Gdx.files.internal(pathSound + "hurry_up.mp3"));
		sound[10]= Gdx.audio.newSound(Gdx.files.internal(pathSound + "time_over.mp3"));
		//fall
		sound[11]= Gdx.audio.newSound(Gdx.files.internal(pathSound + "deepfall.mp3"));
		//menu
		sound[12]= Gdx.audio.newSound(Gdx.files.internal(pathSound + "menu_back.wav"));
		//round
		sound[13]= Gdx.audio.newSound(Gdx.files.internal(pathSound + "klatsch.mp3"));
		//splash/corpse
		sound[14]= Gdx.audio.newSound(Gdx.files.internal(pathSound + "splash1a.mp3"));
		sound[15]= Gdx.audio.newSound(Gdx.files.internal(pathSound + "splash2a.mp3"));
	}
	
	public void playDieSound(){
		sound[0].play(volume);
	}
	
	public void playFallSound(){
		sound[11].play(volume);
	}
	
	public void playWinSound(){
		sound[13].play(volume);
	}
	
	public void playSuddenDeathSound(int i){
		if(i==1){
			sound[8].play(volume);
		} else if(i==2){
			sound[9].play(volume);
		} else if(i==3){
			sound[10].play(volume);
		}
	}
	public void playPowerUPSound(PowerUPEffects myEffect){
		switch(myEffect){
		case diarrhea:
			sound[5].play(volume);
			break;
		case joint:
			sound[4].play(volume);
			break;
		case superspeed:
			sound[6].play(volume);
			break;
		default:
			sound[7].play(volume);
			break;
		}
		
	}
	public void playBombSound(int i){
		if(i==1){
			sound[2].play(volume);
		} else if(i==2){
			sound[3].play(volume);
		} 
	}

	public void playCorpse() {
		int r =(int) Math.round((float) Math.random());
		sound[14+r].play(volume);
	}
	public void close(){
		for(Sound s : sound){
			s.stop();
			s.dispose();
			
		}
	}
}
