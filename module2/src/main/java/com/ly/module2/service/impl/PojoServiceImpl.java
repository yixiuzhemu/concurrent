package com.ly.module2.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ly.module2.dao.PojoDao;
import com.ly.module2.service.PojoService;
@Service
public class PojoServiceImpl implements PojoService {
	@Autowired
	private PojoDao pojoDao;
	@Override
	public String getPojo(String id) {
		// TODO Auto-generated method stub
		return pojoDao.getPojo(id);
	}

}
