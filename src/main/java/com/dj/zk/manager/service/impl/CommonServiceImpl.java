package com.dj.zk.manager.service.impl;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.zookeeper.client.FourLetterWordMain;
import org.springframework.stereotype.Service;

import com.dj.zk.manager.commons.Constants;
import com.dj.zk.manager.model.dto.ConnectorDto;
import com.dj.zk.manager.model.dto.ServerStatusDto;
import com.dj.zk.manager.model.dto.WatcherNodeTreeDto;
import com.dj.zk.manager.service.CommonService;
import com.dj.zk.manager.utils.DateUtils;


@Service
public class CommonServiceImpl implements CommonService {

	private static Logger logger = Logger.getLogger(CommonServiceImpl.class);
 
	@Override
	public List<ConnectorDto> listConnector(InetSocketAddress address,String clientIp) {
			String res = getResDataByInetAddressCommand(address,"cons");
 		    return parseConnectorsRes(res, address,clientIp);
	}
 
	
	private static String getResDataByInetAddressCommand(InetSocketAddress address,String command) {
		try {
			return FourLetterWordMain.send4LetterWord(address.getHostName(),address.getPort(),command);
		} catch (IOException e) {
			logger.error(e.getMessage(),e);
		}
		return "";
	}
	

	@Override
	public List<ConnectorDto> listConnector(String clientIp){
		List<ConnectorDto> listRes = new ArrayList<ConnectorDto>();
		List<InetSocketAddress> lists = Constants.getZooAddresses();
		for (InetSocketAddress inetSocketAddress : lists) {
			listRes.addAll(listConnector(inetSocketAddress,clientIp));
		}
 		return listRes;
	}
	
	
	/**
	 * 
	 * @param res
	 * @param address
	 * @return Map<sessionId,ConnectorDto>
	 */
	private static Map<String, ConnectorDto> parseConectorsToMap(String res,InetSocketAddress address) {
		 Map<String, ConnectorDto> map = new HashMap<String, ConnectorDto>();
		String [] resAry = res.split("\\)");
 		for (String line : resAry) {
			if(StringUtils.isEmpty(line) || StringUtils.isEmpty(line.trim())) {
				continue;
			}
			ConnectorDto dto = parseSingleData(line);
			if(dto != null) {
				dto.setZooHost(address.toString());
				map.put(dto.getSessionId(), dto);
			}
		}
		return map;
	}
	
 
	
	public List<ConnectorDto> parseConnectorsRes(String res,InetSocketAddress address,String clientIp) {
		List<ConnectorDto> listRes = new ArrayList<ConnectorDto>();
		if(StringUtils.isEmpty(res)) {
			return listRes;
		}
		String [] resAry = res.split("\\)");
 		for (String line : resAry) {
			if(StringUtils.isEmpty(line) || StringUtils.isEmpty(line.trim())) {
				continue;
			}
			ConnectorDto dto = parseSingleData(line);
			if(dto != null) {
				
				dto.setZooHost(address.toString());
				
				if(StringUtils.isNotEmpty(clientIp)) {
					if(clientIp.equals(dto.getClientHost())) {
						listRes.add(dto);
					}
				} else {
					listRes.add(dto);
				}
				
				
			}
		}
		return listRes;
	}
	
	/**
	 * @param res
	 * @return
	 */
	private static ConnectorDto parseSingleData(String line) {
		 
 		ConnectorDto cd = new ConnectorDto();
		line = line.replaceAll("/", "").replaceAll("\\[.\\]","").replaceAll("\\)","");
		String [] ary = line.split("\\(");
		if(ary == null || ary.length !=2) {
			return cd;
		}
 		cd.setClientHostPort(ary[0].trim());
		String lineOne = ary[1];
		Map<String, String> map = parseDetail(lineOne);
		String est = map.get("est");
		if(StringUtils.isNotEmpty(est)) {
			cd.setCreateConTime(Long.valueOf(est));
		}
		String lresp = map.get("lresp");
		if(StringUtils.isNotEmpty(lresp)) {
			cd.setLastEstTime(Long.valueOf(lresp));
		}
		
		String sent = map.get("sent");
		if(StringUtils.isNotEmpty(sent)) {
			cd.setSent(Long.valueOf(sent));
		}
		
		String recved = map.get("recved");
		if(StringUtils.isNotEmpty(recved)) {
			cd.setRecved(Long.valueOf(recved));
		}
		
		String sid = map.get("sid");
		if(StringUtils.isNotEmpty(sid)) {
			cd.setSessionId(sid);
		}
 
 		return cd;
	}

