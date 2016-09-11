package com.clone.bomber.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class TextureManager {
	private transient Texture squareGroundTex;
	private transient TextureRegion sqauerModifierHole;
	private transient TextureRegion sqauerModifierArrow;
	private transient TextureRegion sqauerModifierIce;
	private Texture boxTexture;
	private Texture wallTexture;
	private Texture spawnSpotTexture;
	private Texture randomBoxTexture;
	private Texture speedPowerUPtexture;
	private Texture blastradiusPowerUPtexture;
	private Texture bombPowerUPtexture;
	private Texture throwPowerUPtexture;
	private Texture pushPowerUPtexture;
	private Texture jointPowerUPtexture;
	private Texture diarrheaPowerUPtexture;
	private Texture superspeedPowerUPtexture;
	private Texture suddendeathTexture;

	public TextureManager(){
		String groundRegName = "res/maptexture/floor_" +Gdx.app.getPreferences("clonebomber").getString("Mapstyle", "normal").toLowerCase()+".png";
		squareGroundTex = new Texture(groundRegName);
		sqauerModifierIce= new TextureRegion(new Texture("res/maptexture/ice.png"));
		sqauerModifierArrow= new TextureRegion(new Texture("res/maptexture/arrow.png"));
		sqauerModifierHole= new TextureRegion(new Texture("res/maptexture/hole.png"));
		String boxTextureName = "res/maptexture/box_" +Gdx.app.getPreferences("clonebomber").getString("Mapstyle", "normal").toLowerCase()+".png";
		boxTexture=new Texture(boxTextureName);
		String wallTextureName="res/maptexture/wall_" +Gdx.app.getPreferences("clonebomber").getString("Mapstyle", "normal").toLowerCase()+".png";
		wallTexture=(new Texture(wallTextureName));
		spawnSpotTexture=new Texture("res/maptexture/spawn.png");
		randomBoxTexture=new Texture("res/maptexture/randomField.png");
		speedPowerUPtexture=new Texture("res/assets/speed_PowerUP.png");
		blastradiusPowerUPtexture=new Texture("res/assets/blastradius_PowerUP.png");
		bombPowerUPtexture=new Texture("res/assets/bomb_PowerUP.png");
		throwPowerUPtexture=new Texture("res/assets/throw_PowerUP.png");
		pushPowerUPtexture=new Texture("res/assets/push_PowerUP.png");
		jointPowerUPtexture=new Texture("res/assets/joint_PowerUP.png");
		diarrheaPowerUPtexture=new Texture("res/assets/diarrhea_PowerUP.png");
		superspeedPowerUPtexture=new Texture("res/assets/superspeed_PowerUP.png");
		suddendeathTexture=new Texture("res/assets/suddenDeath.png");
	}
	public TextureRegion getSqauerModifierReg(String name) {
		if(name.contains("Ice")){
			return sqauerModifierIce;
		} else if(name.contains("Hole")){
			return sqauerModifierHole;
		} else if(name.contains("arrow")){
			return sqauerModifierArrow;
		}
		return null;
	}
	public Texture getSquareGroundTex() {
		return squareGroundTex;
	}
	public Texture getBoxTexture() {
		return boxTexture;
	}
	public Texture getWallTexture() {
		return wallTexture;
	}

	public Texture getSpawnSpotTexture() {
		return spawnSpotTexture;
	}
	public Texture getRandomBoxTexture() {
		return randomBoxTexture;
	}
	public void disposeAll(){
		squareGroundTex.dispose();
		sqauerModifierIce.getTexture().dispose();
		sqauerModifierHole.getTexture().dispose();
		sqauerModifierArrow.getTexture().dispose();
		boxTexture.dispose();
		wallTexture.dispose();
		spawnSpotTexture.dispose();
		randomBoxTexture.dispose();
		speedPowerUPtexture.dispose();
		blastradiusPowerUPtexture.dispose();
		bombPowerUPtexture.dispose();
		throwPowerUPtexture.dispose();
		pushPowerUPtexture.dispose();
		jointPowerUPtexture.dispose();
		diarrheaPowerUPtexture.dispose();
		superspeedPowerUPtexture.dispose();
		suddendeathTexture.dispose();
	}
	public Texture getPowerUp(String name) {
		if(name.contains("speed")){
			return speedPowerUPtexture;
		} else if(name.contains("blastradius")){
			return blastradiusPowerUPtexture;
		} else if(name.contains("bomb")){
			return bombPowerUPtexture;
		} else if(name.contains("throw")){
			return throwPowerUPtexture;
		} else if(name.contains("push")){
			return pushPowerUPtexture;
		} else if(name.contains("joint")){
			return jointPowerUPtexture;
		} else if(name.contains("diarrhea")){
			return diarrheaPowerUPtexture;
		} else if(name.contains("superspeed")){
			return superspeedPowerUPtexture;
		}
		return null;
	}
	public Texture getSuddendeath() {
		return suddendeathTexture;
	}
	

}
