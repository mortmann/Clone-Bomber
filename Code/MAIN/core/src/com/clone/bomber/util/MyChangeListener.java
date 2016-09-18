package com.clone.bomber.util;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public abstract class MyChangeListener extends ChangeListener {

		protected TextButton myButton;
		protected Image image;
		protected SelectBox<String> selectBox;
		protected MySelectBox mySelectBox;
		public MyChangeListener(TextButton myButton){
			this.setMyButton(myButton);
		}

		public MyChangeListener(Image image) {
			this.image=image;
		}

		public MyChangeListener(SelectBox<String> selectBox) {
			this.selectBox = selectBox;
		}
		public MyChangeListener(MySelectBox selectBox) {
			this.mySelectBox = selectBox;
		}
		public TextButton getMyButton() {
			return myButton;
		}

		public void setMyButton(TextButton myButton) {
			this.myButton = myButton;
		}


}
