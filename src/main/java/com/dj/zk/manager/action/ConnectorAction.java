package com.dj.zk.manager.action;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dj.zk.manager.commons.Constants;
import com.dj.zk.manager.model.PageView;
import com.dj.zk.manager.model.dto.ConnectorDto;
import com.dj.zk.manager.service.CommonService;

/**
 * 
 * @description:列出所有连接者
 * @version  Ver 1.0
 * @author   <a href="mailto:zuiwoxing@gmail.com">dejian.liu</a>
 * @Date	 2013-11-3 下午6:20:04
 */
@Controller
@RequestMapping(value = Constants.BASE_PATH + "connector")
public class ConnectorAction {
	
	@Autowired
	private CommonService commonService;
	
 	@RequestMapping(value = "listconnectors", method = {RequestMethod.POST})
	@ResponseBody
	public PageView<ConnectorDto> listConnectors(HttpServletRequest request,HttpServletResponse response) throws IOException {
 		String host = request.getParameter("host");
 		String clientIp = request.getParameter("clientIp");
 		
 		InetSocketAddress address = Constants.getCacheZkHostMap().get(host);
 		List<ConnectorDto> res = new ArrayList<ConnectorDto>();
 		if(address != null) {
 			res = commonService.listConnector(address,clientIp);
 		} else {
 			res = commonService.listConnector(clientIp);
 		}
 
 		Collections.sort(res, new Comparator<ConnectorDto>() {
			@Override
			public int compare(ConnectorDto o1, ConnectorDto o2) {
				if(o1.getZooHost().compareTo(o2.getZooHost()) > 0) {
					return -1;
				}  
				if (o1.getZooHost().equals(o2.getZooHost())  && StringUtils.isNotEmpty(o1.getClientHostPort()) 
						&& StringUtils.isNotEmpty(o2.getClientHostPort()) 
						&& o1.getClientHostPort().compareTo(o2.getClientHostPort()) > 0) {
					return 1;
				}
 				return 0;
			}
		});
 		return new PageView<ConnectorDto>(res.size(),res);
 	}

}
