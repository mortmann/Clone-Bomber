package com.clone.bomber.entity;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.clone.bomber.map.Box;
import com.clone.bomber.map.Square;
import com.clone.bomber.map.Wall;
import com.clone.bomber.util.Collideable;
import com.clone.bomber.util.MyRectangle;
import com.clone.bomber.util.MySound;

public class Bomb extends Entity{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4795295578497227921L;
	//ANIMATIONS
	private static final int FRAME_COLS = 4;                    
    private transient TextureRegion[] bombFrames;
	private float stateTime;
	private int size;
	private int blastRadius;          
	private float explosionTimer;
	private float beamTimer=.33f;
	private Square mySquare;
	private Vector2 move;
	private float speed=120;
	private Blastbeam[] beams;

	private float holeCooldown;
	//for flying
	private float flySpeed;
	private Square endSquare;
	private boolean isFalling;
	private MySound mySound;
	private Player player;

	private boolean collidesWithWallWhileFlying=true;
	private boolean exploded;
	
	public Bomb(Square s, int size, int blastRadius, Player player, MySound mySound, Texture bomb){
		this.mySound=mySound;
		mySound.playBombSound(1);
		mySquare=s;
		explosionTimer=2.5f;
		position=new Vector3(s.getX(),s.getY(),1);
		this.size=size;
		this.blastRadius=blastRadius;
		this.player=player;
		move= new Vector2();
		hitbox = new MyRectangle();
		hitbox.setSize((size));
		hitbox.setPosition(s.getX(),s.getY());
		hitbox.setMyData(mySquare);
        TextureRegion[][] tmp = TextureRegion.split(bomb, bomb.getWidth()/FRAME_COLS, bomb.getHeight());              
        bombFrames = new TextureRegion[FRAME_COLS];
        holeCooldown=1f;
        int index = 0;
        for (int j = 0; j < FRAME_COLS; j++) {
        	bombFrames[index++] = tmp[0][j]; 
        }

        walkAnimation = new Animation(0.25f, bombFrames);     

	}
	
	public TextureRegion walkAnimation(){
		return walkAnimation.getKeyFrame(stateTime, true); 
	}
	
	
	@Override
	public void render(SpriteBatch batch) {
		if(beams==null){
			batch.draw(this.walkAnimation(), position.x, position.y,size, size, size, size, position.z, position.z, 0);
		} else {
			for(int i=0;i<5;i++){	
				beams[i].render(batch);
			}

		}
	}

	@Override
	public void update(float delta) {
		stateTime+=delta;
		explosionTimer-=delta;
		StandingUpdate(delta);
		FallIfSquareEmpty(delta);	
			
		if(!isFalling && beamTimer==2f){
			this.position.x+=move.x*speed*delta;
			this.position.y+=move.y*speed*delta;
		}
		
		FlyingUpdate(delta);
		
		this.hitbox.setPosition(position.x,position.y);
	}

	private void FlyingUpdate(float delta) {
		if(isFlying && endSquare!=null){
			mySquare.setHasBomb(null);
			if(mySquare!=endSquare){
				x=-Math.abs((mySquare.getPos().dst(position.x,position.y)))/mySquare.getSize();
			} else {
				x-=1*delta;
			}
			double sqrt = Math.pow(x, 2);
			float tempX2 = (float) (a * sqrt);
			flySpeed =  (tempX2+b*x+(c));
			this.position.z=flySpeed;
			if(!(endSquare.getX()-2<position.x && endSquare.getX()+2>position.x&&  endSquare.getY()-2<position.y&& endSquare.getY()+2>position.y)){                      	
				this.position.x+=1/flySpeed*(2*move.x)*speed*delta;
				this.position.y+=1/flySpeed*(2*move.y)*speed*delta;
			} else {
				if(this.collidesWithWallWhileFlying){
					if(endSquare.isHasBomb() && endSquare.getMyBomb()!=this){
						float tempX = Math.abs(move.x);
						float tempY = Math.abs(move.y);
						Square tempS=endSquare;
						if(tempY>tempX){
							if(move.y<0){
								tempS=endSquare.getDown();
							} else {
								tempS=endSquare.getUp();
							}
						} else
						if(tempX>tempY){
							if(move.x<0){
								tempS=endSquare.getLeft();
							} else {
								tempS=endSquare.getRight();
							}
						}
						mySquare=endSquare;
						if(!tempS.isHasWall()){
							
							flyToSquare(tempS);
						} else {
							flyToSquare(endSquare);
						}
					} else {
						this.position.x= endSquare.getX();
						this.position.y= endSquare.getY();
						this.position.z=1;
						move.setZero();
						mySquare=endSquare;
						
						isFlying=false;
						endSquare=null;
					}
					if(!mySquare.isHasBomb()){
						mySquare.setHasBomb(this);
					}				

				} else {			
				this.position.x= endSquare.getX();
				this.position.y= endSquare.getY();
				this.position.z=1;
				move.setZero();
				mySquare=endSquare;
				
				isFlying=false;
				endSquare=null;
				}
			}
		}		
	}

	private void FallIfSquareEmpty(float delta) {
		if(this.mySquare.isEmpty() && !isFlying){
			if(this.position.z>=1 &&!isFalling){
				mySound.playFallSound();
			}
			if(this.position.z>0.1){
				this.position.z-=this.position.z*this.position.z*9.81f*delta;
				this.isFalling=true;
			} else {
				this.dead=true;
			}
		}			
	}

