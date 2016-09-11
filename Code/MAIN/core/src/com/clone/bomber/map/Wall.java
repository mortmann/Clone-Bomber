package com.clone.bomber.map;

import java.io.Serializable;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.clone.bomber.util.Collideable;
import com.clone.bomber.util.MyRectangle;

public class Wall extends Collideable implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7797158323384900998L;
	private Square mySquare;
	private transient Texture texture;
	private boolean dead= false;
	
	public Wall(Square mySquare,Map map){
		this.mySquare=mySquare;
		hitbox = new MyRectangle();
		hitbox.setSize(mySquare.getSize());
		hitbox.setPosition(mySquare.getX(),mySquare.getY());
		load(map);
	}
	public void load(Map map){
		texture=map.getTexManager().getWallTexture();
		hitbox.setMyData(mySquare);
	}
	public void render(SpriteBatch batch){
		if(texture!=null)
			batch.draw(texture, mySquare.getX(), mySquare.getY(), mySquare.getSize(), mySquare.getSize());
	}

	public boolean isDead() {
		return dead;
	}
	public Square getMySquare() {
		return mySquare;
	}

}
