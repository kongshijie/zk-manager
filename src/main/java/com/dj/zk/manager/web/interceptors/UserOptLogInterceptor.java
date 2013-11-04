package com.dj.zk.manager.web.interceptors;

import java.util.Date;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.dj.zk.manager.commons.Constants;
import com.dj.zk.manager.model.UserInfo;
import com.dj.zk.manager.utils.DateUtils;
import com.dj.zk.manager.utils.NetUtils;

/**
 * 
 * @description:日志过滤
 * @version  Ver 1.0
 * @author   <a href="mailto:zuiwoxing@gmail.com">dejian.liu</a>
 * @Date	 2013-11-3 下午6:29:29
 */
public class UserOptLogInterceptor extends HandlerInterceptorAdapter {

	private static Logger logger = Logger.getLogger(UserOptLogInterceptor.class);
	
	private boolean isEnable = true;
	

	@SuppressWarnings("unchecked")
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		try {
			if(!isEnable) {
				return true;
			}
			StringBuffer buf = new StringBuffer();
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			Object userObj = request.getSession().getAttribute(Constants.USER_INFO_SESSION);
			if(userObj != null) {
				String userName = ((UserInfo)userObj).getUserName();
				buf.append("用户:"+userName+"---");
			}
 			String requestIp = NetUtils.getRequestIp(request);
 			buf.append("调用者IP:"+requestIp+"---");
			String requestTime = DateUtils.format(new Date(), DateUtils.DATE_HH_MM_SS);
			buf.append("时间:"+requestTime+"---");
			String url = request.getRequestURI().toString();
			buf.append("调用URI:"+url+"---");
			String className = handlerMethod.getBean().getClass().getName();
			String methodName = handlerMethod.getMethod().getName();
			buf.append("调用类及方法:"+className+"."+methodName+"---");
			buf.append("【");
			Enumeration<String> paramNames = request.getParameterNames();
			while(paramNames.hasMoreElements()) {
				String name = paramNames.nextElement();
				String nameValue = request.getParameter(name);
				buf.append(name).append("=").append(nameValue).append("|");
			}
			buf.append("】");
			logger.info(buf.toString());
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}  
		return true;
	}


	public boolean isEnable() {
		return isEnable;
	}


	public void setEnable(boolean isEnable) {
		this.isEnable = isEnable;
	}
	
	
}
