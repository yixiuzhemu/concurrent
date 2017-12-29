package com.ly.bean.vo;

public class MessageResponseVO {
	private Long code;
	private String message;
	private Object obj;
	public MessageResponseVO() {
		super();
	}
	
	public MessageResponseVO(Long code, String message, Object obj) {
		super();
		this.code = code;
		this.message = message;
		this.obj = obj;
	}
	public Long getCode() {
		return code;
	}
	public void setCode(Long code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Object getObj() {
		return obj;
	}
	public void setObj(Object obj) {
		this.obj = obj;
	}
	
}
