package com.ly.zookeeper;
/**
 * 配置事件监听
 * @author ly
 *
 */
public interface ConfigChangeListener {
	/**
	 * 监听配置文件是否有变更，如果有，则会出发change方法
	 * @param p1
	 * @param value
	 */
	public abstract void change(String p1,Object value);
}
