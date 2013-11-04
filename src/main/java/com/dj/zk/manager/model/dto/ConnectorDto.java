package com.dj.zk.manager.model.dto;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.dj.zk.manager.utils.DateUtils;


/**
 * 
 * @description:列出所有连接者
 * @version  Ver 1.0
 * @author   <a href="mailto:zuiwoxing@gmail.com">dejian.liu</a>
 * @Date	 2013-11-3 下午6:23:39
 */
public class ConnectorDto implements Serializable {
	private static final long serialVersionUID = -7251371700406779354L;

	/**
	 * zookeeper节点 
	 */
	private String zooHost;
	
	/**
	 * 客户端IP:PORT
	 */
	private String clientHostPort;
	
	/**
	 * 总发送包大小
	 */
	private Long sent;
	
	/**
	 * 总接收包大小
	 */
	private Long recved;
	
	/**
	 * sessionId
	 */
	private String sessionId;
	
	/**
	 * session 创建时间
	 */
	private Long createConTime;
	
	private String createConTimeDate;
	
	
	/**
	 * 最后确认时间
	 */
	private Long lastEstTime;
	
	private String lastEstTimeDate;
	
	/**
	 * session时差
	 */
	private Long sessionDif = Long.valueOf(0);
	
	private String sessionDifDate;
	
	/**
	 * 【临时字段】(排序使用)
	 */
	private String clientHost;

	public String getZooHost() {
		return zooHost;
	}

	public void setZooHost(String zooHost) {
		this.zooHost = zooHost;
	}

	public String getClientHostPort() {
		return clientHostPort;
	}
 

	public void setClientHostPort(String clientHostPort) {
		this.clientHostPort = clientHostPort;
	}
 
	public Long getSent() {
		return sent;
	}
 
	public void setSent(Long sent) {
		this.sent = sent;
	}

 
	public Long getRecved() {
		return recved;
	}
 
	public void setRecved(Long recved) {
		this.recved = recved;
	}
 
	public String getSessionId() {
		return sessionId;
	}
 
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
 
	public Long getCreateConTime() {
		return createConTime;
	}

    public void setCreateConTime(Long createConTime) {
		this.createConTime = createConTime;
	}
 
	public Long getLastEstTime() {
		return lastEstTime;
	}
 
	public void setLastEstTime(Long lastEstTime) {
		this.lastEstTime = lastEstTime;
	}
 
	public String getCreateConTimeDate() {
		if(this.createConTime != null) {
			createConTimeDate = DateUtils.format(new Date(this.createConTime), DateUtils.DATE_HH_MM_SS);
		}
		return createConTimeDate;
	}

	public void setCreateConTimeDate(String createConTimeDate) {
		this.createConTimeDate = createConTimeDate;
	}

	public String getLastEstTimeDate() {
		if(this.lastEstTime != null) {
			lastEstTimeDate = DateUtils.format(new Date(this.lastEstTime), DateUtils.DATE_HH_MM_SS);
		}
		return lastEstTimeDate;
	}

	public void setLastEstTimeDate(String lastEstTimeDate) {
		this.lastEstTimeDate = lastEstTimeDate;
	}
	
 
	public Long getSessionDif() {
		if(this.lastEstTime != null && this.createConTime != null) {
			sessionDif = lastEstTime - createConTime;
		} else if (this.lastEstTime == null && this.createConTime != null) {
			sessionDif = System.currentTimeMillis() - createConTime;
		}
		return sessionDif;
	}

	public void setSessionDif(Long sessionDif) {
		this.sessionDif = sessionDif;
	}
	
	

	public String getSessionDifDate() {
		return sessionDifDate = DateUtils.subtractParse(this.getSessionDif(), "d-H-m-s");
	}

	public void setSessionDifDate(String sessionDifDate) {
		this.sessionDifDate = sessionDifDate;
	}

	public String getClientHost() {
		if(StringUtils.isNotEmpty(this.getClientHostPort()) && this.getClientHostPort().indexOf(":") !=-1) {
			clientHost = this.getClientHostPort().split(":")[0];
		}
		return clientHost;
	}

	public void setClientHost(String clientHost) {
		this.clientHost = clientHost;
	}
	
	

	 
}
