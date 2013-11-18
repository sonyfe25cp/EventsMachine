package gossip.gossip.event;

import gossip.model.Event;


/**
 * 定义了Boot里面需要用到的处理的接口
 */
public interface Handler {
	public void handle(Event event);
}
