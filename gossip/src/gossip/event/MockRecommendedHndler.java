package gossip.event;

import gossip.Handler;
import gossip.model.Event;

import java.util.Collection;
import java.util.Iterator;

/**
 * 当前每个新闻的推荐度是根据时间计算的，距离当前越近，推荐程度越高
 * 
 * @author lins 2012-8-19
 */
public class MockRecommendedHndler implements Handler {

	public void handle(Collection<Event> events) {
		Iterator<Event> it = events.iterator();
		while (it.hasNext()) {
			Event e = it.next();
			e.recommended = e.createTime;
		}
	}

}