	private static Map<String, String> parseDetail(String lineOne) {
		Map<String, String> map = new HashMap<String, String>();
		String ary [] = lineOne.split(",");
		for (String s : ary) {
			String keyValue [] = s.split("=");
			map.put(keyValue[0].trim(), keyValue[1].trim());
		}
		return map;
	}

	


	@Override
	public List<WatcherNodeTreeDto> listAllWatcherNode(InetSocketAddress address) {
		 List<WatcherNodeTreeDto> resList = new ArrayList<WatcherNodeTreeDto>();
			/**
			 * key = sessionId
			 * value = ConnectorDto
			 */
			Map<String, ConnectorDto> nodeMap = new HashMap<String, ConnectorDto>();
			String res = getResDataByInetAddressCommand(address,"cons");
			nodeMap.putAll(parseConectorsToMap(res, address));
 
			
			/**
			 * key = node
			 * value = List<sessionId>
			 */
			Map<String, List<String>> nodeWatcherMap = new HashMap<String, List<String>>();
			String wchp = getResDataByInetAddressCommand(address,"wchp");
			nodeWatcherMap.putAll(parseLineToMap(address,wchp));

			
			/**
			 * 信息组合
			 */
			for(Iterator<Map.Entry<String, List<String>>> it =nodeWatcherMap.entrySet().iterator();it.hasNext(); ) {
				 Map.Entry<String, List<String>> entry = it.next();
				 WatcherNodeTreeDto dto = new WatcherNodeTreeDto();
				 dto.setNode(entry.getKey());
				 dto.setZkHost("");
				 List<String> sessionIds = entry.getValue();
				 Long costTime = Long.valueOf(0);
				 if(sessionIds != null && sessionIds.size() > 0) {
					 List<WatcherNodeTreeDto> childs = new ArrayList<WatcherNodeTreeDto>();
					 for (String sessionId : sessionIds) {
						 WatcherNodeTreeDto child = new WatcherNodeTreeDto();
						 child.setNode(sessionId);
						 ConnectorDto cd =  nodeMap.get(sessionId);
						 if(cd!= null) {
							 child.setClientIpHost(cd.getClientHostPort());
							 costTime+=cd.getSessionDif();
							 child.setSessionTime(cd.getSessionDifDate());
							 child.setZkHost(cd.getZooHost());
						 }
						 childs.add(child);
					}
					 dto.setChildren(childs);
				 }
				 
				 dto.setSessionTime("总计:"+DateUtils.subtractParse(costTime, "d-H-m-s"));
				 dto.setClientIpHost("总计:【"+dto.getChildren().size()+"】个");
				 resList.add(dto);
			}
			
		
			return resList;
	}

	
	
