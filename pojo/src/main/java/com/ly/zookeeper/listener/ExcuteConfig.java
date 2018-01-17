package com.ly.zookeeper.listener;

import java.util.concurrent.Future;

public class ExcuteConfig implements Runnable {
	private String path;
	private String savePath;
	public ExcuteConfig(String path,String savePath) {
		this.path = path;
		this.savePath = savePath;
	}
	@Override
	public void run() {
		
		try {
			PropertiesListener.listenerProperties(path,savePath);
			wait();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
