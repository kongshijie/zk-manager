package com.dj.zk.manager.utils.zookeeper.support;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArraySet;

import org.apache.log4j.Logger;
import org.apache.zookeeper.data.ACL;

import com.dj.zk.manager.utils.zookeeper.ChildListener;
import com.dj.zk.manager.utils.zookeeper.StateListener;
import com.dj.zk.manager.utils.zookeeper.ZookeeperClient;
 

/**
 * 
 * @description:引用dubbo 对zookeeper的部分封装，并且增加了新的API
 * @version  Ver 1.0
 * @author   <a href="mailto:zuiwoxing@gmail.com">dejian.liu</a>
 * @Date	 2013-11-3 下午6:27:55
 */
public abstract class AbstractZookeeperClient<TargetChildListener> implements ZookeeperClient {

	protected static final Logger logger = Logger.getLogger(AbstractZookeeperClient.class);

    private String connections;

    private byte [] auths;
    
	private final Set<StateListener> stateListeners = new CopyOnWriteArraySet<StateListener>();

	private final ConcurrentMap<String, ConcurrentMap<ChildListener, TargetChildListener>> childListeners = new ConcurrentHashMap<String, ConcurrentMap<ChildListener, TargetChildListener>>();

	private volatile boolean closed = false;

	public AbstractZookeeperClient(String connections,byte [] auths) {
		this.connections = connections;
		this.auths = auths;
	}

	public String getConnections() {
		return connections;
	}


	public void setConnections(String connections) {
		this.connections = connections;
	}

	public byte[] getAuths() {
		return auths;
	}


	public void setAuths(byte[] auths) {
		this.auths = auths;
	}


 

	/**
	 * @param path 路径
	 * @param isPersist  是否持久
	 * @param isSequeue 是否有序
	 * @param 权限
	 */
	public String create(String path, boolean isPersist,boolean isSequence,byte [] data,ArrayList<ACL> acls) {
 
		if(isPersist && isSequence) {
			return createPersistentSequence(path, data,acls);
		} else if (isPersist && !isSequence) {
			return createPersistentNoSequence(path,data, acls);
		} else if (!isPersist && isSequence) {
			return createEphemeralSequence(path,data, acls);
		} else if (!isPersist && !isSequence){
			return createEphemeralNoSequence(path,data, acls);
		}
       return null;
	}
	
	

	public void addStateListener(StateListener listener) {
		stateListeners.add(listener);
	}

	public void removeStateListener(StateListener listener) {
		stateListeners.remove(listener);
	}

	public Set<StateListener> getSessionListeners() {
		return stateListeners;
	}

	public List<String> addChildListener(String path, final ChildListener listener) {
		ConcurrentMap<ChildListener, TargetChildListener> listeners = childListeners.get(path);
		if (listeners == null) {
			childListeners.putIfAbsent(path, new ConcurrentHashMap<ChildListener, TargetChildListener>());
			listeners = childListeners.get(path);
		}
		TargetChildListener targetListener = listeners.get(listener);
		if (targetListener == null) {
			listeners.putIfAbsent(listener, createTargetChildListener(path, listener));
			targetListener = listeners.get(listener);
		}
		return addTargetChildListener(path, targetListener);
	}

	public void removeChildListener(String path, ChildListener listener) {
		ConcurrentMap<ChildListener, TargetChildListener> listeners = childListeners.get(path);
		if (listeners != null) {
			TargetChildListener targetListener = listeners.remove(listener);
			if (targetListener != null) {
				removeTargetChildListener(path, targetListener);
			}
		}
	}

	protected void stateChanged(int state) {
		for (StateListener sessionListener : getSessionListeners()) {
			sessionListener.stateChanged(state);
		}
	}

	public void close() {
		if (closed) {
			return;
		}
		closed = true;
		try {
			doClose();
		} catch (Throwable t) {
			logger.warn(t.getMessage(), t);
		}
	}

	protected abstract void doClose();

	protected abstract String createPersistentSequence(String path,byte [] data,ArrayList<ACL> acls);

	protected abstract String createPersistentNoSequence(String path,byte [] data,ArrayList<ACL> acls);
	
	protected abstract String createEphemeralSequence(String path,byte [] data,ArrayList<ACL> acls);
	
	protected abstract String createEphemeralNoSequence(String path,byte [] data,ArrayList<ACL> acls);

	protected abstract TargetChildListener createTargetChildListener(String path, ChildListener listener);

	protected abstract List<String> addTargetChildListener(String path, TargetChildListener listener);

	protected abstract void removeTargetChildListener(String path, TargetChildListener listener);

}
