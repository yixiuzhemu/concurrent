package com.ly.module2.test;

import com.ly.zookeeper.ZookeeperConfig;
import com.ly.zookeeper.listener.PropertiesListener;


public class Test {

	@org.junit.Test
	public void test() {
		
		PropertiesListener.publisherProperties("E://resource");
	}
	

	@org.junit.Test
	public void test1() {
		
	}
	
}
