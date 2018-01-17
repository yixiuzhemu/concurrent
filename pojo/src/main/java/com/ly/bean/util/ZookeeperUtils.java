package com.ly.bean.util;

import org.I0Itec.zkclient.ZkClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ZookeeperUtils {
	private static final Logger logger = LoggerFactory.getLogger(ZookeeperUtils.class);
	/**
	 * 将rootNode 与 key 的路径拼接成一个路径
	 * @param rootNode
	 * @param key
	 * @return
	 */
	public static String getZkPath (String rootNode,String key){
		if(!StringUtils.isEmpty(rootNode)){
			if(key.startsWith("/")){
				key = key.substring(1);
			}
			if(rootNode.endsWith("/")){
				return rootNode + key;
			}
			return rootNode + "/" + key;
		}
		return key;
	}
	
	public static void mkPaths(ZkClient client, String path){
		String[] subs = path.split("\\/");
		if(subs.length<2){
			return ;
		}
		String curPath = "";
		for(int i = 1;i < subs.length; i++){
			curPath = curPath + "/" + subs[i];
			if(!client.exists(curPath)){
				if(logger.isDebugEnabled()){
					logger.debug("Trying to create zk node : {}", curPath);
				}
				client.createPersistent(curPath);
				if(logger.isDebugEnabled()){
					logger.debug("Zk node created successfully: {}",curPath);
				}
			}
		}
	}
}
