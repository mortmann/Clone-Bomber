package com.clone.bomber.screens;

import java.util.ArrayList;
import java.util.Collections;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.clone.bomber.GameClass;
import com.clone.bomber.entity.Blastbeam;
import com.clone.bomber.entity.Bomb;
import com.clone.bomber.entity.Player;
import com.clone.bomber.entity.PowerUP;
import com.clone.bomber.entity.PowerUPEffects;
import com.clone.bomber.entity.Suddendeath;
import com.clone.bomber.map.Box;
import com.clone.bomber.map.Map;
import com.clone.bomber.map.MapManager;
import com.clone.bomber.map.Square;
import com.clone.bomber.map.Wall;
import com.clone.bomber.util.Collideable;
import com.clone.bomber.util.MyRectangle;
import com.clone.bomber.util.MyScreen;
import com.clone.bomber.util.MySound;

public class BomberGame extends MyScreen {
	//MAP
	private Map map;
	private ArrayList<Collideable> collideables;
	private Suddendeath suddenDeath;

	//GAME
	private ArrayList<Player> players;
	private boolean paused=false;
	private int alivePlayerCount;
	private Array<String> mapNames;
	private float waitForScore = 3f;

	//UTILS
	private MySound mySound;
	private ArrayList<Player> playerApp;
	
	//SHAKE SCREEN
	private boolean shake;
	private float currentMagnitude=1;
	private float magnitudeDecreaseFactor=3f;
	private boolean newRound;
	//ui
	private ArrayList<Label[]> labels;
	private Image[] negativeEImage;
	private BitmapFont font;
	private Table upgradesTabel;

	Array<Controller> controllers = Controllers.getControllers();

	public BomberGame(boolean newRound, GameClass gameClass, ArrayList<Player> players, int playerNumber,Array<String> maps) {
		this.gameClass = gameClass;		
		setMap(maps);
		if(players==null){
			this.players = new ArrayList<Player>();
		} else {
			this.players=players;
		}
		alivePlayerCount = playerNumber;
		playerApp=new ArrayList<Player>();
		collideables = new ArrayList<Collideable>();
		spriteBatch = new SpriteBatch();
		waitForScore = 3f;
		mySound=new MySound();
		this.newRound=newRound;
		font = new BitmapFont(Gdx.files.internal("res/gui/november.fnt"));
		font.setColor(Color.YELLOW);
		CreatePlayer();
		
	}

