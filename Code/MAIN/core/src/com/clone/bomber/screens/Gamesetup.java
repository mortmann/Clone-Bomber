package com.clone.bomber.screens;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldFilter;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.clone.bomber.GameClass;
import com.clone.bomber.map.Map;
import com.clone.bomber.map.MapManager;
import com.clone.bomber.map.Square;
import com.clone.bomber.util.MyChangeListener;
import com.clone.bomber.util.MyClickListener;


public class Gamesetup implements Screen, InputProcessor  {
	public static final int ID = 3;	
	private static final int maxNumberPlayer=8;
	
	private static final String[] characterNames = {"bsd","tux","snake","spider","dull_blue","dull_green","dull_yellow","dull_red"};
	private static final String[] controlsNames = {"Keyboard","Controller","App"};
	private static final String[] teamNames = {"No Team","Gold","Leaf","Blood","Water"};
	
	//get MapNames through fileNames
	private Array<String> mapNames;
	private Preferences prefs;
	private TextButton loadButton;
	private Label mapDescriptionLabel;
	private List<String> list;
	private ScrollPane mapScrollPane;
	private MapManager mapManager;
	private Stage stage;
	private Skin skin;
	private FitViewport viewPort;
	private OrthographicCamera camera;
	private GameClass gameClass;
	private Label selectionLabel;
	private Table[] playerTable;
	private TextButton setButton;
	private TextButton[] playerUpButton;
	private TextButton[] playerDownButton;
	private TextButton[] playerRightButton;
	private TextButton[] playerLeftButton;
	private TextButton[] playerActionButton;
	private Array<MySelectBox> playerControllerBoxArray;
	private Array<SelectBox<String>> playerTeamBoxArray;
	private Label playerNumberLabel;
	private Label playerNumberDefLabel;
	
	private TextButton playerNumberPlusButton;
	private TextButton playerNumberMinusButton;
	
	private Label winNumberLabel;
	private Label winNumberDefLabel;
	private TextButton winNumberPlusButton;
	private TextButton winNumberMinusButton;

	//CONTROLLER STUFF
	private Label controllerNameLabel;
	private String[] controllerPlayer;
	private Array<Controller> controllers;
	private static final int controllerX = GameClass.viewportWidth/2 - 150;
	private static final int controllerY = 0;
	private static final int controllerHeight = 150;
	private static final int controllerWidth = 300;
	
	private Map map;
	private OrthographicCamera mapCamera;
	private SelectBox<String> selectBox;
	
	private Label mapStyleLable;
	private Image[] playerImage;
	private Dialog noControllerDialog;
	//test
	private FitViewport viewMapPort;
	private HashMap<Integer, Array<String>> hashMap;
	private Table gameSetupTable;
	private Texture spawnSpot;
	private Texture randomBox;
	private TextField suddenDeathTimer;
	private Label suddenDeathTimerDef;
	private SpriteBatch spriteBatch;

	//for remembering if the controls are to set
	private String[] playersLastControls;
	
	public Gamesetup(GameClass gc){
		this.gameClass=gc;		
	}
	
