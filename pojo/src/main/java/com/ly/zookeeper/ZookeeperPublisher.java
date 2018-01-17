package com.ly.zookeeper;

import java.io.File;
import java.io.IOException;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;
import org.apache.commons.io.FileUtils;

import com.ly.bean.util.ZookeeperUtils;

public class ZookeeperPublisher {
	public static void publishConfig(ZkClient client,String rootNode,File confDir){
		File[] confs = confDir.listFiles();
		int success = 0;
		int failed = 0;
		for (File file : confs) {
			if(!file.isFile()){
				continue;
			}
			String name = file.getName();
			String path = ZookeeperUtils.getZkPath(rootNode, name);
			ZookeeperUtils.mkPaths(client, path);
			String content;
			try{
				content = FileUtils.readFileToString(file,"UTF-8");
			}catch(IOException e){
				System.err.println("错误：读取文件内容时遇到异常:"+e.getMessage());
				failed++;
				continue;
			}
			if(!client.exists(path)){
				try {
					client.createPersistent(path);
					client.writeData(path, content);
				} catch (Throwable e) {
					// TODO: handle exception
					System.err.println("错误：尝试发布配置失败："+e.getMessage());
					failed++;
					continue;
				}
				System.out.println("提示：已经成功将配置文件"+file+"内容发布为新的ZK配置"+path);
			}else{
				try {
					client.writeData(path, content);
				} catch (Throwable e) {
					// TODO: handle exception
					System.err.println("错误：尝试发布配置失败："+e.getMessage());
					failed++;
					continue;
				}
				System.out.println("提示：已经成功将配置文件"+file+"内容更新到ZK配置"+path);
			}
			success++;
		}
		System.out.println("提示: 完成配置发布，成功" + success + "，失败" + failed + "。");
	}
	
	public static void main(String[] args) {
		ZookeeperConfig.loadProperties();
		ZkClient client = new ZkClient(Constant.ZK_ADDRESS);
		client.setZkSerializer(new SerializableSerializer());
		
		File confDir = new File(Constant.CONFIG_FILES);
		if((!confDir.exists()) || (!confDir.isDirectory())){
			System.err.println("错误： 配置目录" + confDir + "不存在或非法! ");
			System.exit(1);
		}
		publishConfig(client, Constant.ZK_CONFIG_ROOTNODE, confDir);
	}
}
