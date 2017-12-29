package com.ly.module2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ly.bean.base.BaseController;
import com.ly.module2.service.PojoService;
@Controller
@RequestMapping("/pojo")
public class PojoController extends BaseController {
	@Autowired
	private PojoService pojoService;
	
	@RequestMapping("/getPojoById")
	@ResponseBody
	public String getPojoById(String id){
		return pojoService.getPojo(id);
	}
}
