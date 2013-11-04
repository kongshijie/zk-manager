package com.dj.zk.manager.action;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dj.zk.manager.commons.Constants;
import com.dj.zk.manager.model.dto.ConnectorDto;
import com.dj.zk.manager.model.dto.WatcherNodeTreeDto;
import com.dj.zk.manager.service.CommonService;
import com.dj.zk.manager.utils.json.JsonUtils;
import com.dj.zk.manager.utils.response.ResponseUtils;

/**
 * 
 * @description:zookeeper watcher
 * @version  Ver 1.0
 * @author   <a href="mailto:zuiwoxing@gmail.com">dejian.liu</a>
 * @Date	 2013-11-3 下午6:21:11
 */
@Controller
@RequestMapping(value = Constants.BASE_PATH + "watcher")
public class WatcherAction {
 
	

	@Autowired
	private CommonService commonService;

 	@RequestMapping(value = "listwatcherbyclient", method = {RequestMethod.POST})
	@ResponseBody
	public List<WatcherNodeTreeDto> listConBySession(HttpServletRequest request,HttpServletResponse response) {
 		String clientHost = request.getParameter("id");
  		List<WatcherNodeTreeDto> listRes = new ArrayList<WatcherNodeTreeDto>();
 		listRes = commonService.listAllByClient(clientHost);
		return listRes;
	}

 	@RequestMapping(value = "clientInfo", method = {RequestMethod.GET})
	public void hostInfo(HttpServletRequest request,HttpServletResponse response) {
 		
		List<ConnectorDto> listDtos = commonService.listConnector(null);
		
		Collections.sort(listDtos, new Comparator<ConnectorDto>() {
			@Override
			public int compare(ConnectorDto o1, ConnectorDto o2) {
				if(StringUtils.isNotEmpty(o1.getClientHostPort()) 
						&& StringUtils.isNotEmpty(o2.getClientHostPort())
						&& o1.getClientHost().compareTo(o2.getClientHost()) > 0
						) {
	                  return 1;
				}
 				return 0;
			}
		});
		
		String [][] clientInfoData = new String[listDtos.size()][2];
		
 		int index = 0;
 		Set<String> sets = new HashSet<String>();
		for (ConnectorDto dto : listDtos) {
 			clientInfoData[index][0] = dto.getClientHostPort();
			clientInfoData[index][1] = dto.getClientHostPort();
			sets.add(dto.getClientHost());
			index++;
		}
 		String [][] clientHostData = new String[sets.size()][2];
		index = 0;
		for (String host : sets) {
			clientHostData[index][0] = host;
			clientHostData[index][1] = host;
			index++;
		}
 
		StringBuffer buf = new StringBuffer();
		buf.append("var clientInfoData = ").append(JsonUtils.toJson(clientInfoData, null, null, null)).append(";\r\n");
		buf.append("var clientHostData = ").append(JsonUtils.toJson(clientHostData, null, null, null)).append(";\r\n");
         ResponseUtils.responseScript(response, buf.toString());
	}
 	
 	
 	@RequestMapping(value = "listwatcherbynode", method = {RequestMethod.POST})
	@ResponseBody
	public List<WatcherNodeTreeDto> listConByNode(HttpServletRequest request,HttpServletResponse response) {
 		String host = request.getParameter("id");
 		List<WatcherNodeTreeDto> listRes = new ArrayList<WatcherNodeTreeDto>();
 		InetSocketAddress address = Constants.getCacheZkHostMap().get(host);
 		if(address != null) {
 			listRes = commonService.listAllWatcherNode(address);
  		} else {
 			 listRes = commonService.listAllWatcherNode();
 		}
	    System.out.println("dataSize:"+listRes.size());
		if(listRes != null && listRes.size() > 1000) {
			listRes = listRes.subList(0,1000);
		}
 		return listRes;
 	}
 	
  
}
