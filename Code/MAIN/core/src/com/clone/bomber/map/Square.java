package com.clone.bomber.map;

import java.io.Serializable;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.clone.bomber.entity.Bomb;

public class Square implements Serializable{
	private static final long serialVersionUID = -1830097991549157121L;
	private int number;
	private String groundRegName;
	private String modifierRegName;
	private transient Texture groundTex;
	private transient TextureRegion modifierReg;
	
	private int x;
	private int y;
	private Vector2 center;
	private int size;
	private Rectangle hitbox;
	private boolean empty;
	
	private boolean isIce=false;
	private boolean hasWall=false;
	private boolean hasBomb=false;
	private boolean hasBox=false;
	private boolean hasHole=false;
	
	private int arrow=-1;
	private Map map;
	private transient Bomb myBomb;
	private Wall myWall;
	private Box myBox;
	private int degree=0;
	private transient boolean hasPowerUp;
	private boolean debugOn;
	
	public Square(int number, int size,int x,int y,boolean empty, Map map){
		this.map=map;
		this.number=number;
		this.size=(size);			
		this.y=(y-size);
		this.x=(x);
		center=new Vector2(x+size/2,(y-size)+size/2);
		this.empty=empty;
		hitbox = new Rectangle();
		hitbox.setSize(this.size);
		hitbox.setPosition(this.x,this.y);

		load();
	}
	public void load(){
		debugOn=Gdx.app.getPreferences("clonebomber").getBoolean("debug",false);
		// on load decide which texture it needs to load
		if(empty==true && debugOn){
		} else {
			groundTex = map.getTexManager().getSquareGroundTex();
		}
		//decide which modifier it has
		if(hasHole){
			modifierRegName=("Hole");
		} else if(isIce){
			modifierRegName=("Ice");
		} else if(arrow!=-1){
			modifierRegName=("Arrow");
		}
		
		if(modifierRegName!=null && !empty){
			modifierReg=map.getTexManager().getSqauerModifierReg(modifierRegName);
		}
	}
	public void render(SpriteBatch batch){

		if(!empty || debugOn)
			batch.draw(groundTex, getX(), getY(), getSize(), getSize());
		if(modifierReg!=null && !empty){
			batch.draw(modifierReg, x, y, size/2, size/2, size, size, 1, 1, degree);
		}
			
			
	}
	
// give back the square it has to its sides
	public Square getRight(){
		int temp = (number) % map.getMapSize();
		if(temp==0){
			temp=map.getMapSize();
		}
		if((temp+1)<map.getMapSize()){
			return map.getSquareNumber(number+1);
		}
		return null;
	}
	public Square getUp(){
		if(number+map.getMapSize() < (map.getMapSize()* map.getMapSize())-map.getMapSize() ){
			return map.getSquareNumber(number+map.getMapSize());
		}
		return null;
	}
	public Square getLeft(){
		int temp = (number) % map.getMapSize();
		if(temp==0){
			temp=map.getMapSize();
		}
		if(temp>0){
			return map.getSquareNumber(number-1);
		}
		return null;
	}
	public Square getDown(){
		if(number-map.getMapSize() > map.getMapSize()){
			return map.getSquareNumber(number-map.getMapSize());
		} 
		return null;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public Rectangle getHitbox() {
		return hitbox;
	}
	public boolean isEmpty() {
		return empty;
	}
	public void setEmpty(boolean empty) {
		this.empty = empty;
	}
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}

	public int getArrow() {
		return arrow;
	}
	public void setArrow(int arrow) {
		if(arrow!=-1){
			modifierRegName="Arrow";
			modifierReg=map.getTexManager().getSqauerModifierReg(modifierRegName);
			if(arrow==0){
				degree=0;
			} else 
			if(arrow==1){
				degree=270;
			} else 
			if(arrow==2){
				degree=180;
			} else 
			if(arrow==3){
				degree=90;
			} 	
		}
		this.arrow = arrow;
	}
	public boolean isHasBomb() {
		return hasBomb;
	}
	public void setHasBomb(Bomb bomb) {
		this.setMyBomb(bomb);
		this.hasBomb = bomb!=null;
	}
	public Vector2 getCenter() {
		return center;
	}
	public void setCenter(Vector2 center) {
		this.center = center;
	}
	public Bomb getMyBomb() {
		return myBomb;
	}
	public void setMyBomb(Bomb myBomb) {
		this.myBomb = myBomb;
	}
	public Vector2 getPos() {
		return new Vector2(x,y);
	}
	public boolean isHasBox() {
		return hasBox;
	}	
	
