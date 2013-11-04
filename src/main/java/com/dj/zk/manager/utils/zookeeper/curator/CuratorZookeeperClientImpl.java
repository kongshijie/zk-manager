package com.dj.zk.manager.utils.zookeeper.curator;

import java.util.ArrayList;
import java.util.List;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;

import com.dj.zk.manager.utils.zookeeper.ChildListener;
import com.dj.zk.manager.utils.zookeeper.StateListener;
import com.dj.zk.manager.utils.zookeeper.support.AbstractZookeeperClient;
import com.netflix.curator.framework.CuratorFramework;
import com.netflix.curator.framework.CuratorFrameworkFactory;
import com.netflix.curator.framework.CuratorFrameworkFactory.Builder;
import com.netflix.curator.framework.api.CuratorWatcher;
import com.netflix.curator.framework.state.ConnectionState;
import com.netflix.curator.framework.state.ConnectionStateListener;
import com.netflix.curator.retry.RetryNTimes;

/**
 * 
 * @description:
 * @version  Ver 1.0
 * @author   <a href="mailto:zuiwoxing@gmail.com">dejian.liu</a>
 * @Date	 2013-11-3 下午6:27:36
 */
public class CuratorZookeeperClientImpl extends
		AbstractZookeeperClient<CuratorWatcher> implements
		CuratorZookeeperClient {

	private final CuratorFramework client;

	public CuratorZookeeperClientImpl(String connections, byte[] authoritys) {
		super(connections, authoritys);
		try {
			Builder builder = CuratorFrameworkFactory.builder()
					.connectString(connections)
					.retryPolicy(new RetryNTimes(Integer.MAX_VALUE, 2000)) // 设置为永久重试，每隔2秒重试一次
					.connectionTimeoutMs(30000);// 30秒连接超时

			if (authoritys != null && authoritys.length > 0) {
				builder = builder.authorization("digest", authoritys);
			}

			client = builder.build();
			client.getConnectionStateListenable().addListener(
					new ConnectionStateListener() {
						public void stateChanged(CuratorFramework client,
								ConnectionState state) {
							if (state == ConnectionState.LOST) {
								CuratorZookeeperClientImpl.this
										.stateChanged(StateListener.DISCONNECTED);
							} else if (state == ConnectionState.CONNECTED) {
								CuratorZookeeperClientImpl.this
										.stateChanged(StateListener.CONNECTED);
							} else if (state == ConnectionState.RECONNECTED) {
								CuratorZookeeperClientImpl.this
										.stateChanged(StateListener.RECONNECTED);
							}
						}
					});
			client.start();
		} catch (Exception e) {
			throw new IllegalStateException(e.getMessage(), e);
		}
	}

	/**
	 * @param path
	 *            路径
	 * @param ZooDefs
	 *            .Ids.OPEN_ACL_UNSAFE 权限
	 */
	public void createPersistent(String path, ArrayList<ACL> acls) {
		try {
			client.create().creatingParentsIfNeeded()// 如果指定的节点的父节点不存在，递归创建父节点
					.withMode(CreateMode.PERSISTENT)// 存储类型（临时的还是持久的）
					.withACL(acls)// 访问权限
					.forPath(path);
		} catch (Exception e) {
			throw new IllegalStateException(e.getMessage(), e);
		}
	}

	/**
	 * @param path
	 *            路径
	 * @param ZooDefs
	 *            .Ids.OPEN_ACL_UNSAFE 权限
	 */
	public void createEphemeral(String path, ArrayList<ACL> acls) {
		try {
			client.create().creatingParentsIfNeeded()
					.withMode(CreateMode.EPHEMERAL).withACL(acls).forPath(path);
		} catch (Exception e) {
			throw new IllegalStateException(e.getMessage(), e);
		}
	}

	public void delete(String path) {
		try {
			client.delete().forPath(path);
		} catch (Exception e) {
			throw new IllegalStateException(e.getMessage(), e);
		}
	}

	public List<String> getChildren(String path) {
		try {
			return client.getChildren().forPath(path);
		} catch (Exception e) {
			throw new IllegalStateException(e.getMessage(), e);
		}
	}

	public boolean isConnected() {
		return client.getZookeeperClient().isConnected();
	}

	public void doClose() {
		client.close();
	}

	private class CuratorWatcherImpl implements CuratorWatcher {

		private volatile ChildListener listener;

		public CuratorWatcherImpl(ChildListener listener) {
			this.listener = listener;
		}

		public void unwatch() {
			this.listener = null;
		}

		public void process(WatchedEvent event) throws Exception {
			if (listener != null) {
				listener.childChanged(event.getPath(), client.getChildren()
						.usingWatcher(this).forPath(event.getPath()));
			}
		}
	}

	public CuratorWatcher createTargetChildListener(String path,
			ChildListener listener) {
		return new CuratorWatcherImpl(listener);
	}

	public List<String> addTargetChildListener(String path,
			CuratorWatcher listener) {
		try {
			return client.getChildren().usingWatcher(listener).forPath(path);
		} catch (Exception e) {
			throw new IllegalStateException(e.getMessage(), e);
		}
	}

	public void removeTargetChildListener(String path, CuratorWatcher listener) {
		((CuratorWatcherImpl) listener).unwatch();
	}

	@Override
	protected String createPersistentSequence(String path, byte[] data,
			ArrayList<ACL> acls) {
		try {
			return client.create().creatingParentsIfNeeded()// 如果指定的节点的父节点不存在，递归创建父节点
					.withMode(CreateMode.PERSISTENT_SEQUENTIAL)// 存储类型（临时的还是持久的）
					.withACL(acls)// 访问权限
					.forPath(path, data);
		} catch (Exception e) {
			throw new IllegalStateException(e.getMessage(), e);
		}
	}

	@Override
	protected String createPersistentNoSequence(String path, byte[] data,
			ArrayList<ACL> acls) {
		try {
			return client.create().creatingParentsIfNeeded()// 如果指定的节点的父节点不存在，递归创建父节点
					.withMode(CreateMode.PERSISTENT)// 存储类型（临时的还是持久的）
					.withACL(acls)// 访问权限
					.forPath(path, data);
		} catch (Exception e) {
			throw new IllegalStateException(e.getMessage(), e);
		}
	}

	@Override
	protected String createEphemeralSequence(String path, byte[] data,
			ArrayList<ACL> acls) {
		try {
			return client.create().creatingParentsIfNeeded()// 如果指定的节点的父节点不存在，递归创建父节点
					.withMode(CreateMode.EPHEMERAL_SEQUENTIAL)// 存储类型（临时的还是持久的）
					.withACL(acls)// 访问权限
					.forPath(path, data);
		} catch (Exception e) {
			throw new IllegalStateException(e.getMessage(), e);
		}
	}

	@Override
	protected String createEphemeralNoSequence(String path, byte[] data,
			ArrayList<ACL> acls) {
		try {
			return client.create().creatingParentsIfNeeded()// 如果指定的节点的父节点不存在，递归创建父节点
					.withMode(CreateMode.EPHEMERAL)// 存储类型（临时的还是持久的）
					.withACL(acls)// 访问权限
					.forPath(path, data);
		} catch (Exception e) {
			throw new IllegalStateException(e.getMessage(), e);
		}
	}

	@Override
	public byte[] getData(String path) {
		try {
			return client.getData().forPath(path);
		} catch (Exception e) {
			throw new IllegalStateException(e.getMessage(), e);
		}
	}

	@Override
	public void setData(String path, byte[] data) {
		try {
			client.setData().forPath(path, data);
		} catch (Exception e) {
			throw new IllegalStateException(e.getMessage(), e);
		}
	}

	@Override
	public ZooKeeper getZookeeper() {
		try {
			return client.getZookeeperClient().getZooKeeper();
		} catch (Exception e) {
			throw new IllegalStateException(e.getMessage(), e);
		}
	}

	@Override
	public boolean isExist(String path) {
		try {
			Stat stat = client.checkExists().forPath(path);
			if (stat == null) {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	@Override
	public CuratorFramework getCuratorFramework() {
		return client;
	}

}
