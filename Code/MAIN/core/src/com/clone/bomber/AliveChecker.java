package com.clone.bomber;

import com.esotericsoftware.kryonet.Server;

public class AliveChecker implements Runnable {
	private Thread thread;
	private Server server;
	public AliveChecker(Thread thread,Server server){
		this.thread=thread;
		this.server=server;
		
	}
	@Override
	public void run() {
		while(thread.isAlive()){
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		server.close();
		
	}

}
