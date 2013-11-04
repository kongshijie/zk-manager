package com.dj.zk.manager.service;

import java.util.List;

import com.dj.zk.manager.model.ZkModel;

/**
 * 
 * @description: 
 * @version  Ver 1.0
 * @author   <a href="mailto:zuiwoxing@gmail.com">dejian.liu</a>
 * @Date	 2013-11-3 下午6:26:40
 */
public interface ZookeeperService {
	
	/**
	 * @description 取得当前path下的子节点
	 * @param path
	 * @return
	 */
	public List<ZkModel> listChildNode(String path);
	
	/**
	 * @description 取得当前节点下所有子节点
	 * @param path
	 * @return
	 */
	public List<String> getAllChilds(String path);
	
	/**
	 * @description 检查节点是否存在
	 * @param path
 	 * @return
	 */
	public boolean checkNodeExist(String path);
	
	/**
	 * @description 创建节点
	 * @param path
	 * @return
	 */
	public String createNode(String path,byte [] data);
	
	/**
	 * @description 直接创建不检查节点是否存在
	 * @param path
	 * @param data
	 * @return
	 */
	public String creaetNodeDirect(String path,byte [] data);
	
	/**
	 * @description 删除节点以及以下的子节点
	 * @param path
	 */
	public void deleteNode(String path);
	
	/**
	 * @description 保存数据
	 * @param path 
	 * @param datas
	 */
	public void setData(String path,byte [] datas);
	
	/**
	 * @description 根据节点path查询数据
	 * @param path
	 * @return
	 */
	public byte[] getData(String path);

	/**
	 * @description 节点复制
	 * @param sourceNode 源节点名称
	 * @param sourcesPath 源节点全路径 
	 * @param targetPath  目标节点全路径
	 */
	public void copyNodes(String sourceNode,String sourcesPath,String targetPath);
	
	
	
}
