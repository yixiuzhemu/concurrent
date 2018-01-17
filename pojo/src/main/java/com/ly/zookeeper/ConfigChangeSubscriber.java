package com.ly.zookeeper;

import java.util.List;

/**
 * 配置改变的订阅者，在每一个zk文件上订阅一个监听器
 * @author ly
 *
 */
public interface ConfigChangeSubscriber {
	/**
	 * 获取子节点
	 * @param key
	 * @return
	 */
	public abstract String readData(String key);
	/**
	 * 订阅
	 * @param key
	 * @param listener
	 */
	public abstract void subscribe(String key,ConfigChangeListener listener);
	/**
	 * 获取ROOT所有的keys
	 * @return
	 */
	public abstract List<String> listKeys();
	
}
