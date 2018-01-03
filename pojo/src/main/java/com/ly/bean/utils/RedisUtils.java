package com.ly.bean.utils;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import com.ly.bean.redis.Redis;

public class RedisUtils{
	/**
	 * redis模板
	 */
	private static StringRedisTemplate redisTemplateInstance;
	/**
	 * 添加redis缓存 ，以json的形式存储
	 * @param key 
	 * @param value
	 * @param time 过期时间
	 * @param unit 计时类型
	 */
	public static<T> void add(String key,T value,Long time,TimeUnit unit){
		redisTemplateInstance = Redis.getRedisTemplateInstance();
		if(key == null || "".equals(key)){
			System.err.print("key不能为null!");
			return;
		}
		if(unit == null){
			unit = TimeUnit.SECONDS;
		}
		String terminalValue = null;
		if(!(value instanceof String) && (value instanceof Object) ){
			terminalValue = JsonUtils.objectToJson(value);
		}else if(value instanceof String){
			terminalValue = value.toString();
		}
		if(terminalValue != null && !"".equals(terminalValue)){
			ValueOperations<String,String> opsForValue = redisTemplateInstance.opsForValue();
			
			if(time == null || time <= 0){
				opsForValue.set(key, terminalValue);
			}else{
				opsForValue.set(key, terminalValue , time, unit);
			}
			return;
		}
		System.err.print("添加缓存失败!");
		
	}
	/**
	 * 添加redis缓存 ，以json的形式存储 默认时间类型为秒
	 * @param key
	 * @param value
	 * @param time
	 */
	public static<T> void add(String key,T value,Long time){
		RedisUtils.add(key,value,time,null);
	}
	/**
	 * 添加redis缓存 ，以json的形式存储 不会过期
	 * @param key
	 * @param value
	 */
	public static<T> void add(String key,T value){
		RedisUtils.add(key,value,null,null);
	}
	/**
	 * 获取缓存信息，并以pojo形式返回
	 * @param key
	 * @param type
	 * @return
	 */
	public static<T> T getByPojo(String key,Class<T> type){
		String value = RedisUtils.getValue(key);
		if(value != null){
			if(type != null){
				T t = JsonUtils.jsonToPojo(value, type);
				return t;
			}
		}
		return null;
	} 
	/**
	 * 获取缓存信息，并以List形式返回
	 * @param key
	 * @param type
	 * @return
	 */
	public static<T> List<T> getByList(String key,Class<T> type){
		String value = RedisUtils.getValue(key);
		if(value != null){
			if(type != null){
				List<T> t = JsonUtils.jsonToList(value, type);
				return t;
			}
		}
		return null;
	}
	/**
	 * 获取缓存信息，并以String形式返回
	 * @param key
	 * @return
	 */
	public static String get(String key){
		return RedisUtils.getValue(key); 
	}
	/**
	 * 获得缓存信息，并返回json串
	 * @param key
	 * @return
	 */
	private static String getValue(String key){
		redisTemplateInstance = Redis.getRedisTemplateInstance();
		if(key == null || "".equals(key)){
			System.err.print("key不能为null!");
			return null;
		}
		ValueOperations<String, String> opsForValue = redisTemplateInstance.opsForValue();
		if(opsForValue!=null){
			return opsForValue.get(key);
		}
		return null;
	}
	/**
	 * 移除缓存信息
	 * @param key
	 */
	public static void remove(String key){
		redisTemplateInstance = Redis.getRedisTemplateInstance();
		if(key == null || "".equals(key)){
			System.err.print("key不能为null!");
			return;
		}
		redisTemplateInstance.delete(key);
	}
}
