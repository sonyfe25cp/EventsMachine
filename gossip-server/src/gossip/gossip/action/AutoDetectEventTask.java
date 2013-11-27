package gossip.gossip.action;

import gossip.gossip.service.GossipEventService;
import gossip.gossip.service.GossipNewsService;
import gossip.model.Event;
import gossip.model.News;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
@Configuration
@EnableScheduling
public class AutoDetectEventTask {
	@Autowired
	private GossipEventService gossipEventService;
	@Autowired
	private GossipNewsService gossipNewsService;
	
	@Scheduled(cron="0 35 * * * ?")
	public void run(){
		Calendar cal = Calendar.getInstance();
		Date now = cal.getTime();
		//step0. 取出今天的新闻包括本周的未被识别为事件的新闻
		List<News> newsToday = gossipNewsService.getNewsLast7Days(now);
		System.out.println("newsToday need to compute is : "+newsToday.size());
		//step1. 找出今天的events，标记没有存为事件的那些新闻
		List<Event> eventsTody = gossipEventService.computeEventFromNews(newsToday);
		System.out.println("eventsToday is : "+eventsTody.size());
		//step2. 找出前面7天的events
		List<Event> eventsLast7Days = gossipEventService.getEventsLast7Days(now);
		System.out.println("eventsLast7Days is : "+eventsLast7Days.size());
		//step3. 合并今天的与本周的，并找出新的
		List<Event> newOrUpdatedEvents = gossipEventService.mergeEvents(eventsLast7Days, eventsTody);
		//step4. 插入or更新这些events
		gossipEventService.updateOrInsert(newOrUpdatedEvents);
	}
	public GossipEventService getGossipEventService() {
		return gossipEventService;
	}
	public void setGossipEventService(GossipEventService gossipEventService) {
		this.gossipEventService = gossipEventService;
	}
	public GossipNewsService getGossipNewsService() {
		return gossipNewsService;
	}
	public void setGossipNewsService(GossipNewsService gossipNewsService) {
		this.gossipNewsService = gossipNewsService;
	}
	
}