	@Override
	public void show() {	
		prefs = Gdx.app.getPreferences("clonebomber");
		hashMap = new HashMap<Integer, Array<String>>();
		
		mapCamera =new OrthographicCamera(GameClass.viewportWidth,
				GameClass.viewportHeight);
		mapCamera.zoom=1.75f;
		spriteBatch = new SpriteBatch();
		viewMapPort = new FitViewport(GameClass.viewportWidth,
				GameClass.viewportHeight, mapCamera);
		viewMapPort.update(Gdx.graphics.getWidth(),Gdx.graphics.getHeight(),true); 
		viewMapPort.apply();		
		viewMapPort.getCamera().position.set(GameClass.viewportWidth / 2,
				GameClass.viewportHeight / 2  - 200,1.75f);
		viewMapPort.getCamera().update();
		
		camera = new OrthographicCamera(GameClass.viewportWidth,
				GameClass.viewportHeight);
		camera.translate(new Vector3(GameClass.viewportWidth / 2,
				GameClass.viewportHeight / 2, 0));
		viewPort = new FitViewport(GameClass.viewportWidth,
				GameClass.viewportHeight, camera);
		viewPort.apply();
		
		viewPort.setCamera(camera);
		viewPort.apply();
		
		
		FileHandle dirHandle = Gdx.files.internal("./maps/");
	    Array<FileHandle> handles = new Array<FileHandle>();
	    mapNames=new Array<String>();
	    getDirectoryHandles(dirHandle, handles);
	    
	    stage=new Stage();
	    skin = new Skin(Gdx.files.internal("res/gui/uiskin.json"));
	    list =new List<String>(skin);
	    mapManager= new MapManager(map);
	    
	    float x=viewPort.getWorldWidth()/2;
		float y=viewPort.getWorldHeight()/2;

		controllers= Controllers.getControllers();
		
		playerNumberSetUP();
		
		setupPlayerTable();

		//TODO TO something here or remove it?
	    mapDescriptionLabel=new Label("", skin);
	    mapDescriptionLabel.setBounds(x+200, y, 200, 150);
	    
	    //maps names spliting and stuff
  		splitMapNames();
  		updateMaps();
  		
  		//create minimap and add the mapScrollPane listeners so that it changes the minimap
  		map = new Map(19);
  		map = mapManager.loadMap(list.getSelected());
  		mapSelectionAndLoad();
  		
  		controllerReadInAndSetup();
  		
  		createPlayerTablesAndSetValues();
		
		setupGameTable();

		//add all that stuff to the stage
		stage.addActor(gameSetupTable);
		stage.addActor(controllerNameLabel);
		stage.addActor(playerNumberDefLabel);
	    stage.addActor(playerNumberMinusButton);
	    stage.addActor(playerNumberPlusButton);
		stage.addActor(playerNumberLabel);
		stage.addActor(loadButton);
	    stage.addActor(mapDescriptionLabel);
	    stage.addActor(selectionLabel);
	    stage.addActor(mapScrollPane);
	    
	    //stage stuff
		stage.setViewport(viewPort);
		stage.getCamera().update();
		stage.getViewport().apply();
		stage.getViewport().update(Gdx.graphics.getWidth(),Gdx.graphics.getHeight(),true); 
		
		spawnSpot=map.getTexManager().getSpawnSpotTexture();
		randomBox=map.getTexManager().getRandomBoxTexture();
		
		//input stuff
		InputMultiplexer inputMultiplexer = new InputMultiplexer();
		inputMultiplexer.addProcessor(stage);
		inputMultiplexer.addProcessor(this);
		Gdx.input.setInputProcessor(inputMultiplexer);
		
	}

