package com.dj.zk.manager.enums;


/**
 * @description:用户类别
 * @version  Ver 1.0
 * @author   <a href="mailto:zuiwoxing@gmail.com">dejian.liu</a>
 * @Date	 2013-11-3 下午6:22:54
 */
public enum UserType {
	ADMIN(1,"管理员"), COMMON(2,"普通用户");

	private UserType(int code,String desc) {
		this.code = code;
		this.desc = desc;
	}

	private String desc;
	
	private int code;
 
	
	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	public static UserType parse(int code) {
		UserType[] ststus = UserType.values();
		for (UserType appStatus : ststus) {
			if(appStatus.getCode() == code) {
				return appStatus;
			}
		}
		return null;
	}
}
