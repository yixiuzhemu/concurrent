package com.ly.zookeeper;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class DynamicPropertiesHelperFactory {
	private ConfigChangeSubscriber configChangeSubscriber;
	
	private ConcurrentHashMap<String,DynamicPropertiesHelper> helpers = new ConcurrentHashMap<>();
	
	public DynamicPropertiesHelperFactory(ConfigChangeSubscriber configChangerSubscriber){
		this.configChangeSubscriber = configChangerSubscriber;
	}
	/**
	 * 获取DynamicPropertiesHelper 如：
	 * @param key
	 * @return
	 */
	public DynamicPropertiesHelper getHelper(String key){
		DynamicPropertiesHelper helper = this.helpers.get(key);
		if(helper != null){
			return helper;
		}
		return createHelper(key);
	}
	
	private DynamicPropertiesHelper createHelper(String key){
		/**
		 * 获取zookeeper所有的key 信息
		 */
		List<String> keys = this.configChangeSubscriber.listKeys();
		if(keys == null || keys.size()  == 0){
			return null;
		}
		if(!keys.contains(key)){
			return null;
		}
		/*
		 * 获取节点的内容
		 */
		String initValue = this.configChangeSubscriber.readData(key);
		
		final DynamicPropertiesHelper helper = new DynamicPropertiesHelper(initValue);
		//从Zookeeper 获取该节点内容,并且设置本地的PropertiesHelperMap 上,如果设置成功说明本地已经注册过了，没有重新注册并且订阅Wather事件
		DynamicPropertiesHelper old = this.helpers.putIfAbsent(key, helper);
	
		if(old != null){
			return old;
		}
		
		this.configChangeSubscriber.subscribe(key, new ConfigChangeListener() {
			
			@Override
			public void change(String p1, Object value) {
				System.out.println("key=======================p1="+p1+"  value="+value);
				helper.refresh((String)value);
			}
		});
		return helper;
	}
	
	
	
}
