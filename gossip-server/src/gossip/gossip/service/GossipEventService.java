package gossip.gossip.service;

import gossip.gossip.event.EventProcess;
import gossip.gossip.utils.DateTrans;
import gossip.mapper.EventMapper;
import gossip.model.Event;
import gossip.model.News;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GossipEventService {
	private double lambda = 0.5;
	
	@Autowired
	private GossipNewsService gossipNewsService;
	@Autowired
	private EventMapper eventMapper;
	
	/*
	 * basic event service
	 */

	public List<Event> computeEventFromNews(List<News> newsList){
		List<Event> events = new ArrayList<Event>();
		events = GossipEventDetection.simpleDetect(newsList);//得到事件列表
		for(Event event : events){
			List<Integer> newsIds = event.getPagesList();
			for(int a : newsIds){
				System.out.print(a+" ");
			}
			System.out.println();
		}
		//加载各种对事件处理的逻辑，题目，摘要等
		for(Event event : events){
			EventProcess ep = new EventProcess(event);
			ep.start();
//			ep.run();
			try {
				ep.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		//标记已经被发现的新闻为Evented
		for(Event event : events){
			List<Integer> newsIds = event.getPagesList();
			gossipNewsService.batchUpdateNewsStatus(newsIds, News.Evented);
		}
		return events;
	}
	
	public List<Event> mergeEvents(List<Event> originEvents, List<Event> newEvents){
		if(originEvents.size()==0){
			return newEvents;
		}else{
			List<Event> events = new ArrayList<Event>();
			Iterator<Event> newComes = newEvents.iterator();
			
			nextLoop: while(newComes.hasNext()){//若新事件找到可合并的旧事件，则合并，并继续下一个新事件；若都没有，则计为新事件
				Event newEvent = newComes.next();
				for(Event originEvent : originEvents){
					double sim = compairEvents(newEvent, originEvent);
					if(sim > lambda ){
						originEvent.mergeEvent(newEvent);
						events.add(originEvent);
						newComes.remove();
						break nextLoop;
					}
				}
				events.add(newEvent);
			}
			
			
			return events;
		}
	}

	public double compairEvents(Event event1, Event event2){
		List<News> list1 = event1.getNewsList();
		List<News> list2 = event2.getNewsList();
		
		int size1 = list1.size();
		int size2 = list2.size();
		int tmp = 0;
		for(News tmp1 : list1){
			for(News tmp2 : list2){
				boolean flag = gossipNewsService.isSimilar(tmp1, tmp2);
				if(flag)
					tmp++;
			}
		}
		double sim = tmp/(size1+size2+0.0);
		return sim;
	}
	
	/*
	 * basic dao service
	 */
	public void updateOrInsert(Event event){
		int id = event.getId();
		if(id != 0){
			updateEvent(event);
		}else{
			insertEvent(event);
		}
	}
	
	public void insertEvent(Event event){
		eventMapper.insert(event);
	}
	
	public void updateEvent(Event event){
		eventMapper.update(event);
	}

	public void updateOrInsert(List<Event> events){
		for(Event e : events){
			updateOrInsert(e);
		}
	}
	
	public Event getEventById(int id){
		return eventMapper.getEventById(id);
	}
	
	public List<Event> getEventsByDate(Date date){
		return null;
	}
	public List<Event> getEventsLast7Days(Date date){
		Date begin = DateTrans.getDateBefore(date, 7);
		
		List<Event> events =  eventMapper.getEventsAfterDate(begin);
		for(Event event : events){
			String pages = event.getPages();
			String[] pagesTmp = pages.split(",");
			List<News> newsList = new ArrayList<News>();
			for(String pageId : pagesTmp){
				News news = gossipNewsService.getNewsById(Integer.parseInt(pageId));
				newsList.add(news);
			}
			event.setNewsList(newsList);
		}
		return events;
		
	}
	
	
}
