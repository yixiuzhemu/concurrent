package com.ly.module1.dao.impl;

import org.springframework.stereotype.Repository;

import com.ly.bean.base.BaseDao;
import com.ly.bean.pojo.Pojo;
import com.ly.bean.util.RedisUtils;
import com.ly.module1.dao.PojoDao;
@Repository
public class PojoDaoImpl extends BaseDao<Pojo> implements PojoDao {

	@Override
	public Pojo getPojoById(String id) {
		Pojo pojo = RedisUtils.getByPojo(id,Pojo.class);
		if(pojo == null){
			pojo = (Pojo)this.getSession().get(Pojo.class, id);
			RedisUtils.add("pojo:"+id, pojo,360L);
		}
		return pojo;
	}

}
