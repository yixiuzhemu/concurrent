package com.ly.module2.dao.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.ly.bean.base.BaseDao;
import com.ly.bean.pojo.Pojo;
import com.ly.bean.utils.HttpClientUtil;
import com.ly.module2.dao.PojoDao;
@Repository
public class PojoDaoImpl extends BaseDao<Pojo> implements PojoDao {
	@Value("${module1.local.url}")
	private String module1Url;
	@Value("${module1.getpojobyid.url}")
	private String getPojoById;
	@Override
	public String getPojo(String id) {
		Map<String, String> hashMap = new HashMap<String,String>();
		hashMap.put("id", id);
		System.out.println(module1Url+getPojoById);
		return HttpClientUtil.doPost(module1Url+getPojoById,hashMap);
	}

}
