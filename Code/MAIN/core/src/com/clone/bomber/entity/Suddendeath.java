package com.clone.bomber.entity;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.clone.bomber.GameClass;
import com.clone.bomber.map.Map;
import com.clone.bomber.map.Square;
import com.clone.bomber.util.MySound;

public class Suddendeath extends Entity{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2L;
	private Vector3 position;
	private int size;
	private int speed = 250;
	private Vector2 move;
	private Map map;
	private transient TextureRegion[] frames;
	private float timer;
	private MySound mySound;
	private boolean playedSoundOne=false;
	private boolean playedSoundTwo=false;

	private Square endSquare;
	private int clockInt=1;
	private float startTimer;
	
	
	private static final int FRAME_COLS = 3;   
	public Suddendeath(Map map, MySound mySound){
		this.mySound=mySound;
		this.mySquare=map.getSquareNumber(map.getMapSize()*map.getMapSize()-map.getMapSize()/2);
		move=new Vector2();
		flyToSquare(map.getRandomSquare(true));
		this.map=map;
		Preferences prefs = Gdx.app.getPreferences("clonebomber");
		this.timer=prefs.getInteger("Suddendeath-Timer",90);
		startTimer=timer;
		size=mySquare.getSize();
		position=new Vector3(mySquare.getX(),mySquare.getY(),1);
		Texture texture=map.getTexManager().getSuddendeath();
        TextureRegion[][] tmp = TextureRegion.split(texture, texture.getWidth()/FRAME_COLS, texture.getHeight());              
        frames = new TextureRegion[FRAME_COLS];

        
        int index = 0;
        for (int j = 0; j < FRAME_COLS; j++) {
        	frames[index++] = tmp[0][j]; 
        }

     

		
	}
	
	public void render(SpriteBatch batch, BitmapFont font) {
		if(clockInt>1){
			batch.draw(frames[0], position.x, position.y,size/2, size/2, size, size, position.z, position.z, 0);
		}
		batch.draw(frames[clockInt], GameClass.viewportWidth / 2, GameClass.viewportHeight-size,size/2, size/2, size, size, 1, 1, 0);

		batch.end();
		batch.begin();
		if(Math.round(timer)>=0){
			font.draw(batch, Integer.toString( Math.round(timer)),GameClass.viewportWidth / 2 + 50, GameClass.viewportHeight-size/2);
		}
		batch.end();
		batch.begin();
		
	}

	@Override
	public void update(float delta) {
		if(timer > 0){
			timer-=delta;
			if(timer<=(startTimer/3) &&!this.playedSoundOne){
				mySound.playSuddenDeathSound(2);
				playedSoundOne=true;
			}
		} else{	
			if(!this.playedSoundTwo){
				clockInt++;
				mySound.playSuddenDeathSound(3);
				playedSoundTwo=true;
			}
		
			x=-Math.abs((mySquare.getPos().dst(position.x,position.y)))/mySquare.getSize();
			double sqrt = Math.pow(x, 2);
			float tempX2 = (float) (a * sqrt);
			float flySpeed = (tempX2+b*x+(c));
			this.position.z=flySpeed;
			int tolerance=4;
			if(!(endSquare.getX()-tolerance<position.x && endSquare.getX()+tolerance>position.x&&  endSquare.getY()-tolerance<position.y&& endSquare.getY()+tolerance>position.y)){                      	
				this.position.x+=move.x*speed*delta;
				this.position.y+=move.y*speed*delta;
				
			} else {
				this.position.x= endSquare.getX();
				this.position.y= endSquare.getY();
				this.position.z=1;
				mySquare=endSquare;
				mySound.playBombSound(1);
				this.mySquare.getHit();
				Square temp = map.getRandomSquare(true);
				position.z=1;
				flyToSquare(temp);
				this.mySquare=temp;
			}
		}

	}
	public void flyToSquare(Square temp) {
		x=-(Math.abs((mySquare.getPos().dst(temp.getPos())))/mySquare.getSize());
		
		this.endSquare=temp;
		float x1 = 0;
		float x2 = x/2;
		float x3 = x;
		float y1 = 1;
		float y2 = 3f;
		float y3 = 1;
		a = (x1 * (y2-y3)+x2 * (y3-y1)+x3*(y1-y2))/((x1-x2)*(x1-x3)*(x3-x2));
		b = ((x1*x1)*(y2-y3)+(x2*x2)*(y3-y1)+(x3*x3)*(y1-y2))/((x1-x2)*(x1-x3)*(x2-x3));
		c = ((x1*x1)*(x2*y3-x3*y2)+x1*((x3*x3)*y2-(x2*x2)*y3)+x2*x3*y1*(x2-x3))/((x1-x2)*(x1-x3)*(x2-x3));
		if(move.x>temp.getX()){
			move.x=this.mySquare.getX()-temp.getX();
		} else {
			move.x=temp.getX()-this.mySquare.getX();
		}
		if(move.x>temp.getX()){
			move.y=this.mySquare.getY()-temp.getY();
		} else {
			move.y=temp.getY()-this.mySquare.getY();
		}
		move.nor();
		isFlying=true;
	}
	/**
	 * unused
	 */
	@Override
	public void render(SpriteBatch batch) {
	}
	

}
