package com.clone.bomber.entity;

import java.util.ArrayList;
import java.util.Collection;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.clone.bomber.map.Map;
import com.clone.bomber.map.Square;
import com.clone.bomber.screens.BomberGame;
import com.clone.bomber.util.Collideable;
import com.clone.bomber.util.MyRectangle;
import com.clone.bomber.util.MySound;

public class Player extends Entity implements InputProcessor, ControllerListener  {
	private static final long serialVersionUID = 4451695151261006580L;
	//CONTROLS
	//WHICH KEYS
	private int up;
	private int down;
	private int right;
	private int left;
	private int action;	
	//WHAT IS PRESSED
	private boolean upBoolean;
	private boolean downBoolean;
	private boolean rightBoolean;
	private boolean leftBoolean;
	private boolean actionBoolean;
	private int lastPressed=-1;	
	//ANIMATIONS
	
    //draw offset in y direction
	private float yOffset;
	//draw offset in x direction
	private float xOffset;
	//size of drawn modification
	private float sizeMode;	
	private float drawSize;
	private float originalSize; 
	
	private float size;  
	//TEXTURE AND ANIMATIONREGIONS
	private Texture walkSheet;  
	private transient TextureRegion[] walkUpFrames;
	private transient TextureRegion[] walkDownFrames;
	private transient TextureRegion[] walkLeftFrames;
	private transient TextureRegion[] walkRightFrames;
	private transient TextureRegion[] deadFrames;
	private Animation walkUpAnimation;
	private Animation walkDownAnimation;
	private Animation walkLeftAnimation;
	private Animation walkRightAnimation;
	//WHERE IN ANIMATION IT IS 
	private float stateTime;

	//VARIABLES
	private float speed = 120;
	private Vector2 move;
	
	//BOMB STUFF
	private int blastRadius=1;	
	private int bomblimit=1;
	private int bombcount=0;
	private ArrayList<Bomb> myBombs;
	private float normalZ = 1;
	private float speedBonus;

	
	//EFFECTS	
	private int bombThrows;
	private int pushPerks;
	private PowerUPEffects lastEffect;
	private boolean bombDiarrhea=false;
	private boolean bombThrow=false;
	private boolean pushPerk=false;
	private boolean controllerBoolean;
	//if the powerup should change the size of the drawn player
	private boolean growNegativPowerUP;
	private float NegativPowerUPTimer = 15;
	private float NegativPowerUPTime = 15;

	//score
	private int wins=0;
	private int teamNumber=-1;
	
	//bomb
	private Texture bombTexture;
	private ArrayList<Blastbeam> beams;
	
	//which side to block
	private boolean stopUp;
	private boolean stopDown;
	private boolean stopLeft;
	private boolean stopRight;
	
	//square -> easy fast fix for teleporting payer out of his own bomb
	// -> maybe there is better solution but wayne 
	private Square oldSquare;
	private boolean oldSquareHadBomb=false;
	

	//Pointers to import objects
	private Map map;
	private BomberGame bomberGame;
	private MySound mySound;
	
	//for deadSounds -> see otherPlayerContact
	private float soundTimer = 0;


	//if the player uses controller to control this character!
	private Controller myController;
	private boolean isFalling;
	private boolean spawnNegativ;
	
