//package gossip.gossip.dao;
//
//import java.io.IOException;
//import java.sql.Date;
//import java.util.concurrent.TimeoutException;
//
//import net.rubyeye.xmemcached.MemcachedClient;
//import net.rubyeye.xmemcached.MemcachedClientBuilder;
//import net.rubyeye.xmemcached.XMemcachedClientBuilder;
//import net.rubyeye.xmemcached.exception.MemcachedException;
//import net.rubyeye.xmemcached.utils.AddrUtil;
//import net.sf.json.JSONArray;
//import net.sf.json.JSONObject;
//import edu.bit.dlde.utils.DLDEConfiguration;
//import edu.bit.dlde.utils.DLDELogger;
//import gossip.gossip.dao.EventDAO;
//import gossip.gossip.index.gossip.gossipMessager;
//
///**
// * Memcached的守护进程，它负责初始化Memcached已经隔时间地对Memcached进行更新,当前设定的时间为3600s
// * 
// * @author lins 2012-8-13
// */
//public class MemcachedDaemon extends Thread {
//	private DLDELogger logger;
//
//	public DLDELogger getLogger() {
//		return logger;
//	}
//
//	public void setLogger(DLDELogger logger) {
//		this.logger = logger;
//	}
//
//	/** memcached的对象 **/
//	private MemcachedClient memcachedClient;
//
//	public MemcachedClient getMemcachedClient() {
//		return memcachedClient;
//	}
//
//	public void setMemcachedClient(MemcachedClient memcachedClient) {
//		this.memcachedClient = memcachedClient;
//	}
//
//	/** 主要用来读索引 **/
//	private gossip.gossipMessager messager;
//
//	public gossip.gossipMessager getMessager() {
//		return messager;
//	}
//
//	public void setMessager(gossip.gossipMessager messager) {
//		this.messager = messager;
//	}
//
//	/** memcached里面数据的过期时间 **/
//	public static int expiration;
//	private String addrs;
//	private int[] weights;
//
//	public int isMemcached;
//
//	public boolean isOn() {
//		return isMemcached != 0 ? true : false;
//	}
//
//	/**
//	 * 读取配置文件，然后初始化各种变量
//	 */
//	public MemcachedDaemon() {
//		isMemcached = Integer.valueOf(DLDEConfiguration.getInstance(
//				"gossip.gossip-server.properties").getValue("isMemcached"));
//		if (isMemcached == 0)
//			return;
//		expiration = Integer.valueOf(DLDEConfiguration.getInstance(
//				"gossip.gossip-server.properties").getValue("expiration"));
//		addrs = DLDEConfiguration.getInstance("gossip.gossip-server.properties")
//				.getValue("MemcachedAddr");
//		String[] weightStrs = DLDEConfiguration
//				.getInstance("gossip.gossip-server.properties")
//				.getValue("MemcachedWeight").split(" ");
//		weights = new int[weightStrs.length];
//		for (int i = 0; i < weightStrs.length; i++) {
//			weights[i] = Integer.parseInt(weightStrs[i]);
//		}
//		System.out.println("initating MemcachedDaemon");
//		// memcachedPath = DLDEConfiguration.getInstance(
//		// "gossip.gossip-server.properties").getValue("MemcachedPath");
//	}
//
//	/**
//	 * 初始化memcached
//	 * 
//	 * @see java.lang.Thread#run()
//	 */
//	public void run() {
//		if (isMemcached == 0)
//			return;
//
//		/** 创建client **/
//		MemcachedClientBuilder builder = new XMemcachedClientBuilder(
//				AddrUtil.getAddresses(addrs), weights);
//		// 设置连接池大小，即客户端个数
//		builder.setConnectionPoolSize(50);
//		// 宕机报警
//		builder.setFailureMode(true);
//		try {
//			memcachedClient = builder.build();
//		} catch (IOException e1) {
//			e1.printStackTrace();
//		}
//	}
//}
