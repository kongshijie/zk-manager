package com.dj.zk.manager.utils.zookeeper;

/**
 * 
 * @description:
 * @version  Ver 1.0
 * @author   <a href="mailto:zuiwoxing@gmail.com">dejian.liu</a>
 * @Date	 2013-11-3 下午6:28:12
 */
public interface StateListener {

	int DISCONNECTED = 0;

	int CONNECTED = 1;

	int RECONNECTED = 2;

	void stateChanged(int connected);

}
