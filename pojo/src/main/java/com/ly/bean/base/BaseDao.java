package com.ly.bean.base;

import java.lang.reflect.Type;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ly.bean.pojo.BasePojo;
@Transactional(isolation=Isolation.DEFAULT,propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
public class BaseDao <T extends BasePojo> {
	@Autowired
	private SessionFactory sessionFactory;
	
	private Class<T> entityClass;
	
	@SuppressWarnings("unchecked")
	public BaseDao(){
		Type[] types = this.getClass().getGenericInterfaces();
		Type currentType = types[0];
		if(currentType instanceof Class){
			this.entityClass = (Class<T>)currentType;
		}
	}
	
	/**
	 * 获取当前实体类
	 * @return
	 */
	public Class<T> getEntityClass(){
		return entityClass;
	}
	/**
	 * 获取当前实体类的名称
	 * @return
	 */
	public String getEntityName(){
		return getEntityClass().getSimpleName();
	}
	/**
	 * 获取当前线程session(只能在事务环境下进行操作)
	 * @return
	 */
	public Session getSession(){
		return sessionFactory.getCurrentSession();
	}
	
	public T getById(String id){
		Session session = this.getSession();
		T t = (T)session.get(this.getEntityClass(), id);
		return t;
	}
}
