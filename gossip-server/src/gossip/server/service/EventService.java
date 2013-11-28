package gossip.server.service;

import gossip.mapper.EventMapper;
import gossip.mapper.Page;
import gossip.model.Event;
import gossip.model.News;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventService {
	@Autowired
	private EventMapper eventMapper;
	
	@Autowired
	private NewsService newsService;
	
//	private DLDELogger logger = new DLDELogger();

	
	public void update(Event event){
		eventMapper.update(event);
	}
	
	
	public Event getEventById(int id) {
		Event event = eventMapper.getEventById(id);
		
		String pages = event.getPages();
		String[] ids = pages.split(",");
		List<News> newsList = new ArrayList<News>();
		for(String tmp : ids){
			News news = newsService.getNewsById(Integer.parseInt(tmp));
			newsList.add(news);
		}
		event.setNewsList(newsList);
		return event;
	}

	public Event getEventJSONById(int id) {
		Event event = getEventById(id);
		return event;
	}

	public List<Event> getEventList(Page page, int year, int month,
			int day) {
		if (year != 0) {
			return getEventListTimeConstrain(page, year, month, day);
		} else {
			return getEventListSimply(page);
		}
	}

	private List<Event> getEventListTimeConstrain(Page page,	int year, int month, int day) {
		return null;
	}

	private List<Event> getEventListSimply(Page page) {
		return getEventRanking(page);
	}

	/**
	 * 从数据库里面读出按照recommeded排序的event，并且将id和排序号放入jsonObj
	 * 
	 * @return <ranking, event-id>...
	 */
	public List<Event> getEventRanking(Page page) {
		List<Event> events = eventMapper.getEventRanking(page);
		return events;
	}
	
	

	public EventMapper getEventMapper() {
		return eventMapper;
	}

	public void setEventMapper(EventMapper eventMapper) {
		this.eventMapper = eventMapper;
	}

	public static void main(String[] args) {
		EventService eventService = new EventService();
		Event event = eventService.getEventById(7);
		System.out.println(event.getTitle() + "   " + event.getKeyWords());
	}
}
