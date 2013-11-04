package com.dj.zk.manager.service;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;

import com.dj.zk.manager.model.dto.ConnectorDto;
import com.dj.zk.manager.model.dto.ServerStatusDto;
import com.dj.zk.manager.model.dto.WatcherNodeTreeDto;


/**
 * 
 * @description:
 * @version  Ver 1.0
 * @author   <a href="mailto:zuiwoxing@gmail.com">dejian.liu</a>
 * @Date	 2013-11-3 下午6:26:08
 */
public interface CommonService {
	
	/**
	 * @description 根据zoo地址查询所有连接者
	 * @param address
	 * @return
	 */
	public List<ConnectorDto> listConnector(InetSocketAddress address,String clientIp)throws IOException ;

	/**
	 * @description 列出所有 
	 * @return
	 * @throws IOException
	 */
	public List<ConnectorDto> listConnector(String clientIp);
	
	/**
	 * @description 列出所有监听者
	 * @return
	 */
	public 	List<WatcherNodeTreeDto> listAllWatcherNode();
	
	/**
	 * @description 根据客户端IP列出所有watcher
	 * @return
	 */
	public List<WatcherNodeTreeDto> listAllByClient(String clientHost);
	
	public 	List<WatcherNodeTreeDto> listAllWatcherNode(InetSocketAddress address);
	
	/**
	 * 列出所有ZKServer状态
	 * @return
	 */
	public List<ServerStatusDto> listAllServerStatus();
	
 
 
}
