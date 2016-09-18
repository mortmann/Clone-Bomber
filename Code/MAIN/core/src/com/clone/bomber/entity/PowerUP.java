package com.clone.bomber.entity;

import java.io.Serializable;
import java.util.Random;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.clone.bomber.map.Map;
import com.clone.bomber.map.Square;
import com.clone.bomber.util.MyRectangle;


public class PowerUP extends Entity implements Serializable {
	private static final long serialVersionUID = 4009111874389716708L;
	private float startTimer=.34f;
	private Square mySquare;
	private PowerUPEffects myEffect;
	private transient TextureRegion texture;
	private boolean isNegativ=false;
	private float maxX;
	public PowerUP(Square square,Map map){
		this.mySquare=square;
		mySquare.setHasPowerUp(true);
		position = new Vector3(square.getX(),square.getY(),1);
		hitbox = new MyRectangle();
		hitbox.setSize(mySquare.getSize());
		hitbox.setPosition(mySquare.getX(),mySquare.getY());
		Random r = new Random();
		//decide which powerUP is it
		float goodBad = r.nextFloat();
		if(goodBad<=0.85f){
			isNegativ = false;
			float effect = r.nextFloat();
			if(effect<=0.25f){
				myEffect = (PowerUPEffects.speed);
			} else
			if(effect<=0.5f){
				myEffect = (PowerUPEffects.blastradius);
			} else
			if(effect<=0.75f){
				myEffect = (PowerUPEffects.bomb);
			} else
			if(effect<=0.875f){
				myEffect = (PowerUPEffects.throwable);
			} else
			if(effect<=1f){
				myEffect = (PowerUPEffects.push);
			}
		} else {
			isNegativ = true;
			float effect = r.nextFloat();
			if(effect<=0.33f){
				myEffect = (PowerUPEffects.joint);
			} else
			if(effect<=0.66f){
				myEffect = (PowerUPEffects.diarrhea);
			} else
			if(effect<=1f){
				myEffect = (PowerUPEffects.superspeed);
			}
		}
		texture = new TextureRegion(map.getTexManager().getPowerUp(myEffect));
	}	
	public PowerUP(Square square, Map map,PowerUPEffects effects){
		this.mySquare=square;
		mySquare.setHasPowerUp(true);
		position = new Vector3(square.getX(),square.getY(),1);
		hitbox = new MyRectangle();
		hitbox.setSize(mySquare.getSize());
		hitbox.setPosition(mySquare.getX(),mySquare.getY());
		myEffect= effects;
		texture = new TextureRegion(map.getTexManager().getPowerUp(myEffect));
		if(effects == (PowerUPEffects.superspeed) || effects == PowerUPEffects.diarrhea||effects == (PowerUPEffects.joint )){
			isNegativ = true;
		}
	}

	@Override
	public void render(SpriteBatch batch) {
//		batch.draw(texture, position.x, position.y, position.z, mySquare.getSize());
		batch.draw(texture, position.x, position.y,mySquare.getSize(), mySquare.getSize(), mySquare.getSize(), mySquare.getSize(), position.z, position.z, 0);

	}

	@Override
	public void update(float delta) {
		if(startTimer>0){
			startTimer-=delta;	
		}
		if(isFlying){
			x=-Math.abs((mySquare.getPos().dst(position.x,position.y)))/mySquare.getSize();
			double sqrt = Math.pow(x, 2);
			float tempX2 = (float) (a * sqrt);
			float flySpeed = (tempX2+b*x+(c));
			this.position.z=flySpeed;
//			if(!(endSquare.getX()-2<position.x && endSquare.getX()+2>position.x&&
//					endSquare.getY()-2<position.y&& endSquare.getY()+2>position.y)){                      	
			if(Math.abs(x)<Math.abs(maxX)){
				this.position.x+=2*(1/flySpeed*(move.x)*100*delta);
				this.position.y+=2*(1/flySpeed*(move.y)*100*delta);
			} else {
				this.position.x= endSquare.getX();
				this.position.y= endSquare.getY();
				this.position.z=1;
				mySquare=endSquare;
				hitbox = new MyRectangle();
				hitbox.setSize(mySquare.getSize());
				hitbox.setPosition(mySquare.getX(),mySquare.getY());
				mySquare.setHasPowerUp(true);
				isFlying = false;
			}
		}
		
	}
	
	public void destroy(){
		if(isFlying){
			return;
		}
		if(isNegativ == true){
			Square temp=mySquare.getRandom(false);
			while(temp.isHasWall()){
				temp=mySquare.getRandom(false);
			}
			flyToSquare(temp);
		} else
		if(startTimer<=0){
			this.dead=true;
		}
	}
	
	public void collect(Player player){
		player.addPowerUP(myEffect);
	}

	public void kill() {
		mySquare.setHasPowerUp(false);
		this.dead=true;
	}
	public void flyToSquare(Square temp) {
		hitbox = new MyRectangle();
		hitbox.setSize(1);
		hitbox.setPosition(0,0);

		x=-(Math.abs((mySquare.getPos().dst(temp.getPos())))/mySquare.getSize());
		maxX = x;
		move=new Vector2(0,0);
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
//		System.out.println("y="+a+"*x^2"+b+"*x"+c);
		
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

	public Square getMySquare() {
		return mySquare;
	}

	public void setMySquare(Square mySquare) {
		this.mySquare = mySquare;
	}
	public PowerUPEffects getMyEffect() {
		return myEffect;
	}
}
