package com.dj.zk.manager.action;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.zookeeper.client.FourLetterWordMain;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dj.zk.manager.commons.Constants;
import com.dj.zk.manager.commons.ResponseEntity;
import com.dj.zk.manager.utils.json.JsonUtils;
import com.dj.zk.manager.utils.response.ResponseUtils;

/**
 * 
 * @description:命令行
 * @version  Ver 1.0
 * @author   <a href="mailto:zuiwoxing@gmail.com">dejian.liu</a>
 * @Date	 2013-11-3 下午6:19:06
 */
@Controller
@RequestMapping(value = Constants.BASE_PATH + "command")
public class CommandAction {

	private static Logger logger = Logger.getLogger(CommandAction.class);
	
	
 	
 	@RequestMapping(value = "execute", method = {RequestMethod.POST})
	@ResponseBody
	public ResponseEntity commandExecute(HttpServletRequest request,HttpServletResponse response) {
		String zkHost = request.getParameter("host");
		String command = request.getParameter("command");
		InetSocketAddress address = Constants.getCacheZkHostMap().get(zkHost);
		ResponseEntity re = new ResponseEntity();
		if(address != null) {
			try {
				String res = FourLetterWordMain.send4LetterWord(address.getHostName(),address.getPort(), command);
				re.setMessage(res);
				re.setStatus(HttpServletResponse.SC_OK);
 			} catch (IOException e) {
				re.setMessage(e.getMessage());
				re.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				logger.error(e.getMessage(),e);
			}
		} else {
			re.setMessage("zkhost can't null!");
			re.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
        return re;
	}
 	
 	
	@RequestMapping(value = "hostinfo", method = {RequestMethod.GET})
	public void hostInfo(HttpServletRequest request,HttpServletResponse response) {
		List<InetSocketAddress> listAddress = Constants.getZooAddresses();
		Map<String, String> commandMap = Constants.getZooCommandMap();
		String [][] hostInfoData = new String[listAddress.size()][2];
		String [][] commandData = new String[commandMap.size()][2];
		int index = 0;
		for (InetSocketAddress inet : listAddress) {
			hostInfoData[index][0] = inet.toString();
			hostInfoData[index][1] = inet.toString();
			index++;
		}
		index = 0;
		for(Iterator<Map.Entry<String, String>> it = commandMap.entrySet().iterator();it.hasNext();) {
			Map.Entry<String, String> entry = it.next();
			commandData[index][0] = entry.getKey();
			commandData[index][1] = entry.getValue();
			index++;
		}
		
		StringBuffer buf = new StringBuffer();
		buf.append("var hostInfoData = ").append(JsonUtils.toJson(hostInfoData, null, null, null)).append(";\r\n");
		buf.append("var commandData = ").append(JsonUtils.toJson(commandData, null, null, null)).append(";\r\n");
        ResponseUtils.responseScript(response, buf.toString());
	}
 	
 
}
