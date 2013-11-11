package gossip;

import gossip.model.Event;

import java.util.Collection;


/**
 * 定义了Boot里面需要用到的处理的接口
 *@author lins 2012-8-18
 */
public interface Handler {
	public void handle(Collection<Event> events);
}
