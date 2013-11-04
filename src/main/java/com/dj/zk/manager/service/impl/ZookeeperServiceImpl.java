package com.dj.zk.manager.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.zookeeper.ZooDefs.Ids;
import org.springframework.stereotype.Service;

import com.dj.zk.manager.commons.Constants;
import com.dj.zk.manager.exceptions.GeneralException;
import com.dj.zk.manager.model.ZkModel;
import com.dj.zk.manager.service.ZookeeperService;
import com.dj.zk.manager.utils.zookeeper.ZookeeperTransporter;
import com.dj.zk.manager.utils.zookeeper.curator.CuratorZookeeperClient;
import com.dj.zk.manager.utils.zookeeper.curator.CuratorZookeeperTransporter;

 
@Service
public class ZookeeperServiceImpl implements ZookeeperService {

	private static Logger logger = Logger.getLogger(ZookeeperServiceImpl.class);
	
	private ZookeeperTransporter zookeeperTransporter;

	private CuratorZookeeperClient zookeeperClient;

	public ZookeeperServiceImpl() {
		zookeeperTransporter = new CuratorZookeeperTransporter();
		String connections = Constants.getZookeeperConnectUrl();
		if (StringUtils.isEmpty(connections)) {
			throw new GeneralException("zookeeper connections can't null!");
		}
		if(StringUtils.isNotEmpty(Constants.getZooDigestSecret())) {
			zookeeperClient = zookeeperTransporter.connect(connections, Constants.getZooDigestSecret().getBytes());
		} else {
			zookeeperClient = zookeeperTransporter.connect(connections);
		}
	}
	
	/**
	 * 初始化根节点
	 */
	@PostConstruct
	public void init() {
		try {
			if(!zookeeperClient.isExist(Constants.getDefaultRoot())) {
				if(StringUtils.isEmpty(Constants.getZooDigestSecret())) {
					zookeeperClient.create(Constants.getDefaultRoot(), true,false, null, Ids.OPEN_ACL_UNSAFE);
				} else {
					zookeeperClient.create(Constants.getDefaultRoot(), true,false, null, Ids.CREATOR_ALL_ACL);
				}
			}
			if(!zookeeperClient.isExist(Constants.ZOO_CONF_DATA)) {
				if(StringUtils.isEmpty(Constants.getZooDigestSecret())) {
					zookeeperClient.create(Constants.ZOO_CONF_DATA, true,false, null, Ids.OPEN_ACL_UNSAFE);
				} else {
					zookeeperClient.create(Constants.ZOO_CONF_DATA, true,false, null, Ids.CREATOR_ALL_ACL);
				}
			}
			
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		
	}

	@Override
	public List<ZkModel> listChildNode(String path) {
		List<ZkModel> listZks = new ArrayList<ZkModel>();
		try {
			List<String> lists = zookeeperClient.getChildren(path);
			if (lists != null && lists.size() > 0) {
				for (String s : lists) {
					String childPath;
					if(!path.endsWith("/")) {
						childPath = path + "/" + s;
					} else {
						childPath = path + s;
					}
  					byte[] bufs = zookeeperClient.getData(childPath);
					ZkModel zk = new ZkModel();
					zk.setText(s);
					zk.setPath(childPath);
					List<String> listChilds = zookeeperClient.getChildren(childPath);
					if(listChilds != null && listChilds.size() > 0) {
						zk.setLeaf(false);
					} else {
						zk.setLeaf(true);
					}
					if(bufs != null) {
						zk.setData(new String(bufs, Constants.charset));
					}
  					listZks.add(zk);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return listZks;
	}

	@Override
	public boolean checkNodeExist(String path) {
		return zookeeperClient.isExist(path);
 
	}

	/**
	 * 如果已经存在抛出异常
	 */
	@Override
	public String createNode(String path,byte [] data) {
		boolean flag = checkNodeExist(path);
		if(flag) {
			throw new GeneralException(path + " 已经存在!");
		}
		if(StringUtils.isEmpty(Constants.getZooDigestSecret())) {
			return zookeeperClient.create(path, true, false, data,Ids.OPEN_ACL_UNSAFE);
		}
		return zookeeperClient.create(path, true, false, data,Ids.CREATOR_ALL_ACL);//ACL授权
	}

	public String creaetNodeDirect(String path,byte [] data) {
		if(StringUtils.isEmpty(Constants.getZooDigestSecret())) {
			zookeeperClient.create(path, true, false, data,Ids.OPEN_ACL_UNSAFE);
		}
		return zookeeperClient.create(path, true, false, data,Ids.CREATOR_ALL_ACL);//ACL授权
	}
	
	@Override
	public void deleteNode(String path) {
		if(this.checkNodeExist(path)) {
			List<String> childs = zookeeperClient.getChildren(path);
			for (String child : childs) {
				String node = path + "/"+child;
				deleteNode(node);
			}
			zookeeperClient.delete(path);
		}
	}
 
	@Override
	public void setData(String path, byte[] data) {
		zookeeperClient.setData(path, data);
	}

	@Override
	public List<String> getAllChilds(String path) {
		List<String> listRes = new ArrayList<String>();
		getAllChilds(path, listRes);
		return listRes;
	}
	
	private void getAllChilds(String path,List<String> listRes) {
		List<String> listPaths = zookeeperClient.getChildren(path);
		if(listPaths != null && listPaths.size() > 0){
			for (String p : listPaths) {
				String fullPath = path+"/"+p;
 				listRes.add(fullPath);
 				List<String> childs = zookeeperClient.getChildren(fullPath);
				if(childs != null && childs.size() > 0) {
					for (String s : childs) {
						String childPath = fullPath+"/"+s;
						listRes.add(childPath);
 						getAllChilds(childPath, listRes);
					}
				}
			}
		}
	}

	@Override
	public byte [] getData(String path) {
		return zookeeperClient.getData(path);
	}

	
	@Override
	public void copyNodes(String sourceNode,String sourcesPath, String targetPath) {
		//第一步:创建第一个节点,如果节点存在,那么就进行数据覆盖。
		String firstNodePath = targetPath;
		if(firstNodePath.endsWith("/")) {
			firstNodePath=firstNodePath+sourceNode;
		} else {
			firstNodePath=firstNodePath+"/"+sourceNode;
		}
		byte[] sourceData = this.getData(sourcesPath);
		if(checkNodeExist(firstNodePath)) {
			this.setData(firstNodePath, sourceData);
		} else {
			this.creaetNodeDirect(firstNodePath, sourceData);//这样不会触发监听
		}
 
		List<String> lists = getAllChilds(sourcesPath);
	 
	    if(lists != null) {
	    	for (String chidpath : lists) {
	    		byte [] data = this.getData(chidpath);
	    		String newPath = StringUtils.substringAfter(chidpath,sourceNode);
	    		newPath = firstNodePath+newPath;
 	    		if(checkNodeExist(newPath)) {
	    			this.setData(newPath, data);
	    		} else {
	    			this.creaetNodeDirect(newPath, data);
	    		}
			}
	    	
	    }
	}
 

}
