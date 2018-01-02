package com.ly.module1.test;


import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

public class RedisTest {

	@Test
	public void test() {
		ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-redis.xml");
		RedisTemplate<String,Object> redisTemplate = (RedisTemplate<String,Object>) applicationContext.getBean("redisTemplate");
		ValueOperations<String, Object> stringValue = redisTemplate.opsForValue();
		stringValue.set("key", "value", 3000, TimeUnit.SECONDS);
	}
	
	@Test
	public void test1(){
		ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-redis.xml");
		RedisTemplate<String,Object> redisTemplate = (RedisTemplate<String,Object>) applicationContext.getBean("redisTemplate");
		ValueOperations<String, Object> stringValue = redisTemplate.opsForValue();
		Object string = stringValue.get("key");
		System.out.println(string);
	}

}
