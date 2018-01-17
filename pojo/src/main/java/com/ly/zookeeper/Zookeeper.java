package com.ly.zookeeper;

import org.I0Itec.zkclient.ZkClient;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Zookeeper {
	private static volatile ZkClient zkClient;
	
	private static volatile DynamicPropertiesHelperFactory helperFactory;
	
	private static volatile ConfigChangeSubscriber zkConfig;
	
	@SuppressWarnings("resource")
	public static ZkClient getZkClientInstance(){
		if(zkClient == null){
			synchronized (Zookeeper.class) {
				if(zkClient == null){
					ClassPathXmlApplicationContext cpxac = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-zookeeper.xml");
					zkClient = cpxac.getBean(ZkClient.class);
				}
			}
		}
		return zkClient;
	}
	@SuppressWarnings("resource")
	public static DynamicPropertiesHelperFactory getHelperFactoryInstance(){
		if(helperFactory == null){
			synchronized (Zookeeper.class) {
				if(helperFactory == null){
					ClassPathXmlApplicationContext cpxac = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-zookeeper.xml");
					helperFactory = cpxac.getBean(DynamicPropertiesHelperFactory.class);
				}
			}
		}
		return helperFactory;
	}
	
	@SuppressWarnings("resource")
	public static ConfigChangeSubscriber getZkConfigInstance(){
		if(zkConfig == null){
			synchronized (Zookeeper.class) {
				if(zkConfig == null){
					ClassPathXmlApplicationContext cpxac = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-zookeeper.xml");
					zkConfig = cpxac.getBean(ConfigChangeSubscriber.class);
				}
			}
		}
		return zkConfig;
	}
}
