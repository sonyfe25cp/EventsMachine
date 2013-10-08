package gossip.server.mapper;

import gossip.server.model.Event;

import java.util.List;

public interface EventMapper {
	
	public Event getEventById(int id);

	public List<Event> getEvent();

	public void insertEvent(Event event);

	public void updateEvent(Event event);
	
	public void deleteEventById(int id);
	
	public List<Integer> getEventRanking();

}
