package com.clone.bomber.util;

import java.util.ArrayList;
import java.util.Hashtable;

import com.clone.bomber.util.Packets.*;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

public class MyServerListener extends Listener {
	
	private ArrayList<Connection> connections;
	private Network network;
	private boolean started;
	private Hashtable<String,Integer> hash;
	
	public MyServerListener(Network network) {
		this.network=network;
		hash= new Hashtable<String,Integer>();
	}

	public void connected (Connection connection) {
		System.out.println("s "+connection.getRemoteAddressTCP().toString());
		if(connections==null){
			connections=new ArrayList<Connection>();
		} 
		System.out.println(hash.containsKey(connection.getRemoteAddressTCP().toString().split(":")[0]));
		if(hash.containsKey(connection.getRemoteAddressTCP().toString().split(":")[0]) && started){
			Packet2StartMessage m = new Packet2StartMessage();
			m.player=hash.get(connection.getRemoteAddressTCP().toString().split(":")[0]);
			connection.sendTCP(m);
		} else {
			connections.add(connection);
		}
	}

	public void disconnected (Connection connection) {
	}

	public void received (Connection connection, Object object) {
		if(object instanceof Packets.Packet0LoginRequest){
			Packet1LoginAnswer answer = new Packet1LoginAnswer();
			connection.sendTCP(answer);
		} else if(object instanceof Packets.Packet3UpdateMessage){
			int p = ((Packets.Packet3UpdateMessage) object).player;
			int dir = ((Packet3UpdateMessage) object).direction;
			boolean action = ((Packet3UpdateMessage) object).action; 
			network.updatePlayer(p,dir,action);
		}
	}
	public void sendStart(int player){
		this.started=true;
		Packet2StartMessage m = new Packet2StartMessage();
		m.player=player;
		if(connections==null){
			connections=new ArrayList<Connection>();
		} else {
			for(int i = connections.size();i>=0;i--){
				hash.put(connections.get(i).getRemoteAddressTCP().toString().split(":")[0], player);
				connections.get(i).sendTCP(m);
				connections.remove(i);
			}
		}
	}
	public void endRound(){
		if(started){
			started=false;
			connections=new ArrayList<Connection>();
		}
	}

	public void setAlive() {
	}
}
