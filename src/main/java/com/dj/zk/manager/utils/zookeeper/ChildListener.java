package com.dj.zk.manager.utils.zookeeper;

import java.util.List;

/**
 * 
 * @description:
 * @version  Ver 1.0
 * @author   <a href="mailto:zuiwoxing@gmail.com">dejian.liu</a>
 * @Date	 2013-11-3 下午6:28:07
 */
public interface ChildListener {

	void childChanged(String path, List<String> children);

}
