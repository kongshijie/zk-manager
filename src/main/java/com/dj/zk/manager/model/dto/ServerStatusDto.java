package com.dj.zk.manager.model.dto;

import java.io.Serializable;


/**
 * 
 * @description:服务状态
 * @version  Ver 1.0
 * @author   <a href="mailto:zuiwoxing@gmail.com">dejian.liu</a>
 * @Date	 2013-11-3 下午6:23:58
 */
public class ServerStatusDto implements Serializable {
 
	private static final long serialVersionUID = 1L;
	
	private String serverVersion;
	
	/**
	 * 总发包量
	 */
	private Long sent;
	
	/**
	 * 总接收量
	 */
	private Long received;
	
	/**
	 * 连接数
	 */
	private String connections;
	
	/**
	 * leader/follower
	 */
	private String mode;
	
	/**
	 * 节点数
	 */
	private String nodeCount;

	/**
	 * 当前状态(dead/activate)
	 */
	private String status;

	/**
	 * 服务主机与端口
	 */
	private String hostPort;
	
	
	public String getServerVersion() {
		return serverVersion;
	}

	public void setServerVersion(String serverVersion) {
		this.serverVersion = serverVersion;
	}

	public Long getSent() {
		return sent;
	}

	public void setSent(Long sent) {
		this.sent = sent;
	}

	public Long getReceived() {
		return received;
	}

	public void setReceived(Long received) {
		this.received = received;
	}

 

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	 

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getConnections() {
		return connections;
	}

	public void setConnections(String connections) {
		this.connections = connections;
	}

	public String getNodeCount() {
		return nodeCount;
	}

	public void setNodeCount(String nodeCount) {
		this.nodeCount = nodeCount;
	}

	public String getHostPort() {
		return hostPort;
	}

	public void setHostPort(String hostPort) {
		this.hostPort = hostPort;
	}
	
	
	
}
