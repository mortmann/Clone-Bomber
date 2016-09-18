package com.clone.bomber.map;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;


public class MapManager {
	private String mapName = "";
	private static String path="maps/";
	private Map map;

	public MapManager(){
		if(!Gdx.files.local(path).exists()){
			Gdx.files.local(path).mkdirs();
		}
	}
	public MapManager( Map map){
		this.map=map;
		path="maps/"+mapName;
	}
	
	public static void createMap(Map map, String mapName){
		ObjectOutputStream out = null;
		if( !Gdx.files.local(path + mapName + ".dat").exists()){
			Gdx.files.local(path).mkdirs();
		}
		//"/custom/"
		FileHandle f = Gdx.files.local("./" + path + "/custom/" + Integer.toString(map.getPlayerNumber())+"-"+ mapName +".dat");
		try{
			out = new ObjectOutputStream(new FileOutputStream(f.path())); 
			
	        out.writeObject(map);
	        out.close();
	    }catch(Exception e){
	        e.printStackTrace();
	    }finally {
		try {
			if(out!=null)
				out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	}
	public Map loadMap(String selected) {	
		if(selected!=null){
			FileHandle f = Gdx.files.local(path + selected + ".dat");
			ObjectInputStream ois = null;
			try{
//				if(f.exists()==false){
//					return new Map(17);
//				}
				FileInputStream fis = new FileInputStream(f.path());
				ois = new ObjectInputStream(fis);
				map=(Map) ois.readObject();
		        ois.close();
		    }catch(Exception e){
		        e.printStackTrace();
		    }finally {
				try {
					if(ois!=null)
						ois.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
		} else {
			map=new Map(17);
		}
		return map;
	}
	public void setMapName(String selected) {
		this.mapName=null;
		this.mapName=selected;
	}
	public String getDescription() {
		FileHandle f = Gdx.files.local(path + mapName + ".dat");
		ObjectInputStream ois = null;
		try{
			ois = new ObjectInputStream(new FileInputStream(f.path()));
			map=(Map) ois.readObject();
	        ois.close();
	    }catch(Exception e){
	        e.printStackTrace();
	    }finally {
			try {
				if(ois!=null)
					ois.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		return Integer.toString(map.getMapSize());
	}
}
