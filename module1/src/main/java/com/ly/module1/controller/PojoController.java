package com.ly.module1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ly.bean.base.BaseController;
import com.ly.bean.pojo.Pojo;
import com.ly.module1.message.PojoMessage;
import com.ly.module1.service.PojoService;

@Controller
@RequestMapping("/pojo")
public class PojoController extends BaseController{
	@Autowired
	private PojoService pojoService;
	@RequestMapping("/getPojoById")
	@ResponseBody
	public String getPojoById(String id){
		Pojo pojo = pojoService.getPojoById(id);
		if(pojo != null){
			return this.formatMessage(PojoMessage.SUCCESS_CODE, PojoMessage.SUCCESS_MESSAGE,pojo);
		}else{
			return this.formatMessage(PojoMessage.EXCEPTION_CODE, PojoMessage.EXCEPTION_MESSAGE, null);
		}
	}
}
