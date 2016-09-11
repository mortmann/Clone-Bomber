package com.clonebomber.controller;

public class Packets {
	public static class Packet0LoginRequest { }
	public static class Packet1LoginAnswer { }
	public static class Packet2StartMessage { int player; }
	public static class Packet3UpdateMessage {int player; int direction;boolean action;}
}
