package com.ly.zookeeper.listener;

public class ExcuteSave implements Runnable {
	private String path;
	public ExcuteSave(String path) {
		this.path = path;
	}
	@Override
	public void run() {
		PropertiesListener.excuteZookeeperMessage(path);
		notifyAll();
	}

}
