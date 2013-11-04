package com.dj.zk.manager.utils;

import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import com.dj.zk.manager.commons.Constants;
import com.dj.zk.manager.enums.UserType;
import com.dj.zk.manager.model.UserInfo;


/**
 * 
 * @description:权限 URL 配置
 * @version  Ver 1.0
 * @author   <a href="mailto:zuiwoxing@gmail.com">dejian.liu</a>
 * @Date	 2013-11-3 下午6:29:00
 */
public class PrivilegeUtils {
 
	
	/**
	 * <url,角色>
	 */
	public static ConcurrentHashMap<String, UserType[]> resourceMap = new ConcurrentHashMap<String, UserType[]>();
 
    
	static {
		resourceMap.put("zoo/list.json", new UserType[]{UserType.ADMIN,UserType.COMMON});
		resourceMap.put("zoo/create.json", new UserType[]{UserType.ADMIN});
		resourceMap.put("zoo/delete.json", new UserType[]{UserType.ADMIN});
		resourceMap.put("zoo/save.json", new UserType[]{UserType.ADMIN});
		resourceMap.put("zoo/finddata.json", new UserType[]{UserType.ADMIN,UserType.COMMON});
		resourceMap.put("zoo/nodecopy.json", new UserType[]{UserType.ADMIN});
		resourceMap.put("used/query.json", new UserType[]{UserType.ADMIN,UserType.COMMON});
		resourceMap.put("zoo/config.do", new UserType[]{UserType.ADMIN,UserType.COMMON});
		
		
		resourceMap.put("login/login.json", new UserType[]{UserType.ADMIN,UserType.COMMON});
		resourceMap.put("index/index.do", new UserType[]{UserType.ADMIN,UserType.COMMON});
		resourceMap.put("page/index.do", new UserType[]{UserType.ADMIN,UserType.COMMON});
		resourceMap.put("page/main.do", new UserType[]{UserType.ADMIN,UserType.COMMON});
		resourceMap.put("page/zoo.do", new UserType[]{UserType.ADMIN,UserType.COMMON});
		resourceMap.put("page/login.do", new UserType[]{UserType.ADMIN,UserType.COMMON});
		resourceMap.put("page/usedapp.do", new UserType[]{UserType.ADMIN,UserType.COMMON});
		resourceMap.put("page/command.do", new UserType[]{UserType.ADMIN,UserType.COMMON});
		resourceMap.put("page/watcher.do", new UserType[]{UserType.ADMIN,UserType.COMMON});
		resourceMap.put("page/connector.do", new UserType[]{UserType.ADMIN,UserType.COMMON});
		resourceMap.put("verify/code.do", new UserType[]{UserType.ADMIN,UserType.COMMON});
		resourceMap.put("command/hostinfo.do", new UserType[]{UserType.ADMIN,UserType.COMMON});
		resourceMap.put("command/execute.do", new UserType[]{UserType.ADMIN,UserType.COMMON});
		resourceMap.put("connector/listconnectors.do", new UserType[]{UserType.ADMIN,UserType.COMMON});
		
		resourceMap.put("watcher/listwatcherbyclient.do", new UserType[]{UserType.ADMIN,UserType.COMMON});
		resourceMap.put("watcher/listwatcherbynode.do", new UserType[]{UserType.ADMIN,UserType.COMMON});
		resourceMap.put("watcher/clientInfo.do", new UserType[]{UserType.ADMIN,UserType.COMMON});		
		resourceMap.put("page/watcherclient.do", new UserType[]{UserType.ADMIN,UserType.COMMON});
		resourceMap.put("page/monitor.do", new UserType[]{UserType.ADMIN,UserType.COMMON});
		resourceMap.put("monitor/serverstatus.do", new UserType[]{UserType.ADMIN,UserType.COMMON});
		resourceMap.put("monitor/monitorall.do", new UserType[]{UserType.ADMIN,UserType.COMMON});
		
	}
	
	/**
	 * @description 检查用户权限
	 * @param userInfo
	 * @return
	 */
	public static boolean checkPrivilege(HttpServletRequest request,UserInfo userInfo) {
		String uri = request.getRequestURI();
		String contextPath = request.getContextPath();
		String prex = contextPath+Constants.BASE_PATH;
		String res = StringUtils.substringAfterLast(uri, prex);
		UserType [] userTypes = resourceMap.get(res);
		UserType ut = UserType.parse(userInfo.getUserType());
		return ArrayUtils.contains(userTypes, ut);
	}
 
 
}
