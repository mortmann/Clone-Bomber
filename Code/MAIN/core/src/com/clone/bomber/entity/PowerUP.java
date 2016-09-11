package com.clone.bomber.entity;

import java.io.Serializable;
import java.util.Random;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.clone.bomber.map.Map;
import com.clone.bomber.map.Square;
import com.clone.bomber.util.MyRectangle;

public class PowerUP extends Entity implements Serializable {
	private static final long serialVersionUID = 4009111874389716708L;
	private int type;
	private float startTimer=.34f;
	private Square mySquare;
	private transient Texture texture;
	/*
	 * effects = -1 israndom in class
	 * 1 = speed
	 * 2 = blastradius
	 * 3 = bomb
	 * 4 = throw
	 * 5 = push
	 * 6 = joint (inverted controls)
	 * 7 = diarrhea
	 * 8 = superspeed
	 */
	public PowerUP(Square square,int effects,Map map){
		this.mySquare=square;
		position = new Vector3(square.getX(),square.getY(),square.getSize());
		hitbox = new MyRectangle();
		hitbox.setSize(mySquare.getSize());
		hitbox.setPosition(mySquare.getX(),mySquare.getY());
		texture=new Texture("res/assets/speed_PowerUP.png");
		Random r = new Random();
		//decide which powerUP is it
		float goodBad = r.nextFloat();
		if(effects==-1){
			if(goodBad<0.85f){
				float effect = r.nextFloat();
				if(effect<0.25f){
					texture=map.getTexManager().getPowerUp("speed");
					type=1;
				} else
				if(effect<0.5f){
					texture=map.getTexManager().getPowerUp("blastradius");
					type=2;
				} else
				if(effect<0.75f){
					texture=map.getTexManager().getPowerUp("bomb");
					type=3;	
				} else
				if(effect<0.875f){
					texture=map.getTexManager().getPowerUp("throw");
					type=4;
				} else
				if(effect<0.1f){
					texture=map.getTexManager().getPowerUp("push");
					type=5;
				}
			} else {
				float effect = r.nextFloat();
				if(effect<=0.33f){
					texture=map.getTexManager().getPowerUp("joint");
					type=6;
				} else
				if(effect<=0.66f){
					texture=map.getTexManager().getPowerUp("diarrhea");
					type=7;
				} else
				if(effect<0.1f){
					texture=map.getTexManager().getPowerUp("superspeed");
					type=8;	
				}
			}
		} else {
			type=effects;
		}
	}	

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	@Override
	public void render(SpriteBatch batch) {
//		batch.draw(texture, position.x, position.y, position.z, mySquare.getSize());
		batch.draw(new TextureRegion(texture), position.x, position.y,mySquare.getSize(), mySquare.getSize(), mySquare.getSize(), mySquare.getSize(), position.z, position.z, 0);

	}

	@Override
	public void update(float delta) {
		startTimer-=delta;
		if(isFlying){
			System.out.println("isflying");
			x=-Math.abs((mySquare.getPos().dst(position.x,position.y)))/mySquare.getSize();
			double sqrt = Math.pow(x, 2);
			float tempX2 = (float) (a * sqrt);
			float flySpeed = (tempX2+b*x+(c));
			this.position.z=flySpeed;
			if(!(endSquare.getX()-2<position.x && endSquare.getX()+2>position.x&&  endSquare.getY()-2<position.y&& endSquare.getY()+2>position.y)){                      	
				this.position.x+=1/flySpeed*move.x*100*delta;
				this.position.y+=1/flySpeed*move.y*100*delta;
			} else {
				this.position.x= endSquare.getX();
				this.position.y= endSquare.getY();
				this.position.z=1;
				mySquare=endSquare;
			}
		}
		
	}
	
	public void destroy(){
		if(type>=6){
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
		player.addPowerUP(this.type);
	}

	public void kill() {
		this.dead=true;
		
	}
	public void flyToSquare(Square temp) {
		x=-(Math.abs((mySquare.getPos().dst(temp.getPos())))/mySquare.getSize());
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
	
}
