package com.ly.zookeeper.impl;

import junit.framework.TestCase;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.ly.zookeeper.DynamicPropertiesHelper;
import com.ly.zookeeper.DynamicPropertiesHelperFactory;

public class ReadPropertiesTest extends TestCase {

	private DynamicPropertiesHelperFactory helperFactory;
	
	protected void setUp() throws Exception {
		super.setUp();
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(
				"classpath:spring/applicationContext-zookeeper.xml");
		this.helperFactory = ((DynamicPropertiesHelperFactory) ctx
				.getBean(DynamicPropertiesHelperFactory.class));
	}
	
	

	public void testReadProperties() throws InterruptedException {
		DynamicPropertiesHelper helper = this.helperFactory.getHelper("test2.properties");

		while (true) {
			System.out.println(helper.getProperty("bb"));
			Thread.sleep(5000L);
		}

	}
	
	
}
