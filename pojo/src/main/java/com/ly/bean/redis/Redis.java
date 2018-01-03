package com.ly.bean.redis;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.redis.core.StringRedisTemplate;

public class Redis{
	private volatile  static  StringRedisTemplate redisTemplate;
	
	@SuppressWarnings({ "resource" })
	public  static  StringRedisTemplate getRedisTemplateInstance(){
		if(redisTemplate == null){
			synchronized (Redis.class) {
				if(redisTemplate == null){
					ClassPathXmlApplicationContext applicationContext = 
							new ClassPathXmlApplicationContext("classpath:spring/applicationContext-redis.xml");
					redisTemplate = (StringRedisTemplate) applicationContext.getBean("redisTemplate");
				}
			}
		}
		return redisTemplate;
	}
	
}
