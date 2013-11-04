package com.dj.zk.manager.commons;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;
import org.apache.zookeeper.client.ConnectStringParser;
import org.apache.zookeeper.client.HostProvider;
import org.apache.zookeeper.client.StaticHostProvider;
import org.codehaus.jackson.type.TypeReference;

import com.dj.zk.manager.exceptions.GeneralException;
import com.dj.zk.manager.model.UserInfo;
import com.dj.zk.manager.utils.ZooConfigUtils;
import com.dj.zk.manager.utils.json.JsonUtils;



/**
 * @description:常量
 * @version  Ver 1.0
 * @author   <a href="mailto:zuiwoxing@gmail.com">dejian.liu</a>
 * @Date	 2013-11-3 下午6:22:08
 */
public abstract class Constants {
	
	public final static String BASE_PATH = "/zookeeper/";
	
	public final static String ZOOKEEPER_CONNECT_URL = "zookeeper.connect.url";
	
	/**
	 * 用户session
	 */
	public static final String USER_INFO_SESSION = "user_info_session";
	
	/**
	 * 默认根节点
	 */
	public final static String  ZOO_ROOT_DEFAULT = "zookeeper.root.default";
	
	/**
	 * zookeeper授权方式
	 */
	public final static String ZOO_AUTH_TYPE_ = "zoo.auth.type";
	
	/**
	 * digest密钥
	 */
//	public final static String ZOO_DIGEST_SECRET = "config:zooadmin";
	
	public final static String ZOO_DIGEST_SECRET_="zoo.digest.secret";
	
	/**
	 * 全局密钥(暂时没用)
	 */
//	public final static String ZOO_GLOBAL_SECRET = "super:jzORiXADeFIm9iTxrMVzeI5GLJE=";
 
	public final static String charset = "UTF-8";
	
	public final static String VERIFY_CODE = "verifyCode";
	/**
	 * 用户信息
	 */
	public final static String USER_INFOS = "user.infos";
	
	/**
	 * 配置文件(根路径)
	 */
	public static final String ZOO_CONF = "/bidconf";
	/**
	 * 存储所有数据信息(根路径)
	 */
	public final static String ZOO_CONF_DATA = "/bidconfusers";
	
	public final static String MD5_SALT="dejian.liu";
	
	static ArrayList<InetSocketAddress> zooCacheLists = new ArrayList<InetSocketAddress>();
	
	/**
	 * key = host:port
	 * value = InetSocketAddress
	 */
	static Map<String, InetSocketAddress> cacheZkHostMap = new ConcurrentHashMap<String, InetSocketAddress>();
	
	static Map<String, String> zooCommandMap = new HashMap<String, String>();
	
	
	static HostProvider hostProvider = null;
    
	/**
	 * 用户信息
	 */
	public final static ConcurrentHashMap<String, UserInfo> userMap = new ConcurrentHashMap<String, UserInfo>();
	
	static {
		loadUserInfo();
		getZooAddresses();
		zooCommandMap.put("conf", "conf_取得配置文件信息");
		zooCommandMap.put("cons", "cons_取得连接信息");
//		zooCommandMap.put("crst", "crst_重置所有统计信息");
		zooCommandMap.put("dump", "dump_取得所有等待会话");
		zooCommandMap.put("envi", "envi_取得zkServer启动信息");
		zooCommandMap.put("ruok", "ruok_检查服务是否正常");
		zooCommandMap.put("stat", "stat_取得连接状态及相关信息");
		zooCommandMap.put("srvr", "srvr_获取统计信息");
//		zooCommandMap.put("srst", "srst_重置Server统计信息");
		zooCommandMap.put("wchs", "wchs_取得Watcher统计信息");
		zooCommandMap.put("wchc", "wchc_Watcher信息(按Session分组)");
		zooCommandMap.put("wchp", "wchp_Watcher信息(按节点分组)");
		zooCommandMap.put("mntr", "mntr_取得统计总信息");
	}
	
	public static String getZookeeperConnectUrl() {
		String zooUrls =  ZooConfigUtils.get(ZOOKEEPER_CONNECT_URL);
		if(StringUtils.isEmpty(zooUrls)) {
			throw new GeneralException("zoo urls can't null !");
		}
		return zooUrls;
	}

	public static String getDefaultRoot() {
		String root =  ZooConfigUtils.get(ZOO_ROOT_DEFAULT);
		if(StringUtils.isEmpty(root)) {
			throw new GeneralException("zoo root path can't null!");
		}
		if(!root.startsWith("/")) {
			return "/"+root;
		}
		return root;
	}
	
	public static String getZooAuthType() {
		return ZooConfigUtils.get(ZOO_AUTH_TYPE_);
	}
	
	public static String getZooDigestSecret() {
		return ZooConfigUtils.get(ZOO_DIGEST_SECRET_);
	}
 
	public static void loadUserInfo() {
		if(!userMap.isEmpty()) {
			return;
		}
		String json = ZooConfigUtils.get(USER_INFOS);
 		List<UserInfo> lists = JsonUtils.json2GenericObject(json, new TypeReference<List<UserInfo>>() {}, null);
		for (UserInfo user : lists) {
			user.setId(System.nanoTime());
			userMap.put(user.getUserName(), user);
 		}
	}
	
	
	/**
	 * @description 取得zoo地址列表
	 * @return
	 */
	public static List<InetSocketAddress> getZooAddresses() {
		 String zooUrls =  getZookeeperConnectUrl();
		 ConnectStringParser parse = new ConnectStringParser(zooUrls);
		 if(zooCacheLists.size() > 0) {
			 return zooCacheLists;
		 }
		 List<InetSocketAddress> resList = parse.getServerAddresses();
		 for (InetSocketAddress net : resList) {
			cacheZkHostMap.put(net.toString(), net);
		}
		 zooCacheLists.addAll(resList);
		 return zooCacheLists;
	}
	
	/**
	 * @description 取得轮循zoo地址
	 * @return
	 */
	public static InetSocketAddress getLoopZooAddress() {
		if(hostProvider != null) {
			try {
				hostProvider = new StaticHostProvider(zooCacheLists);
			} catch (UnknownHostException e) {
			}
		}
		return hostProvider.next(10);
	}
	
	
	public static ConcurrentHashMap<String, UserInfo> getUsermap() {
		return userMap;
	}
 
	public static Map<String, InetSocketAddress> getCacheZkHostMap() {
		return cacheZkHostMap;
	}
	
	

	public static Map<String, String> getZooCommandMap() {
		return zooCommandMap;
	}

	public static void main(String[] args) {
		loadUserInfo();
//		List<UserInfo> lists = new ArrayList<UserInfo>();
//		lists.add(new UserInfo("liu","123"));
//		System.out.println(JsonUtils.toJson(lists, null,null,null));
		
	}
	
}
