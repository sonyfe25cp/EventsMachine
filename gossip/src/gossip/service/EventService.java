package gossip.service;

import gossip.event.Event;
import gossip.mapper.NewsMapper;
import gossip.model.News;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EventService {
	private double lambda = 0.5;
	private NewsService newsService;
	private NewsMapper newsMapper;
	
	
	/*
	 * basic event service
	 */

	public List<Event> computeEventFromNews(List<News> newsList){
		return EventDetection.simpleDetect(newsList);
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
		
	}

	public void updateOrInsert(List<Event> events){
		
	}
	
	public Event getEventById(int id){
		return null;
	}
	
	public List<Event> getEventsByDate(Date date){
		return null;
	}
	
	
}