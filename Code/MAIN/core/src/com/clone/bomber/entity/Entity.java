package com.clone.bomber.entity;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.clone.bomber.map.Square;
import com.clone.bomber.util.Collideable;

public abstract class Entity extends Collideable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1570920355112612408L;
	protected Vector3 position;
	protected boolean dead =false;
	protected transient Animation walkAnimation;  
	protected transient Animation idleAnimation;
	protected Square endSquare;
	protected Square mySquare;
	protected float x;
	protected Vector2 move;
	protected boolean isFlying;
	protected float a;
	protected float b;
	protected float c;  


	public Vector3 getPosition() {
		return position;
	}
	public void setPosition(Vector3 position) {
		this.position = position;
	}
	public abstract void update(float delta);
	
	public abstract void render(SpriteBatch batch);
	
	public boolean isDead() {
		return dead;
	}
	public void setDead(boolean dead) {
		this.dead = dead;
	}	


}
