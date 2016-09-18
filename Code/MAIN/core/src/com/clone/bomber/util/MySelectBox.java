package com.clone.bomber.util;

import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class MySelectBox extends SelectBox<String> {
	private boolean opened=true;
	public MySelectBox(Skin skin) {
		super(skin);
	}
	public boolean isOpened() {
		return opened;
	}
	public void setOpened(boolean opened) {
		this.opened = opened;
	}

}
