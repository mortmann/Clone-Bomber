package com.clone.bomber.screens;

import java.io.BufferedReader;
import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.clone.bomber.GameClass;
import com.clone.bomber.map.Box;
import com.clone.bomber.map.Map;
import com.clone.bomber.map.MapManager;
import com.clone.bomber.map.Square;
import com.clone.bomber.map.Wall;
import com.clone.bomber.util.MyScreen;

public class Editor extends MyScreen {

	private Map map;
	private Square clickedSquare;
	private ScrollPane mapObjectsScrollPane;
	private List<String> listMapObject;
	private transient Texture spawnSpot;
	private TextButton saveMap;
	private MapManager mapManager;
	private TextField mapName;
	private Label playerNumberDefLabel;
	private Label playerNumberLabel;
	private TextButton playerNumberPlusButton;
	private TextButton playerNumberMinusButton;
	private ScrollPane mapScrollPane;
	private TextButton loadButton;
	private Array<String> mapNames;
	private List<String> list;
	private Texture randomBox;
	private boolean importClanBomber;
	private Dialog saveMapDialog;
	private TextButton saveMapOverWriteButton;
	private TextButton saveMapCancelButton;
	private static String[] objects={"Wall","Box","RandomBox","Ice","Hole","Spawnspot","UpArrow","RightArrow","DownArrow","LeftArrow","Clear","Void"}; 
	