	@Override
	public List<WatcherNodeTreeDto> listAllWatcherNode() {
		 List<WatcherNodeTreeDto> resList = new ArrayList<WatcherNodeTreeDto>();
		 
		List<InetSocketAddress> lists = Constants.getZooAddresses();
		/**
		 * key = sessionId
		 * value = ConnectorDto
		 */
		Map<String, ConnectorDto> nodeMap = new HashMap<String, ConnectorDto>();
		for (InetSocketAddress address : lists) {
			String res = getResDataByInetAddressCommand(address,"cons");
			nodeMap.putAll(parseConectorsToMap(res, address));
		}
		
		/**
		 * key = node
		 * value = List<sessionId>
		 */
		Map<String, List<String>> nodeWatcherMap = new HashMap<String, List<String>>();
		for (InetSocketAddress address : lists) {
			String res = getResDataByInetAddressCommand(address,"wchp");
			nodeWatcherMap.putAll(parseLineToMap(address,res));
		}
		
		/**
		 * 信息组合
		 */
		for(Iterator<Map.Entry<String, List<String>>> it =nodeWatcherMap.entrySet().iterator();it.hasNext(); ) {
			 Map.Entry<String, List<String>> entry = it.next();
			 WatcherNodeTreeDto dto = new WatcherNodeTreeDto();
			 dto.setNode(entry.getKey());
			 dto.setSessionTime("");
			 dto.setZkHost("");
			 List<String> sessionIds = entry.getValue();
			 Long costTime = Long.valueOf(0);
			 if(sessionIds != null && sessionIds.size() > 0) {
				 List<WatcherNodeTreeDto> childs = new ArrayList<WatcherNodeTreeDto>();
				 for (String sessionId : sessionIds) {
					 WatcherNodeTreeDto child = new WatcherNodeTreeDto();
					 child.setNode(sessionId);
					 ConnectorDto cd =  nodeMap.get(sessionId);
					 if(cd!= null) {
						 costTime+=cd.getSessionDif();
						 child.setClientIpHost(cd.getClientHostPort());
						 child.setSessionTime(cd.getSessionDifDate());
						 child.setZkHost(cd.getZooHost());
					 }
					 childs.add(child);
				}
				 dto.setChildren(childs);
			 }
			 dto.setSessionTime("总计:"+DateUtils.subtractParse(costTime, "d-H-m-s"));
			 dto.setClientIpHost("总计:【"+dto.getChildren().size()+"】个");
			 resList.add(dto);
		}
		return resList;
	}


	
	@Override
	public List<WatcherNodeTreeDto> listAllByClient(String clientHost) {
		 List<WatcherNodeTreeDto> resList = new ArrayList<WatcherNodeTreeDto>();
		List<InetSocketAddress> lists = Constants.getZooAddresses();
 
		/**
		 * key = sessionId
		 * value = ConnectorDto
		 */
		Map<String, ConnectorDto> nodeMap = new HashMap<String, ConnectorDto>();
		for (InetSocketAddress address : lists) {
			String res = getResDataByInetAddressCommand(address,"cons");
			nodeMap.putAll(parseConectorsToMap(res, address));
		}
		
		
		
		/**
		 * key = sessionId
		 * value = List<nodePath>
		 */
		
		Map<String, List<String>> nodeWatcherMap = new HashMap<String, List<String>>();
		for (InetSocketAddress address : lists) {
			String res = getResDataByInetAddressCommand(address,"wchc");
			nodeWatcherMap.putAll(parseLineKeySessionToMap(res));
		}
		
		/**
		 * 数据组装
		 */
		for(Iterator<Map.Entry<String, List<String>>> it  = nodeWatcherMap.entrySet().iterator();it.hasNext();) {
			Map.Entry<String, List<String>> entry = it.next();
			List<String> lis = entry.getValue();
			String sessionId = entry.getKey();
			
			
			ConnectorDto cd = nodeMap.get(sessionId);
			
			//数据按客户端来过滤
			if(StringUtils.isNotEmpty(clientHost) && cd != null && !clientHost.equals(cd.getClientHostPort())) {
				continue;
			}
			
			WatcherNodeTreeDto dto = new WatcherNodeTreeDto();
			if(cd != null) {
				dto.setNode(cd.getClientHostPort());  //反过来将 clientHostIpPort作为node显示
				dto.setZkHost(cd.getZooHost());
				dto.setSessionTime(cd.getSessionDifDate());
			} else {
				dto.setNode(sessionId);
			}
			
			if(lis != null && lis.size() >0) {
				List<WatcherNodeTreeDto> childs = new ArrayList<WatcherNodeTreeDto>();
				for (String s : lis) {
					WatcherNodeTreeDto tmpDto = new WatcherNodeTreeDto();
					tmpDto.setNode(s);
					if(cd != null) {
						tmpDto.setClientIpHost("");
						tmpDto.setZkHost("");
						tmpDto.setSessionTime("");
					}
					
					childs.add(tmpDto);
				}
				dto.setChildren(childs);
			}
			if(dto.getChildren() !=null) {
				dto.setClientIpHost("总计:【"+dto.getChildren().size()+"】个节点");
			} else {
				dto.setClientIpHost("总计:【"+0+"】个节点");
			}
			resList.add(dto);
	    }
 		return resList;
	}

	
	
	
	/**
	 * @description 解析单行
	 * @param address
	 * @param res
	 * @return
	 */
	private  Map<String, List<String>> parseLineToMap(InetSocketAddress address,String res) {
		String [] arys = res.split("\n");		 
	    Pattern p = Pattern.compile("/.*");
	    Map<String, List<String>> tmpMap = new HashMap<String, List<String>>();
	    List<String> ls = null;
	    for(int i =0; i <arys.length; i++) {
	    	String line = arys[i];
	    	if(p.matcher(line).matches()) {
	    		ls = new ArrayList<String>();
	    		tmpMap.put(address.toString()+line,ls);
 	    	} else {
 	    		if(StringUtils.isNotEmpty(line) && StringUtils.isNotEmpty(line.trim())) {
 	 	    		ls.add(line.trim());
 	    		}
 	    	}
 	    }
	    return tmpMap;
	}
	
	
	/**
	 * 
	 * @param res
	 * @return Map<sessionid,nodes>
	 */
	private static  Map<String, List<String>> parseLineKeySessionToMap(String res) {
		String [] arys = res.split("\n");		 
	    Pattern p = Pattern.compile("/.*");
	    Map<String, List<String>> tmpMap = new ConcurrentHashMap<String, List<String>>();
	    List<String> ls = null;
	    for(int i =0; i <arys.length; i++) {
	    	String line = arys[i];
	    	if(StringUtils.isEmpty(line) && StringUtils.isEmpty(line.trim())) {
	    		continue;
	    	}
	    	if(p.matcher(line.trim()).matches()) {
	    		if(StringUtils.isNotEmpty(line) && StringUtils.isNotEmpty(line.trim())) {
 	 	    		ls.add(line.trim());
 	    		}
 	    	} else {
 	    		ls = new ArrayList<String>();
	    		tmpMap.put(line,ls);
 	    	}
 	    }
	    for(Iterator<Map.Entry<String, List<String>>> it  = tmpMap.entrySet().iterator();it.hasNext();) {
			Map.Entry<String, List<String>> entry = it.next();
			List<String> lis = entry.getValue();
			if(lis == null || lis.size() == 0) {
				tmpMap.remove(entry.getKey());
			}
 	     }
	    
	    return tmpMap;
	}
	

