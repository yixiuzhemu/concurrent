package com.ly.zookeeper.impl;

import java.util.List;


import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.apache.commons.lang3.StringUtils;

import com.ly.bean.util.ZookeeperUtils;
import com.ly.zookeeper.ConfigChangeListener;
import com.ly.zookeeper.ConfigChangeSubscriber;

public class ZkConfigChangeSubscriberImpl implements ConfigChangeSubscriber{
	
	private ZkClient zkClient;
	private String rootNode;
	
	public ZkConfigChangeSubscriberImpl(ZkClient zkClient, String rootNode) {
		super();
		this.zkClient = zkClient;
		this.rootNode = rootNode;
	}
	
	
	@Override
	public String readData(String key) {
		String path = ZookeeperUtils.getZkPath(this.rootNode, key);
		return (String)this.zkClient.readData(path);
	}

	@Override
	public void subscribe(String key, ConfigChangeListener listener) {
		// TODO Auto-generated method stub
		String path = ZookeeperUtils.getZkPath(this.rootNode, key);
		if(!this.zkClient.exists(path)){
			throw new RuntimeException("配置（"+path+"）不存在，必须先定义配置才能监听配置");
		}
		this.zkClient.subscribeDataChanges(path, new DataListenerAdapter(listener));
	}

	@Override
	public List<String> listKeys() {
		// TODO Auto-generated method stub
		return this.zkClient.getChildren(this.rootNode);
	}
	
	private String getKey(String path){
		String key = path;
		if(!StringUtils.isEmpty(this.rootNode)){
			key = path.replaceFirst(this.rootNode, "");
			if(key.startsWith("/")){
				key = key.substring(1);
			}
		}
		return key;
	}
	/**
	 * 数据监听器适配类，当zk数据变化时，触发ConfigChangeListener
	 * @author ly
	 *
	 */
	private class DataListenerAdapter implements IZkDataListener{
		private ConfigChangeListener configListener;
		
		public DataListenerAdapter(ConfigChangeListener configListener){
			this.configListener = configListener;
		}
		
		public void handleDataChange(String path,Object value) throws Exception{
			configListener.change(getKey(path), value);
		}
		
		public void handleDataDeleted(String s) throws Exception{
			
		}
	}
}
