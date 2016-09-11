package com.clonebomber.controller;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldFilter;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class Main implements ApplicationListener, InputProcessor  {
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private Skin skin;
    private Stage stage;
    private MyButton upButton;
    private MyButton downButton;
    private MyButton leftButton;
    private MyButton rightButton;
    private ImageButton actionButton;
    private TextField ipAddress;
	private FitViewport viewPort;
	private MyClient myC;

    // Pick a resolution that is 16:9 but not unreadibly small
    public final static float VIRTUAL_SCREEN_HEIGHT = 540;
    public final static float VIRTUAL_SCREEN_WIDTH = 960;
    
    private int player=-1;
    private int direction;
	private boolean action;
	private TextButton connectButton;
	private Label playerLabel;
	private Label connectedLabel;
	private Dialog serverInfoDialog;
	private boolean dialogShown;
	private Preferences prefs;
  
	public Main(){
    	create();
    }  
	public void setPlayer(int player) {
		playerLabel.setText("Player: " + Integer.toString(player));
		this.player = player;
	}
    @Override
    public void create() {        
    	prefs = Gdx.app.getPreferences("clonebomberController");
        myC = new MyClient(this);
        camera = new OrthographicCamera(VIRTUAL_SCREEN_HEIGHT,VIRTUAL_SCREEN_WIDTH);
        batch = new SpriteBatch();
        skin = new Skin(Gdx.files.internal("data/uiskin.json"));
        stage = new Stage();
        connectButton = new TextButton("Connect to Server", skin);
        playerLabel=new Label("Player: -",skin);
        connectedLabel=new Label("Connected: " + false,skin);
        connectedLabel.setBounds(VIRTUAL_SCREEN_WIDTH/2-100, VIRTUAL_SCREEN_HEIGHT-50, 150, 50);
        playerLabel.setBounds(VIRTUAL_SCREEN_WIDTH/2+100, VIRTUAL_SCREEN_HEIGHT-50, 150, 50);
        
        if (Gdx.app.getType() == ApplicationType.Android) {       
        	
        	Texture drawOne =new Texture(Gdx.files.classpath("assets/arrowUP.png"));
        	Texture drawTWO =new Texture(Gdx.files.classpath("assets/arrowUP_click.png"));
        	Image img =new Image(new TextureRegion(new Texture(Gdx.files.classpath("assets/background.png"))));
            upButton = new MyButton(drawOne,drawTWO);
            drawOne =new Texture(Gdx.files.classpath("assets/arrowDOWN.png"));
            drawTWO =new Texture(Gdx.files.classpath("assets/arrowDOWN_click.png"));
            downButton= new MyButton(drawOne,drawTWO);
            drawOne = new Texture(Gdx.files.classpath("assets/arrowLEFT.png"));
            drawTWO = new Texture(Gdx.files.classpath("assets/arrowLEFT_click.png")) ;
            leftButton= new MyButton(drawOne,drawTWO);
            drawOne = new Texture(Gdx.files.classpath("assets/arrowRIGHT.png" ));
            drawTWO = new Texture(Gdx.files.classpath("assets/arrowRIGHT_click.png" ));
            rightButton= new MyButton(drawOne,drawTWO);
            TextureRegionDrawable drawableOne = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.classpath("assets/action.png"))));
            TextureRegionDrawable drawableTWO = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.classpath("assets/action_click.png"))));
            actionButton= new ImageButton(drawableOne,drawableTWO);
            stage.addActor(img);
        } else {
                Image img =new Image(new TextureRegion(new Texture(Gdx.files.classpath("background.png")))) ;
                Texture drawOne = new Texture(Gdx.files.classpath("arrowUP.png")) ;
                Texture drawTWO = new Texture(Gdx.files.classpath("arrowUP_click.png"));
                upButton = new MyButton(drawOne,drawTWO);
                drawOne = new Texture(Gdx.files.classpath("arrowDOWN.png" ));
                drawTWO = new Texture(Gdx.files.classpath("arrowDOWN_click.png" ));
                downButton= new MyButton(drawOne,drawTWO);
                drawOne = new Texture(Gdx.files.classpath("arrowLEFT.png" ));
                drawTWO = new Texture(Gdx.files.classpath("arrowLEFT_click.png" ));
                leftButton= new MyButton(drawOne,drawTWO);
                drawOne = new Texture(Gdx.files.classpath("arrowRIGHT.png" ));
                drawTWO = new Texture(Gdx.files.classpath("arrowRIGHT_click.png" ));
                rightButton= new MyButton(drawOne,drawTWO);
                TextureRegionDrawable drawableOne = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.classpath("action.png"))));
                TextureRegionDrawable drawableTWO = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.classpath("action_click.png"))));
                actionButton= new ImageButton(drawableOne,drawableTWO);
                stage.addActor(img);
        }
        rightButton.setBounds(VIRTUAL_SCREEN_WIDTH/2+200, VIRTUAL_SCREEN_HEIGHT/2-100, 100, 100);
        leftButton.setBounds(VIRTUAL_SCREEN_WIDTH/2-00, VIRTUAL_SCREEN_HEIGHT/2-100, 100, 100);
        downButton.setBounds(VIRTUAL_SCREEN_WIDTH/2+100, VIRTUAL_SCREEN_HEIGHT/2-200, 100, 100);
        upButton.setBounds(VIRTUAL_SCREEN_WIDTH/2+100, VIRTUAL_SCREEN_HEIGHT/2-00, 100, 100);
        actionButton.setBounds(VIRTUAL_SCREEN_WIDTH/2-300, VIRTUAL_SCREEN_HEIGHT/2-150, 200, 200);
        ipAddress = new TextField(prefs.getString("IP", "192.168.178.1"), skin);
        ipAddress.setBounds(VIRTUAL_SCREEN_WIDTH/2-400, VIRTUAL_SCREEN_HEIGHT-50, 150, 50);
        connectButton.setBounds(VIRTUAL_SCREEN_WIDTH/2-250, VIRTUAL_SCREEN_HEIGHT-50, 150, 50);
        ipAddress.setTextFieldFilter(new TextFieldFilter(){
			@Override
			public boolean acceptChar (TextField textField, char c) {
				return Character.isDigit(c) || c=='.';
			}
		});
        direction=-1;
        // Add scene to stage
        serverInfoDialog=new Dialog("", skin);
        stage.addActor(connectedLabel);
        stage.addActor(playerLabel);
        stage.addActor(connectButton);
        stage.addActor(ipAddress);