	public static void main(String[] args) {
		List<InetSocketAddress> addresses = Constants.getZooAddresses();
		InetSocketAddress a = addresses.get(0);
//		String res = getResDataByInetAddressCommand(a,"cons");
//		 Map<String, ConnectorDto> maps = 	parseConectorsToMap(res, addresses.get(0));
//		for(Iterator<Map.Entry<String, ConnectorDto>> it  = maps.entrySet().iterator();it.hasNext();) {
//			Map.Entry<String, ConnectorDto> entry = it.next();
//			System.out.println(entry.getKey()+"======"+ToStringBuilder.reflectionToString(entry.getValue()));
//		}
     
		String res = getResDataByInetAddressCommand(a,"wchc");
		System.out.println(res);
		System.out.println("======================================");
		Map<String, List<String>> maps = parseLineKeySessionToMap(res);
		
		for(Iterator<Map.Entry<String, List<String>>> it  = maps.entrySet().iterator();it.hasNext();) {
			Map.Entry<String, List<String>> entry = it.next();
			List<String> lis = entry.getValue();
			System.out.println(entry.getKey()+"======"+ Arrays.toString(lis.toArray(new String[lis.size()])));
	     }
 	
	}


	@Override
	public List<ServerStatusDto> listAllServerStatus() {
		List<ServerStatusDto> lists = new ArrayList<ServerStatusDto>();
		List<InetSocketAddress> listsNets = Constants.getZooAddresses();
		for (InetSocketAddress inet : listsNets) {
			String res = getResDataByInetAddressCommand(inet,"srvr");
			String [] ary = res.split("\n");
			ServerStatusDto ss = new ServerStatusDto();
			ss.setHostPort(inet.toString());
			ss.setServerVersion(ary[0].trim());
			ss.setReceived(Long.valueOf(ary[2].split(":")[1].trim()));
			ss.setSent(Long.valueOf(ary[3].split(":")[1].trim()));
			ss.setConnections(ary[4].split(":")[1]);
			ss.setMode(ary[7].split(":")[1].trim());
			ss.setNodeCount(ary[8].split(":")[1]);
			String imok = getResDataByInetAddressCommand(inet,"ruok");
			if(StringUtils.isNotEmpty(imok) && StringUtils.isNotEmpty(imok.trim()) && "imok".equals(imok.trim())) {
				ss.setStatus("activate");
			} else {
				ss.setStatus("dead");
			}
			lists.add(ss);
		}
		return lists;
	}
//	Zookeeper version: 3.4.5-1392090, built on 09/30/2012 17:52 GMT
//	Latency min/avg/max: 0/0/698
//	Received: 5214467
//	Sent: 5236267
//	Connections: 34
//	Outstanding: 0
//	Zxid: 0x6300113b4c
//	Mode: follower
//	Node count: 1334
 
	
 
}
