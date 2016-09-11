package com.clone.bomber.util;

import java.io.Serializable;

public class Collideable implements Serializable{
	private static final long serialVersionUID = -5996662927198100929L;
	protected MyRectangle hitbox; 

	public Object getUserData() {
		return this;
	}
	public MyRectangle getHitbox() {
		return hitbox;
	}
	public void setHitbox(MyRectangle hitbox) {
		this.hitbox = hitbox;
	}
	
}
