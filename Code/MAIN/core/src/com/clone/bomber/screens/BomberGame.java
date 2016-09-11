package com.clone.bomber.screens;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.clone.bomber.GameClass;
import com.clone.bomber.entity.Bomb;
import com.clone.bomber.entity.Player;
import com.clone.bomber.entity.PowerUP;
import com.clone.bomber.entity.Suddendeath;
import com.clone.bomber.map.Box;
import com.clone.bomber.map.Map;
import com.clone.bomber.map.MapManager;
import com.clone.bomber.map.Square;
import com.clone.bomber.util.Collideable;
import com.clone.bomber.util.MyRectangle;
import com.clone.bomber.util.MySound;

public class BomberGame implements Screen, InputProcessor {
	private Stage stage;
	private Skin skin;
	private SpriteBatch spriteBatch;
	private FillViewport viewPort;
	private OrthographicCamera camera;
	private GameClass gameClass;
	private Map map;
	private ArrayList<Player> players;
	private ArrayList<Collideable> collideables;
	private Suddendeath suddenDeath;
	private Boolean paused=false;
	private int playerNumber;
	private MySound mySound;
	private float waitForScore = 3f;
	private Array<String> mapNames;
	private boolean shake;
	private Vector3 normalCameraPos;
	private float currentMagnitude=1;
	private float magnitudeDecreaseFactor=3f;
	private boolean newRound;
	private Table upgradesTabel;
	private ArrayList<Label[]> labels;
	private Image[] negativeEImage;
	private ArrayList<Player> playerApp;
	private BitmapFont font;

	public BomberGame(boolean newRound, GameClass gameClass, ArrayList<Player> players) {
		this.gameClass = gameClass;		
		camera = new OrthographicCamera(GameClass.viewportWidth,
				GameClass.viewportHeight);
		normalCameraPos=new Vector3(GameClass.viewportWidth / 2,
				GameClass.viewportHeight / 2, 0);
		camera.translate(normalCameraPos);
		viewPort = new FillViewport(GameClass.viewportWidth,
				GameClass.viewportHeight, camera);
		viewPort.apply();	
		stage = new Stage();
		if(players==null){
			this.players = new ArrayList<Player>();
		} else {
			this.players=players;
			
		}
		playerApp=new ArrayList<Player>();
		collideables = new ArrayList<Collideable>();
		skin = new Skin(Gdx.files.internal("res/gui/uiskin.json"));		
		spriteBatch = new SpriteBatch();
		waitForScore = 3f;
		mySound=new MySound();
		this.newRound=newRound;
		font = new BitmapFont(Gdx.files.internal("res/gui/november.fnt"));
		font.setColor(Color.YELLOW);
//		font.getData().setScale(2, 2);
	}