	private void CreatePlayer() {
		waitForScore=3f;
		map.load();
		negativeEImage = new Image[8];
		labels = new ArrayList<Label[]>();
		for(int s = 0 ; s < 5; s ++ ){
			labels.add(new Label[8]);
		}
		skin = new Skin(Gdx.files.internal("res/gui/uiskin.json"));		
		for(Label[] ls : labels){
			for (int i = 0; i < ls.length; i++) {
				ls[i]=new Label("", skin);;
			}
		}
		upgradesTabel = new Table(skin);
		for(int i =0; i<alivePlayerCount;i++){
			
			upgradesTabel.setBounds(30,0, 240,720);
			upgradesTabel.add("Player " + (i+1)).size(30).row();;
			upgradesTabel.add(new Image(map.getTexManager().getPowerUp(PowerUPEffects.speed))).size(20);
			upgradesTabel.add(labels.get(0)[i]).size(30);
			upgradesTabel.add(new Image(map.getTexManager().getPowerUp(PowerUPEffects.blastradius))).size(20);
			upgradesTabel.add(labels.get(1)[i]).size(30);
			upgradesTabel.add(new Image(map.getTexManager().getPowerUp(PowerUPEffects.bomb))).size(20);
			upgradesTabel.add(labels.get(2)[i]).size(30);
			upgradesTabel.add(new Image(map.getTexManager().getPowerUp(PowerUPEffects.throwable))).size(20);
			upgradesTabel.add(labels.get(3)[i]).size(30);
			upgradesTabel.add(new Image(map.getTexManager().getPowerUp(PowerUPEffects.push))).size(20);
			upgradesTabel.add(labels.get(4)[i]).size(30);
			negativeEImage[i]=new Image();
			upgradesTabel.add(negativeEImage[i]).size(20);
			upgradesTabel.row();
			if(players.size()<alivePlayerCount){
				int[] keys = new int[5];			
				Preferences prefs = Gdx.app.getPreferences("clonebomber");
				String controls=prefs.getString("Player " + Integer.toString(i)+" Controls", "Tastatur");
				keys[0]=prefs.getInteger("Player " + Integer.toString(i)+" UP", keys[0]);
				keys[1]=prefs.getInteger("Player " + Integer.toString(i)+" DOWN", keys[1]);
				keys[2]=prefs.getInteger("Player " + Integer.toString(i)+" RIGHT", keys[2]);
				keys[3]=prefs.getInteger("Player " + Integer.toString(i)+" LEFT", keys[3]);
				keys[4]=prefs.getInteger("Player " + Integer.toString(i)+" ACTION", keys[4]);
				int team = prefs.getInteger("Player " + Integer.toString(i)+" Team", 0);
				String charakter = prefs.getString("Player " + Integer.toString(i)+" Char");
				players.add(new Player(map.getSpawnSpots().get(i),keys, map, controls, mySound,charakter,this,team));		
				if(controls.contains("Controller") && controllers.size>0){
					players.get(i).setController(controllers.first());
					controllers.removeIndex(0);
				} else if(controls.contains("App")){
					playerApp.add(players.get(i));
				}
			}
		}
		for(int i =0; i < players.size() ;i++){
			players.get(i).reset(this,map.getSpawnSpots().get(i),map);
		}
		System.out.println(players.size());
		suddenDeath=new Suddendeath(map, mySound);


	}

	@Override
	public void OnShow() {
		//TODO SHOULD DO
		//TODO MAKE app nicer -> very last thing todo
		//TODO MAKE UI nicer? -> last thing todo
		//TODO MAKE TEAMS POSSIBLE? -> fix scorescreen ! 
		
		//TODO ADDING THESE
		//TODO MAKE bots ? -> really hard 
		//TODO MAKE BETTER OPTIONS -> make the resolution a dropdown menu -> disable/remove music slider -> make port setable?
		//TODO MAKE MORE THINGS (like selectable upgrades) IN GAMESETUP -> easy 
		
		//TODO OPTIONAL 
		//TODO MAKE CORRECT DPAD NAME SHOWING? -> not really important
		stage.addActor(upgradesTabel);
		InputMultiplexer inputMultiplexer = new InputMultiplexer();
		for (Player p : players) {
			inputMultiplexer.addProcessor(p);
		}
		inputMultiplexer.addProcessor(this);
		Gdx.input.setInputProcessor(inputMultiplexer);
	}

