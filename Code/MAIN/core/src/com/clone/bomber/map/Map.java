package com.clone.bomber.map;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Random;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.clone.bomber.GameClass;
import com.clone.bomber.entity.PowerUP;
import com.clone.bomber.util.Collideable;
import com.clone.bomber.util.MyRectangle;
import com.clone.bomber.util.TextureManager;

public class Map implements Serializable {
	private static final long serialVersionUID = 1L;
	private int size;
	private Square[] squares;
	private ArrayList<Wall> walls;
	private ArrayList<Box> boxes;
	private transient ArrayList<PowerUP> powerUPs;
	private ArrayList<Square> spawnSpots;
	private ArrayList<Square> forRandomSquares;
	private int squareSize;
	private int playerNumber=2;
	private transient TextureManager texManager;
	private ArrayList<Square> squareBoxes;
	public Map(int size){
		texManager = new TextureManager();
		forRandomSquares= new ArrayList<Square>();
		squareBoxes = new ArrayList<Square>();
		this.size=size+2;
		float temp=(GameClass.viewportHeight/this.size);
		temp/=10;
		temp=Math.round(temp)*10;
		squareSize=(int) temp;
		walls= new ArrayList<Wall>();
		boxes= new ArrayList<Box>();
		powerUPs= new ArrayList<PowerUP>();
		squares=new Square[this.size*this.size];
		spawnSpots= new ArrayList<Square>();
		int x= GameClass.viewportWidth/2-squareSize*(this.size/2);
		int y= squareSize/2;
		boolean clear=true;
		for(int i = 1; i <= (this.size)*(this.size);i++){	
			if(i%this.size==1 && i!=1){
				x =	GameClass.viewportWidth/2-squareSize*(this.size/2);
				y+= squareSize;
				clear=true;
			} else
			if(i%this.size==0 || i<this.size || i>(this.size*this.size)-this.size){
				clear=true;
			} else {
				if(i>20 && i<57){
					clear=true;
				} else if(i>304 && i<342) {
					clear=true;
				} else {
					clear = false;
				}
				
			}
			squares[i-1]=new Square(i,squareSize,x,y,clear,this);
			x+=squareSize;
		}
	}

// --------------- FOR IMPORTING MAPS FROM CLANBOMBER! ----------------------------------
	public Square[] getTemp(){
		Square[] temp = new Square[221];
		int i=221;
		for(Square s : squares){
			if(!s.isEmpty()){
				i--;
				temp[i]=s;
			}
		}
		return temp;
	}
	
	public void draw(SpriteBatch spriteBatch,float delta) {
		for(Square s : squares){
			s.render(spriteBatch);
		}
		for(Wall w : walls){	
			w.render(spriteBatch);
		}
		for(Box b : boxes){
			b.render(spriteBatch, delta);
		}
		for(PowerUP u : powerUPs){
			u.render(spriteBatch);
		}
	}
	public int getFieldSize() {
		return squareSize;
	}
	
	public void update(float delta){
		for(int i = boxes.size()-1; i >= 0; i--){
			if(boxes.get(i).isDead()){
				float random = (float) Math.random();
				if(random<0.33){
					powerUPs.add(new PowerUP(boxes.get(i).getMySquare(),this));
				}
				boxes.remove(i);
			}
		}
		for(int i = powerUPs.size()-1; i >= 0; i--){
			powerUPs.get(i).update(delta);
			if(powerUPs.get(i).isDead()){
				powerUPs.remove(i);
			}
		}
	}
	
