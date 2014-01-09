package gossip.server.action;

import gossip.mapper.Page;
import gossip.model.Event;
import gossip.server.service.EventService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * 访问事件的action。
 * 
 * @author lins
 */
@Controller
public class EventAction {
	@Autowired
	private EventService eventService;
	
	@RequestMapping(value = "/events.json")
	@ResponseBody
	public List<Event> getEventList(
			@RequestParam(value = "pageNo", required = false, defaultValue = "0") int pageNo,
			@RequestParam(value = "limit", required = false, defaultValue = "30") int limit
			){
		return eventService.getEventListSimply(new Page(pageNo, limit));
	}
	@RequestMapping(value = "/events.html")
	public ModelAndView eventList(
			@RequestParam(value="sort", required = false, defaultValue = "importance") String sort,
			@RequestParam(value = "pageNo", required = false, defaultValue = "0") int pageNo,
			@RequestParam(value = "limit", required = false, defaultValue = "30") int limit
			){
		
		List<Event> events = null;
		if(sort.equals(Event.Importance)){
			events = eventService.getEventListByRanking(new Page(pageNo, limit));
		}else if(sort.equals(Event.Time)){
			events = eventService.getEventListByTimeDesc(new Page(pageNo, limit));
		}else{
			events = eventService.getEventListSimply(new Page(pageNo, limit));
		}
		return new ModelAndView("/event/event-list").addObject("events", events).addObject("sort", sort);
	}
	
	@RequestMapping(value = "/event/{id}.html")
	public ModelAndView getEventById2(@PathVariable int id){
		Event event =  eventService.getEventById(id);
		return new ModelAndView("/event/event-show").addObject("event", event);
	}
	
	@RequestMapping(value = "/event/{id}.json")
	@ResponseBody
	public Event getEventById(@PathVariable int id){
		return eventService.getEventById(id);
	}
	
	public EventService getEventService() {
		return eventService;
	}

	public void setEventService(EventService eventService) {
		this.eventService = eventService;
	}
}
