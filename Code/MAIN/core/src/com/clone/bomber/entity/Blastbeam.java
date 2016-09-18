package com.clone.bomber.entity;

import java.util.ArrayList;
import java.util.Collection;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.clone.bomber.map.Square;
import com.clone.bomber.util.MyRectangle;

public class Blastbeam extends Entity{
	private static final long serialVersionUID = 4149718051697878144L;
	
	private float lifeTimer;
	//ANIMATIONS        
    private static final int FRAME_COLS = 3;  
    private Texture walkSheet;              
    private TextureRegion[] walkFrames;
	private float stateTime;
	private int size;

	private Square mySquare;
	private int rotation=0;
	private Blastbeam next;
	private int securityDistanceX=0;
	private int securityDistanceY=0;

	private boolean notRender;
	public Blastbeam(Square square,int direction, int size, int blastRadius, float beamTimer) {		
		lifeTimer=beamTimer;
		//if its bigger than 1 than its not the end 
		//so use the middle part texture
		//else use endpart
		if(blastRadius>1){
			walkSheet = new Texture(Gdx.files.internal("res/assets/beam_middle.png"));
		} else if(blastRadius==1){
			walkSheet = new Texture(Gdx.files.internal("res/assets/beam_end.png"));
		}
		hitbox = new MyRectangle();
		this.size=size;
		//decide which direction it is facing
		// 0 -> where the bomb is 
		// 1 - up
		// 2 - right
		// 3 - down
		// 4 - left        

		if(direction==0){
			mySquare=square;
			hitbox.setSize(this.size-7,this.size-7);
			walkSheet= new Texture(Gdx.files.internal("res/assets/beam_cross.png"));
		} else
		if(direction==1){
			rotation=0;
			hitbox.setSize(this.size/2,this.size-7);
			securityDistanceY=-1;
			mySquare=square.getUp();
		}else if(direction==2){
			rotation=270;
			securityDistanceX=1;
			hitbox.setSize(this.size-7,this.size/2);
			mySquare=square.getRight();
		}else if(direction==3){
			rotation=180;
			securityDistanceY=1;
			hitbox.setSize(this.size/2,this.size-7);
			mySquare=square.getDown();
		}else if(direction==4){
			securityDistanceX=-1;
			rotation=90;
			hitbox.setSize(this.size-7,this.size/2);
			mySquare=square.getLeft();
		}	
		if(mySquare==null){
			System.out.println("ERROR no square for blastbeam");
			return;
		}
		if(mySquare.hasStuff()){
        	blastRadius=1;
        	notRender=true;
        } else {
        	notRender=false;
        }
		if(mySquare!=null){
		this.position=new Vector3(mySquare.getCenter().x,mySquare.getCenter().y,0);
		
		
		hitbox.setCenter(mySquare.getX()+this.size/2+securityDistanceX, mySquare.getCenter().y+securityDistanceY);
		
        TextureRegion[][] tmp = TextureRegion.split(walkSheet, walkSheet.getWidth()/FRAME_COLS, walkSheet.getHeight());              
        walkFrames = new TextureRegion[FRAME_COLS];
        int index = 0;
        for (int i = 0; i < FRAME_COLS; i++) {
                walkFrames[index++] = tmp[0][i]; 
        }
        walkAnimation = new Animation(lifeTimer/FRAME_COLS, walkFrames);     
        blastRadius--;

        //if the blastradius is bigger than 0 and its not the middle 
        //create a new one in the same dir
        if(blastRadius>0 && direction != 0){
        	next = new Blastbeam(mySquare,direction,size,blastRadius, beamTimer);
        }      
		}
        
	}
	
	
	@Override
	public void update(float delta) {

		lifeTimer-=delta;
		if(lifeTimer<=0){
			dead=true;
		}
		stateTime+=delta;
		if(next!=null){
			next.update(delta);
		}
		
	}

	public TextureRegion walkAnimation(){
		return walkAnimation.getKeyFrame(stateTime, true); 
	}
	
	
	@Override
	public void render(SpriteBatch batch) {
		if(mySquare!=null && !notRender){
			
			batch.draw(walkAnimation(), position.x - size/2,position.y - size/2 ,size/2,size/2, size, size, 1, 1, rotation);
		}
		if(next!=null){
			next.render(batch);
		}
	}


	public Collection<? extends Blastbeam> getAll() {
		ArrayList<Blastbeam> collideable = new ArrayList<Blastbeam>();
		collideable.add(this);
		if(next!=null){
			collideable.addAll(next.getAll());
		}
		return collideable;
	}

}
