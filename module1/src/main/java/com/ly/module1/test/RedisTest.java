package com.ly.module1.test;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.ly.bean.utils.JsonUtils;
import com.ly.bean.utils.RedisUtils;

public class RedisTest {

	@Test
	public void test() {
		List<String> list = new ArrayList<String>();
		list.add("1");
		list.add("2");
		list.add("3");
		RedisUtils.add("key", list);
	}
	
	@Test
	public void test1(){
		String s = RedisUtils.get("key");
		System.out.println(s);
		List<String> list = RedisUtils.getByList("key", String.class);
		for (int i = 0; i < list.size(); i++) {
			System.out.println(list.get(i));
		}
	}
	@Test
	public void test2(){
		RedisUtils.remove("key");
	}
	@SuppressWarnings("unchecked")
	@Test
	public void test3(){
		Map<String,Integer> map = new HashMap<String,Integer>();
		map.put("key1",1);
		map.put("key2",2);
		map.put("key3",3);
		String objectToJson = JsonUtils.objectToJson(map);
		System.out.println(objectToJson);
		Map<String, Integer> jsonToMap = JsonUtils.jsonToPojo(objectToJson, Map.class);
		System.out.println(jsonToMap);
	}
	
}
