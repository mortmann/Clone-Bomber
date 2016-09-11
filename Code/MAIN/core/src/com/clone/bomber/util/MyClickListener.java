package com.clone.bomber.util;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.clone.bomber.screens.MySelectBox;

public class MyClickListener extends ClickListener {

	protected TextButton myButton;
	protected Image image;
	protected SelectBox<String> selectBox;
	protected MySelectBox mySelectBox;
	public MyClickListener(TextButton myButton){
		this.setMyButton(myButton);
	}

	public MyClickListener(Image image) {
		this.image=image;
	}

	public MyClickListener(SelectBox<String> selectBox) {
		this.selectBox = selectBox;
	}
	public MyClickListener(MySelectBox selectBox) {
		this.mySelectBox = selectBox;
	}
	public TextButton getMyButton() {
		return myButton;
	}

	public void setMyButton(TextButton myButton) {
		this.myButton = myButton;
	}
	
	
}
