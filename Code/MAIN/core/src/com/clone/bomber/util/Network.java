package com.clone.bomber.util;

import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Server;

public class Network {
	private Server server;
	private MyServerListener listener;
	private int[] playerUpdates;
	private boolean[] playerAction;
	private boolean bind;
	
	public Network(){

		server = new Server();
		registerPackets();
		playerUpdates = new int[8];
		playerAction = new boolean[8];
		for (int j = 0; j < playerUpdates.length; j++) {
			playerUpdates[j]=-1;
		}
		listener = new MyServerListener(this);
		server.addListener(listener);
		int tcpPort = Gdx.app.getPreferences("clonebomber").getInteger("TCPPort",54555);
		try {
			server.bind(tcpPort);
			setBind(true);
		} catch (IOException e) {
			setBind(false);
			e.printStackTrace();
		}
		server.start();

	}
	private void registerPackets() {
		Kryo kryo =server.getKryo();
		kryo.register(Packets.Packet0LoginRequest.class);
		kryo.register(Packets.Packet1LoginAnswer.class);
		kryo.register(Packets.Packet2StartMessage.class);
		kryo.register(Packets.Packet3UpdateMessage.class);
	}
	public void sendStartMessage(int player) {

		listener.sendStart(player);
	}
	public void updatePlayer(int p, int dir, boolean action) {
		if(p!=-1){
			playerUpdates[p] = dir;
			playerAction[p]=action;
		}
	}
	public int[] getDirUpdates(){
		return playerUpdates;
	}
	public void setUpdates(){
		for (int j = 0; j < playerUpdates.length; j++) {
			playerUpdates[j]=-1;
		}
	}
	public void closeServer() {
		server.close();
	}

	public boolean[] getPlayerAction() {
		return playerAction;
	}
	public void setPlayerAction(boolean[] playerAction) {
		this.playerAction = playerAction;
	}
	public void endRound(){
		
		listener.endRound();
	}
	public boolean isBind() {
		return bind;
	}
	public void setBind(boolean bind) {
		this.bind = bind;
	}
	public void setAlive(){
		listener.setAlive();
	}
	public Server getServer() {
		return server;
	}
}
