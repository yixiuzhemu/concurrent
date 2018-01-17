package com.ly.zookeeper.listener;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.I0Itec.zkclient.ZkClient;
import org.junit.Test;

import com.ly.zookeeper.ConfigChangeListener;
import com.ly.zookeeper.Constant;
import com.ly.zookeeper.DynamicPropertiesHelper;
import com.ly.zookeeper.DynamicPropertiesHelperFactory;
import com.ly.zookeeper.Zookeeper;
import com.ly.zookeeper.ZookeeperPublisher;
import com.ly.zookeeper.impl.ZkConfigChangeSubscriberImpl;

public class PublisherTest {

	@Test
	public void test() {
		String path = "/resource/database-config";
		String savePath = "E:/properties";
//		PropertiesListener.listenerProperties("/resource/database-config","E:/properties");
		ExcuteConfig excuteConfig = new ExcuteConfig(path, savePath);
		Thread config = new Thread(excuteConfig);
		ExcuteSave excuteSave = new ExcuteSave(path);
		Thread save = new Thread(excuteSave);
		config.start();
		save.start();
//			try {
//				System.in.read();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
	}
	@Test
	public void test1() {
		ZkClient zkClient = Zookeeper.getZkClientInstance();
		zkClient.writeData("/resource/database-config/zkconfig.properties", "aa=2");
			PropertiesListener.addChangeConfigMessage("/resource/database-config/zkconfig.properties", "aa=2");
	}

}
