package com.clone.bomber.util;

import java.io.Serializable;

import com.badlogic.gdx.math.Rectangle;

public class MyRectangle extends Rectangle implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5631172563036983562L;
	private Object myData;
	
	
	public MyRectangle (float x, float y, float width, float height, Object myData) {
		this.setMyData(myData);
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	public MyRectangle (float x, float y, float width, float height) {
		this.setMyData(myData);
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	public MyRectangle(){
	}

	public Object getMyData() {
		return myData;
	}


	public void setMyData(Object myData) {
		this.myData = myData;
	}

	
	
}
