package com.ly.zookeeper.listener;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.springframework.data.redis.connection.ClusterInfo;
import org.springframework.data.redis.connection.RedisClusterConnection;
import org.springframework.data.redis.connection.RedisClusterNode;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.types.RedisClientInfo;

import com.ly.bean.redis.Redis;

public class PublisherTest {

	@Test
	public void test() {
//		String path = "/resource/database-config";
//		String savePath = "E:/properties";
		
//			try {
//				System.in.read();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		 //zk集群的地址  
       
	}
    @Test
    public void test1(){
    	String path = "/resource/database-config";
    	String addPath  = "C:/Users/lenovo/Documents/GitHub/concurrent/pojo/src/main/resources/resources/redis.properties";
    	File file = new File(addPath);
    	String key = addPath.substring(addPath.lastIndexOf("/")+1);
    	try {
			PropertiesListener.addChangeConfigMessage(path+"/"+key,FileUtils.readFileToString(file));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
  @Test
  public void test2(){
	  StringRedisTemplate redisTemplateInstance = Redis.getRedisTemplateInstance();
	  RedisConnectionFactory requiredConnectionFactory = redisTemplateInstance.getRequiredConnectionFactory();
	  RedisClusterConnection clusterConnection = requiredConnectionFactory.getClusterConnection();
	  ClusterInfo clusterGetClusterInfo = clusterConnection.clusterGetClusterInfo();
	  Map<RedisClusterNode, Collection<RedisClusterNode>> clusterGetMasterSlaveMap = clusterConnection.clusterGetMasterSlaveMap();
	  for (Map.Entry<RedisClusterNode, Collection<RedisClusterNode>> map : clusterGetMasterSlaveMap.entrySet()) {
		Collection<RedisClusterNode> value = map.getValue();
		Iterator<RedisClusterNode> iterator = value.iterator();
		while (iterator.hasNext()) {
			RedisClusterNode next = iterator.next();
			List<RedisClientInfo> clientList = clusterConnection.getClientList(next);
			System.out.println(clientList);
			
		}
	}
  }
}
