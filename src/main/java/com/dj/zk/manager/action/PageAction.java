package com.dj.zk.manager.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.dj.zk.manager.commons.Constants;

/**
 * 
 * @description:导航页
 * @version  Ver 1.0
 * @author   <a href="mailto:zuiwoxing@gmail.com">dejian.liu</a>
 * @Date	 2013-11-3 下午6:20:30
 */
@Controller
@RequestMapping(value=Constants.BASE_PATH+"page")
public class PageAction {

	@RequestMapping(value="index", method = RequestMethod.GET)
	public ModelAndView index(HttpServletRequest request,
			HttpServletResponse response) {
 		return new ModelAndView("index/index");
	}
	
	
	@RequestMapping(value="main", method = RequestMethod.GET)
	public ModelAndView main(HttpServletRequest request,
			HttpServletResponse response) {
 		return new ModelAndView("main/main");
	}
	
	@RequestMapping(value="zoo", method = RequestMethod.GET)
	public ModelAndView zoo(HttpServletRequest request,
			HttpServletResponse response) {
 		return new ModelAndView("zoo/zoo");
	}
	
	@RequestMapping(value="usedapp", method = RequestMethod.GET)
	public ModelAndView usedApp(HttpServletRequest request,
			HttpServletResponse response) {
 		return new ModelAndView("used/usedapp");
	}
	
	@RequestMapping(value="login", method = RequestMethod.GET)
	public ModelAndView login(HttpServletRequest request,
			HttpServletResponse response) {
 		return new ModelAndView("login/login");
	}
	
	@RequestMapping(value="command", method = RequestMethod.GET)
	public ModelAndView command(HttpServletRequest request,
			HttpServletResponse response) {
 		return new ModelAndView("command/command");
	}
	
	@RequestMapping(value="watcher", method = RequestMethod.GET)
	public ModelAndView watcher(HttpServletRequest request,
			HttpServletResponse response) {
 		return new ModelAndView("watcher/watcher");
	}
	
	@RequestMapping(value="watcherclient", method = RequestMethod.GET)
	public ModelAndView watcherClient(HttpServletRequest request,
			HttpServletResponse response) {
 		return new ModelAndView("watcherclient/watcherclient");
	}
	
	@RequestMapping(value="connector", method = RequestMethod.GET)
	public ModelAndView connector(HttpServletRequest request,
			HttpServletResponse response) {
 		return new ModelAndView("connector/connector");
	}
	
	@RequestMapping(value="monitor", method = RequestMethod.GET)
	public ModelAndView monitor(HttpServletRequest request,
			HttpServletResponse response) {
 		return new ModelAndView("monitor/monitor");
	}
	
	
}