//        stage.addActor(upButton);
//        stage.addActor(downButton);
//        stage.addActor(leftButton);
//        stage.addActor(rightButton);
        stage.addActor(actionButton);
        viewPort=new FitViewport(VIRTUAL_SCREEN_WIDTH, VIRTUAL_SCREEN_HEIGHT);
        viewPort.setCamera(camera);
        viewPort.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage.setViewport(viewPort);
        stage.getCamera().position.set(VIRTUAL_SCREEN_WIDTH/2,VIRTUAL_SCREEN_HEIGHT/2,0);
        InputMultiplexer multi = new InputMultiplexer();
        multi.addProcessor(stage);
        multi.addProcessor(this);
        Gdx.input.setInputProcessor(multi);
        // Wire up a click listener to our button
        
        upButton.addListener(0);
        downButton.addListener(2);
        rightButton.addListener(1);
        leftButton.addListener(3);
        
        actionButton.addListener(new ClickListener(){
            @Override 
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button){
            	action=true;
            	return true;
            }
            @Override 
        	public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
            	action=false;
        	}
        });
        connectButton.addListener(new ClickListener(){
            @Override 
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button){
            	String s = ipAddress.getText();
            	System.out.println(s);
            	int pointCount=0;
            	int numberCount=0;
            	for(char c : s.toCharArray()){
            		
            		if(c=='.' && (numberCount>=1&&numberCount<=3)){
            			
            			pointCount++;
            			numberCount=0;
            			
            		} else {
            			
            			numberCount++;
            			
            		}
            	}
            	if(pointCount==3){
            		prefs.putString("IP",ipAddress.getText());
            		prefs.flush();
            		myC.connect(ipAddress.getText());	
            	}
            	
            	return true;
            }
            @Override 
        	public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
            	
        	}
        });
    }

    @Override
    public void dispose() {
        batch.dispose();
    }

    @Override
    public void render() {    
        myC.sendMessage(player, direction , action);
        stage.draw();
        stage.act();
        batch.begin();
        batch.setProjectionMatrix(viewPort.getCamera().combined);

        if(!dialogShown){
        	upButton.render(batch);
        	downButton.render(batch);
        	leftButton.render(batch);
        	rightButton.render(batch);
        } 
        
	    batch.end();
    }

    @Override
    public void resize(int width, int height) {
    	System.out.println("resize");
		viewPort.update(width, height);
		stage.getViewport().update(width, height,true); 
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }
	public void setConnected(boolean b) {
		connectButton.setVisible(!b);
		System.out.println("!" + b);
		connectedLabel.setText("Connected: " + b);
	}
	public void showFailedConnect() {
		serverInfoDialog=new Dialog("No server found!",skin);
		serverInfoDialog.add("Please check the IP-Address or restart the maingame!");
		serverInfoDialog.addListener(new ClickListener() {
			@Override
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				serverInfoDialog.hide();
				dialogShown=false;
				return false;
			}
		});	
		dialogShown=true;
		serverInfoDialog.show(stage);
	}
	public void showDisconnected() {
		serverInfoDialog=new Dialog("Disconnected!",skin);
		serverInfoDialog.add("If not intentionally please connect again (can cause Problems)!");
		serverInfoDialog.addListener(new ClickListener() {
			@Override
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				serverInfoDialog.hide();
				dialogShown=false;
				return false;
			}
		});	
		dialogShown=true;
		serverInfoDialog.show(stage);
		
	}
	@Override
	public boolean keyDown(int keycode) {
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
		Vector2 pos = new Vector2(screenX,screenY);
		pos.x=camera.unproject(new Vector3(pos,0)).x;
		pos.y=camera.unproject(new Vector3(pos,0)).y;
	     upButton.checkForTouch(pos);
	     downButton.checkForTouch(pos);
	     leftButton.checkForTouch(pos);
	     rightButton.checkForTouch(pos);
	     if(upButton.isTouched()){
	    	 direction=upButton.getDirection();
	     } else if(direction==upButton.getDirection()){
	    	 direction=-1;
	     }
	     if(downButton.isTouched()){
	    	 direction=downButton.getDirection();
	     } else if(direction==downButton.getDirection()){
	    	 direction=-1;
	     }
	     if(leftButton.isTouched()){
	    	 direction=leftButton.getDirection();
	     } else if(direction==leftButton.getDirection()){
	    	 direction=-1;
	     }
	     if(rightButton.isTouched()){
	    	 direction=rightButton.getDirection();
	     } else if(direction==rightButton.getDirection()){
	    	 direction=-1;
	     }
		return false;
	}
	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		 direction=-1;
	     upButton.unPress();
	     downButton.unPress();
	     leftButton.unPress();
	     rightButton.unPress();

		return false;
	}
	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		Vector2 pos = new Vector2(screenX,screenY);
		pos.x=camera.unproject(new Vector3(pos,0)).x;
		pos.y=camera.unproject(new Vector3(pos,0)).y;
	     upButton.checkForTouch(pos);
	     downButton.checkForTouch(pos);
	     leftButton.checkForTouch(pos);
	     rightButton.checkForTouch(pos);
	     if(upButton.isTouched()){
	    	 direction=upButton.getDirection();
	     } else if(direction==upButton.getDirection()){
	    	 direction=-1;
	     }
	     if(downButton.isTouched()){
	    	 direction=downButton.getDirection();
	     } else if(direction==downButton.getDirection()){
	    	 direction=-1;
	     }
	     if(leftButton.isTouched()){
	    	 direction=leftButton.getDirection();
	     } else if(direction==leftButton.getDirection()){
	    	 direction=-1;
	     }
	     if(rightButton.isTouched()){
	    	 direction=rightButton.getDirection();
	     } else if(direction==rightButton.getDirection()){
	    	 direction=-1;
	     }
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
