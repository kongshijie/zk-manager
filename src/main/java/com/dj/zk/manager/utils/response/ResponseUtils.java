package com.dj.zk.manager.utils.response;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

/**
 * 
* @ClassName: ResponseUtils
* @Description: 响应工作 类别
* @author <a href="mailto:zuiwoxing@gmail.com">dejian.liu</a>
* @date 2011-11-15 下午05:50:43
*
 */
public class ResponseUtils {
	private static Logger logger = Logger.getLogger(ResponseUtils.class);
	
	/**
	 * 
	* @Title: responseJson
	* @Description: JSON 格式 响应
	* @param @param response
	* @param @param res    响应字符串
	* @return void    返回类型
	* @throws
	 */
	public static void responseJson(HttpServletResponse response,String res) {
		try {
			response.setContentType("application/json;charset=UTF-8");
			response.setCharacterEncoding("UTF-8");
			PrintWriter out = response.getWriter();
			out.write(res);
			out.flush();
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		
	}
	
	/**
	 * 
	* @Title: responseScript
	* @Description: JSON 格式 响应
	* @param @param response
	* @param @param res    响应字符串
	* @return void    返回类型
	* @throws
	 */
	public static void responseScript(HttpServletResponse response,String res) {
		try {
			response.setContentType("text/javascript;charset=UTF-8");
			response.setCharacterEncoding("UTF-8");
			PrintWriter out = response.getWriter();
			out.write(res);
			out.flush();
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		
	}
	
	/**
	 * 
	* @Title: responseHtml
	* @Description: text/html 格式 响应
	* @param @param response
	* @param @param res    响应字符串
	* @return void    返回类型
	* @throws
	 */
	public static void responseHtml(HttpServletResponse response,String res) {
		try {
			response.setContentType("text/html;charset=utf-8");
			response.setCharacterEncoding("UTF-8");
			PrintWriter out = response.getWriter();
			out.write(res);
			out.flush();
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		
	}


}
