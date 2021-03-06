package gossip.mapper;

import gossip.model.Event;

import java.util.Date;
import java.util.List;

public interface EventMapper {
	
	public Event getEventById(int id);
	
	public List<Event> getEventsAfterDate(Date createAt);
	
	public void insert(Event event);
	
	public void update(Event event);
	
	

}
