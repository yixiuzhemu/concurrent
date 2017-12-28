package com.ly.module1.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ly.bean.pojo.Pojo;
import com.ly.module1.dao.PojoDao;
import com.ly.module1.service.PojoService;

@Service
public class PojoServiceImpl implements PojoService{
	@Autowired
	private PojoDao pojoDao;

	@Override
	public Pojo getPojoById(String id) {
		return pojoDao.getPojoById(id);
	}
	
	
}