	public Square getSquare(MyRectangle rect) {
		MyRectangle tempR = new MyRectangle();
		float amount = 0;
		Square tempS = null;
		for(Square s : squares){
			boolean intersects =Intersector.intersectRectangles(s.getHitbox(), rect, tempR);
			if(intersects){
				if(s.getHitbox().contains(rect)){
					return s;
				}
			}
			if(intersects){	
				if(amount<tempR.height*tempR.width){
					amount = tempR.height*tempR.width;
					tempS=s;
				} 
			} 
		}
		return tempS;
	}
	public ArrayList<MyRectangle> getMyRectangles() {
		ArrayList<MyRectangle> rect= new ArrayList<MyRectangle>();	
		for(int i = 0; i<boxes.size();i++){
			if(boxes.get(i).isDead()){
				boxes.remove(i);
			} else {
				rect.add(boxes.get(i).getHitbox());
			}
		}
		for(int i = 0; i<walls.size();i++){
			rect.add(walls.get(i).getHitbox());
		}
		return rect;
	}
	public Collection<? extends Collideable> getCollideables() {
		ArrayList<Collideable> collideable = new ArrayList<Collideable>();
		collideable.addAll(boxes);
		collideable.addAll(walls);
		collideable.addAll(powerUPs);
		return collideable;
	}
	public Square getSquareNumber(int i) {
		return squares[i-1];
	}
	public  Square[] getSquares() {
		return squares;
	}
	public int getMapSize() {
		return size;
	}
	public ArrayList<PowerUP> getPowerUPs() {
		return powerUPs;
	}
	public void removeWall(Wall myWall) {
		forRandomSquares.add(myWall.getMySquare());
		walls.remove(myWall);	
	}
	public void removeBox(Box myBox) {
		forRandomSquares.add(myBox.getMySquare());
		squareBoxes.remove(myBox.getMySquare());
		boxes.remove(myBox);
	}
	public void addWall(Wall w) {
		walls.add(w);
	}
	public void addBox(Box b) {
		boxes.add(b);
	}
	public void addSpawnspot(Square s) {
		if(spawnSpots.size()==playerNumber){
			spawnSpots.remove(0);
		} 
		spawnSpots.add(s);
	}
	//for import from clanbomber
	public void addSpawnspotTemp(Square s) {
		spawnSpots.add(s);
	}
	public ArrayList<Square> getSpawnSpots() {
		return spawnSpots;
	}
	public void load() {
		texManager = new TextureManager();
		forRandomSquares= new ArrayList<Square>();
		squareBoxes= new ArrayList<Square>();
		powerUPs= new ArrayList<PowerUP>();
		for(Square s : squares){ 
			s.load();
			if(!s.isHasWall()&&!s.isEmpty()&&!s.isHasBox()){
				forRandomSquares.add(s);
			}
			if(s.isHasBox()){
				squareBoxes.add(s);
			}
		}
		for(int i = boxes.size()-1; i>=0;i--){
			boxes.get(i).load(this);
		}
		for(int i = 0; i<walls.size();i++){
			walls.get(i).load(this);
		}
	}
	public Square getRandomSquare(boolean all) {
		if(all){
			Random r = new Random();
			int random =  Math.abs(r.nextInt(size*size)-1);
			ArrayList<Square> sq = new ArrayList<Square>(Arrays.asList(squares));
			while(sq.get(random).isEmpty()){
				random = Math.abs(r.nextInt(sq.size())-1);
				if(!sq.get(random).isEmpty()){
					return sq.get(random);
				} else {
					sq.remove(random);
				}
			}
			return squares[random];
		}else {
			Random r = new Random();
			int random =  Math.abs(r.nextInt(forRandomSquares.size())-1);
			return forRandomSquares.get(random);
		}
	}
	public int getPlayerNumber() {
		return spawnSpots.size();
	}
	public void setPlayerNumber(int playerNumber) {
		this.playerNumber = playerNumber;
	}
	public void addPowerUps(ArrayList<PowerUP> powers) {
		this.powerUPs.addAll(powers);
	}

	public void removeRandomSquare(Square s){
		forRandomSquares.remove(s);
	}

	public TextureManager getTexManager() {
		return texManager;
	}

	public void dispose() {
		texManager.disposeAll();
		
	}

	public Square getRandomWithBox() {
		ArrayList<Square> temp = new ArrayList<Square>();
		temp.addAll(squareBoxes);
		temp.addAll(forRandomSquares);
		Random r = new Random();
		int random =  Math.abs(r.nextInt(temp.size())-1);
		return temp.get(random);
	}

}
