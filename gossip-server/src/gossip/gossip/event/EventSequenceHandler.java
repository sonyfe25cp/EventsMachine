package gossip.gossip.event;

import gossip.model.Event;

import java.util.Collections;
import java.util.List;
/**
 * 把网页id排序
 * 关系到事件单页的新闻的排序
 * @author ChenJie
 *
 * Nov 13, 2013
 */
public class EventSequenceHandler implements Handler{


	@Override
	public void handle(Event event) {
		List<Integer> ids = event.getPagesList();
		Collections.sort(ids);
		Collections.reverse(ids);
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
