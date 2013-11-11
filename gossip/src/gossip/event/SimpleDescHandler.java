package gossip.event;

import gossip.Handler;
import gossip.model.Event;
import gossip.summary.GenerateSummary;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * 生成事件摘要
 * 
 * @author zhangchangmin
 *
 */
public class SimpleDescHandler implements Handler {
	
	GenerateSummary gs=new GenerateSummary();
	
	public void handle(Collection<Event> events) {
		Iterator<Event> it = events.iterator();
		while (it.hasNext()) {
			Event e = it.next();
				List<Integer> pages=e.getPages();
				String desc=gs.description(pages);
				e.setDesc(desc);
		}
	}
	
}
