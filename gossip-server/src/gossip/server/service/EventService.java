package gossip.server.service;

import gossip.mapper.EventMapper;
import gossip.mapper.Page;
import gossip.model.Event;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventService {
	@Autowired
	private EventMapper eventMapper;
	
//	private DLDELogger logger = new DLDELogger();

	public Event getEventById(int id) {
		Event event = eventMapper.getEventById(id);
		System.out.println("create time: " + event.getCreateTime());
		event.setCreateTime(event.getCreateAt().getTime());
		return event;
	}

	public Event getEventJSONById(int id) {
//		JSONObject json = new JSONObject();
		Event event = getEventById(id);
//		json.put("id", event.getId());
//		json.put("title", event.getTitle());
//		json.put("desc", event.getDesc());
//		json.put("img", event.getImg());
//		json.put("recommended", event.getRecommended());
//		json.put("started_at", event.getCreateTime());
//		json.put("keywords", event.getKeyWords());
//		json.put("started_location", event.getStartedLocation());
//		json.put("news", event.getPages());
//		logger.info("i found the event in database.");
//		return json;
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
//		JSONObject result = new JSONObject();
//		return result;
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
