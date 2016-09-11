package com.clonebomber.controller;


import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

public class MyNetworkListener extends Listener {
	private Client client;
	private Main main;

	public void init(Client client, Main main) {
		this.main=main;
		this.client=client;
	}
	
	public void connected (Connection connection) {
		client.sendTCP(new Packets.Packet0LoginRequest());
	}

	public void disconnected (Connection connection) {
		main.setPlayer(-1);
		main.setConnected(false);
		main.showDisconnected();
	}

	public void received (Connection connection, Object object) {
		if(object instanceof Packets.Packet2StartMessage){
			main.setPlayer(((Packets.Packet2StartMessage) object).player);
		} else if(object instanceof Packets.Packet1LoginAnswer){
			main.setConnected(true);
		}
	}

	
	
}
