package gossip.gossip.event;

import gossip.model.Event;
/**
 * 事件本身的排序，算一个事件自身的推荐值
 * 考虑因素：
 * 		1. 新闻数量
 * 		2. 新闻的正副性，相比正面新闻，负面新闻的关注度更多一些；越负面，应该分越高
 * @author ChenJie
 *
 * Nov 13, 2013
 */
public class EventRecommendHandler implements Handler{

	@Override
	public void handle(Event event) {
		int num = event.getPagesList().size();
		event.setRecommended(num);
	}

}