	@Override
	public void show() {
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
		
		
		labels = new ArrayList<Label[]>();
		for(int s = 0 ; s < 5; s ++ ){
			labels.add(new Label[8]);
		}
		upgradesTabel = new Table(skin);
		
		upgradesTabel.setBounds(30,0, 240,720);
		for(Label[] ls : labels){
			for (int i = 0; i < ls.length; i++) {
				ls[i]=new Label("", skin);;
			}
		}
		negativeEImage = new Image[8];
		Array<Controller> controllers = Controllers.getControllers();
		if(paused==false){
			waitForScore=3f;
			map.load();
			if(newRound){
				for(int i =0; i<playerNumber;i++){
					upgradesTabel.add("Player " + (i+1)).size(30).row();;
					upgradesTabel.add(new Image(map.getTexManager().getPowerUp("speed"))).size(20);
					upgradesTabel.add(labels.get(0)[i]).size(30);
					upgradesTabel.add(new Image(map.getTexManager().getPowerUp("blastradius"))).size(20);
					upgradesTabel.add(labels.get(1)[i]).size(30);
					upgradesTabel.add(new Image(map.getTexManager().getPowerUp("bomb"))).size(20);
					upgradesTabel.add(labels.get(2)[i]).size(30);
					upgradesTabel.add(new Image(map.getTexManager().getPowerUp("throw"))).size(20);
					upgradesTabel.add(labels.get(3)[i]).size(30);
					upgradesTabel.add(new Image(map.getTexManager().getPowerUp("push"))).size(20);
					upgradesTabel.add(labels.get(4)[i]).size(30);
					negativeEImage[i]=new Image();
					upgradesTabel.add(negativeEImage[i]).size(20);
					upgradesTabel.row();
					 
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
			} else {
				for(int i =0; i<playerNumber;i++){
					players.get(i).reset(map.getSpawnSpots().get(i),map);
				}
			}
			
			suddenDeath=new Suddendeath(map, mySound);
		}
		
		InputMultiplexer inputMultiplexer = new InputMultiplexer();
		for (Player p : players) {
			inputMultiplexer.addProcessor(p);
		}
		spriteBatch.setProjectionMatrix(viewPort.getCamera().combined);
		stage.addActor(upgradesTabel);
		inputMultiplexer.addProcessor(this);
		Gdx.input.setInputProcessor(inputMultiplexer);
	}

	@Override
	public void render(float delta) {
		//if framerate is slower than 30 fps than cap it at 30 
		// -> this is to prevent to "big" movements at a time 
		// -> game should slow down if you have worse framerate
		if(delta>0.033333f){
			delta=0.033333f;
		}

		
		//update upgrades
		for(int i =0; i<playerNumber;i++){
			labels.get(0)[i].setText("x " + players.get(i).getSpeed());
			labels.get(1)[i].setText("x " + players.get(i).getBlastRadiusUPs());
			labels.get(2)[i].setText("x " + players.get(i).getBombUPs());
			labels.get(3)[i].setText("x " + players.get(i).getBombThrows());
			labels.get(4)[i].setText("x " + players.get(i).getPushPerks());
			if(players.get(i).getNegativeEffect()!=-1){
				String temp = null;
				if(players.get(i).getNegativeEffect()==6){
					temp="joint";
				} else 
				if(players.get(i).getNegativeEffect()==7){
					temp="diarrhea";
				}else 
				if(players.get(i).getNegativeEffect()==8){
					temp="superspeed";
				}
				if(temp!=null){
					negativeEImage[i].setDrawable(new TextureRegionDrawable(new TextureRegion(map.getTexManager().getPowerUp(temp))));
				}
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
	
			if(players.get(i).isDead()){
				if(players.get(i).isAlive()){
					System.out.println(playerNumber);
					players.get(i).setAlive();
					playerNumber--;
				}
			}
		}
		
		if(playerNumber<=1){
			waitForScore-=delta;	
			if(waitForScore<=0f||playerNumber==0){
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
			if(!p.isDead()){
				collideables.addAll(p.getColliables());
			}
		}			
		for (int i = 0; i < collideables.size(); i++) {
			String iName = collideables.get(i).getUserData().toString();
			if (!iName.contains("Wall") && !iName.contains("Box")) {
				for (int s = 0; s < collideables.size(); s++) {
					if (collideables.get(s) != collideables.get(i)) {
						String sName = collideables.get(s).getUserData()
								.toString();
							if (Intersector.overlaps(collideables.get(s)
									.getHitbox(), collideables.get(i).getHitbox())) {
//-------------------------------------------------------------------------------------------------------		
								//BEAM COLLIOSION
								//do nothing for wall or other beams 
								if (iName.contains("Blastbeam")) {
									if(sName.contains("Box")) {
										((Box) collideables.get(s).getUserData()).explode();
									} else if(sName.contains("Player")) {
										((Player) collideables.get(s).getUserData()).setDead(true);
									} else if(sName.contains("Bomb")) {
										((Bomb) collideables.get(s).getUserData()).collides(iName);
									}else if(sName.contains("PowerUP")){
										((PowerUP) collideables.get(s)).destroy();
									}
								} else if(sName.contains("Blastbeam")){

									if(iName.contains("Box")) {
										((Box) collideables.get(i).getUserData()).explode();
									} else if(iName.contains("Player")) {
										((Player) collideables.get(i).getUserData()).setDead(true);
									} else if(iName.contains("Bomb")) {
										((Bomb) collideables.get(i).getUserData()).collides(sName);
									}else if(iName.contains("PowerUP")){
										((PowerUP) collideables.get(i)).destroy();
									}
								} 	else
//-------------------------------------------------------------------------------------------------------										
								// PLAYER COLLISION
								// Players dont collide with other Players
								if (sName.contains("Player")) {
									Player p = (Player) collideables.get(s)
											.getUserData();
									if (iName.contains("Wall")||iName.contains("Box")||iName.contains("Bomb")) {
										p.checkCollision(collideables.get(i));
									} else if(iName.contains("PowerUP")){
										Rectangle temp= new Rectangle();
										Intersector.intersectRectangles(collideables.get(s)
												.getHitbox(), collideables.get(i).getHitbox(), temp);
										if(temp.area()>29){
											PowerUP up = ((PowerUP)collideables.get(i)
													.getUserData());
											up.getMySquare().setHasPowerUp(false);
											p.addPowerUP(up.getType());
											up.kill();
										}
									} else if (iName.contains("Player")) {
										Player op = (Player) collideables.get(i)
												.getUserData();
										op.playerContact(p.getNegativeEffect());
									}	
								} else
								if (iName.contains("Player")) {
									Player p = (Player) collideables.get(i)
												.getUserData();
									if (sName.contains("Wall")||sName.contains("Box")||iName.contains("Bomb")) {
										p.checkCollision(collideables.get(s));
									} else if(sName.contains("PowerUP")){
										//Do nothing get handles by other collision
									} else if (sName.contains("Player")) {
										Player op = (Player) collideables.get(s)
												.getUserData();
										op.playerContact(p.getNegativeEffect());
									}
								}
//-------------------------------------------------------------------------------------------------------			
								// BOMB COLLISION
								if (iName.contains("Bomb")) {
									if (!sName.contains("Player")) {
										if(sName.contains("Wall")){
											((Bomb) collideables.get(i).getUserData())
												.collides(sName);
										}  else {
											((Bomb) collideables.get(i).getUserData())
												.collides(sName);
										}
									}  else {
										Player p = (Player) collideables.get(s)
												.getUserData();
										if(p.isPushPerk()){
											((Bomb) collideables.get(i).getUserData())
												.startMove(p.getMySquare());
										}
										((Bomb) collideables.get(i).getUserData())
											.stopMove(false,p.getMySquare());
									}
								}
								if (sName.contains("Bomb")) {
									if (!iName.contains("Player")) {
										if(iName.contains("Wall")){
											((Bomb) collideables.get(s).getUserData())
												.collides(iName);
										} else {
										((Bomb) collideables.get(s).getUserData())
											.collides(iName);
										}
									} else {
										Player p = (Player) collideables.get(i)
												.getUserData();
										if(p.isPushPerk()){
											((Bomb) collideables.get(s).getUserData())
												.startMove(p.getMySquare());
										}
										((Bomb) collideables.get(s).getUserData())
											.stopMove(false,p.getMySquare());
									}
								}
//-------------------------------------------------------------------------------------------------------			
							
						}
					}
				}
			}
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
	
	public int getPlayerNumber() {
		return playerNumber;
	}

	public void setPlayerNumber(int playerNumber) {
		this.playerNumber = playerNumber;
	}

	public void setShake(boolean shake) {
		this.currentMagnitude=1.75f;
		this.shake = shake;
	}

}
