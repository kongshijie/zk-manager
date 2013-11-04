package com.dj.zk.manager.model;


/**
 * 
 * @description:角色与zoopath资源权限
 * @version  Ver 1.0
 * @author   <a href="mailto:zuiwoxing@gmail.com">dejian.liu</a>
 * @Date	 2013-11-3 下午6:25:43
 */
public class UserInfo {
	
	private Long id;
	
	private String userName;
	
	private String userPwd;
	
	private long createTime;
	
	private long updateTime;
	
 
	private Integer userType = Integer.valueOf(0);
	
	public UserInfo() {
	}
	
	

	public UserInfo(String userName, String userPwd) {
		super();
		this.userName = userName;
		this.userPwd = userPwd;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPwd() {
		return userPwd;
	}

	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}
 
	public Integer getUserType() {
		return userType;
	}
 
	public void setUserType(Integer userType) {
		this.userType = userType;
	}
 
}
