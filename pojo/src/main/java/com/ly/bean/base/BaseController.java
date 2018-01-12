package com.ly.bean.base;

import org.springframework.stereotype.Controller;

import com.ly.bean.util.JsonUtils;
import com.ly.bean.vo.MessageResponseVO;

@Controller
public class BaseController {
	protected String formatMessage(Long code ,String message,Object obj){
		MessageResponseVO responseVO = new MessageResponseVO(code,message,obj);
		return JsonUtils.objectToJson(responseVO);
	}
}
