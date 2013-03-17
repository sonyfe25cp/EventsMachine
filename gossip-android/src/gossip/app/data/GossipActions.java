package gossip.app.data;

/**
 * 放各种ACTION
 * @author lins
 */
public interface GossipActions {
	public static String ACTION_HELPER = "gossip.helper";
	public static String ACTION_MAIN = "gossip.main";
	public static String ACTION_FETCH_EVENTS = "gossip.internet.fetch.events";
	public static String ACTION_FETCH_NEWS = "gossip.internet.fetch.news";
	public static String ACTION_EVENT_DETAIL ="gossip.main.event.detail";
	public static String ACTION_NEWS = "gossip.main.event.news";
}
