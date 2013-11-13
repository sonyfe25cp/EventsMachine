package gossip.gossip.event;

import gossip.model.Event;

import java.util.Collections;
import java.util.List;

public class EventSequenceHandler implements Handler{


	@Override
	public void handle(Event event) {
		List<Integer> ids = event.getPagesList();
		Collections.sort(ids);
		StringBuilder sb =  new StringBuilder();
		int i = 1;
		for(Integer tmp : ids){
			sb.append(tmp);
			if(i != ids.size()){
				sb.append(",");
			}
		}
		String pages = sb.toString();
		event.setPages(pages);
		event.setPagesList(ids);
	}

}
