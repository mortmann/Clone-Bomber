package com.clonebomber.controller;

import java.io.IOException;
import com.clonebomber.controller.Packets.*;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;

public class MyClient {
	private Client client;
	private Main main;
	public MyClient(Main main){
		this.main=main;
		MyNetworkListener nl = new MyNetworkListener();
		client= new Client();
		nl.init(client, main);
		client.addListener(nl);
		registerPackets();
		

		
	}
	private void registerPackets() {
		Kryo kryo = client.getKryo();
		kryo.register(Packets.Packet0LoginRequest.class);
		kryo.register(Packets.Packet1LoginAnswer.class);
		kryo.register(Packets.Packet2StartMessage.class);
		kryo.register(Packets.Packet3UpdateMessage.class);
	}
	
	public void sendMessage(int player, int dir, boolean action){
		Packet3UpdateMessage m = new Packet3UpdateMessage();
		m.player = player;
		m.direction = dir;
		m.action = action;
		client.sendTCP(m);
	}
	public void connect(String text) {
		System.out.println("connect to " + text);
		client.start();
		try {
			client.connect(60000, text, 54555);
		} catch (IOException e) {
			main.showFailedConnect();
			client.stop();
		}
		
	}
	
}
