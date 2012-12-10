package gossip.dao;

import java.io.IOException;
import java.sql.Date;
import java.util.concurrent.TimeoutException;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.MemcachedClientBuilder;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import net.rubyeye.xmemcached.exception.MemcachedException;
import net.rubyeye.xmemcached.utils.AddrUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import edu.bit.dlde.utils.DLDEConfiguration;
import edu.bit.dlde.utils.DLDELogger;
import gossip.dao.EventDAO;
import gossip.index.GossipMessager;

/**
 * Memcached的守护进程，它负责初始化Memcached已经隔时间地对Memcached进行更新,当前设定的时间为3600s
 * 
 * @author lins 2012-8-13
 */
public class MemcachedDaemon extends Thread {
	private DLDELogger logger;

	public DLDELogger getLogger() {
		return logger;
	}

	public void setLogger(DLDELogger logger) {
		this.logger = logger;
	}

	/** memcached的对象 **/
	private MemcachedClient memcachedClient;

	public MemcachedClient getMemcachedClient() {
		return memcachedClient;
	}

	public void setMemcachedClient(MemcachedClient memcachedClient) {
		this.memcachedClient = memcachedClient;
	}

	public EventDAO eventDAO;

	public EventDAO getEventDAO() {
		return eventDAO;
	}

	public void setEventDAO(EventDAO eventDAO) {
		this.eventDAO = eventDAO;
	}

	/** 主要用来读索引 **/
	private GossipMessager messager;

	public GossipMessager getMessager() {
		return messager;
	}

	public void setMessager(GossipMessager messager) {
		this.messager = messager;
	}

	/** memcached里面数据的过期时间 **/
	public static int expiration;
	private String addrs;
	private int[] weights;

	public int isMemcached;

	public boolean isOn() {
		return isMemcached != 0 ? true : false;
	}

	/**
	 * 读取配置文件，然后初始化各种变量
	 */
	public MemcachedDaemon() {
		isMemcached = Integer.valueOf(DLDEConfiguration.getInstance(
				"gossip-server.properties").getValue("isMemcached"));
		if (isMemcached == 0)
			return;
		expiration = Integer.valueOf(DLDEConfiguration.getInstance(
				"gossip-server.properties").getValue("expiration"));
		addrs = DLDEConfiguration.getInstance("gossip-server.properties")
				.getValue("MemcachedAddr");
		String[] weightStrs = DLDEConfiguration
				.getInstance("gossip-server.properties")
				.getValue("MemcachedWeight").split(" ");
		weights = new int[weightStrs.length];
		for (int i = 0; i < weightStrs.length; i++) {
			weights[i] = Integer.parseInt(weightStrs[i]);
		}
		System.out.println("initating MemcachedDaemon");
		// memcachedPath = DLDEConfiguration.getInstance(
		// "gossip-server.properties").getValue("MemcachedPath");
	}

	/**
	 * 初始化memcached
	 * 
	 * @see java.lang.Thread#run()
	 */
	public void run() {
		if (isMemcached == 0)
			return;

		/** 创建client **/
		MemcachedClientBuilder builder = new XMemcachedClientBuilder(
				AddrUtil.getAddresses(addrs), weights);
		// 设置连接池大小，即客户端个数
		builder.setConnectionPoolSize(50);
		// 宕机报警
		builder.setFailureMode(true);
		try {
			memcachedClient = builder.build();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * 当对应id的文档不存在与memcached里面时： 发送信息到索引进程，阻塞地返回数据，然后添加进memcached
	 * @deprecated
	 */
	public void fireNewsNotFoundEvent(long id) {
		if (isMemcached == 0)
			return;
		/** socket到索引进程，等待响应 **/
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("id", id);
		String response = messager.sendMessage(jsonObj,
				GossipMessager.GET_DOC_BY_ID);

		if (response == null || response.equals(""))
			return;

		/** 将响应结果放入memcached,并且记录需要查重的id **/
		try {
			JSONObject repJsonObj = JSONObject.fromObject(response);
			String key = MemcachedKeyUtils.generateKey(MemcachedKeyUtils.NEWS,
					id);
			memcachedClient.add(key, expiration, repJsonObj);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 当对应id的event不存在与memcached里面时： 阻塞地读取数据库，然后添加进memcached
	 * @deprecated
	 */
	public void fireEventsNotFoundEvent(int id) {
		if (isMemcached == 0)
			return;
		JSONObject event = eventDAO.getEventJSONById(id);

		if (event.isEmpty())
			return;

		/** 将响应结果放入memcached **/
		try {
			String key = MemcachedKeyUtils.generateKey(MemcachedKeyUtils.EVENT,
					id);
			memcachedClient.add(key, expiration, event);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 通过socket链接到indexer，获得新闻对应的排列
	 * @deprecated
	 * @param begin
	 * @param limit
	 */
	public void fireNewsRankingNotFoundEvent(int begin, int limit) {
		if (isMemcached == 0)
			return;
		/** socket到索引进程，等待响应 **/
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("begin", begin);
		jsonObj.put("offset", limit);
		String response = messager.sendMessage(jsonObj,
				GossipMessager.GET_NEWS_RANKING);

		if (response == null || response.equals(""))
			return;

		/** 将响应结果放入memcached,并且记录需要查重的id **/
		try {
			JSONObject repJsonObj = JSONObject.fromObject(response);
			memcachedClient.add(MemcachedKeyUtils.generateKey(
					MemcachedKeyUtils.NEWS_RANKING, begin, limit), expiration,
					repJsonObj);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * memcached里面的rank过期不见了
	 * @deprecated
	 */
	public void fireEventRankingNotFoundEvent() {
		if (isMemcached == 0)
			return;
		JSONObject jsonObj = eventDAO.getEventRanking();
		if (jsonObj != null && !jsonObj.isEmpty()) {
			try {
				memcachedClient.add(MemcachedKeyUtils.EVENT_RANKING_KEY,
						expiration * 24, jsonObj);
			} catch (TimeoutException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (MemcachedException e) {
				e.printStackTrace();
			}
			logger.debug("i've put ranking into memcached.");
		}
	}

	public void fireEventsByDateNotFoundEvent(Date date) {
		if (isMemcached == 0)
			return;
		JSONArray jsonArry = eventDAO.getEventJSONByDate(date);
		if (jsonArry != null && !jsonArry.isEmpty()) {
			try {
				memcachedClient.add(MemcachedKeyUtils.generateKey(
						MemcachedKeyUtils.EVENTS_BY_DATE, date.getTime()),
						expiration * 24, jsonArry);
			} catch (TimeoutException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (MemcachedException e) {
				e.printStackTrace();
			}
			logger.debug("i've put ranking into memcached.");
		}
	}
}
