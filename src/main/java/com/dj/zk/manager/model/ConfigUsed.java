package com.dj.zk.manager.model;

import java.io.Serializable;
import java.util.Date;


/**
 * 
 * @description:配置文件使用记录
 * @version  Ver 1.0
 * @author   <a href="mailto:zuiwoxing@gmail.com">dejian.liu</a>
 * @Date	 2013-11-3 下午6:24:24
 */
public class ConfigUsed implements Serializable {

	private static final long serialVersionUID = -7800368711834489760L;

	/**
	 * 配置路径
	 */
	private String configPath;

	/**
	 * 应用路径
	 */
	private String appPath;
	
	/**
	 * 进程ID
	 */
	private String processId;

	/**
	 * 应用名称
	 */
	private String appName;

	/**
	 * 应用服务IP
	 */
	private String serverIp;


	/**
	 * 应用启动配置信息
	 */
	private String startConf;

	/**
	 * 系统配置(系统属性)
	 */
	private String systemConf;
	
	/**
	 * 创建时间
	 */
	private long createTime;

	private Date createTimeDate;
 

	public String getConfigPath() {
		return configPath;
	}

	public void setConfigPath(String configPath) {
		this.configPath = configPath;
	}

	public String getAppPath() {
		return appPath;
	}

	public void setAppPath(String appPath) {
		this.appPath = appPath;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getServerIp() {
		return serverIp;
	}

	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}

    
	public String getStartConf() {
		return startConf;
	}

	public void setStartConf(String startConf) {
		this.startConf = startConf;
	}

	public String getSystemConf() {
		return systemConf;
	}

	public void setSystemConf(String systemConf) {
		this.systemConf = systemConf;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
 
	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	public Date getCreateTimeDate() {
		if(this.createTime != 0) {
			createTimeDate = new Date(this.createTime);
		}
		return createTimeDate;
	}

	public void setCreateTimeDate(Date createTimeDate) {
		this.createTimeDate = createTimeDate;
	}

}
