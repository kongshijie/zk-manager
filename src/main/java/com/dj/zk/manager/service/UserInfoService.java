package com.dj.zk.manager.service;

import com.dj.zk.manager.model.UserInfo;

/**
 * 
 * @description:用户信息service
 * @version  Ver 1.0
 * @author   <a href="mailto:zuiwoxing@gmail.com">dejian.liu</a>
 * @Date	 2013-11-3 下午6:26:27
 */
public interface UserInfoService {
	
	public UserInfo findByUserNameAndPwd(String userName,String userPwd);

}
