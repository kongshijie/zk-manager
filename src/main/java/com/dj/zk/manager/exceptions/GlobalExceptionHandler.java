package com.dj.zk.manager.exceptions;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import com.dj.zk.manager.utils.response.ResponseUtils;



/**
 * @description:全局异常拦截器
 * @version  Ver 1.0
 * @author   <a href="mailto:zuiwoxing@gmail.com">dejian.liu</a>
 * @Date	 2013-11-3 下午6:23:20
 */
public class GlobalExceptionHandler extends SimpleMappingExceptionResolver {

	protected static Log logger = LogFactory.getLog(GlobalExceptionHandler.class);
	
	private MessageSource messageResource;
	
 
	@Override
	public ModelAndView doResolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex) {
 		String viewName = determineViewName(ex, request);
		String reqType =  request.getHeader("X-Requested-With");
 		String errorMsg = getExceptionMessage(ex, request);
		request.setAttribute("errorMsg", errorMsg);
		if ("XMLHttpRequest".equals(reqType)) {
 
			//交给BidExceptionFilter处理了
		    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
 
			String callback = request.getParameter("callback");
	 		if (StringUtils.isNotEmpty(callback)) {
	 	 		String str = callback + "({"+errorMsg+"})";
	 	 		ResponseUtils.responseScript(response, str);
	 		} else {
	 			ResponseUtils.responseJson(response, errorMsg);
	 		}
            
			return null;
		} else {
 
			if (viewName != null)  {
				return super.doResolveException(request, response, handler, ex);
			}  else {
				return new ModelAndView("errors/error");
			}
		}
		
 
	}

	public String getExceptionMessage(Exception ex,HttpServletRequest request) {
		String message = "";
		if (ex instanceof BaseException) {
			BaseException be = (BaseException) ex;
			String code = be.getCode();
			if (StringUtils.isNotEmpty(code)) {
				message = messageResource.getMessage(code, be.getValues(), request.getLocale());
			}
			if (StringUtils.isEmpty(message)) {
				message = ex.getMessage();
			}
		} else {
			String isAllException = "false";// PropertiesHolder.getProperty("is.print.all.exception","true");
			if (!"true".equals(isAllException.trim())) {
	    		message = "系统正在维护!";
			} else {
				StringWriter sw = new StringWriter();
	    		PrintWriter pw = new PrintWriter(sw);
	    		ex.printStackTrace(pw);
	    		try {
	    			message = sw.toString();
	    			pw.close();
				} catch (Exception e) {
					logger.error( e.getMessage(),  e); 
				}
			}
			
			logger.error(ex.getMessage(), ex); 
		}
		
		return message;
	}
	
	
	public void setMessageResource(
			ReloadableResourceBundleMessageSource messageResource) {
		this.messageResource = messageResource;
	}

 

}

