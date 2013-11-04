package com.dj.zk.manager.action;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dj.zk.manager.commons.Constants;
import com.dj.zk.manager.commons.ResponseEntity;
import com.dj.zk.manager.model.PageView;
import com.dj.zk.manager.model.dto.ServerStatusDto;
import com.dj.zk.manager.service.CommonService;
import com.dj.zk.manager.utils.DateUtils;
import com.dj.zk.manager.utils.HttpClientProxy;

/**
 * 
 * @description:监控Action
 * @version  Ver 1.0
 * @author   <a href="mailto:zuiwoxing@gmail.com">dejian.liu</a>
 * @Date	 2013-11-3 下午4:18:41
 */
@Controller
@RequestMapping(value = Constants.BASE_PATH + "monitor")
public class MonitorAction {

	@Autowired
	private CommonService commonService;
 
	
	@RequestMapping(value="serverstatus", method = RequestMethod.GET)
	@ResponseBody
	public PageView<ServerStatusDto> listAllServerStatus(HttpServletRequest request,
			HttpServletResponse response) {
		PageView<ServerStatusDto> pv = new PageView<ServerStatusDto>();
		List<ServerStatusDto> lists = commonService.listAllServerStatus();
		pv.setItems(lists);
		pv.setTotalCount(lists.size());
		
		return pv;
	}
	
	
 
	@RequestMapping(value="monitorall", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity monitorall(HttpServletRequest request,
			HttpServletResponse response) {
		ResponseEntity res = new ResponseEntity();
		String timeInterval = request.getParameter("timeInterval");
		if(StringUtils.isEmpty(timeInterval) || !StringUtils.isNumeric(timeInterval.trim())) {
			res.setMessage("时间(s)间隔不合法!");
		}
		String failUrl = request.getParameter("failUrl");
		List<ServerStatusDto> lists = commonService.listAllServerStatus();
		StringBuffer buf = new StringBuffer();
		buf.append("======"+DateUtils.format(new Date(),DateUtils.DATE_HH_MM_SS)+"====\r\n");
		for (ServerStatusDto sdto : lists) {
			buf.append("ZK服务:").append(sdto.getHostPort()).append("\r\n");
			buf.append("MODE:").append(sdto.getMode()).append("\r\n");
			buf.append("连接数:").append(sdto.getConnections()).append("\r\n");
			buf.append("节点数:").append(sdto.getNodeCount()).append("\r\n");
			buf.append("状态:").append(sdto.getStatus()).append("\r\n");
			buf.append("版本:").append(sdto.getServerVersion()).append("\r\n");		
			if("dead".equals(sdto.getStatus())) {
				Map<String, Object> parameters = new HashMap<String, Object>();
				parameters.put("zkserver", sdto.getHostPort());
				parameters.put("status", sdto.getStatus());
				String failUrlRes = HttpClientProxy.doGet(failUrl, parameters, "UTF-8");
				buf.append(failUrlRes).append("\r\n");
			}
		}
		
		res.setMessage(buf.toString());
		return res;
	}

}
