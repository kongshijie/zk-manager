package com.dj.zk.manager.action;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.dj.zk.manager.commons.Constants;
import com.dj.zk.manager.model.ConfigUsed;
import com.dj.zk.manager.model.PageView;
import com.dj.zk.manager.service.ZookeeperService;
import com.dj.zk.manager.utils.DateUtils;
import com.dj.zk.manager.utils.json.JsonUtils;
import com.dj.zk.manager.utils.response.ResponseUtils;


/**
 * 
 * @description:已使用应用Action 用来做统一配置管理
 * @version  Ver 1.0
 * @author   <a href="mailto:zuiwoxing@gmail.com">dejian.liu</a>
 * @Date	 2013-11-3 下午6:20:46
 */
@Controller
@RequestMapping(value = Constants.BASE_PATH + "used")
public class UsedAppAction {
	
	private static Logger logger = Logger.getLogger(UsedAppAction.class);
	
	@Autowired
	private ZookeeperService zookeeperService;
	
	private static Properties loadPropFromByte(Properties p,byte [] data) {
		try {
			p.load(new ByteArrayInputStream(data));
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return p;
	}
	
	@RequestMapping(value = { "query" }, method = { RequestMethod.POST ,RequestMethod.GET})
	public ModelAndView query(HttpServletRequest request,HttpServletResponse response, ModelMap modelMap) {
		String serverIp = request.getParameter("serverIp");
		String configPath = request.getParameter("configPath");
		
		List<String> lists = zookeeperService.getAllChilds(Constants.ZOO_CONF_DATA);
		List<ConfigUsed> listcus = new ArrayList<ConfigUsed>();
		Properties p = new Properties();
		
		if(lists != null && lists.size() > 0) {
			for (String path : lists) {
				byte [] data = zookeeperService.getData(path);
				loadPropFromByte(p, data);
				ConfigUsed cu = new ConfigUsed();
				cu.setAppName(p.getProperty("appName"));
				cu.setProcessId(p.getProperty("processId"));
				cu.setAppPath(p.getProperty("appPath"));
				cu.setConfigPath(p.getProperty("configPath"));
				cu.setServerIp(p.getProperty("serverIp"));
				cu.setCreateTime(Long.valueOf(p.getProperty("createTime")));
				cu.setStartConf(p.getProperty("startConf"));
				cu.setSystemConf(p.getProperty("systemConf"));
				if(StringUtils.isEmpty(serverIp) && StringUtils.isEmpty(configPath)) {
					listcus.add(cu);
				} else if (!StringUtils.isEmpty(serverIp) && !StringUtils.isEmpty(configPath)) {
					if(cu.getServerIp().startsWith(serverIp) && cu.getConfigPath().startsWith(configPath)) {
		        		listcus.add(cu);
		        	}
				} else if (!StringUtils.isEmpty(serverIp) && StringUtils.isEmpty(configPath)) {
					if(cu.getServerIp().startsWith(serverIp)) {
		        		listcus.add(cu);
		        	}
				} else if (StringUtils.isEmpty(serverIp) && !StringUtils.isEmpty(configPath)) {
					if(cu.getConfigPath().startsWith(configPath)) {
		        		listcus.add(cu);
		        	}
				}
				p.clear();
			}
		}
		
		Collections.sort(listcus, new Comparator<ConfigUsed>() {

			@Override
			public int compare(ConfigUsed o1, ConfigUsed o2) {
 				 if(o1.getCreateTime() > o2.getCreateTime()) {
 					 return -1;
 				 } else {
 					 return 1;
 				 }
 				
			}
			
		});
		
		PageView<ConfigUsed> pv = new PageView<ConfigUsed>(lists.size(), listcus);
        String res = JsonUtils.toJson(pv, null,null, DateUtils.DATE_HH_MM_SS);
		ResponseUtils.responseJson(response, res);
		return null;
	}

}
