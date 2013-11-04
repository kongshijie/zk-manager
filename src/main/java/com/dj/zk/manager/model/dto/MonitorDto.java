package com.dj.zk.manager.model.dto;

import java.io.Serializable;
import java.net.InetSocketAddress;


/**
 * 
 * @description:定期检查
 * @version  Ver 1.0
 * @author   <a href="mailto:zuiwoxing@gmail.com">dejian.liu</a>
 * @Date	 2013-11-3 下午6:23:48
 */
public class MonitorDto implements Serializable{
	
 
	private static final long serialVersionUID = 1L;

	private InetSocketAddress address;
	
	/**
	 * 时间间隔（秒）
	 */
	private Integer timeInterval;

	
	/**
	 * 失败后调用URL
	 */
	private String failUrl;


	public InetSocketAddress getAddress() {
		return address;
	}


	public void setAddress(InetSocketAddress address) {
		this.address = address;
	}


	public Integer getTimeInterval() {
		return timeInterval;
	}


	public void setTimeInterval(Integer timeInterval) {
		this.timeInterval = timeInterval;
	}


	public String getFailUrl() {
		return failUrl;
	}


	public void setFailUrl(String failUrl) {
		this.failUrl = failUrl;
	}
	
	
	
	
}
