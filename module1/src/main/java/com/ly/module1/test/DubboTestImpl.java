package com.ly.module1.test;

import java.util.ArrayList;
import java.util.List;

public class DubboTestImpl implements DubboTest {

	@Override
	public String sayHello(String name) {
		System.out.println("hello,"+name);
		return null;
	}

	@Override
	public List<String> getUsers() {
			List<String> list = new ArrayList<String>();  
	          
	        list.add("hejingyuan");  
	        list.add("xvshu");  
	          
	        return list;  
	}

}