	public Box getMyBox() {
		return myBox;
	}
	public boolean isIce() {
		return isIce;
	}
//change the modifier -> all squares can only have ONE modifier
//so if set modifier -> remove all others
//can only placed on not empty ones
	public void setIce(boolean isIce) {
		if(isIce){
			modifierRegName=("Ice");
			modifierReg=map.getTexManager().getSqauerModifierReg(modifierRegName);
			if(hasWall){
				if(hasBox){
					map.removeBox(this.myBox);
				}
			}
			if(hasBox){
				if(hasWall){
					map.removeWall(this.myWall);
				}
			}
			arrow=-1;
			this.degree=0;
		}
		this.isIce = isIce;
	}
	public void setHasBox(boolean hasBox, Box myBox) {
		
		if(hasBox){
			if(hasWall){
				map.removeWall(this.myWall);
			}
			if(hasHole){
				hasHole=false;
			}
			arrow=-1;
			isIce=false;
			this.myBox=myBox;
		} 
		this.hasBox = hasBox;
	}
	public boolean isHasWall() {
		return hasWall;
	}
	public void setHasWall(boolean hasWall, Wall myWall) {
		if(hasWall){
			if(hasBox){
				hasBox=false;
				map.removeBox(this.myBox);
			}
			if(hasHole){
				hasHole=false;
			}
			arrow=-1;
			isIce=false;
		}
		this.myWall=myWall;
		this.hasWall = hasWall;
	}
	public boolean hasHole() {
		return hasHole;
	}
	public void setHole(boolean hasHole) {
		if(hasHole){
			isIce=false;
			arrow=-1;
			this.degree=0;
			modifierRegName=("Hole");
			modifierReg=map.getTexManager().getSqauerModifierReg(modifierRegName);
			if(hasBox){
				hasBox=false;
				map.removeBox(this.myBox);
			}
			if(hasWall){
				hasWall=false;
				map.removeWall(this.myWall);
			}
		}
		this.hasHole = hasHole;
	}
	
	public int getMaxSquares(){
		return map.getMapSize()*map.getMapSize();
	}
	public Square getSquare(int endSquare) {
		return map.getSquareNumber(endSquare);
	}
	public void clearSquare(boolean b){
		isIce=false;
		arrow=-1;
		if(hasBox){
			hasBox=false;
			map.removeBox(this.myBox);
		}
		if(hasWall){
			hasWall=false;
			map.removeWall(this.myWall);
		}
		this.hasHole=false;
		this.degree=0;
		modifierRegName=null;
		if(b){
			groundRegName="res/maptexture/empty_temp.png";
			groundTex = new Texture(groundRegName);
			this.setEmpty(b);
		} else {
			groundRegName= "res/maptexture/floor_" +Gdx.app.getPreferences("clonebomber").getString("Mapstyle", "normal").toLowerCase()+".png";
			groundTex = new Texture(groundRegName);
			this.setEmpty(b);
		}
	}

	public void getHit(){
		if(hasBox){
			hasBox=false;
			map.removeBox(this.myBox);
		} else
		if(hasWall){
			hasWall=false;
			map.removeWall(this.myWall);
		} else {
			map.removeRandomSquare(this);
			this.empty=true;
//			groundRegName="res/maptexture/empty_TEMP.png";
//			groundTex = new Texture(groundRegName);
			
		}
		
	}
	/*
	 * easy access to map size
	 */
	public int getRowSize(){
		return this.map.getMapSize();
	}
	public Square getRandom(boolean withWall) {
		return map.getRandomSquare(withWall);
	}
	public boolean hasStuff() {
		
		return this.hasWall||this.hasBox||this.hasPowerUp;
	}
	public boolean isHasPowerUp() {
		return hasPowerUp;
	}
	public void setHasPowerUp(boolean hasPowerUp) {
		this.hasPowerUp = hasPowerUp;
	}
}