	private void createPlayerTablesAndSetValues() {
		//creating the tables and setting there values
		for(int i =0; i<maxNumberPlayer;i++){
			int[] keys = new int[5];			
			boolean con=prefs.getString("Player " + Integer.toString(i)+" Controls", "Tastatur").contains("Controller");
			if(con && controllers.size==0){
				noControllerDialog = new Dialog("Warning! No controllers connected!", skin);
				TextButton tempB = new TextButton("close", skin);
				tempB.addListener(new ClickListener() {
					@Override
					public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
						noControllerDialog.hide();
						return false;
					}
				});	
				noControllerDialog.add("One of the Players requiers one! Connect and restart game!");
				noControllerDialog.add(tempB);
				noControllerDialog.show(stage);
			}
			keys[0]=prefs.getInteger("Player " + Integer.toString(i)+" UP", keys[0]);
			keys[1]=prefs.getInteger("Player " + Integer.toString(i)+" DOWN", keys[1]);
			keys[2]=prefs.getInteger("Player " + Integer.toString(i)+" RIGHT", keys[2]);
			keys[3]=prefs.getInteger("Player " + Integer.toString(i)+" LEFT", keys[3]);
			keys[4]=prefs.getInteger("Player " + Integer.toString(i)+" ACTION", keys[4]);
			if(!con){
				if(keys[0]!=-1)
					playerUpButton[i].setText(playerUpButton[i].getName() + Keys.toString(keys[0]));
				if(keys[1]!=-1)
					playerDownButton[i].setText(playerDownButton[i].getName() + Keys.toString(keys[1]));
				if(keys[2]!=-1)
					playerRightButton[i].setText(playerRightButton[i].getName() + Keys.toString(keys[2]));
				if(keys[3]!=-1)
					playerLeftButton[i].setText(playerLeftButton[i].getName() + Keys.toString(keys[3]));
				if(keys[4]!=-1)
					playerActionButton[i].setText(playerActionButton[i].getName() + Keys.toString(keys[4]));
			} else {
				playerUpButton[i].setText(playerUpButton[i].getName() + Integer.toString(keys[0]));
				playerDownButton[i].setText(playerDownButton[i].getName() + Integer.toString(keys[1]));
				playerRightButton[i].setText(playerRightButton[i].getName() + Integer.toString(keys[2]));
				playerLeftButton[i].setText(playerLeftButton[i].getName() + Integer.toString(keys[3]));
				playerActionButton[i].setText(playerActionButton[i].getName() + Integer.toString(keys[4]));
			}
			playerControllerBoxArray.get(i).setSelected(prefs.getString("Player " + Integer.toString(i)+" Controls", "Tastatur"));
			this.playersLastControls[i] = playerControllerBoxArray.get(i).getSelected();
			playerTeamBoxArray.get(i).setSelected(prefs.getString("Player "  + Integer.toString(i)+" Team","No Team"));
		}
	}

	private void mapSelectionAndLoad() {
	    float x=viewPort.getWorldWidth()/2;
		float y=viewPort.getWorldHeight()/2;
		//map Selection and load button def
		mapScrollPane = new ScrollPane(list, skin);	
	    mapScrollPane.setBounds(x+245, y-220, 350, 500);
	    loadButton =new TextButton("LOAD LEVEL", skin);
	    loadButton.setBounds(x+245, y-270, 350, 50);
	    
	    
	    list.getSelection().setMultiple(true);
	    list.getSelection().setRequired(true);
	
	    selectionLabel=new Label("Map Select: \n     Press STRG For Muliselect!", skin);
	    selectionLabel.setBounds(x+250, y+275, 350, 80);
		
		mapScrollPane.addListener(new ClickListener(){
  			@Override
  			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
  				mapManager.setMapName(list.getSelected());
  				map = mapManager.loadMap(list.getSelected());
  				map.load();
  				return false;
  			}
  		});	
  		loadButton.addListener(new ClickListener(){
  			private Dialog teamDialog;
			@Override
  			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
  				boolean enoughTeams=false;
  				int numberOfTeams=0;
  				int[] teams = new int[]{0,0,0,0,0};//size 5
  				for(int i =0; i<Integer.valueOf(playerNumberLabel.getText().toString());i++){
  					teams[playerTeamBoxArray.get(i).getSelectedIndex()]++;
  				}
  				for(int i =0; i< teams.length;i++){
  					if(teams[i] >0){
  						numberOfTeams++;
  					}
  				}
  				
  				if(numberOfTeams>1){
  					enoughTeams=true;
  				} else if(teams[0] == Integer.valueOf(playerNumberLabel.getText().toString())){
  					enoughTeams=true;
  				}
  				if(enoughTeams){
  				for(int i =0; i<Integer.valueOf(playerNumberLabel.getText().toString());i++){
  					int[] keys = new int[5];		
  					if(playerControllerBoxArray.get(i).getSelected().contains("Tastatur")){
	  					keys[0]=Keys.valueOf(playerUpButton[i].getText().toString().substring(4));
	  					keys[1]=Keys.valueOf(playerDownButton[i].getText().toString().substring(6));
	  					keys[2]=Keys.valueOf(playerRightButton[i].getText().toString().substring(7));
	  					keys[3]=Keys.valueOf(playerLeftButton[i].getText().toString().substring(6));
	  					keys[4]=Keys.valueOf(playerActionButton[i].getText().toString().substring(8));
  					} else if(playerControllerBoxArray.get(i).getSelected().contains("Controller")){
	  					keys[0]=Integer.valueOf(playerUpButton[i].getText().toString().substring(4));
	  					keys[1]=Integer.valueOf(playerDownButton[i].getText().toString().substring(6));
	  					keys[2]=Integer.valueOf(playerRightButton[i].getText().toString().substring(7));
	  					keys[3]=Integer.valueOf(playerLeftButton[i].getText().toString().substring(6));
	  					keys[4]=Integer.valueOf(playerActionButton[i].getText().toString().substring(8));
  					} else if(!playerControllerBoxArray.get(i).getSelected().contains("App")){
	  					keys[0]=-1;
	  					keys[1]=-1;
	  					keys[2]=-1;
	  					keys[3]=-1;
	  					keys[4]=-1;
  					}
  					prefs.putString("Player " + Integer.toString(i)+" Controls", playerControllerBoxArray.get(i).getSelected());
  					if(playerControllerBoxArray.get(i).getSelected().contains("App")){
  						gameClass.getNet().sendStartMessage(i);
  					}
  					prefs.putInteger("Player " + Integer.toString(i)+" UP", keys[0]);
  					prefs.putInteger("Player " + Integer.toString(i)+" DOWN", keys[1]);
  					prefs.putInteger("Player " + Integer.toString(i)+" RIGHT", keys[2]);
  					prefs.putInteger("Player " + Integer.toString(i)+" LEFT", keys[3]);
  					prefs.putInteger("Player " + Integer.toString(i)+" ACTION", keys[4]);
  					prefs.putString("Player "  + Integer.toString(i)+" Char",playerImage[i].getName());
  					prefs.putInteger("Player "  + Integer.toString(i)+" Team",playerTeamBoxArray.get(i).getSelectedIndex());
  					prefs.flush();
  				}
  				prefs.putInteger("Wins", Integer.valueOf(winNumberLabel.getText().toString()));
  				prefs.putInteger("Suddendeath-Timer", Integer.valueOf(suddenDeathTimer.getText()));
  				gameClass.setGame(true,list.getSelection().toArray(),Integer.valueOf(playerNumberLabel.getText().toString()), null);
  				} else {
  					teamDialog = new Dialog("Don´t be silly! Dont be all on one Team!",skin);
  					TextButton b = new TextButton("Okay", skin);
  					b.addListener(new ClickListener(){
  						@Override
  						public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
  							teamDialog.hide();
  							return false;
  						}
  					});
  					teamDialog.add(b);
  					teamDialog.show(stage);
  				}
  				return false;
  			}
  		});	
  		

	}

	private void controllerReadInAndSetup() {
		//controller stuff decleration and listeners
		controllerNameLabel = new Label("", skin);
		controllerNameLabel.setBounds(controllerX, controllerY, controllerWidth, controllerHeight);
		controllerPlayer=new String[8];
		
		if(controllers.size>0){
			controllerNameLabel.setText("Controller Names: \n");
		}
		for (Controller controller : Controllers.getControllers()) {
			controllerNameLabel.setText(controllerNameLabel.getText() + controller.getName() + "\n");
			controller.addListener(new ControllerAdapter() {
				@Override
				public boolean buttonDown(Controller controller, int buttonCode) {
					buttonCode+=1;
					if(setButton!=null){
						if(playerControllerBoxArray.get(Integer.parseInt(setButton.getParent().getName())).getSelected().contains("Controller")){
							setButton.setText(setButton.getName() + buttonCode);
							setButton = null;
						}
					}
					return false;
				}
				@Override
				public boolean povMoved(Controller controller, int povCode,
						PovDirection value) {
					if(setButton!=null){
						if(playerControllerBoxArray.get(Integer.parseInt(setButton.getParent().getName())).getSelected().contains("Controller")){
							setButton.setText(setButton.getName() + povCode);
							setButton = null;
						}
					}
					return false;
				}			
			});
		}
	}

	private void playerNumberSetUP() {
		//player number definitions
	    playerNumberDefLabel= new Label("Number of Player", skin);
		playerNumberDefLabel.setBounds(30, viewPort.getWorldHeight()-45, 100, 40);
		playerNumberLabel= new Label(Integer.toString(prefs.getInteger("PlayerNumber", 2)), skin);
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
				}
				for(int i = Integer.valueOf(playerNumberLabel.getText().toString())-1; i > 0 ; i--){
					playerTable[i].setVisible(true);
				}
				
				 updateMaps();
				return false;
			}
		});	
		playerNumberMinusButton.addListener(new ClickListener() {
			@Override
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				int temp=Integer.valueOf(playerNumberLabel.getText().toString())-1;
				if(temp>=2) {
					playerNumberLabel.setText(Integer.toString(temp));
				} 
				if(temp>1){
					for(int i = maxNumberPlayer-1; i > temp-1 ; i--){
						playerTable[i].setVisible(false);
					}
				}
				 updateMaps();
				return false;
			}
		});	
	}

	private void setupGameTable() {
		//set up win number stuff
		winNumberDefLabel= new Label("Number of Wins", skin);
		winNumberLabel= new Label(Integer.toString(prefs.getInteger("Wins", 5)), skin);
		winNumberPlusButton= new TextButton("+", skin);
		winNumberMinusButton= new TextButton("-", skin);
		winNumberPlusButton.addListener(new ClickListener() {
			@Override
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				int temp=Integer.valueOf(winNumberLabel.getText().toString())+1;
				if(temp<=100){
					winNumberLabel.setText(Integer.toString(temp));
				}
				return false;
			}
		});	
		winNumberMinusButton.addListener(new ClickListener() {
			@Override
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				int temp=Integer.valueOf(winNumberLabel.getText().toString())-1;
				if(temp>=1) {
					winNumberLabel.setText(Integer.toString(temp));
				}
				return false;
			}
		});	
		selectBox = new SelectBox<String>(skin);
		selectBox.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				prefs.putString("Mapstyle", selectBox.getSelected());
				
				map.load();
			}
		});
		
		//set up maptype selection
		selectBox.setItems("Normal", "Graveyard", "Future");
		selectBox.setSelected(prefs.getString("Mapstyle", "Normal"));
		mapStyleLable = new Label("Map-Style",skin);
		
		//suddendeath
		suddenDeathTimerDef = new Label("Suddendeath-Timer",skin);
		suddenDeathTimer= new TextField("90", skin);
		suddenDeathTimer.setAlignment(Align.center);
		suddenDeathTimer.setTextFieldFilter(new TextFieldFilter(){
			@Override
			public boolean acceptChar (TextField textField, char c) {
				return Character.isDigit(c);
			}
		});

		//create the gamesetup table 
		gameSetupTable=new Table();
		gameSetupTable.add(winNumberDefLabel).size(125, 25);
		gameSetupTable.add(winNumberLabel).padLeft(20).size(25, 25).center();
		gameSetupTable.add(winNumberMinusButton).size(35, 25);
		gameSetupTable.add(winNumberPlusButton).size(35, 25).padLeft(1).row();
		gameSetupTable.add(mapStyleLable).size(125, 25);
		gameSetupTable.add(selectBox).size(100, 25).padLeft(20).colspan(3).row();
		gameSetupTable.add(suddenDeathTimerDef).size(125, 25);
		gameSetupTable.add();
		gameSetupTable.add(suddenDeathTimer).size(75, 25).colspan(2);
		gameSetupTable.setBounds(GameClass.viewportWidth/2 - 100, 200, 200, 50);
	}

	private void setupPlayerTable() {
	    float y=viewPort.getWorldHeight()/2;
	    
		//create the player tabeles and define it
		playerUpButton= new TextButton[maxNumberPlayer];
		playerDownButton= new TextButton[maxNumberPlayer];
		playerRightButton= new TextButton[maxNumberPlayer];
		playerLeftButton= new TextButton[maxNumberPlayer];
		playerActionButton= new TextButton[maxNumberPlayer];
//		playerControllerButton= new CheckBox[maxNumberPlayer];
		playerControllerBoxArray = new Array<MySelectBox>();
		playerTeamBoxArray = new Array<SelectBox<String>>();
		playerImage = new Image[maxNumberPlayer];
		playerTable =new Table[maxNumberPlayer];
		playersLastControls=new String[maxNumberPlayer];
		int tableX=0;
		for(int i =0; i<maxNumberPlayer;i++){		
			
//		playerControllerButton[i] = new CheckBox("Controller", skin);
//		playerControllerButton[i].setChecked(false);
		controllerPlayer=new String[maxNumberPlayer];
		controllerPlayer[i]="";	
		MySelectBox tempS = new MySelectBox(skin);
		tempS.setItems(controlsNames);
		tempS.setName(Integer.toString(i));
		playerControllerBoxArray.add(tempS);
		SelectBox<String> tempR = new SelectBox<String>(skin);
		tempR.setItems(teamNames);
		tempR.setName(Integer.toString(i));
		playerTeamBoxArray.add(tempR);
		playerUpButton[i] = new TextButton("UP: ", skin);
		playerUpButton[i].setBounds(0, 0,100, 30);
		playerUpButton[i].align(Align.left);
		playerUpButton[i].setName("Up: ");
		
		playerDownButton[i] = new TextButton("Down: ", skin);
		playerDownButton[i].setBounds(0, 0, 100, 30);
		playerDownButton[i].setName("Down: ");
		
		playerRightButton[i] = new TextButton("Right: ", skin);
		playerRightButton[i].setBounds(0, 0,100, 30);
		playerRightButton[i].setName("Right: ");
		
		playerLeftButton[i] = new TextButton("Left: ", skin);
		playerLeftButton[i].setBounds(0, 0, 100, 30);
		playerLeftButton[i].setName("Left: ");
		
		playerActionButton[i] = new TextButton("Action: ", skin);
		playerActionButton[i].setBounds(0, 0, 100,30);
		playerActionButton[i].setName("Action: ");			
		String temp=prefs.getString("Player "  + Integer.toString(i)+" Char",characterNames[i]);
		TextureRegion texReg= new TextureRegion(new Texture("res/assets/bomber_"+temp+".png"));
		texReg.setRegion(0, 0,texReg.getRegionWidth()/10, texReg.getRegionHeight()/4);
		playerImage[i] = new Image(texReg);
		playerImage[i].setName(prefs.getString("Player "  + Integer.toString(i)+" Char",characterNames[i]));
		playerImage[i].setUserObject(i);
		playerImage[i].setScaling(Scaling.fit);
		playerImage[i].addListener(new MyClickListener(playerImage[i]) {
			@Override
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				int player= (Integer) image.getUserObject();
				player++;
				if(player>7){
					player=0;
				}
				prefs.putString("Player "  + image.getParent().getName()+" Char",characterNames[player]);
				String temp = "res/assets/bomber_"+characterNames[player]+".png";
				TextureRegion texReg= new TextureRegion(new Texture(temp));
				texReg.setRegion(0, 0,texReg.getRegionWidth()/10, texReg.getRegionHeight()/4);
				this.image.setDrawable(new TextureRegionDrawable(texReg));
				image.setName(characterNames[player]);
				
				image.setUserObject(player);
				return false;
			}
		});
		spriteBatch.setProjectionMatrix(viewMapPort.getCamera().combined);
		
		playersLastControls[i]="";
		playerControllerBoxArray.get(i).addListener(new MyChangeListener(playerControllerBoxArray.get(i)) {
			@Override
			public void changed(ChangeEvent event, Actor actor) {		
				mySelectBox.setOpened(!mySelectBox.isOpened());
				int number= Integer.valueOf(mySelectBox.getName());	
					if(mySelectBox.getSelected().contains("Controller")){
						if(playersLastControls[number].contains("Controller")){
							int[] keys = new int[5];
							keys [0]=prefs.getInteger("Player " + Integer.toString(number)+" UP", keys[0]);
							keys[1]=prefs.getInteger("Player " + Integer.toString(number)+" DOWN", keys[1]);
							keys[2]=prefs.getInteger("Player " + Integer.toString(number)+" RIGHT", keys[2]);
							keys[3]=prefs.getInteger("Player " + Integer.toString(number)+" LEFT", keys[3]);
							keys[4]=prefs.getInteger("Player " + Integer.toString(number)+" ACTION", keys[4]);
							playerUpButton[number].setText(playerUpButton[number].getName() + Integer.toString(keys[0]));
							playerDownButton[number].setText(playerDownButton[number].getName() + Integer.toString(keys[1]));
							playerRightButton[number].setText(playerRightButton[number].getName() + Integer.toString(keys[2]));
							playerLeftButton[number].setText(playerLeftButton[number].getName() + Integer.toString(keys[3]));
							playerActionButton[number].setText(playerActionButton[number].getName() + Integer.toString(keys[4]));
						}
						playerUpButton[number].setVisible(true);
						playerDownButton[number].setVisible(true);
						playerRightButton[number].setVisible(true);
						playerLeftButton[number].setVisible(true);
						playerActionButton[number].setVisible(true);
						if(controllers.size>0){
							controllerPlayer[number]=controllers.first().getName();
							if(controllers.size>=2){
								controllers.swap(0, controllers.size);
							}
						}
						playersLastControls[number]=mySelectBox.getSelected();
					} else if(mySelectBox.getSelected().contains("Tastatur")){
						
						if(playersLastControls[number].contains("Tastatur")){
							int[] keys = new int[5];
							keys [0]=prefs.getInteger("Player " + Integer.toString(number)+" UP", keys[0]);
							keys[1]=prefs.getInteger("Player " + Integer.toString(number)+" DOWN", keys[1]);
							keys[2]=prefs.getInteger("Player " + Integer.toString(number)+" RIGHT", keys[2]);
							keys[3]=prefs.getInteger("Player " + Integer.toString(number)+" LEFT", keys[3]);
							keys[4]=prefs.getInteger("Player " + Integer.toString(number)+" ACTION", keys[4]);
							if(keys[0]>-1){
								if(!Keys.toString(keys[0]).contains("Unknown")){
									playerUpButton[number].setText(playerUpButton[number].getName() + Keys.toString(keys[0]));
								} else {
									playerUpButton[number].setText(playerUpButton[number].getName() + "-");
								}
							}
							if(keys[1]>-1){
								if(!Keys.toString(keys[1]).contains("Unknown")){
									playerDownButton[number].setText(playerDownButton[number].getName() + Keys.toString(keys[1]));
								} else {
									playerDownButton[number].setText(playerDownButton[number].getName() + "-");
								}
							}
							if(keys[2]>-1){
								if(!Keys.toString(keys[2]).contains("Unknown")){
									playerRightButton[number].setText(playerRightButton[number].getName() + Keys.toString(keys[2]));
								} else {
									playerRightButton[number].setText(playerRightButton[number].getName() + "-");
								}
							}
							if(keys[3]>-1){
								if(!Keys.toString(keys[3]).contains("Unknown")){
									playerLeftButton[number].setText(playerLeftButton[number].getName() + Keys.toString(keys[3]));
								} else {
									playerLeftButton[number].setText(playerLeftButton[number].getName() + "-");
								}
							}
							if(keys[4]>-1){
								if(!Keys.toString(keys[4]).contains("Unknown")){
									playerActionButton[number].setText(playerActionButton[number].getName() + Keys.toString(keys[4]));
								} else {
									playerActionButton[number].setText(playerActionButton[number].getName() + "-");
								}
							}
						}
						playerUpButton[number].setVisible(true);
						playerDownButton[number].setVisible(true);
						playerRightButton[number].setVisible(true);
						playerLeftButton[number].setVisible(true);
						playerActionButton[number].setVisible(true);
						controllerPlayer[Integer.valueOf(0)]="";
						playersLastControls[number]=mySelectBox.getSelected();
					} else if(mySelectBox.getSelected().contains("App")){
						controllerPlayer[Integer.valueOf(mySelectBox.getParent().getName())]="";
						playerUpButton[number].setVisible(false);
						playerDownButton[number].setVisible(false);
						playerRightButton[number].setVisible(false);
						playerLeftButton[number].setVisible(false);
						playerActionButton[number].setVisible(false);
					}
			}
		});	
		playerControllerBoxArray.get(i).addListener(new MyClickListener(playerControllerBoxArray.get(i)) {
			@Override
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				int number= Integer.valueOf(mySelectBox.getParent().getName());
				if(mySelectBox.isOpened()){
					playerUpButton[number].setText(playerUpButton[number].getName() + "> <");
					playerDownButton[number].setText(playerDownButton[number].getName() + "> <");
					playerRightButton[number].setText(playerRightButton[number].getName() + "> <");
					playerLeftButton[number].setText(playerLeftButton[number].getName() + "> <");
					playerActionButton[number].setText(playerActionButton[number].getName() + "> <");
				}
				return false;
			}
		});
		if(controllers.size==0){
			for(int d =0 ; d < playerControllerBoxArray.size; d++){
				playerControllerBoxArray.get(d).setItems("Tastatur", "App");
			}
		}
		playerUpButton[i].addListener(new MyClickListener(playerUpButton[i]) {
			@Override
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				myButton.setText(myButton.getName()+"> <");
				setButton=myButton;
				return false;
			}
		});	
		
		playerDownButton[i].addListener(new MyClickListener(playerDownButton[i]) {
			@Override
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				myButton.setText(myButton.getName()+"> <");
				setButton=myButton;
				return false;
			}
		});	
		playerRightButton[i].addListener(new MyClickListener(playerRightButton[i]) {
			@Override
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				myButton.setText(myButton.getName()+"> <");
				setButton=myButton;
				return false;
			}
		});	
		playerLeftButton[i].addListener(new MyClickListener(playerLeftButton[i]) {
			@Override
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				myButton.setText(myButton.getName()+"> <");
				setButton=myButton;
				return false;
			}
		});	
		playerActionButton[i].addListener(new MyClickListener(playerActionButton[i]) {
			@Override
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				myButton.setText(myButton.getName()+"> <");
				setButton=myButton;
				return false;
			}
		});	
		
			playerTable[i]=new Table();
			playerTable[i].add(playerControllerBoxArray.get(i)).size(100, 30).row();
			playerTable[i].add(playerUpButton[i]).size(100, 30).row();
			playerTable[i].add(playerDownButton[i]).size(100, 30).row();
			playerTable[i].add(playerLeftButton[i]).size(100, 30).row();
			playerTable[i].add(playerRightButton[i]).size(100, 30).row();
			playerTable[i].add(playerActionButton[i]).size(100, 30).row();
			playerTable[i].add(playerTeamBoxArray.get(i)).size(100, 30).row();
			playerTable[i].add(playerImage[i]).size(40, 60).row();
			playerTable[i].setName(Integer.toString(i));
			
			if(i==4){
				y-=270;
				tableX=0;
			} else if(i==0){
				
			} else {
				tableX++;
			}
			playerTable[i].setPosition(80+tableX*100, y+140);
			stage.addActor(playerTable[i]);
			if(i>=Integer.valueOf(playerNumberLabel.getText().toString())){
				playerTable[i].setVisible(false);
			}
		
		}
	}

	public void getDirectoryHandles(FileHandle begin, Array<FileHandle> handles){

	    FileHandle[] newHandles = begin.list();
	    for (FileHandle f : newHandles){
	    	
	    	if(f.isDirectory()){	
	    		getDirectoryHandles(f, handles);
	    	} else {
	    		if(f.extension().contains("dat")){
	    			try {
	    				mapNames.add(f.pathWithoutExtension().substring(7));
					} catch (Exception e) {
						e.printStackTrace();
					}
	    		}
	        } 
	    }
		if(Gdx.input.isKeyPressed(Keys.ESCAPE) ||Gdx.input.isKeyPressed(Keys.ENTER)){
			stage.setKeyboardFocus(null);
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
			
			if(temp.charAt(0) == 'c'){
				i = Character.getNumericValue(temp.substring(7).charAt(0));
			}
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

	    hashMap.put(2, namesTwo);
	    hashMap.put(3, namesThree);
	    hashMap.put(4, namesFour);
	    hashMap.put(5, namesFive);
	    hashMap.put(6, namesSix);
	    hashMap.put(7, namesSeven);
	    hashMap.put(8, namesEight);
	}
	private void updateMaps(){
		//update for the player Number which maps can be played
		Array<String> names = new Array<String>();
		for(int i =Integer.valueOf(playerNumberLabel.getText().toString()); i <= maxNumberPlayer;i++){
			names.addAll(hashMap.get(i));
		}
		list.setItems(names);
		
		map=mapManager.loadMap(list.getSelected());
		map.load();
	}
	
	@Override
	public void render(float delta) {
		
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
		
	}

	@Override
	public void hide() {
		
	}

	@Override
	public void dispose() {
		spriteBatch.dispose();
		stage.dispose();
		map.dispose();
	}

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		
		if(Keys.ESCAPE== keycode){
			gameClass.openPauseMenu(this);
			return true;
		} else
		if(setButton!=null){
			if(playerControllerBoxArray.get(Integer.parseInt(setButton.getParent().getName())).getSelected().contains("Tastatur")){
				setButton.setText(setButton.getName() + Keys.toString(keycode));
				setButton = null;
			}
		} else
		  if(Keys.A==keycode){
			  list.getSelection().setAll(mapNames);
		  }
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		System.out.println(screenX + " " + screenY);
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

	public static String[] getTeamnames() {
		return teamNames;
	}
	
}
