package gossip.gossip.event;

import gossip.model.Event;
/**
 * 关键词策略
 * step1. 找出所有新闻的共有词
 * step2. 找出共有词中，idf最大的名词，或者是不在已知词表中的名词
 * step3. 取top10
 * @author ChenJie
 *
 * Nov 13, 2013
 */
public class EventKeyWordsHandler implements Handler{


	@Override
	public void handle(Event event) {
		
	}

}
