package com.ly.bean.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class FileUtils {
	/**
	 * 创建文件目录
	 * @param path
	 */
	public static void createDirecotry(String path){
		File file = new File(path);
		//文件不存在 并且文件目录不存在
		if(!file.exists() && !file.isDirectory()){
			file.mkdirs();
		}
	}
	/**
	 * 获取文件前缀
	 * @param fileName
	 * @return
	 */
	public static String getPrefix(String fileName){
		Integer index = fileName.lastIndexOf(".");
		String prefix = "";
		if(index>0){
			prefix = fileName.substring(index);
		}
		return prefix;
	}
	public static String saveFile(HttpServletRequest request,String filePath,String realName) throws Exception{
		//创建文件路径
		FileUtils.createDirecotry(filePath);
		int msgSize = 0;
		//设置读取字节大小
		byte[] buffer = new byte[4096];
		//创建文件名称
		String name = UUID.randomUUID().toString()+getPrefix(realName);
		//获取输出流
		FileOutputStream outputStream = new FileOutputStream(filePath+name);
		//获取输入流
		ServletInputStream inputStream = request.getInputStream();
		
		while(-1 != (msgSize = inputStream.read(buffer))){
			//将输出流写到指定的目录下
			outputStream.write(buffer,0,msgSize);
		}
		//关闭输入流
		inputStream.close();
		//关闭输出流
		outputStream.close();
		return name;
	}
	public static String saveFiles(HttpServletRequest request,String filePath,String realName) throws Exception{
		FileUtils.createDirecotry(filePath);
		DiskFileItemFactory factory = new DiskFileItemFactory();
		//设置文件上传临时存储空间的临界值
		factory.setSizeThreshold(1024*1024);
		//若超过临界值 则保存在temp目录下
		factory.setRepository(new File(System.getProperty("java.io.tmpdir")));
		//获取到文件上传对象
		ServletFileUpload upload = new ServletFileUpload(factory);
		//设置文件上传大小
		upload.setSizeMax(2048*1024*1024);
		String fName="";
		//解析request ,将每个文件封装为单独的fileItem
		List<FileItem> fileItems = upload.parseRequest(request);
		//遍历每一个FileItem
		Iterator<FileItem> iterator = fileItems.iterator();
		while(iterator.hasNext()){
			FileItem fileItem = iterator.next();
			//判断该项 是否是文件
			if(!fileItem.isFormField()){
				//创建随机文件名
				String tmpName = UUID.randomUUID().toString()+getPrefix(fileItem.getName());
				File file = new File(filePath+tmpName);
				//写入文件
				fileItem.write(file);
				fName += ","+tmpName;
				
			}
		}
		if(fName.length()>0){
			fName=fName.substring(1);
		}
		return fName;
	}
}
