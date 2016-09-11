package com.clone.bomber.screens;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.clone.bomber.GameClass;
import com.clone.bomber.entity.Player;
import com.clone.bomber.map.Map;
import com.clone.bomber.map.MapManager;
import com.clone.bomber.map.Square;
import com.clone.bomber.util.MySound;

public class ScoreScreen implements Screen,InputProcessor {
	private GameClass gameClass;
	private ArrayList<Player> players;
	private Stage stage;
	private Skin skin;
	private SpriteBatch spriteBatch;
	private FitViewport viewPort;
	private OrthographicCamera camera;
	private Array<String> arraySelection;
	private Table playerTable;
	private Table[] teamTable;
	private Table allTeamsTable;
	private Image[] playerImage;
	private boolean won;
	private FitViewport viewMapPort;
	private OrthographicCamera mapCamera;
	private MapManager mapManager;
	private Map map;
	private Texture spawnSpot;
	private Texture randomBox;
	private Table cupTable;
	private boolean teamsBool;
	
	private static String[] teamNames = {"No Team","Gold","Leaf","Blood","Water"};
	
	public ScoreScreen(GameClass gameClass){
		spriteBatch = new SpriteBatch();
		this.gameClass=gameClass;	
		camera = new OrthographicCamera(GameClass.viewportWidth,
				GameClass.viewportHeight);
		camera.translate(new Vector3(GameClass.viewportWidth / 2,
				GameClass.viewportHeight / 2, 0));
		viewPort = new FitViewport(GameClass.viewportWidth,
				GameClass.viewportHeight, camera);
		viewPort.apply();	
		mapCamera =new OrthographicCamera(GameClass.viewportWidth,
				GameClass.viewportHeight);
		mapCamera.translate(new Vector3(GameClass.viewportWidth / 2 +100,
				GameClass.viewportHeight / 2 +100, 0));
		mapCamera.zoom=1.75f;
		mapCamera.update();
		
		viewMapPort = new FitViewport(GameClass.viewportWidth,
				GameClass.viewportHeight, mapCamera);
		viewMapPort.getCamera().position.set(200,200,1.75f);
		viewMapPort.getCamera().update();
	    skin = new Skin(Gdx.files.internal("res/gui/uiskin.json"));
		mapManager=new MapManager();
		stage = new Stage();
		stage.setViewport(viewPort);
		playerTable =new Table(skin);
		allTeamsTable=new Table(skin);
		cupTable=new Table(skin);
		playerImage= new Image[8];
		map=new Map(17);
		spawnSpot=map.getTexManager().getSpawnSpotTexture();
		randomBox=map.getTexManager().getRandomBoxTexture();
		teamTable=new Table[5];
	}
	
	
	public void setInformation(ArrayList<Player> players, Array<String> arraySelection){
		this.players=players;
		if(arraySelection.size>1){
			arraySelection.swap(0, arraySelection.size-1);
		}
		this.arraySelection=arraySelection;
		map =  mapManager.loadMap(arraySelection.first());
		map.load();

	}
	
	
	@Override
	public void show() {		
		InputMultiplexer inputMultiplexer = new InputMultiplexer();
		inputMultiplexer.addProcessor(this);
		inputMultiplexer.addProcessor(stage);
		Gdx.input.setInputProcessor(inputMultiplexer);
		MySound mySound= new MySound();
		for(Player p : players){
			if(!p.isDead()){
				p.addWin();			
			}
		}
		Preferences prefs = Gdx.app.getPreferences("clonebomber");
		
			boolean enoughTeams=false;
			int numberOfTeams=0;
			int team=0;
			for(int i =0; i<players.size();i++){
				if(team!=players.get(i).getTeamNumber()){
					numberOfTeams++;
					team=players.get(i).getTeamNumber();
				}	
			}
			if(numberOfTeams>1 && team>0){
				enoughTeams=true;
			} else if(team==0){
				enoughTeams=false;
			}
			float x = GameClass.viewportWidth/2 - 500;
			float y = GameClass.viewportHeight - 100;
			if(enoughTeams){
			int teamNumber=0;
			for(int i = 0;i<players.size();i++){
				teamsBool=enoughTeams;		
				if(players.get(i).getTeamNumber()==0){
					teamTable[0]= new Table(skin);
//					 x =  225;
//					 y = 50 + 210 ;
					 teamTable[0].setBounds(x +225, y+260, 400, 210);
					 teamTable[0].add(teamNames[0]);
					 allTeamsTable.add(teamTable[0]).size(400,210).pad(25);
					 teamNumber++;
				} else 
				if(players.get(i).getTeamNumber()==1){
						
					teamTable[1]= new Table(skin);
//					 x = 100;
//					 y = GameClass.viewportHeight - 50;
//					 teamTable[1].setBounds(x+100, y-50, 400, 210);
					 teamTable[1].add(teamNames[1]);
					 allTeamsTable.add(teamTable[1]).size(400,210).pad(25);
					 teamNumber++;
				} else 
				if(players.get(i).getTeamNumber()==2){
					teamTable[2]= new Table(skin);
//					 x = 350;
//					 y = GameClass.viewportHeight - 50;
//					 teamTable[2].setBounds(x+350, y-50, 400, 210);
					 teamTable[2].add(teamNames[2]);
					 allTeamsTable.add(teamTable[2]).size(400,210).pad(25);
					 teamNumber++;
				} else 
				if(players.get(i).getTeamNumber()==3){
					teamTable[3]= new Table(skin);
//					 x = GameClass.viewportWidth/2 - 500;
//					 y = GameClass.viewportHeight - 400;
//					 teamTable[3].setBounds(x, y-400, 400, 210);
					 teamTable[3].add(teamNames[3]);
					 allTeamsTable.add(teamTable[3]).size(400,210).pad(25);
					 teamNumber++;
				} else 
				if(players.get(i).getTeamNumber()==4){
					teamTable[4]= new Table(skin);
//					 x = 350;
//					 y = GameClass.viewportHeight - 100;
//					 teamTable[4].setBounds(x+350, y, 400, 210);
					 teamTable[4].add(teamNames[4]);
					 allTeamsTable.add(teamTable[4]).size(400,210).pad(25);
					 teamNumber++;
				}
				if(teamNumber==2 || teamNumber==5){
					allTeamsTable.row();
				}
		}
			 allTeamsTable.setBounds(25,  GameClass.viewportHeight -25, 600, 680);
			 stage.addActor(allTeamsTable);
		}
		for(int i = 0;i<players.size();i++){
			playerTable=new Table(skin);
			cupTable=new Table(skin);
			if(players.get(i).isDead()){
				playerImage[i] = new Image(players.get(i).getDeadFrames()[0]);
			} else {			
//				String temp=prefs.getString("Player "  + Integer.toString(i)+" Char");
//				TextureRegion texReg= new TextureRegion(new Texture(Gdx.files.internal("res/assets/bomber_" + temp + ".png")));
//				texReg.setRegion(0, 0,texReg.getRegionWidth()/10, texReg.getRegionHeight()/4);
				playerImage[i] = new Image(players.get(i).getDownFrame());	
			}
			
			playerTable.add("Player " + Integer.toString(i+1));
			playerImage[i].setUserObject(i);
			if(playerImage[i]!=null)
			playerTable.add(playerImage[i]).size(40, 40);
			for(int w = 0; w<players.get(i).getWins();w++){
				if(i!=0 && i % 10 == 0){
					playerTable.row();
				}
				cupTable.add(new Image(new Texture("res/gui/cup.png"))).size(30, 30);
				if(players.get(i).getWins()==prefs.getInteger("Wins")){
					this.won=true;
					mySound.playWinSound();
						
					
				}
			}	
			if(teamsBool){
				playerTable.add(cupTable);
				if(players.get(i).getTeamNumber()==0){
					teamTable[0].add(playerTable);
					stage.addActor(teamTable[0]);
				} else 
				if(players.get(i).getTeamNumber()==1){
					teamTable[1].add(playerTable);
					stage.addActor(teamTable[1]);
				} else 
				if(players.get(i).getTeamNumber()==2){
					teamTable[2].add(playerTable);
					stage.addActor(teamTable[2]);
				} else 
				if(players.get(i).getTeamNumber()==3){
					teamTable[3].add(playerTable);
					stage.addActor(teamTable[3]);
				} else 
				if(players.get(i).getTeamNumber()==4){
					teamTable[4].add(playerTable);
					stage.addActor(teamTable[4]);
				}
				playerTable.clear();
				
			} else
				playerTable.add(cupTable).row();	
				playerTable.setBounds( x , y-100, 200, 100);
				stage.addActor(playerTable);
			}
			cupTable.clear();
	}

