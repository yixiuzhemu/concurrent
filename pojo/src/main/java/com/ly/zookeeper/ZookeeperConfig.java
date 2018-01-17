package com.ly.zookeeper;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Properties;

import org.I0Itec.zkclient.ZkClient;
import org.apache.commons.io.FileUtils;

public class ZookeeperConfig {
	/**
	 * 获取zookeeper的配置文件，根据配置文件获取相关信息，但是配置在常量类中，可以直接修改常量类（但是改配置文件对于上线版本会方便一点）
	 */
	public static final void loadProperties(){
		InputStream is = ZookeeperConfig.class.getResourceAsStream("/zookeeper/zkconfig.properties");
		if(is == null){
			throw new RuntimeException("找不到config.properties文件");
		}
		Properties properties = new Properties();
		try {
			properties.load(new BufferedReader(new InputStreamReader(is)));
		} catch (UnsupportedEncodingException e) {
			// TODO: handle exception
			throw new RuntimeException(e);
		}catch(IOException e){
			throw new RuntimeException(e);
		}
		Constant.ZK_CONFIG_ROOTNODE = properties.getProperty("ZK_CONFIG_ROOTNODE");
		Constant.ZK_CONF_ENCODING = properties.getProperty("ZK_CONF_ENCODING");
		Constant.ZK_TIMEOUT = Integer.parseInt(properties.getProperty("ZK_TIMEOUT"));
		Constant.ZK_ADDRESS = properties.getProperty("ZK_ADDRESS");
	}
	/**
	 * 将zookeeper中的配置文件保存到本地
	 * @param client
	 * @param rootNode
	 * @param confDir
	 */
	public static void saveConfigs(ZkClient client,String rootNode,File confDir){
		List<String> configs = client.getChildren(rootNode);
		for (String config : configs) {
			String  content =(String)client.readData(rootNode+"/"+config);
			File confFile = new File(confDir,config);
			try {
				FileUtils.writeStringToFile(confFile, content, "UTF-8");
			} catch (IOException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			System.out.println("配置成功保存到本地:"+confFile.getAbsolutePath());
		}
	}
}
