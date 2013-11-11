package gossip.service;

import gossip.mapper.EventMapper;
import gossip.model.Event;
import gossip.model.News;
import gossip.utils.DateTrans;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EventService {
	private double lambda = 0.5;
	
	private NewsService newsService;
	
	private EventMapper eventMapper;
	
	/*
	 * basic event service
	 */

	public List<Event> computeEventFromNews(List<News> newsList){
		List<Event> events = new ArrayList<Event>();
		events = EventDetection.simpleDetect(newsList);//得到事件列表
		//标记已经被发现的新闻为Evented
		for(Event event : events){
			List<Integer> newsIds = event.getPages();
			newsService.batchUpdateNewsStatus(newsIds, News.Evented);
		}
		return events;
	}
	
	public List<Event> mergeEvents(List<Event> originEvents, List<Event> newEvents){
		List<Event> events = new ArrayList<Event>();
		for(Event newEvent : newEvents){
			for(Event originEvent : originEvents){
				double sim = compairEvents(newEvent, originEvent);
				if(sim > lambda ){
					originEvent.mergeEvent(newEvent);
					events.add(originEvent);
				}else{
					events.add(newEvent);
				}
			}
		}
		return events;
	}

	public double compairEvents(Event event1, Event event2){
		List<News> list1 = event1.getNewsList();
		List<News> list2 = event2.getNewsList();
		
		int size1 = list1.size();
		int size2 = list2.size();
		int tmp = 0;
		for(News tmp1 : list1){
			for(News tmp2 : list2){
				boolean flag = newsService.isSimilar(tmp1, tmp2);
				if(flag)
					tmp++;
			}
		}
		double sim = tmp/(list1.size()+list2.size());
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
		
//		DateFormat df = new SimpleDateFormat("yyyyMMdd");
//		
//		String beginDate = df.format(begin);
//		
//		System.out.println("begin Date : " + beginDate);
		
		return eventMapper.getEventsAfterDate(begin);
	}
	
	
}
