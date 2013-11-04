package com.dj.zk.manager.action;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.dj.zk.manager.commons.Constants;
import com.dj.zk.manager.model.UserInfo;
import com.dj.zk.manager.service.UserInfoService;


/**
 * 
 * @description:登录Action
 * @version  Ver 1.0
 * @author   <a href="mailto:zuiwoxing@gmail.com">dejian.liu</a>
 * @Date	 2013-11-3 下午6:20:16
 */
@Controller
@RequestMapping(value = Constants.BASE_PATH + "login")
public class LoginAction {

	@Autowired
	private UserInfoService userInfoService;
	
 
 
	@RequestMapping(value = "login", method = {RequestMethod.POST,RequestMethod.GET})
	public void login(HttpServletRequest request,UserInfo userInfo,ModelMap model) {
		String verifyCode = request.getParameter("verifyCode");
		if(!request.getSession().getAttribute(Constants.VERIFY_CODE).toString().equalsIgnoreCase(verifyCode)) {
			model.clear();
			model.put("msg","failse");
			return;
		}
		
 		if(userInfo != null && StringUtils.isNotEmpty(userInfo.getUserName()) && StringUtils.isNotEmpty(userInfo.getUserPwd())) {
			UserInfo ui =userInfoService.findByUserNameAndPwd(userInfo.getUserName(),userInfo.getUserPwd());
	        if(ui != null && ui.getId() != null) {
	        	request.getSession().setAttribute(Constants.USER_INFO_SESSION, ui);
	        	request.getSession().removeAttribute(Constants.VERIFY_CODE);
				model.clear();
				model.put("msg","ok");
				return;
	        }
		}
		model.clear();
		model.put("msg","failse");

	}
	
	@RequestMapping(value = "logout", method = {RequestMethod.GET})
	public ModelAndView logout(HttpServletRequest request) {
 		request.getSession().removeAttribute(Constants.USER_INFO_SESSION);
		return new ModelAndView("login/login");
	}
	
	
}