	@Override
	public void render(float delta) {
		//if framerate is slower than 30 fps than cap it at 30 
		// -> this is to prevent to "big" movements at a time 
		// -> game should slow down if you have worse framerate
		if(delta>0.033333f){
			delta=0.033333f;//maybe do it like update x2 when bigger?
		}

		
		//update upgrades
		for(int i =0; i<alivePlayerCount;i++){
			labels.get(0)[i].setText("x " + players.get(i).getSpeed());
			labels.get(1)[i].setText("x " + players.get(i).getBlastRadiusUPs());
			labels.get(2)[i].setText("x " + players.get(i).getBombUPs());
			labels.get(3)[i].setText("x " + players.get(i).getBombThrows());
			labels.get(4)[i].setText("x " + players.get(i).getPushPerks());
			if(players.get(i).getLastEffect().IsNegativ()){
				negativeEImage[i].setDrawable(new TextureRegionDrawable(new TextureRegion(map.getTexManager().getPowerUp(players.get(i).getLastEffect()))));
			}
		}
		
		//shake effect for explosions
		if(shake){
		     //Random number between 150 and 210
		      double randomAngle = 150.0f + Math.random()*60;
		      float viewportOffsetX = (float) ( Math.sin(randomAngle / 180.0 * Math.PI) * currentMagnitude-.75f);
		      float viewportOffsetY = (float) ( Math.cos(randomAngle / 180.0 * Math.PI) * currentMagnitude-.75f);
		      viewPort.getCamera().position.set(
		    		  normalCameraPos.x + viewportOffsetX,
		    		  normalCameraPos.y + viewportOffsetY, 1);
		     currentMagnitude -= magnitudeDecreaseFactor*delta;
			viewPort.getCamera().update();
			if(currentMagnitude<=0.11f){  
				viewPort.getCamera().position.set(normalCameraPos);
				viewPort.getCamera().update();
				shake=false;
			}
		 }
		
		//collision detection
		checkCollision();
		//Suddendeath updating
		this.suddenDeath.update(delta);
		spriteBatch.begin();
		
	
		// first draw the map && updating
		map.draw(spriteBatch, delta);
		map.update(delta);
		//get appUpdates
		if(playerApp.size()>0){
			int[] appUpdates=gameClass.getNet().getDirUpdates();
			boolean[] actions=gameClass.getNet().getPlayerAction();;
			//gameClass.getNet().setUpdates();
			
			for (Player p : playerApp) {	
				int nr = players.indexOf(p);
				p.updateMovementApp(appUpdates[nr], actions[nr]);
			}
		}
		//than players & updating player -> Player handles their own Bombs
		for (int i = 0; i < players.size(); i++) {		
			players.get(i).render(spriteBatch);
			players.get(i).update(delta);
			players.get(i).updateSquare(map.getSquare(players.get(i).getHitbox()));
	
//			if(players.get(i).isDead()){
//				if(players.get(i).isAlive()){
//					System.out.println(playerNumber);
//					players.get(i).setAlive();
//					playerNumber--;
//				}
//			}
		}
		boolean oneTeamsAlive=false;
		int lastTeam = -1;
		for(Player p : players){
			if(p.isDead()==false){
				if(lastTeam==-1){
					lastTeam = p.getTeamNumber();
					continue;
				}
				if(lastTeam == p.getTeamNumber()&&lastTeam!=0){
					oneTeamsAlive = true;
					break;
				}
			}
		}
		if(alivePlayerCount<=1||oneTeamsAlive){
			waitForScore-=delta;	
			if(waitForScore<=0f||alivePlayerCount==0){
				gameClass.setScoreScreen(players, mapNames);
			}
		}

		
		
		//draw suddendeath
		this.suddenDeath.render(spriteBatch,font);
		spriteBatch.end();
		//then the stage && updating
		stage.draw();
		stage.act();
		
		
		
		
		
		
		
		
//-------------------------------------------------------------------------------------------------------
		if(Gdx.app.getPreferences("clonebomber").getBoolean("debug",false)   ){
			this.collideables.clear();
			this.collideables.addAll(map.getCollideables());
	
			for (Player p : players) {
				collideables.addAll(p.getColliables());
			}			
			BitmapFont font = new BitmapFont();
			spriteBatch.begin();
			for (Square s : map.getSquares()) {
				font.draw(spriteBatch, Integer.toString( s.getNumber()),s.getCenter().x, s.getCenter().y);
			}	
			spriteBatch.end();
			font.dispose();
			ShapeRenderer sr = new ShapeRenderer();
			sr.setProjectionMatrix(this.viewPort.getCamera().combined);
			sr.setAutoShapeType(true);
			sr.begin(ShapeRenderer.ShapeType.Line);	
			for(Square s : map.getSquares()){
				Rectangle r = s.getHitbox();
				sr.rect(r.x, r.y,r.width, r.height);
			}
			for(Collideable c : collideables){
				sr.rect(c.getHitbox().x, c.getHitbox().y,c.getHitbox().width, c.getHitbox().height);
			}	
			sr.end();
			sr.dispose();
		}
		
//-------------------------------------------------------------------------------------------------------
		
	}

