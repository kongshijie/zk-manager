package com.dj.zk.manager.commons;

import java.util.HashMap;
import java.util.Map;


/**
 * 
 * @description:响应消息
 * @version  Ver 1.0
 * @author   <a href="mailto:zuiwoxing@gmail.com">dejian.liu</a>
 * @Date	 2013-11-3 下午6:22:30
 */
public class ResponseEntity {

	private int status;
	
	private String message;
	
	private Map<String, Object> params = new HashMap<String, Object>();
	

	public ResponseEntity() {
	}
	
	public ResponseEntity(int status, String message) {
		super();
		this.status = status;
		this.message = message;
	}

 
    
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Map<String, Object> getParams() {
		return params;
	}

	public void setParams(Map<String, Object> params) {
		this.params = params;
	}
	
	
	
	
}
