package com.clone.bomber.screens;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.clone.bomber.GameClass;
import com.clone.bomber.entity.Player;
import com.clone.bomber.map.Map;
import com.clone.bomber.map.MapManager;
import com.clone.bomber.map.Square;
import com.clone.bomber.util.MyScreen;
import com.clone.bomber.util.MySound;



public class IntermediateScoreScreen extends MyScreen {
	private static final int PLAYER_TABLE_WIDTH=500;
	private static final int TEAM_TABLE_WIDTH=250;
	private static final int PLAYER_TABLE_HEIGHT=75;
	private static final int MAX_PLAYER = 8;
	
	
	private boolean won;
	
	private OrthographicCamera mapCamera;
	private FitViewport viewMapPort;
	
	private MapManager mapManager;
	private Table[] playerTable;
	private Table allTeamsTable;
	private Table cupTable;
	private Image[] playerImage;
	private Map map;
	private Texture randomBox;
	private Table[] teamTable;
	private Texture spawnSpot;
	private ArrayList<Player> players;
	private Array<String> arraySelection;
	private MySound mySound;
	private Table allPlayerTable;
	
	public IntermediateScoreScreen(GameClass gameClass){
		this.gameClass=gameClass;		
	}	
	@Override
	public void OnShow(){
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
		
		mapManager=new MapManager();
		playerTable =new Table[MAX_PLAYER];
		allTeamsTable=new Table(skin);
		cupTable=new Table(skin);
		playerImage= new Image[MAX_PLAYER];
		map=new Map(17);
		spawnSpot=map.getTexManager().getSpawnSpotTexture();
		randomBox=map.getTexManager().getRandomBoxTexture();
		teamTable=new Table[5];
		
		InputMultiplexer inputMultiplexer = new InputMultiplexer();
		inputMultiplexer.addProcessor(this);
		inputMultiplexer.addProcessor(stage);
		Gdx.input.setInputProcessor(inputMultiplexer);
		mySound= new MySound();
		boolean enoughTeams=false;
		int numberOfTeams=0;
			int[] teams = new int[]{0,0,0,0,0};//size 5

			for(int i =0; i<Integer.valueOf(players.size());i++){
				teams[players.get(i).getTeamNumber()]++;
			}
			for(int i =0; i< teams.length;i++){
				if(teams[i] >0){
					numberOfTeams++;
				}
			}
			if(numberOfTeams>1){
				enoughTeams=true;
			}
			
		if(!enoughTeams){
			for(Player p : players){
				if(!p.isDead()){
					p.addWin();			
				}
			}
		} else {
			int wonTeam=-1;
			for(Player p : players){
				if(!p.isDead()){
					wonTeam = p.getTeamNumber();
				}
			}
			for(Player p : players){
				if(p.getTeamNumber()==wonTeam){
					p.addWin();
				}
			}
		}
		//TABLE LAYOUTS FOR 
		//TEAMS
		if(enoughTeams){
			System.out.println("TEAM LAYOUT");
		int addedTeamsToTable=0;
		for(int i = 0;i<players.size();i++){
			int teamNumber=players.get(i).getTeamNumber();
			if(teamNumber==-1){
				teamNumber=0;
			}
			if(teamTable[teamNumber]==null){
				addedTeamsToTable++;
				System.out.println("ADD TEAM " + teamNumber);
				teamTable[teamNumber] = new Table(skin);
				
				Label label = new Label("TEAM " + Gamesetup.getTeamnames()[teamNumber] +":",skin);
				
//				teamTable[teamNumber].setBackground(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("res/gui/whitepixel.png")))));
				if(Gamesetup.getTeamnames()[teamNumber].contains("Gold")){
					label.setColor(new Color(0.857f,0.647f,0.12f,1));
				} else
				if(Gamesetup.getTeamnames()[teamNumber].contains("Leaf")){
					label.setColor(Color.GREEN);
				} else
				if(Gamesetup.getTeamnames()[teamNumber].contains("Blood")){
					label.setColor(Color.RED);
				} else
				if(Gamesetup.getTeamnames()[teamNumber].contains("Water")){
					label.setColor(Color.BLUE);
				} else
				if(Gamesetup.getTeamnames()[teamNumber].contains("No Team")){
					label.setColor(Color.GRAY);
				}
//				teamTable[teamNumber].add("TEAM " + Gamesetup.getTeamnames()[teamNumber] +":").row();
				teamTable[teamNumber].add(label).row();
				allTeamsTable.add(teamTable[teamNumber]);
				allTeamsTable.add().size(50);
				if(addedTeamsToTable==2 || addedTeamsToTable==4){
					allTeamsTable.row();
				}
			}
		}
		createPlayerTables();
			
		for(int i = 0;i<players.size();i++){	
			System.out.println("ADD PLAYERTABLE " + i);
			teamTable[players.get(i).getTeamNumber()].add(playerTable[i]).size(TEAM_TABLE_WIDTH, PLAYER_TABLE_HEIGHT).row();
		}
			allTeamsTable.setBounds(100, 100, TEAM_TABLE_WIDTH*2, PLAYER_TABLE_HEIGHT*MAX_PLAYER);
			stage.addActor(allTeamsTable);
			
		}
		//NOT FOR TEAMS
		else {
			float x = GameClass.viewportWidth/2 - PLAYER_TABLE_WIDTH;
			float y = GameClass.viewportHeight/2 - (PLAYER_TABLE_HEIGHT*MAX_PLAYER) /2;
			System.out.println("PLAYER LAYOUT");
			createPlayerTables();
			
			allPlayerTable=new Table(skin);
			allPlayerTable.left();
			for(int i = 0;i<players.size();i++){
				allPlayerTable.add(playerTable[i]).size(PLAYER_TABLE_WIDTH, PLAYER_TABLE_HEIGHT).row();
			}
			allPlayerTable.setBounds(x, y, PLAYER_TABLE_WIDTH, PLAYER_TABLE_HEIGHT*MAX_PLAYER);
			stage.addActor(allPlayerTable);
		}
//			stage.setDebugAll(true);
		
	}
	
	public void setInformation(ArrayList<Player> players, Array<String> arraySelection){
		this.players=players;
		if(arraySelection.size>1){
			arraySelection.swap(0, arraySelection.size-1);
		}
		this.arraySelection=arraySelection;
		if(mapManager==null){
			mapManager = new MapManager();
		}
		map =  mapManager.loadMap(arraySelection.first());
		map.load();
	}
	
	private void createPlayerTables(){
		
		for(int i = 0;i<players.size();i++){
			cupTable=new Table();
			System.out.println("CREATE PLAYERTABLE " + i);
			playerTable[i]=new Table(skin);
			playerTable[i].left();
			if(players.get(i).isDead()){
				playerImage[i] = new Image(players.get(i).getDeadFrames()[0]);
			} else {
				playerImage[i] = new Image(players.get(i).getDownFrame());	
			}
			playerTable[i].add("Player " + Integer.toString(i+1)).left();
			playerTable[i].add(playerImage[i]);
			
			for(int w = 0; w<players.get(i).getWins();w++){
				if(i!=0 && i % 10 == 0){
					playerTable[i].row();
				}
				cupTable.add(new Image(new Texture("res/gui/cup.png"))).size(30, 30);
				if(players.get(i).getWins()==Gdx.app.getPreferences("clonebomber").getInteger("Wins")){
					this.won=true;
					mySound.playWinSound();
				}
			}
			
			playerTable[i].add(cupTable).row();
		}
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
	public void dispose() {
		map.dispose();
		stage.dispose();
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