	public Editor(GameClass gameClass) {
		this.gameClass=gameClass;
	}
	@Override
	public void OnShow(){
		OnShow();
		listMapObject= new List<String>(skin);
		listMapObject.setItems(objects);
		mapObjectsScrollPane= new ScrollPane(listMapObject,skin);
		mapObjectsScrollPane.setBounds(10, 50, 200, 575);
		saveMap= new TextButton("Save Map",skin);
		mapName= new TextField("Map Name", skin);
		saveMap.setPosition(viewPort.getWorldWidth()-250, 25);
		saveMap.setSize(225, 50);
		mapName.setPosition(viewPort.getWorldWidth()-250, 75);
		mapName.setSize(225, 50);
		
		playerNumberDefLabel= new Label("Number of Player", skin);
		playerNumberDefLabel.setBounds(30, viewPort.getWorldHeight()-45, 100, 40);
		playerNumberLabel= new Label("2", skin);
		playerNumberLabel.setBounds(50, viewPort.getWorldHeight()-75, 40, 40);
		
		playerNumberPlusButton= new TextButton("+", skin);
		playerNumberMinusButton= new TextButton("-", skin);
		playerNumberPlusButton.setBounds(90, viewPort.getWorldHeight()-55, 40, 20);
		playerNumberMinusButton.setBounds(90, viewPort.getWorldHeight()-75, 40, 20);
		playerNumberPlusButton.addListener(new ClickListener() {
			@Override
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				int temp=Integer.valueOf(playerNumberLabel.getText().toString())+1;
				if(temp<=8){
					playerNumberLabel.setText(Integer.toString(temp));
					map.setPlayerNumber(temp);
				}
				
				return false;
			}
		});	
		playerNumberMinusButton.addListener(new ClickListener() {
			@Override
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				int temp=Integer.valueOf(playerNumberLabel.getText().toString())-1;
				if(temp>=2) {
					playerNumberLabel.setText(Integer.toString(temp));
					map.setPlayerNumber(temp);
				} 
				return false;
			}
		});	
		FileHandle dirHandle = Gdx.files.internal("./maps/");
	    Array<FileHandle> handles = new Array<FileHandle>();
	    mapNames=new Array<String>();
	    getDirectoryHandles(dirHandle, handles);
	    splitMapNames();
	    list =new List<String>(skin);
	    list.setItems(mapNames);
	    list.getSelection().setRequired(true);
		mapScrollPane = new ScrollPane(list, skin);	
	    mapScrollPane.setBounds(viewPort.getWorldWidth()-250, viewPort.getWorldHeight()-425, 225, 400);
	    loadButton =new TextButton("LOAD LEVEL", skin);
	    loadButton.setBounds(viewPort.getWorldWidth()-250, viewPort.getWorldHeight()-475, 225, 50);
	    mapManager= new MapManager(map);
	    loadButton.addListener(new ClickListener() {
			@Override
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
					playerNumberLabel.setText(list.getSelected().substring(0, 1));
					map=mapManager.loadMap(list.getSelected());
					map.load();
				return false;
			}
		});	
	    saveMapDialog = new Dialog("It already exists a Map with this Name", skin);
	    saveMapDialog.setBounds(GameClass.viewportWidth/2-200, GameClass.viewportHeight/2, 400, 200);
	    saveMapOverWriteButton=new TextButton("Overwrite", skin);
	    saveMapOverWriteButton.addListener(new ClickListener() {
			@Override
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				MapManager.createMap(map, mapName.getText());
				saveMapDialog.hide();
				return false;
			}
		});	

	    saveMapCancelButton=new TextButton("Cancel", skin);	    
	    saveMapCancelButton.addListener(new ClickListener() {
			@Override
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				saveMapDialog.hide();
				return false;
			}
		});	
	    saveMapDialog.add(saveMapOverWriteButton);
	    saveMapDialog.add(saveMapCancelButton);
	    
	    
	    stage.addActor(mapScrollPane);
		stage.addActor(loadButton);
		stage.addActor(playerNumberDefLabel);
		stage.addActor(playerNumberLabel);
		stage.addActor(playerNumberMinusButton);
		stage.addActor(playerNumberPlusButton);
		stage.addActor(mapObjectsScrollPane);
		stage.addActor(mapName);
		stage.addActor(saveMap);
		saveMap.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y)  {
				if( mapName.getText().length()>0 && !Gdx.files.local("./maps/custom/"+map.getSpawnSpots().size()+"-"+mapName.getText() + ".dat").exists()){
					MapManager.createMap(map, mapName.getText());
				} else {
					saveMapDialog.show(stage);
				}
			}
		});	
		stage.setViewport(viewPort);
	
		map = new Map(17);	
		spawnSpot=map.getTexManager().getSpawnSpotTexture();
		randomBox=map.getTexManager().getRandomBoxTexture();
		clickedSquare=null;
		spriteBatch = new SpriteBatch();
	}
	public void getDirectoryHandles(FileHandle begin, Array<FileHandle> handles){
	    FileHandle[] newHandles = begin.list();

	    for (FileHandle f : newHandles){
	    	String dirName="";
	    	if(f.isDirectory()){
	    		dirName=f.toString().substring(6) + "/";	
	    		getDirectoryHandles(f, handles);
	    	} else {
	    		if(f.extension().contains("dat")){
	    			mapNames.add(dirName + f.nameWithoutExtension().toString());
	    		} 
// --------------- FOR IMPORTING MAPS FROM CLANBOMBER! ----------------------------------
	    		importClanBomber=false;
	    		if(importClanBomber){
	    		if(f.extension().contains("map")){
	    			map = new Map(17);
	    			String name =f.nameWithoutExtension();
	    			BufferedReader br = f.reader(255);		
					
					try {
						br.readLine();
						br.readLine();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
	    					try {
	    						int squareCount=220;
	    						Square[] squares=map.getTemp();
	    						String string = br.readLine();
								for(int reihe=13; reihe>0;reihe--){
									for(int zelle=16; zelle >=0; zelle--){
										Square s = squares[squareCount];
										if(!s.isEmpty()){
											if(string!=null){
											char[] chars=string.toCharArray();							
												if(chars[zelle] == ' '){
													//s.clearSquare(false);
												} else if(chars[zelle] == '-'){
													s.clearSquare(true);
												} else if(chars[zelle] == '*'){
													Wall w = new Wall(s,map);	
													map.addWall(w);
													s.setHasWall(true, w);
												} else if(chars[zelle] == '+'){
													Box b = new Box(s, map);	
													map.addBox(b);
													s.setHasBox(true, b);
												} else if(chars[zelle] == 'v'){
													s.setArrow(0);
												} else if(chars[zelle] == '^'){
													s.setArrow(2);
												} else if(chars[zelle] == '>'){
													s.setArrow(3);
												} else if(chars[zelle] == '<'){
													s.setArrow(1);
												} else if(chars[zelle] == 'S'){
													s.setIce(true);
												} else if(chars[zelle] == 'o'){
													s.setHole(true);
												} else if(Character.isDigit(chars[zelle])){
													map.addSpawnspotTemp(s);
												} else if(chars[zelle] == 'R'){
													Box b = new Box(s,true,map);	
													map.addBox(b);
													s.setHasBox(true, b);
												}
											
												} else {
													s.clearSquare(true);
												}	
											
											}								
										squareCount--;
									}
									
									string = br.readLine();
									
								}
							}  catch (IOException e) {
								
								e.printStackTrace();
							}		
	    			
	    				MapManager.createMap(map, name);
	    		}
	    		}
//------------------------------------------------------------------------------------------------------------
	        } 
	    }
	}
	public void splitMapNames(){
		//get the playernumber from game and split them among the hashtable in arrays
		Array<String> namesTwo = new Array<String>();
		Array<String> namesThree = new Array<String>();
		Array<String> namesFour = new Array<String>();
		Array<String> namesFive = new Array<String>();
		Array<String> namesSix = new Array<String>();
		Array<String> namesSeven = new Array<String>();
		Array<String> namesEight = new Array<String>();
		for(String temp : mapNames){
			int i = Character.getNumericValue(temp.charAt(0));
			if(i==2){
				namesTwo.add(temp);
			} else
			if(i==3){
				namesThree.add(temp);
			} else
			if(i==4){
				namesFour.add(temp);
			}else
			if(i==5){
				namesFive.add(temp);
			}else
			if(i==6){
				namesSix.add(temp);
	    	}else
			if(i==7){
				namesSeven.add(temp);
			}else
			if(i==8){
				namesEight.add(temp);
			}
		}
		mapNames.clear();
		mapNames.addAll(namesTwo);
		mapNames.addAll(namesThree);
		mapNames.addAll(namesFour);
		mapNames.addAll(namesFive);
		mapNames.addAll(namesSix);
		mapNames.addAll(namesSeven);
		mapNames.addAll(namesEight);
	}
	@Override
	public void show() {
		InputMultiplexer inputMultiplexer = new InputMultiplexer();
		inputMultiplexer.addProcessor(stage);
		inputMultiplexer.addProcessor(this);
		Gdx.input.setInputProcessor(inputMultiplexer);

	}

	@Override
	public void render(float delta) {
		spriteBatch.begin();
		spriteBatch.setProjectionMatrix(viewPort.getCamera().combined);
		// first draw the map
		if(Gdx.input.isKeyPressed(Keys.ESCAPE)){
			stage.setKeyboardFocus(null);
		}
		map.draw(spriteBatch, delta);
		for(Square s : map.getSpawnSpots()){
			spriteBatch.draw(spawnSpot, s.getX(), s.getY(), s.getSize(), s.getSize());
		}
		for(Square s : map.getSquares()){
			if(s.getMyBox()!=null){
				if(s.getMyBox().isRandom()){
					
					spriteBatch.draw(randomBox, s.getX(),s.getY(),s.getSize(),s.getSize());
				}
			}
		}
		spriteBatch.end();
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
		InputMultiplexer inputMultiplexer = new InputMultiplexer();
		inputMultiplexer.addProcessor(stage);
		inputMultiplexer.addProcessor(this);
		Gdx.input.setInputProcessor(inputMultiplexer);
	}

	@Override
	public void hide() {

	}

	@Override
	public void dispose() {
		
	}

	@Override
	public boolean keyDown(int keycode) {
		
		if(Keys.ESCAPE== keycode){
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
		Vector3 mousePos = this.camera.unproject(new Vector3(screenX,screenY,0));
		for(Square s : map.getSquares()){
			if(s.getHitbox().contains(mousePos.x,mousePos.y)){
				if(map.getSpawnSpots().contains(s)){
					map.getSpawnSpots().remove(s);
				}
				clickedSquare=s;
				int i = s.getNumber();
					if(i%map.getMapSize()==1 && i!=1){
						clickedSquare=null;
					} else
					if(i%map.getMapSize()==0 || i<map.getMapSize() || i>(map.getMapSize()*map.getMapSize())-map.getMapSize()){
						clickedSquare=null;
					}
				
				if(clickedSquare!=null){
					if(!s.isEmpty()){				
						if(listMapObject.getSelected().contains("Wall")){
							if(!s.isHasWall()){
								Wall w = new Wall(s,map);
								s.setHasWall(true,w);
								this.map.addWall(w);
							}
						} else
						if(listMapObject.getSelected().contains("RandomBox")){
							if(!s.isHasBox()){
								Box b = new Box(s,true,map);
								s.setHasBox(true,b);
								this.map.addBox(b);
							}
						} else
						if(listMapObject.getSelected().contains("Box")){
							if(!s.isHasBox()){
								Box b = new Box(s,map);
								s.setHasBox(true,b);
								this.map.addBox(b);
							}
						} else
						if(listMapObject.getSelected().contains("Ice")){
							s.setIce(true);
						} else
						if(listMapObject.getSelected().contains("UpArrow")){
							s.setArrow(0);
						} else
						if(listMapObject.getSelected().contains("RightArrow")){
							s.setArrow(1);
						} else
						if(listMapObject.getSelected().contains("DownArrow")){
							s.setArrow(2);
						} else
						if(listMapObject.getSelected().contains("LeftArrow")){
							s.setArrow(3);
						} else
						if(listMapObject.getSelected().contains("Hole")){
							
							s.setHole(true);
						} else
						if(listMapObject.getSelected().contains("Spawnspot")){
							map.addSpawnspot(s);
						} 
					}
					if(listMapObject.getSelected().contains("Clear")){
						s.clearSquare(false);
					} 

					if(listMapObject.getSelected().contains("Void")){
						s.clearSquare(true);
					}
				}
			}
		}
		stage.setKeyboardFocus(null);
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
