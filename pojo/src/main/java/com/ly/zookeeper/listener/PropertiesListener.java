package com.ly.zookeeper.listener;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.I0Itec.zkclient.ZkClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import com.ly.bean.redis.Redis;
import com.ly.zookeeper.ConfigChangeListener;
import com.ly.zookeeper.Constant;
import com.ly.zookeeper.DynamicPropertiesHelper;
import com.ly.zookeeper.DynamicPropertiesHelperFactory;
import com.ly.zookeeper.PropertyChangeListener;
import com.ly.zookeeper.ZookeeperConfig;
import com.ly.zookeeper.ZookeeperInstance;
import com.ly.zookeeper.ZookeeperPublisher;
import com.ly.zookeeper.impl.ZkConfigChangeSubscriberImpl;


public class PropertiesListener {
	public static void listenerProperties(String path,String savePath){
		//获取zkClient实例对象
		ZkClient zkClient = ZookeeperInstance.getZkClientInstance();
		//创建订阅者的监听器
		ZkConfigChangeSubscriberImpl changeSubscriberImpl = new ZkConfigChangeSubscriberImpl(zkClient, path);
		//获取当前nodeName下的所有配置文件
		List<String> listKeys = changeSubscriberImpl.listKeys();
		CountDownLatch latch = new CountDownLatch(1);
		for (String key : listKeys) {
			changeSubscriberImpl.subscribe(key , new ConfigChangeListener() {
				@Override
				public void change(String p1, Object value) {
					System.out.println("接收到数据变更通知: key=" + p1 + ", value="
							+ value);
					PropertiesListener.getConfigs(path,savePath);
					latch.countDown();
				}
			});
		}
		try {
			System.in.read();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void registProperties(String key){
		//获取动态属性工厂实例
		DynamicPropertiesHelperFactory helperFactory = ZookeeperInstance.getHelperFactoryInstance();
		DynamicPropertiesHelper helper = helperFactory.getHelper(key);
		helper.registerListener(key, new PropertyChangeListener() {
			@Override
			public void propertyChnaged(String oldValue, String newValue) {
				System.out.println("property chaged: oldValue="
						+ oldValue + ", newValue=" + newValue);
			}
		});

	}
	/**
	 * 将指定文件夹下的文件发布到zookeeper
	 * @param filePath
	 */
	public static void publisherProperties(String filePath){
		ZkClient zkClient = ZookeeperInstance.getZkClientInstance();
		File file = new File(filePath);
		ZookeeperPublisher.publishConfig(zkClient, Constant.ZK_CONFIG_ROOTNODE, file);
	}
	/**
	 * 将配置文件缓存到本地
	 * @param path
	 */
	public static void getConfigs(String path,String savePath){
		ZkClient zkClient = ZookeeperInstance.getZkClientInstance();
		File file = new File(savePath);
		ZookeeperConfig.saveConfigs(zkClient, path, file);
	}

	public static void addChangeConfigMessage(String path,String content){
		StringRedisTemplate template = Redis.getRedisTemplateInstance();
		ValueOperations<String, String> forValue = template.opsForValue();
		forValue.set("zookeeper:message:"+path, content);
	}
	
	public static void excuteZookeeperMessage(String path){
		StringRedisTemplate template = Redis.getRedisTemplateInstance();
		ZkClient zkClient = ZookeeperInstance.getZkClientInstance();
		ValueOperations<String, String> forValue = template.opsForValue();
		String string = forValue.get("zookeeper:message:"+path);
		if(!StringUtils.isEmpty(string)){
			zkClient.writeData(path, string);
		}
	}
}