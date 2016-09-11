package com.clone.bomber.map;

import java.io.Serializable;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.clone.bomber.util.Collideable;
import com.clone.bomber.util.MyRectangle;


public class Box extends Collideable implements Serializable {
	private static final long serialVersionUID = 4867197150240751196L;
		private Square mySquare;
		private transient Texture texture;
		private boolean isRandom=false;
		private boolean dead = false;
		private float dietime = .33f;
		private transient TextureRegion[][] texs;
		private boolean nothere=false;
		private boolean explode;
		public Box(Square mySquare,Map map) {
			this.mySquare=mySquare;
			hitbox = new MyRectangle();
			hitbox.setSize(mySquare.getSize());
			hitbox.setPosition(mySquare.getX(),mySquare.getY());
			
			load(map);
		}
		public Box(Square mySquare, boolean isRandom,Map map) {
			//random is for onload decide if its there or if it can be removed
			this.isRandom=isRandom;
			this.mySquare=mySquare;
			hitbox = new MyRectangle();
			hitbox.setSize(mySquare.getSize());
			hitbox.setPosition(mySquare.getX(),mySquare.getY());
			
			load(map);
		}
		public void load(Map map){

			hitbox.setMyData(mySquare);
			dietime = .33f;
			texture=map.getTexManager().getBoxTexture();
	        texs = TextureRegion.split(texture, texture.getWidth()/2, texture.getHeight());  			
	        //if random -> decide if its there or not
			if(isRandom){
				float i = (float) Math.random();
			
				if(i<0.5){
					mySquare.setHasBox(false, this);
					nothere=true;
					hitbox=new MyRectangle();
				} 
			}    
		}
		public void render(SpriteBatch batch, float delta){
//			batch.draw(texs[0][0], 10, 10, 30, 30);

				if(!explode){
				//	if(texs!=null)
					batch.draw(texs[0][0], mySquare.getX(), mySquare.getY(), mySquare.getSize(), mySquare.getSize());
				} else {
					batch.draw(texs[0][1], mySquare.getX(), mySquare.getY(), mySquare.getSize(), mySquare.getSize());
					if(dietime<0){
						dead=true;
					}
					dietime-=delta;
				}
			
		}

		public void explode() {
			//if it get hit by a beam
			//"explode" -> set death timer and set dead
			explode=true;
			this.mySquare.setHasBox(false, this);
			
		//	this.setDead(true);
			
		}

		public boolean isDead() {
			return dead;
		}

		public void setDead(boolean dead) {
			this.dead = dead;
		}

		public Square getMySquare() {
			return mySquare;
		}

		public void setMySquare(Square mySquare) {
			this.mySquare = mySquare;
		}
		public boolean isRandom() {
			return isRandom;
		}
		public void setRandom(boolean isRandom) {
			this.isRandom = isRandom;
		}
		public boolean isNothere() {
			return nothere;
		}
		public void setNothere(boolean nothere) {
			this.nothere = nothere;
		}

}