	/**
	 * CONSTRUCTOR 
	 * @param square - where it spawns
	 * @param controls - how it get controled (keys/Controls)
	 * @param map - the map object
	 * @param control - how it get controled CONTROLLER / KEYBOOARD / APP
	 * @param mySound - Sound object
	 * @param charakter - which texture to use
	 * @param bomberGame - the maingame screen object
	 * @param team - if on a team -> teamnumber else -1
	 */
	public Player(Square square, int[] controls,Map map,String control, MySound mySound, String charakter, BomberGame bomberGame, int team){
		this.mySound=mySound;
		lastEffect = PowerUPEffects.speed;//it does not matter what it is except no negativ
		this.bomberGame=bomberGame;
		this.bombTexture=new Texture("res/assets/bomb_" + charakter + ".png");
		this.size=square.getSize();
		if(control.contains("Keyboard")){
			controllerBoolean =false;
		} else if(control.contains("Controller")){
			controllerBoolean =true;
		} else if(control.contains("App")){
			controllerBoolean =false;
		}
		this.teamNumber=team;
		beams=new ArrayList<Blastbeam>();
		hitbox = new MyRectangle();
		myBombs=new ArrayList<Bomb>();
		hitbox.setSize(size-10);
		setPosition(square);
		this.up=controls[0];
		this.down=controls[1];
		this.right=controls[2];
		this.left=controls[3];
		this.action=controls[4];
		this.map=map;
		move=new Vector2();
		move.setZero();
		speedBonus = 20;
		
		//sizeMode
		//for all beside dull = 1.25f
		//for dull = 1		
		//for snake & tux & bsd
		//yOffset
		//for all
		//yOffset=size/4;
		//except spider 
		//yOffset=0
		xOffset=0;
		if(charakter.contains("spider")){
			sizeMode=1f;
			yOffset=0;
		} else{
			sizeMode=1.2f;
			yOffset=(size)/4-5;
		}
		if(charakter.contains("dull")){
			sizeMode=1f;
			//additional xOffset for dulls
			xOffset=-3f;
		} 
		this.size*=sizeMode;
	
		drawSize=size;
		this.originalSize=size;
		stopUp = false;
		stopDown = false;
		stopLeft = false;
		stopRight = false;
		
		walkSheet = new Texture(Gdx.files.internal("res/assets/bomber_" + charakter + ".png"));
		//Setup of animations from texture
        TextureRegion[][] tmp = TextureRegion.split(walkSheet, walkSheet.getWidth()/10, walkSheet.getHeight()/4);              
        walkDownFrames = new TextureRegion[9];
        walkLeftFrames = new TextureRegion[9];
        walkRightFrames = new TextureRegion[9];
        walkUpFrames = new TextureRegion[9];
        deadFrames = new TextureRegion[9];
        int deadIndex=0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 10; j++) {
            	if(j!=9 && i==0){
            		walkDownFrames[j] = tmp[i][j]; 
            	}
            	if(j!=9 && i==1){
            		walkLeftFrames[j] = tmp[i][j]; 
            	}
            	if(j!=9 && i==3){
            		walkRightFrames[j] = tmp[i][j]; 
            	}
            	if(j!=9 && i==2){
            		walkUpFrames[j] = tmp[i][j]; 
            	}
            	if(j==9){
            		deadFrames[deadIndex] = tmp[i][j];
            		deadIndex++;
            	}
            }
        }
        dead=false;
        walkUpAnimation = new Animation(0.1f, walkUpFrames);     
        walkDownAnimation = new Animation(0.1f, walkDownFrames); 
        walkLeftAnimation = new Animation(0.1f, walkLeftFrames); 
        walkRightAnimation = new Animation(0.1f, walkRightFrames); 
        stateTime=0;
	}	
	
	public void setPosition(Square square){
		this.position=new Vector3(square.getX()+((square.getSize())-(size-10))/2,square.getY()+((square.getSize())-(size-10))/2 ,normalZ);
		this.mySquare = square;
		hitbox.setPosition(position.x,position.y);
	}
	
	public TextureRegion walkUpAnimation(){
		return walkUpAnimation.getKeyFrame(stateTime, true); 
	}
	public TextureRegion walkDownAnimation(){
		return walkDownAnimation.getKeyFrame(stateTime, true); 
	}
	public TextureRegion walkRightAnimation(){
		return walkRightAnimation.getKeyFrame(stateTime, true); 
	}
	public TextureRegion walkLeftAnimation(){
		
		return walkLeftAnimation.getKeyFrame(stateTime, true); 
	}

	@Override
	public void render(SpriteBatch batch) {
		//BOMB HANDLING
		// render
		for (int i = 0; i < myBombs.size(); i++) {
			myBombs.get(i).render(batch);
		}	
		if(position.z<0.1){
			return;
		}
		float xoffset=(size-10)/4 + xOffset;
		if(!dead){
			if(move.y==1){
				batch.draw(this.walkUpAnimation(), position.x-xoffset, position.y-yOffset,drawSize/2, drawSize/2, drawSize, drawSize, position.z, position.z, 0);
			} else if(move.y==-1){
				batch.draw(this.walkDownAnimation(), position.x-xoffset, position.y-yOffset,drawSize/2, drawSize/2, drawSize, drawSize, position.z, position.z, 0);
			} else if(move.x==1){
				batch.draw(this.walkRightAnimation(), position.x-xoffset, position.y-yOffset,drawSize/2, drawSize/2, drawSize, drawSize, position.z, position.z, 0);
			} else if(move.x==-1){
				batch.draw(this.walkLeftAnimation(), position.x-xoffset, position.y-yOffset,drawSize/2, drawSize/2, drawSize, drawSize, position.z, position.z, 0);
			}
			if(move.len()==0) {
				if(this.lastPressed==0) {
					batch.draw(walkUpAnimation.getKeyFrame(0, true), position.x-xoffset, position.y-yOffset,drawSize/2, drawSize/2, drawSize, drawSize, position.z, position.z, 0);
				}else 
				if(this.lastPressed==1||this.lastPressed==-1){
					batch.draw(walkDownAnimation.getKeyFrame(0, true), position.x-xoffset, position.y-yOffset,drawSize/2, drawSize/2, drawSize, drawSize, position.z, position.z, 0);
				}else 
				if(this.lastPressed==2){
					
					batch.draw(walkRightAnimation.getKeyFrame(0, true), position.x-xoffset, position.y-yOffset,drawSize/2, drawSize/2, drawSize, drawSize, position.z, position.z, 0);
				}else 
				if(this.lastPressed==3){
					batch.draw(walkLeftAnimation.getKeyFrame(0, true), position.x-xoffset, position.y-yOffset,drawSize/2, drawSize/2, drawSize, drawSize, position.z, position.z, 0);
				}
			}
		} else{
				if(this.lastPressed==0){
					batch.draw(deadFrames[2], position.x-xoffset, position.y-yOffset,drawSize/2, drawSize/2, drawSize, drawSize, position.z, position.z, 0);
				}else 
				if(this.lastPressed==1||this.lastPressed==-1){
					batch.draw(deadFrames[0], position.x-xoffset, position.y-yOffset,drawSize/2, drawSize/2, drawSize, drawSize, position.z, position.z, 0);
				}else 
				if(this.lastPressed==2){
					batch.draw(deadFrames[3], position.x-xoffset, position.y-yOffset,drawSize/2, drawSize/2, drawSize, drawSize, position.z, position.z, 0);
				}else 
				if(this.lastPressed==3){
					batch.draw(deadFrames[1], position.x-xoffset, position.y-yOffset,drawSize/2, drawSize/2, drawSize, drawSize, position.z, position.z, 0);
				}
		}
		
	}
	/**
	 * dead = should be always be true! (maybe use this for teamplayer revival in the future?)
	 * 
	 * creates for each "pickuped" POWERUP a new PowerUP Object
	 * 
	 */
	public void setDead(boolean dead) {
		if(this.dead){
			return;
		}
		if(this.position.z==1){
		mySound.playDieSound();
		ArrayList<PowerUP> powers=new ArrayList<PowerUP>();
			if(PowerUPEffects.IsNegativEffect(lastEffect)){
				powers.add(new PowerUP(this.mySquare,map,lastEffect));
			}
			int speedUps = (int) ((speed-120 )/ speedBonus);
			for(int i = 0; i<speedUps; i++){
				powers.add(new PowerUP(this.mySquare,map,PowerUPEffects.speed));
			}
			for(int i = 0; i<blastRadius-1; i++){
				powers.add(new PowerUP(this.mySquare,map,PowerUPEffects.blastradius));
			}
			for(int i = 0; i<bomblimit-1; i++){
				powers.add(new PowerUP(this.mySquare,map,PowerUPEffects.bomb));
			}
			for(int i = 0; i<bombThrows; i++){
				powers.add(new PowerUP(this.mySquare,map,PowerUPEffects.throwable));
			}
			for(int i = 0; i<pushPerks; i++){
				powers.add(new PowerUP(this.mySquare,map,PowerUPEffects.push));
			}
			for(PowerUP p : powers){
				Square temp= map.getRandomSquare(false);
				p.flyToSquare(temp);
			}
			map.addPowerUps(powers);
		}
		
		this.dead = dead;
	}	


	@Override
	public void update(float delta) {
		//BOMB HANDLING
		// updating
		for (int i = 0; i < myBombs.size(); i++) {
			myBombs.get(i).update(delta);
			if(myBombs.get(i).isExploded()){
				myBombs.get(i).setExploded();
				bombcount--;
				bomberGame.setShake(true);
			}
			if(myBombs.get(i).isDead()){
				beams.removeAll(myBombs.get(i).getBeams());
				myBombs.get(i).getMySquare().setHasBomb(null);
					
				myBombs.remove(i);
			}
		}
		//effect for negative Effects
		if(PowerUPEffects.IsNegativEffect(lastEffect)){
			if(drawSize <= (originalSize + 10)  && growNegativPowerUP){
				this.drawSize+=10f*delta;
			} else
			if(drawSize > originalSize){
				growNegativPowerUP=false;
				this.drawSize-=10f*delta;
			} else {
				growNegativPowerUP=true;
			}
			NegativPowerUPTimer-=delta;
			if(NegativPowerUPTimer<=0){
				lastEffect=PowerUPEffects.speed;
			}
		} else {
			NegativPowerUPTimer = NegativPowerUPTime;
		}
		soundTimer-=delta;
		
		//if not dead or falling(or maybe later flying) dont let player control
		if(!dead && this.position.z==1){
			stateTime+=delta;
		//Check if clips with smth collideable
		//if player wants to move there -> block it
		if(stopUp){
			if(move.y>0){
				move.y=0;
			}
		} 
		if(stopDown) {
			if(move.y<0){
				move.y=0;
			}
		} 
		if(stopRight){
			if(move.x<0){
				move.x=0;
			}
		} 
		if(stopLeft){
			if(move.x>0){
				move.x=0;
			}
		}
		//update player position 
		this.position.x+=move.x*speed*delta;
		this.position.y+=move.y*speed*delta;
		//convert player input to movement vector 
		//only the last pressed key is important for the direction
		if(this.upBoolean && lastPressed==0){
			move.y=1;
			move.x=0;
		} else
		if(this.downBoolean && lastPressed==1 ){
			move.y=-1;
			move.x=0;
		} else
		if(this.rightBoolean && lastPressed==2){
			move.x=1;
			move.y=0;
		} else
		if(this.leftBoolean && lastPressed==3){
			move.y=0;
			move.x=-1;
		} else {
			//if nothing pressed and the player is on ice move him in last pressed dir
			//if it is not ice reset move vector
			if(!mySquare.isIce()){
				move.setZero();
			}
		}
		//if player pressed the action button/ has the "diarrhea"
		if(this.actionBoolean || bombDiarrhea){
			actionBoolean=false;
			//when player is on square with a bomb and can throw 
			// -> throw it to 5 squares in the direction he is looking
			// -> Don't make it 5 if its on the edge (restrict it to playable area)
			if(mySquare.isHasBomb()&& this.bombThrow){
				Square temp = mySquare;
				int tiles = 3;
				if( lastPressed==0){
					while(temp.getUp()!=null && tiles>=0){
						temp = temp.getUp();
						tiles--;
					}
					mySquare.getMyBomb().flyToSquare(temp);
				} else 
				if(lastPressed==2){
					while(temp.getRight()!=null && tiles>=0){
						temp = temp.getRight();
						tiles--;
					}
					mySquare.getMyBomb().flyToSquare(temp);
				} else 
				if(lastPressed==1 || lastPressed==-1){
					while(temp.getDown()!=null && tiles>=0){
						temp = temp.getDown();
						tiles--;
					}
					mySquare.getMyBomb().flyToSquare(temp);
				} else 
				if(lastPressed==3){
					while(temp.getLeft()!=null && tiles>=0){
						temp = temp.getLeft();
						tiles--;
					}
					mySquare.getMyBomb().flyToSquare(temp);
				}
			} else {
				//if there is no Bomb create one
				createBomb();
			}
		}
		//normalize vector for the case if its needed
		move.nor();
		
		//update the hitbox position
		//this.hitbox.setCenter(this.position.x+size/2, this.position.y+size/2);
		this.hitbox.setPosition(this.position.x, this.position.y);
		}
		//if the square where the player stands is empty
		//fall down and die
		if(this.mySquare.isEmpty()||isFalling){
			if(!isFalling){
				this.hitbox.setSize(0);
				mySound.playFallSound();
			}
			isFalling = true;
			if(this.position.z>0.1){
				this.position.z-=0.3f*this.position.z*9.81f*delta;
			} else {
			    this.dead=true;
			}
		}
		//reset clipping booleans -> already handle it
		stopUp = false;
		stopDown = false;
		stopLeft = false;
		stopRight = false;

		
	}
	private void createBomb() {	
		//when the player has more bombs "in the pocket"
		if(bombcount<bomblimit){
			//place a bomb if its Square has no bomb
			if(!mySquare.isHasBomb()){
					//create the bomb -> update square and increase placed Bomb Count
					Bomb b = new Bomb(mySquare,mySquare.getSize(),blastRadius, this, mySound,bombTexture);
					mySquare.setHasBomb(b);
					myBombs.add(b);
					bombcount++;
			}
		}
	}
	public void updateSquare(Square square) {
		//when the square is not null or if its the same square 
		if(!(square==null) && mySquare!=square){
			//update the mySquare and the last visited Square
			oldSquareHadBomb=mySquare.isHasBomb();
			oldSquare=mySquare;
			this.mySquare=square;
		}
		
	}
	/**
	 * checks and handles all collisions with other collideables
	 * @param collideable
	 */
	public void checkCollision(Collideable collideable) {
		//CHECK IF the userdata is square (is needed for somethings)
		if(collideable.getHitbox().getMyData() instanceof Square){
		//get the Rectangles and get the intersect Rectangle
		MyRectangle myRect = hitbox;
		MyRectangle otherRect = collideable.getHitbox();
		MyRectangle tempR = new MyRectangle();
		Square tempS = (Square) otherRect.getMyData();
		if(tempS.getNumber()!=mySquare.getNumber()){
		Intersector.intersectRectangles(otherRect, myRect, tempR);
		boolean ignoreCollision=false;
		//if the oldSquare had a bomb (the player placed it there)
		if(oldSquareHadBomb){
			//and the player collidies with a bomb
			if(collideable instanceof Bomb && ((Bomb)collideable).isFlying==false){
				//check if its the the bomb is in the old place 
				// -> stands half in it -> ignore the collision
				if(tempS==oldSquare){
					ignoreCollision=true;
				}
				//but let the player only walk in the new square
				if(oldSquare.getNumber()+1==mySquare.getNumber()){
					stopRight=true;
					stopUp=true;
					stopDown=true;
				} else 
				if(oldSquare.getNumber()-1==mySquare.getNumber()){
					stopLeft=true;
					stopUp=true;
					stopDown=true;
				} else 
				if(oldSquare.getNumber()-mySquare.getRowSize()==mySquare.getNumber()){
					stopLeft=true;
					stopRight=true;
					stopUp=true;
				} else 
				if(oldSquare.getNumber()+mySquare.getRowSize()==mySquare.getNumber()){
					stopLeft=true;
					stopRight=true;
					stopDown=true;
				}
			} 
		}
		//test for bombs if its flying 
		if(collideable.toString().contains("Bomb")){
			if(((Bomb)collideable).isFlying()){
				//if its flies ignore the collision
				return;
			}
		}
		//if not ignore the collision
		//get the direction it is colliding with and block that direction
		if(!ignoreCollision){
			if(otherRect.x < myRect.x && tempS.getNumber()==mySquare.getNumber()-1){ 
				if(otherRect.x + otherRect.width == tempR.getX()+tempR.width){
					if(move.x==-1){
						move.x=0;
						stopLeft=true;
					}
					this.position.x+=tempR.width;
				}
			} else		
			//right
			if(otherRect.x  > myRect.x && tempS.getNumber()==mySquare.getNumber()+1){
				if(otherRect.x == tempR.getX()){
					if(move.x==1){
						move.x=0;
						stopRight=true;
					}
					this.position.x-=tempR.width;
				}
			} 
			//up
			if(otherRect.y > myRect.y && tempS.getNumber()-mySquare.getRowSize()==mySquare.getNumber()){
				if(otherRect.y  == tempR.getY()){
					if(move.y==1){
						move.y=0;
						stopUp=true;
					}
					this.position.y-=tempR.height;
				}
			} else
		//down
			if(otherRect.y  < myRect.y  && tempS.getNumber()+mySquare.getRowSize()==mySquare.getNumber()){ 
				if(otherRect.y+otherRect.height == tempR.getY()+tempR.height){
					if(move.y==-1){
						move.y=0;
						stopDown=true;

					}
					this.position.y+=tempR.height;
				}
			}
		}
		}
		}
		
	}

	public Square getMySquare() {
		return mySquare;
	}
	public void setMySquare(Square mySquare) {
		if(!dead){
			this.mySquare = mySquare;
		}
	}
	public boolean isPushPerk() {
		return pushPerk;
	}
	public void setPushPerk(boolean pushPerk) {
		this.pushPerk = pushPerk;
	}

	@Override
	public boolean keyDown(int keycode) {
		//handle the input if its not controller controlled
		if(!controllerBoolean && up!=-1){
			if(keycode==up){
				upBoolean=true;
				lastPressed=0;
				return true;
			} else if(keycode==down){
				downBoolean=true;
				lastPressed=1;
				return true;
			} else if(keycode==right){
				rightBoolean=true;
				lastPressed=2;
				return true;
			} else if(keycode==left){
				leftBoolean=true;
				lastPressed=3;
				return true;
			} else if(keycode==action){
				actionBoolean=true;
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		//handle the input if its not controller controlled
		if(!controllerBoolean && up!=-1){
			if(keycode==up){
				upBoolean=false;
				updateLastKey();
				return true;
			} else if(keycode==down){
				downBoolean=false;
				updateLastKey();
				return true;
			} else if(keycode==right){
				rightBoolean=false;
				updateLastKey();
				return true;
			} else if(keycode==left){
				leftBoolean=false;
				updateLastKey();
				return true;
			} else if(keycode==action){
				actionBoolean=false;
				return true;
			}
		}
		return false;
	}

	private void updateLastKey() {
		if(upBoolean){
			lastPressed=0;
		} else if(downBoolean){
			lastPressed=1;
		} else if(rightBoolean){
			lastPressed=2;
		} else if(leftBoolean){
			lastPressed=3;
		}
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
	/**
	 * adds a specific PowerUP to the player and resolves its effect
	 * when NEGATIVE effect on player removes it!
	 * @param myEffect
	 */
	public void addPowerUP(PowerUPEffects myEffect) {
		//add a PowerUP
		//play the sound
		mySound.playPowerUPSound(myEffect);
		if(PowerUPEffects.IsNegativEffect(lastEffect)){	
			ArrayList<PowerUP> powers=new ArrayList<PowerUP>();
			if(lastEffect==PowerUPEffects.joint){
				invertControls();
			} else
			if(lastEffect==PowerUPEffects.diarrhea){
				bombDiarrhea=false;
			} else
			if(lastEffect==PowerUPEffects.superspeed){
				this.speed-=1000;
			}
			if(spawnNegativ){
				powers.add(new PowerUP(this.mySquare,map,lastEffect));
				growNegativPowerUP=false;
				drawSize=originalSize;
				Square temp= map.getRandomSquare(false);
				powers.get(0).flyToSquare(temp);
				map.addPowerUps(powers);
			}
		}
		lastEffect=myEffect;
		switch(myEffect){
		case blastradius:
			this.blastRadius+=1;
			break;
		case bomb:
			this.bomblimit+=1;
			break;
		case diarrhea:
			spawnNegativ = true;
			bombDiarrhea=true;
			lastEffect=myEffect;
			growNegativPowerUP=true;
			break;
		case joint:
			spawnNegativ = true;
			invertControls();
			lastEffect=myEffect;
			growNegativPowerUP=true;
			break;
		case push:
			pushPerks++;
			this.pushPerk=true;
			break;
		case speed:
			this.speed+=speedBonus;
			break;
		case superspeed:
			spawnNegativ = true;
			this.speed+=1000;
			lastEffect=myEffect;
			growNegativPowerUP=true;
			break;
		case throwable:
			bombThrows++;
			this.bombThrow=true;
			break;
		default:
			break;
		}
	}
	/**
	 * inverts the controls to the opposite
	 */
	public void invertControls(){
		int temp=up;
		up=down;
		down=temp;
		temp=right;
		right=left;
		left=temp;
	}

	public int getWins() {
		return wins;
	}

	public void addWin() {
		this.wins++;
	}

	public int getBombUPs() {
		return bomblimit-1;
	}

	public void setBomblimit(int bomblimit) {
		this.bomblimit = bomblimit;
	}

	public int getBlastRadiusUPs() {
		return blastRadius-1;
	}

	public int getBombThrows() {
		return bombThrows;
	}

	public int getSpeed() {
		return (int) ((speed-120 )/ speedBonus);
	}

	public int getPushPerks() {
		return pushPerks;
	}

	public int getTeamNumber() {
		return teamNumber;
	}

	public void setTeamNumber(int teamNumber) {
		this.teamNumber = teamNumber;
	}
	/**
	 * Returns all Collideable that is controled by this Class  (ex. bombs,blastbeams)
	 * @return as Collection ArrayList
	 */
	public Collection<? extends Collideable> getColliables() {
		//returns the player sided controlled bombs, there beams and its own hitbox
		ArrayList<Collideable> collideables=new ArrayList<Collideable>();
		collideables.add(this);
		collideables.addAll(myBombs);
		collideables.addAll(beams);
		return collideables;
	}

	public Square getSquare(MyRectangle hitbox) {
		return map.getSquare(hitbox);
	}

	public void addBeamHitboxes(ArrayList<Blastbeam> beams) {
		this.beams=beams;
	}
	/**
	 * adds negative effect from a other player
	 * or plays a sound if this player is dead and counter is 0 
	 * @param otherPlayerNegativeEffect
	 */
	public void playerContact(PowerUPEffects otherPlayerNegativeEffect) {
		//if it is dead play sound
		if(soundTimer<=0 && dead){
			mySound.playCorpse();
			soundTimer=1f;
			return;
		}  
		//when the other has a negativ and this one dont
		//then do it	
		if(otherPlayerNegativeEffect.IsNegativ() && lastEffect.IsNegativ()==false){
			if(otherPlayerNegativeEffect==lastEffect){
				return;
			}
			spawnNegativ = false;
			this.addPowerUP(otherPlayerNegativeEffect);
		}
	}

	public TextureRegion[] getDeadFrames() {
		return deadFrames;
	}
	public TextureRegion getDownFrame() {
		return walkDownFrames[0];
	}
	/**
	 * reset player informations that is only on one map important
	 * ex. bombs, Square, etc
	 * 
	 * @param square
	 * @param map
	 */
	public void reset(Square square, Map map) {
		this.mySquare = square;
		this.size=square.getSize();
		beams=new ArrayList<Blastbeam>();
		hitbox = new MyRectangle();
		myBombs=new ArrayList<Bomb>();
		setPosition(square);
		hitbox.setSize(size-10);
		lastEffect = PowerUPEffects.speed;
		hitbox.setCenter(position.x,position.y);
		move=new Vector2();
		move.setZero();
		stopUp = false;
		stopDown = false;
		stopLeft = false;
		stopRight = false;
        dead=false;
        isFalling = false;
        stateTime=0;
	}

	public PowerUPEffects getLastEffect() {
		return lastEffect;
	}

	public void setNegativeEffect(PowerUPEffects negativeEffect) {
		this.lastEffect = negativeEffect;
	}

	public void setController(Controller first) {
		myController=first;
		myController.addListener(this);
	}
	//DOES NOT WORK ON PC
	@Override
	public void connected(Controller controller) {
	}

	@Override
	public void disconnected(Controller controller) {
	}
	//WORKS ON PC
	@Override
	public boolean buttonDown(Controller controller, int buttonCode) {
		if(controllerBoolean && up!=-1){
			buttonCode+=1;
			if(buttonCode==up){
				upBoolean=true;
				lastPressed=0;
				return true;
			} else if(buttonCode==down){
				downBoolean=true;
				lastPressed=1;
				return true;
			} else if(buttonCode==right){
				rightBoolean=true;
				lastPressed=2;
				return true;
			} else if(buttonCode==left){
				leftBoolean=true;
				lastPressed=3;
				return true;
			} else if(buttonCode==action){
				
				actionBoolean=true;
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean buttonUp(Controller controller, int buttonCode) {
		if(controllerBoolean && up!=-1){
			buttonCode+=1;
			if(buttonCode==up){
				upBoolean=false;
				updateLastKey();
				return true;
			} else if(buttonCode==down){
				downBoolean=false;
				updateLastKey();
				return true;
			} else if(buttonCode==right){
				rightBoolean=false;
				updateLastKey();
				return true;
			} else if(buttonCode==left){
				leftBoolean=false;
				updateLastKey();
				return true;
			} else if(buttonCode==action){
				actionBoolean=false;
				return true;
			}	
		}
		return false;
	}

	@Override
	public boolean axisMoved(Controller controller, int axisCode, float value) {
		return false;
	}

	@Override
	public boolean povMoved(Controller controller, int povCode,
			PovDirection value) {
		if(controllerBoolean && up!=-1){
			if(0==up && PovDirection.north.compareTo(value)==0){
				upBoolean=true;
				downBoolean=false;
				leftBoolean=false;
				rightBoolean=false;
				actionBoolean=false;
				updateLastKey();
				return true;
			} else if(0==down && PovDirection.south.compareTo(value)==0){
				downBoolean=true;
				upBoolean=false;
				leftBoolean=false;
				rightBoolean=false;
				actionBoolean=false;
				updateLastKey();
				return true;
			} else if(0==right && PovDirection.east.compareTo(value)==0){
				rightBoolean=true;
				downBoolean=false;
				leftBoolean=false;
				upBoolean=false;
				actionBoolean=false;
				updateLastKey();
				return true;
			} else if(0==left && PovDirection.west.compareTo(value)==0){
				leftBoolean=true;
				downBoolean=false;
				upBoolean=false;
				rightBoolean=false;
				actionBoolean=false;
				updateLastKey();
				return true;
			} else if(0==action && PovDirection.center.compareTo(value)==0){
				actionBoolean=true;
				leftBoolean=false;
				downBoolean=false;
				upBoolean=false;
				rightBoolean=false;
				return true;
			} else{
				actionBoolean=false;
				leftBoolean=false;
				downBoolean=false;
				upBoolean=false;
				rightBoolean=false;
			}
		}
		return false;
	}

	@Override
	public boolean xSliderMoved(Controller controller, int sliderCode,
			boolean value) {
		return false;
	}

	@Override
	public boolean ySliderMoved(Controller controller, int sliderCode,
			boolean value) {
		return false;
	}

	@Override
	public boolean accelerometerMoved(Controller controller,
			int accelerometerCode, Vector3 value) {
		return false;
	}
	/**
	 * 
	 * @param direction up=0 right=1 down=2 left=3
	 */
	public void updateMovementApp(int direction,boolean action){			
				if(direction==0){
					upBoolean=true;
					downBoolean=false;
					rightBoolean=false;
					leftBoolean=false;
					updateLastKey();
				} else if(direction==2){
					downBoolean=true;
					upBoolean=false;
					rightBoolean=false;
					leftBoolean=false;
					updateLastKey();
				} else if(direction==1){
					rightBoolean=true;
					downBoolean=false;
					upBoolean=false;
					leftBoolean=false;
					updateLastKey();
				} else if(direction==3){
					leftBoolean=true;
					downBoolean=false;
					rightBoolean=false;
					upBoolean=false;
					updateLastKey();
				} else {
					leftBoolean=false;
					downBoolean=false;
					rightBoolean=false;
					upBoolean=false;
				}
				actionBoolean=action;	
		}
	/**
	 * disposes all textures handled by this class
	 */
	public void dispose(){
        int deadIndex=0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 10; j++) {
            	if(j!=9 && i==0){
            		walkDownFrames[j].getTexture().dispose();
            	}
            	if(j!=9 && i==1){
            		walkLeftFrames[j].getTexture().dispose();
            	}
            	if(j!=9 && i==3){
            		walkRightFrames[j].getTexture().dispose();
            	}
            	if(j!=9 && i==2){
            		walkUpFrames[j].getTexture().dispose();
            	}
            	if(j==9){
            		deadFrames[deadIndex].getTexture().dispose();
            		deadIndex++;
            	}
            }
        }
        this.bombTexture.dispose();
	}
}
