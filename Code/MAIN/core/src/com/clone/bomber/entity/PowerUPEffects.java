package com.clone.bomber.entity;

public enum PowerUPEffects {
	speed, blastradius, bomb, throwable, 
	push, joint, diarrhea, superspeed;
	public static boolean IsNegativEffect(PowerUPEffects e){
		if(e == joint|| e == diarrhea||e == PowerUPEffects.superspeed){
			return true;
		}
		return false;
	}
	public boolean IsNegativ(){
		return IsNegativEffect(this);
	}
};
