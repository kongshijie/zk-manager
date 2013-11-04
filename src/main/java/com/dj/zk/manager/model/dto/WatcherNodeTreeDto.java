package com.dj.zk.manager.model.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * 
 * @description:xxx
 * @version  Ver 1.0
 * @author   <a href="mailto:zuiwoxing@gmail.com">dejian.liu</a>
 * @Date	 2013-11-3 下午6:24:14
 */
public class WatcherNodeTreeDto implements Serializable {
 
	private static final long serialVersionUID = 144135544271908784L;

	/**
	 * 节点
	 */
	private String node;

	/**
	 * 主机
	 */
	private String zkHost;
	
	/**
	 * 监听客户端
	 */
	private String clientIpHost;
	
	/**
	 * 会话时长
	 */
	private String sessionTime;
 
	
	private List<WatcherNodeTreeDto> children = new ArrayList<WatcherNodeTreeDto>();


	public String getNode() {
		return node;
	}


	public void setNode(String node) {
		this.node = node;
	}


	public String getZkHost() {
		return zkHost;
	}


	public void setZkHost(String zkHost) {
		this.zkHost = zkHost;
	}


	public String getClientIpHost() {
		return clientIpHost;
	}


	public void setClientIpHost(String clientIpHost) {
		this.clientIpHost = clientIpHost;
	}


	public String getSessionTime() {
		return sessionTime;
	}


	public void setSessionTime(String sessionTime) {
		this.sessionTime = sessionTime;
	}


	public List<WatcherNodeTreeDto> getChildren() {
		return children;
	}


	public void setChildren(List<WatcherNodeTreeDto> children) {
		this.children = children;
	}

    
    
}
