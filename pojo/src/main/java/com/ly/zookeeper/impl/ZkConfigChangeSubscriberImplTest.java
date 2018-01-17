package com.ly.zookeeper.impl;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import junit.framework.TestCase;

import org.I0Itec.zkclient.ZkClient;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.ly.bean.util.ZookeeperUtils;
import com.ly.zookeeper.ConfigChangeListener;
import com.ly.zookeeper.ConfigChangeSubscriber;

public class ZkConfigChangeSubscriberImplTest extends TestCase {

	private ZkClient zkClient;
	ConfigChangeSubscriber zkConfig;

	public void setUp() {
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(
				"classpath:spring/applicationContext-zookeeper.xml");
		this.zkClient = ((ZkClient) ctx.getBean("zkClient"));
		this.zkConfig = ((ConfigChangeSubscriber) ctx
				.getBean("configChangeSubscriber"));
		//ZookeeperUtils.mkPaths(this.zkClient, "/zkSample/conf");
		if (!this.zkClient.exists("/zkSample/conf/test1.properties"))
			ZookeeperUtils.mkPaths(this.zkClient, "/zkSample/conf/test1.properties");

		if (!this.zkClient.exists("/zkSample/conf/test2.properties"))
			ZookeeperUtils.mkPaths(this.zkClient, "/zkSample/conf/test2.properties");
	}

	
	public void testSubscribe() throws IOException, InterruptedException {
		final CountDownLatch latch = new CountDownLatch(1);
		this.zkConfig.subscribe("test1.properties", new ConfigChangeListener() {
			public void change(String key, Object value) {
				System.out.println("test1接收到数据变更通知: key=" + key + ", value="
						+ value);
				latch.countDown();
			}
		});
		this.zkClient.writeData("/zkSample/conf/test1.properties", "aa=1");
		this.zkConfig.subscribe("test2.properties", new ConfigChangeListener() {
			public void change(String key, Object value) {
				System.out.println("test2接收到数据变更通知: key=" + key + ", value="
						+ value);
				latch.countDown();
			}
		});
		
		this.zkClient.writeData("/zkSample/conf/test2.properties", "bb=1");
		boolean notified = latch.await(30L, TimeUnit.SECONDS);
		if (!notified)
			fail("客户端没有收到变更通知");
	}

	public void testA() throws InterruptedException {
		List<String> list = this.zkClient.getChildren("/zkSample/conf");
		for (String s : list) {
			System.out.println("children:" + s);
		}

	}

	public void testB() throws InterruptedException {
		this.zkClient.writeData("/zkSample/conf/test2.properties", "test=123");
		System.out.println(this.zkClient.readData("/zkSample/conf/test2.properties"));

	}

	/*public void tearDown() {
		this.zkClient.delete("/zkSample/conf/test1.properties");
		this.zkClient.delete("/zkSample/conf/test2.properties");
	}*/
	
	
}


