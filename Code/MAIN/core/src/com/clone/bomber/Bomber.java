package com.clone.bomber;

import java.awt.Frame;


import javax.swing.JOptionPane;
import javax.swing.UIManager;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;

public class Bomber extends ApplicationAdapter {
	private GameClass gameClass = null;
	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		if(gameClass==null){
			gameClass = new GameClass();
			gameClass.create();
		}
		try {
			gameClass.render();
		} 
		catch (Exception e){
			gameClass.getNet().closeServer();
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch(Exception f) {
				f.printStackTrace();
			}
			String stracktrace="";
			for(StackTraceElement s : e.getStackTrace()){
				stracktrace+=s.toString() +"\n";
			}
			String dumpName = "Crashdump_"+e.hashCode() + "_"+((int)(Math.random()*100)) + ".dump";
			String reason = "Reason is ";
			if(e.getCause()==null){
				reason += "unkown";
			} else {
				reason +=e.getCause();
			}
			JOptionPane.showMessageDialog(new Frame(), "Stracktrace:\n"+stracktrace+reason+"\nCrashdump saved: "+dumpName,"CRASH " + e.getMessage() +"!",JOptionPane.ERROR_MESSAGE );
			System.out.println("CRASH " + e.getMessage());
			FileHandle file = Gdx.files.local(dumpName);
			if(e.getCause()!=null){
				file.writeString(e.getCause().toString(), true);
			}
			if(e.getLocalizedMessage()!=null)
				file.writeString(e.getLocalizedMessage(), true);
			file.writeString(e.getClass().getName(), true);
			file.writeString(e.toString(), true);
			file.writeString(stracktrace, true);
			if(e.getMessage()!=null)
				file.writeString(e.getMessage(), true);
			e.printStackTrace();
			Gdx.app.exit();
		} 
			

	}
}