	@Override
	public void render(float delta) {
		spriteBatch.setProjectionMatrix(viewMapPort.getCamera().combined);
		spriteBatch.begin();
		map.draw(spriteBatch, delta);
		
		for(Square s : map.getSpawnSpots()){
			spriteBatch.draw(spawnSpot, s.getX(),s.getY(),s.getSize(),s.getSize());
		}
		for(Square s : map.getSquares()){
			if(s.getMyBox()!=null){
				if(s.getMyBox().isRandom()){
					spriteBatch.draw(randomBox, s.getX(),s.getY(),s.getSize(),s.getSize());
				}
			}
		}
		spriteBatch.end();
		
		//then the stage && updating
		stage.draw();
		stage.act();
	}

	@Override
	public void resize(int width, int height) {
		viewPort.update(width, height);
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void pause() {
		
	}

	@Override
	public void resume() {
		InputMultiplexer inputMultiplexer = new InputMultiplexer();
		inputMultiplexer.addProcessor(this);
		inputMultiplexer.addProcessor(stage);
		Gdx.input.setInputProcessor(inputMultiplexer);
	}

	@Override
	public void hide() {
		Gdx.input.setInputProcessor(null);
	}

	@Override
	public void dispose() {
		map.dispose();
		spriteBatch.dispose();
	}

	@Override
	public boolean keyDown(int keycode) {
		if(keycode==Keys.SPACE){
			if(!won){
				gameClass.setGame(false,arraySelection, players.size(), players);
			} else {
				for(Player p : players){
					p.dispose();
				}
				
				gameClass.setScreenString("Mainmenu");
			}
		} else if(Keys.ESCAPE== keycode){
			gameClass.openPauseMenu(this);
			return true;
		}
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
		System.out.println("touch " +  screenX+ screenY + " " + camera.unproject(new Vector3(screenX , screenY,0)));
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
