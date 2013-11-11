package gossip.event;

import gossip.Handler;
import gossip.model.Event;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;

/**
 * 用于检测event是否过期的handler
 * 
 * @author lins 2012-8-19
 */
public class SimpleRetireHandler implements Handler {

	public void handle(Collection<Event> events) {
		Iterator<Event> it = events.iterator();
		long now = new Date().getTime();
		HashSet<Event> toBeRemoved = new HashSet<Event>();
		while (it.hasNext()) {
			Event e = it.next();
			if (now - e.createTime > 24 * 3600)// 距离现在超过一天
			{
				toBeRemoved.add(e);
			}
		}
		if (events != null)
			events.removeAll(toBeRemoved);
	}

}
