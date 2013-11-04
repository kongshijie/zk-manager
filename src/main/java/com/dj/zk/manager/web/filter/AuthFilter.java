package com.dj.zk.manager.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.dj.zk.manager.commons.Constants;
import com.dj.zk.manager.commons.ResponseEntity;
import com.dj.zk.manager.model.UserInfo;
import com.dj.zk.manager.utils.PrivilegeUtils;
import com.dj.zk.manager.utils.json.JsonUtils;
import com.dj.zk.manager.utils.response.ResponseUtils;



/**
 * 
 * @description:
 * @version  Ver 1.0
 * @author   <a href="mailto:zuiwoxing@gmail.com">dejian.liu</a>
 * @Date	 2013-11-3 下午6:29:20
 */
public class AuthFilter implements Filter {

	/**
	 * 分隔符
	 */
	private static final String SPLITTER = ";";

	/**
	 * 过滤跳过的url数组
	 */
	private static String excludeUrls;

	@Override
	public void init(FilterConfig config) throws ServletException {
         excludeUrls = config.getInitParameter("excludeUrls");
 	}


	/**
	 * URL过滤,进行权限验证
	 * @param servletRequest
	 *            ServletRequest
	 * @param servletResponse
	 *            ServletResponse
	 * @param chain
	 *            FilterChain
	 */
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		
		Object userObj = request.getSession().getAttribute(Constants.USER_INFO_SESSION);
		String url = request.getRequestURI();
   		if(matchUrls(request, excludeUrls) || url.endsWith("login.json") || url.endsWith("zoo/config.do")|| url.endsWith("verify/code.do")) {
			filterChain.doFilter(request, response);
			return;
		}
		if(userObj == null) {
			request.getRequestDispatcher(Constants.BASE_PATH+"page/login.do").forward(request, response);
		} else {
			UserInfo userInfo = (UserInfo)userObj;
			if(StringUtils.isNotEmpty(userInfo.getUserName()) && StringUtils.isNotEmpty(userInfo.getUserPwd())) {
				String referer = request.getHeader("Referer");
 				if(url.replaceAll("/","").equals(request.getContextPath().replaceAll("/",""))) {
					if(StringUtils.isNotEmpty(referer)) {
						response.sendRedirect(referer);
					} else {
						request.getRequestDispatcher(Constants.BASE_PATH+"page/main.do").forward(request, response);
						return;
					}
				}
				boolean isPass = PrivilegeUtils.checkPrivilege(request, userInfo);
				if(!isPass) {
					response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
 				    ResponseEntity re = new ResponseEntity(HttpServletResponse.SC_UNAUTHORIZED, "对不起，你无权操作该功能!");
				    String json = JsonUtils.toJson(re, null, null, null);
				    ResponseUtils.responseJson(response, json);
				} else {
					filterChain.doFilter(request, response);
				}
			} else {
				request.getRequestDispatcher(Constants.BASE_PATH+"page/main.do").forward(request, response);
			}
		}  
	}

	/**
	 * @description 资源匹配
	 * @param request
	 * @return
	 */
	private boolean matchUrls(ServletRequest request,String  matchs) {
		try {
			if(StringUtils.isEmpty(matchs)) {
				return false;
			}
			String[] excludes = matchs.split(SPLITTER);
			HttpServletRequest httprequest = (HttpServletRequest) request;
			for (int i = 0; i < excludes.length; i++) {
				String path = httprequest.getRequestURI();
				String contextPath = httprequest.getContextPath();
				String regx = excludes[i].replaceAll("\\.", "\\\\.");
				regx = regx.replaceAll("\\*", "\\.*");
				if (excludes[i].endsWith("/")) {
					regx = regx + ".*";
				}
				if (regx.startsWith("/")) {
					regx = regx.replaceFirst("/", contextPath + "/");
					regx = regx.replaceAll("//", "/");
				}
				if (path.matches(regx)) {
					return true;
				}
					
			}
		} catch (Throwable localThrowable) {
		}
		return false;
	}

	public void destroy() {
	}
 
 
}
