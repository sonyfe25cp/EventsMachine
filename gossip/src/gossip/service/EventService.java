package gossip.service;

import gossip.event.Event;
import gossip.model.News;

import java.util.Date;
import java.util.List;

public class EventService {
	
	private NewsService newsService;
	
	
	/*
	 * basic event service
	 */

	public List<Event> computeEventFromNews(List<News> newsList){
		return EventDetection.simpleDetect(newsList);
	}
	
	public List<Event> mergeEvents(List<Event> originEvents, List<Event> newEvents){
		return null;
	}

	public double compairEvents(Event event1, Event event2){
		return 0;
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
