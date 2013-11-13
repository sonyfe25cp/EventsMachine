package gossip.gossip.event;

import gossip.model.Event;

public class EventSummaryHandler implements Handler{

	@Override
	public void handle(Event event) {
		event.setDesc("zan shi mei you");
	}

}
