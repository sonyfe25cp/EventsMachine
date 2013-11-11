package gossip.gossip.dao;

/**
 * 用来生存Memcached里面key的工具类
 * 
 * @author lins 2012-8-15
 */
public class MemcachedKeyUtils {
	public final static String EVENT_RANKING_KEY = "event-rank";
	public final static String NEWS_RANKING_KEY = "news-rank";

	public final static int ERROR = -1;
	public final static int EVENT = 0;
	public final static int NEWS = 1;
	public final static int NEWS_RANKING = 2;
	public final static int EVENTS_BY_DATE = 3;

	/**
	 * @param keyType
	 *            当前包括MemcachedKeyUtils.EVENT和MemcachedKeyUtils.NEWS
	 * @param args
	 *            对EVENT来说就是事件排名;对于NEWS来说就是索引里面的docId
	 */
	public static String generateKey(int keyType, long... args) {
		StringBuilder sb = new StringBuilder();
		switch (keyType) {
		case EVENT:
			sb.append("event:").append(args[0]);
			break;
		case NEWS:
			sb.append("news:").append(args[0]);
			break;
		case NEWS_RANKING:
			sb.append("news-rank:").append(args[0]).append(",").append(args[1]);
			break;
		case EVENTS_BY_DATE:
			sb.append("news-date:").append(args[0]);
			break;
		default:
			return "";
		}
		return sb.toString();
	}

	public static int getId(String key) {
		if (key == null)
			return -1;
		String[] str = key.split(":");
		return str.length > 1 ? Integer.parseInt(str[1]) : -1;
	}

	public static String getTypeStr(String key) {
		if (key == null)
			return null;
		String[] str = key.split(":");
		return str.length > 1 ? str[0] : null;
	}

	public static int getTypeInt(String key) {
		if (key == null)
			return ERROR;
		String[] str = key.split(":");
		String type = str.length > 1 ? str[0] : null;
		if (type == null)
			return ERROR;
		if (type.equals("event"))
			return EVENT;
		if (type.equals("news"))
			return NEWS;
		return ERROR;

	}

}
