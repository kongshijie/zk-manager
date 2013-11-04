package com.dj.zk.manager.service.impl;

import org.springframework.stereotype.Service;

import com.dj.zk.manager.commons.Constants;
import com.dj.zk.manager.model.UserInfo;
import com.dj.zk.manager.service.UserInfoService;
import com.dj.zk.manager.utils.MD5;


@Service
public class UserInfoServiceImpl implements UserInfoService {

	@Override
	public UserInfo findByUserNameAndPwd(String userName, String userPwd) {
 	    UserInfo ui = Constants.getUsermap().get(userName);
        if(ui != null && ui.getUserPwd().equals(MD5.encrypt(Constants.MD5_SALT+userPwd))) {
        	return ui;
        }
	    return null;
	}

}
