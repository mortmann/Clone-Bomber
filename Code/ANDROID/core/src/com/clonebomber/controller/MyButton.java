package com.clonebomber.controller;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class MyButton {
	private Rectangle myRectangle;
	private Texture image;
	private Texture pressedImage;
	private int direction;
	private boolean touched;
	private int width;
	private float y;
	private float x;
	private int height;
	
	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	public boolean isTouched() {
		return touched;
	}

	public MyButton(Texture drawOne, Texture drawTWO) {
		this.image = drawOne;
		this.pressedImage = drawTWO;
	}

	public void setBounds(float x, float y, int width, int height) {
		myRectangle=new Rectangle();
		this.x=x;
		this.y=y;
		this.width=width;
		this.height=height;
		System.out.println(y);
		myRectangle.set(x, y, width, height);
	}

	public void addListener(int direction) {
		this.direction=direction;
	}
	
	public void checkForTouch(Vector2 position){
		if(myRectangle.contains(position)){
			this.touched=true;
		} else {
			touched=false;
		}
	}
	public void render(SpriteBatch batch){
		if(touched){
			batch.draw(pressedImage, x, y, width, height);
		} else {
			batch.draw(image, x, y, width, height);
		}
	}

	public void unPress() {
		touched=false;
	}
	
	
	
}