	private void StandingUpdate(float delta) {
		if(!isFlying){
			if(move.len()>0 && explosionTimer>0){
				mySquare.setHasBomb(null);
				mySquare = player.getSquare(this.hitbox);
				mySquare.setHasBomb(this);
			}
			if((position.x+size/4-3<=mySquare.getCenter().x && position.x+size/4-2>=mySquare.getCenter().x) && (position.y+size/4-3<=mySquare.getCenter().y && position.y+size/4-2>=mySquare.getCenter().y)){	
				int a =mySquare.getArrow();
				if(a!=-1){
					if(a==0){
						this.move.set(0, 1);
					} else 
					if(a==1){
						this.move.set(1, 0);
					} else 
					if(a==2){
						this.move.set(0, -1);
					} else 
					if(a==3){
						this.move.set(-1, 0);
					}
				}
			}
			if(explosionTimer<=0 && !isFlying && !mySquare.hasHole()){
				if(beamTimer<=0){
					this.dead=true;
				}
				if(beams==null){
					mySound.playBombSound(2);
					createBeams();
					exploded=true;
					player.addBeamHitboxes(this.getBeams());
				}
				for(int i=0;i<5;i++){
					beams[i].update(delta);
				}
				this.mySquare.setHasBomb(null);
				beamTimer-=delta;
			}
		}
		if(mySquare.hasHole() && endSquare==null){
			collidesWithWallWhileFlying=false;
			this.hitbox.setSize(0);
			size=0;
			holeCooldown-=delta;
			mySquare.setHasBomb(null);
			if(holeCooldown<0f){
				explosionTimer=0;
				this.hitbox.setSize(mySquare.getSize());
				size=mySquare.getSize();
				holeCooldown=1f;
				isFlying=true;
				Square tempS = mySquare.getRandom(false);
				while(tempS.isHasWall()){
					tempS=mySquare.getRandom(false);
				}
				this.flyToSquare(tempS);
			}
		}			
	}

	private void createBeams() {
		this.hitbox=new MyRectangle();
		hitbox.setMyData(mySquare);
        beams = new Blastbeam[5];
        stateTime=0;
        for(int i=0;i<5;i++){
			beams[i]=new Blastbeam(mySquare,i,mySquare.getSize(),blastRadius,beamTimer);
		}
	}

	public void stopMove(boolean d, Square square) {
		//decide if it should stop
		//if there is square (which means player interaction)
		boolean stop=true;
		if(square!=null && !d){
			int my=this.mySquare.getNumber();
			int block=square.getNumber();
			//check where the player stands and where the bomb moves
			//if its the player that "started the movement dont stop it"
			if(my!=block){
				if(my<block){
					if(my+1==block){
						//stop left
						if(move.x==1){
							stop=false;
						}
						
					} else if(my+19==block){
						//stop down
						if(move.y==1){
							stop=false;
						}
					}
				}
				if(my>block){
					if(my-1==block){
						//stop right
						if(move.x==-1){
							stop=false;
						}
					} else if(my-19==block){
						//stop up
						if(move.y==-1){
							stop=false;
						}
					}
				}
			}
		} 
		//decide if when its flying and hits wall stop
		//or is not flying
		if(isFlying==d && stop&& collidesWithWallWhileFlying){
			move.y=0;
			move.x=0;
		}	
		if(d && collidesWithWallWhileFlying){
			move.y=0;
			move.x=0;
			this.position.z=1;
			isFlying=false;
			mySquare=player.getSquare(hitbox);
	//		this.hitbox.setPosition(mySquare.getPos());
		}
	
		
	}
	
	public void startMove(Square playerSquare) {
		//starts a move from the playerSquare Direction
		int my=this.mySquare.getNumber();
		int player=playerSquare.getNumber();
		if(my!=player){
			if(my<player){
				if(my+1==player){
					//move right
					move.x=1;
					
				} else if(my+19==player){
					//move up
					move.y=1;
				}
			}
			if(my>player){
				if(my-1==player){
					//move left
					move.x=-1;
				} else if(my-19==player){
					//move down
					move.y=-1;
				}
			}
		}
	}

	public void setLifetime(float f) {
		explosionTimer=f;
	}

	public ArrayList<Blastbeam> getBeams() {
		//returns the beams
		ArrayList<Blastbeam> collideable = new ArrayList<Blastbeam>();
		if(beams!=null){
	        for(int i=0;i<5;i++){

	        	collideable.addAll(beams[i].getAll());
	        	
			}
		}
        return collideable;
	}


	public Square getMySquare() {
		return mySquare;
	}

	public void setMySquare(Square mySquare) {
		this.mySquare = mySquare;
	}

	public boolean isFlying() {
		return isFlying;
	}

	public void setFlying(boolean isFlying) {
		this.isFlying = isFlying;
	}

	public void collides(Collideable firstCheck) {
		// decide what happens if it collides with anything but player
		//if its flying only walls can stop it
		//if not than decide what happen
		if(!isFlying){
			if(firstCheck instanceof Box){
				//stop move
				this.stopMove(false,null);
			} else if(firstCheck instanceof Blastbeam){
				//set the lifetime to a short delay
				this.setLifetime(0.05f);
			} 
		} else {
			if(firstCheck instanceof Wall){
				this.stopMove(true,null);
			}
		}
	}
	public void flyToSquare(Square temp) {
		if(mySquare!=temp){
			x=-(Math.abs((mySquare.getPos().dst(temp.getPos())))/mySquare.getSize());
		} else {
			x=-1.5f;
		}
		this.endSquare=temp;
		float x1 = 0;
		float x2 = x/2;
		float x3 = x;
		float y1 = 1;
		float y2 = Math.abs(x)/3;
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

	public boolean isExploded() {
		return exploded;
	}

	public void setExploded() {
		exploded=false;
		
	}
}
