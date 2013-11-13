package gossip.gossip.event;

import gossip.model.Event;
import gossip.model.News;

import java.util.List;
/**
 * 目前事件的标题采用最新的新闻的标题
 * @author ChenJie
 *
 * Nov 12, 2013
 */
public class EventTitleHandler implements Handler{

	@Override
	public void handle(Event event) {
		List<News> datas = event.getNewsList();
		String finalTitle = "";
		int id = Integer.MIN_VALUE;
		if(datas == null || datas.size() == 0){
			System.out.println("error ! ");
			return;
		}
		for(News data : datas){
			String title = data.getTitle();
			int temId = data.getId();
			if(temId >= id){
				finalTitle = title;
				id = temId;
			}
		}
		event.setTitle(finalTitle);
	}


}
