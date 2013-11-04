package com.dj.zk.manager.utils.zookeeper.curator;

import com.dj.zk.manager.utils.zookeeper.ZookeeperTransporter;


/**
 * 
 * @description:
 * @version  Ver 1.0
 * @author   <a href="mailto:zuiwoxing@gmail.com">dejian.liu</a>
 * @Date	 2013-11-3 下午6:27:43
 */
public class CuratorZookeeperTransporter implements ZookeeperTransporter {

	@Override
	public CuratorZookeeperClient connect(String connections) {
		return new CuratorZookeeperClientImpl(connections,null);
	}

	@Override
	public CuratorZookeeperClient connect(String connections, byte[] auths) {
		return new CuratorZookeeperClientImpl(connections,auths);
	}

 

}
