package com.ly.module2.test;

import java.io.IOException;
import java.util.List;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Test {

	@org.junit.Test
	public void test5(){
		ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-dubbo-consumption.xml");
		applicationContext.start();
		DubboTest bean = (DubboTest) applicationContext.getBean("demoService");
		bean.sayHello("ly");
		List<String> users = bean.getUsers();
		for (String string : users) {
			System.out.println(string);
		}
		try {
			System.in.read();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
