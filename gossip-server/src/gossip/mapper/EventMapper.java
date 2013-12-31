package gossip.mapper;

import gossip.model.Event;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface EventMapper {
	
	public Event getEventById(int id);
	
	public List<Event> getEventsAfterDate(Date createAt);
	
	public void insert(Event event);
	
	public void batchInsert(@Param("eventList") List<Event> eventList);
	
	public void update(Event event);
	
	public List<Event> getEvent();

	public void insertEvent(Event event);

	public void updateEvent(Event event);
	
	public void deleteEventById(int id);
	
//	public List<Event> getEventRanking(Page page);
	
	//importance || time
	public List<Event> getEventListOrderByWhat(@Param("page")Page page, @Param("orderType") String orderType);
	
	public List<Event> getEventListSelectWhat(@Param("page") Page page, @Param("updateAt") String updateAt);

}