	private void checkCollision() {
		this.collideables.clear();
		this.collideables.addAll(map.getCollideables());
		for (Player p : players) {
//			if(!p.isDead()){
				collideables.addAll(p.getColliables());
//			}
		}			
		for (Collideable firstCheck : collideables) {
//			String iName = collideables.get(i).getUserData().toString();
			if((firstCheck instanceof Wall) || (firstCheck instanceof Box)){
				continue;
			}
			for (Collideable checkWith : collideables) {
				if (firstCheck == checkWith) {
					continue;
				}
				if (Intersector.overlaps(firstCheck
						.getHitbox(), checkWith.getHitbox())) {
//-------------------------------------------------------------------------------------------------------		
					//BEAM COLLIOSION
					//do nothing for wall or other beams 
					if (firstCheck instanceof Blastbeam) {
						CheckForBlastbeam(firstCheck,checkWith);
					} 
					else 
//-------------------------------------------------------------------------------------------------------										
					// PLAYER COLLISION
					// Players dont collide with other Players
					if (firstCheck instanceof Player) {
						Player p = (Player) firstCheck;
						if (checkWith instanceof Wall||checkWith instanceof Box||checkWith instanceof Bomb) {
							p.checkCollision(checkWith);
						} else
						if(checkWith instanceof PowerUP){
							Rectangle temp= new Rectangle();
							Intersector.intersectRectangles(checkWith
									.getHitbox(),firstCheck.getHitbox(), temp);
							if(temp.area()>(map.getFieldSize()/2)){
								PowerUP up = ((PowerUP)checkWith);
								p.addPowerUP(up.getMyEffect());
								up.kill();
							}
						} else if (checkWith instanceof Player) {
							Player op = (Player)firstCheck;
							op.playerContact(p.getLastEffect());
						}	
					} 
//-------------------------------------------------------------------------------------------------------			
					// BOMB COLLISION
					if (firstCheck instanceof Bomb) {
						if ((checkWith instanceof Player) == false) {
							if(checkWith instanceof Wall){
								((Bomb) firstCheck)
									.collides(checkWith);
							}  else {
								((Bomb) firstCheck)
									.collides(checkWith);
							}
						}  else {
							Player p = (Player) checkWith;
							if(p.isPushPerk()){
								((Bomb)firstCheck)
									.startMove(p.getMySquare());
							}
							((Bomb)firstCheck)
								.stopMove(false,p.getMySquare());
						}
					}
//-------------------------------------------------------------------------------------------------------			
							
						}
					}
				}

	}

	private void CheckForBlastbeam(Collideable firstCheck,Collideable checkWith){
		if(checkWith instanceof Box) {
			((Box)checkWith).explode();
		} else if(checkWith instanceof Player) {
			((Player)checkWith).setDead(true);
		} else if(checkWith instanceof Bomb) {
			((Bomb)checkWith).collides(firstCheck);
		}else if(checkWith instanceof PowerUP){
			((PowerUP) checkWith).destroy();
		}		
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

	}

	@Override
	public void hide() {
		Gdx.input.setInputProcessor(null);

	}

	@Override
	public void dispose() {
		font.dispose();
		spriteBatch.dispose();
		stage.dispose();
		map.dispose();
	}
	public int getFieldSize() {
		return map.getFieldSize();
	}

	public void setMap(Array<String> arraySelection) {
		paused=false;
		MapManager mapManager = new MapManager();
		this.mapNames=arraySelection;
		this.map=mapManager.loadMap(arraySelection.first());
		map.load();
	}

	public Square getSquare(MyRectangle hitbox) {
		return map.getSquare(hitbox);	
	}

	@Override
	public boolean keyDown(int keycode) {
		if(Keys.ESCAPE== keycode){
			paused=true;
			gameClass.openPauseMenu(this);
			return true;
		}
		return false;
	}

	
	
	public int getPlayerNumber() {
		return alivePlayerCount;
	}

	public void setShake(boolean shake) {
		this.currentMagnitude=1.75f;
		this.shake = shake;
	}

	public void reducePlayerCount() {
		alivePlayerCount--;
	}

}
