package com.clone.bomber;

import java.util.ArrayList;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.Array;
import com.clone.bomber.entity.Player;
import com.clone.bomber.screens.BomberGame;
import com.clone.bomber.screens.Editor;
import com.clone.bomber.screens.Gamesetup;
import com.clone.bomber.screens.IntermediateScoreScreen;
import com.clone.bomber.screens.Mainmenu;
import com.clone.bomber.screens.Options;
import com.clone.bomber.screens.PauseMenu;
import com.clone.bomber.util.Network;

public class GameClass extends Game{
    private Mainmenu mainMenuScreen;
    private Options optionsScreen;
	private Screen lastScreen;
	private Screen lastBeforOptionScreen;
	private BomberGame gameScreen;
	private Gamesetup setupScreen;
	private Editor editorScreen;
	private PauseMenu pauseScreen;
	private Network net;
    private Thread aliveC;
	private IntermediateScoreScreen intermediateScoreScreen;
    public static final int viewportHeight = 720;
    public static final int viewportWidth = 1280;
    

     public void create() { 
 		Preferences prefs = Gdx.app.getPreferences("clonebomber");
		Gdx.graphics.setDisplayMode(
				prefs.getInteger("resWidth",GameClass.viewportWidth),
				prefs.getInteger("resHeight",GameClass.viewportHeight),
				prefs.getBoolean("fullscreen", false)
		); 
    	net =new Network();
    	AliveChecker ac = new AliveChecker(Thread.currentThread(),net.getServer());
    	aliveC = new Thread(ac);
    	aliveC.start();
    	mainMenuScreen = new Mainmenu(this);
        pauseScreen = new PauseMenu(this); 
        optionsScreen = new Options(this);
        setScreen(mainMenuScreen);  
     }

    public void setScreen(Screen screen){
        super.setScreen(screen);
    }

    public void render() {
        super.render();
    }

	public void resize(int width, int height) {
		super.resize(width, height);
	}


	public void pause() {
		super.pause();
	}


	public void resume() {
		super.resume();
	}

	public void dispose() {
		net.closeServer();
		aliveC.interrupt();
		super.dispose();		
		Gdx.app.exit();
	}
	/*
	 * string = options | Gamesetup | Editor | Mainmenu
	 */
	public void setScreenString(String string) {
		if(string.contains("options")){
			setScreen(optionsScreen);
		} else if(string.contains("Gamesetup")){
			setupScreen = new Gamesetup(this);
			setScreen(setupScreen);
		} else if(string.contains("Editor")){
			editorScreen = new Editor(this);
			setScreen(editorScreen);
		} else if(string.contains("Mainmenu")){
			setScreen(mainMenuScreen);
		} 
	}

	public void goBack() {
		setScreen(lastScreen);
		
	}
	public void openPauseMenu(Screen lastscreen){
		this.lastScreen=lastscreen;
		setScreen(pauseScreen);
	}
	public void openOptions() {
		this.lastBeforOptionScreen=this.getScreen();
		setScreen(optionsScreen);
	}

	public void setGame(boolean newRound ,Array<String> arraySelection, Integer playerNumber, ArrayList<Player> players) {
		gameScreen=new BomberGame(newRound,this,players);
		if(newRound){
			setupScreen.dispose();
		}
		gameScreen.setPlayerNumber(playerNumber);
		gameScreen.setMap(arraySelection);
		setScreen(gameScreen);
	}
	
	public void setScoreScreen(ArrayList<Player> players,Array<String> arraySelection  ){
		intermediateScoreScreen = new IntermediateScoreScreen(this);
		gameScreen.dispose();
		intermediateScoreScreen.setInformation(players, arraySelection);
		setScreen(intermediateScoreScreen);
	}

	public void goBackOptions() {
		setScreen(lastBeforOptionScreen);
		
	}

	public Network getNet() {
		return net;
	}


	
}
