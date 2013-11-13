package gossip.server.action;

import gossip.mapper.Page;
import gossip.model.Event;
import gossip.server.service.EventService;

import java.util.List;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 访问事件的action。
 * 
 * @author lins
 */
@Controller
@RequestMapping("/events")
public class EventAction {
	@Autowired
	private EventService eventService;
	
	@RequestMapping(value = "")
	@ResponseBody
	public List<Event> getEventList(
			@RequestParam(value = "pageNo", required = false, defaultValue = "0") int pageNo,
			@RequestParam(value = "limit", required = false, defaultValue = "10") int limit,
			@RequestParam(value = "year", required = false, defaultValue = "0") int year,
			@RequestParam(value = "month", required = false, defaultValue = "1") int month,
			@RequestParam(value = "day", required = false, defaultValue = "1") int day) {
		System.out.println("into event");
		return eventService.getEventList(new Page(pageNo, limit), year, month, day);
	}
	
	@RequestMapping(value = "/{id}")
	@ResponseBody
	public Event getEventById(@PathVariable int id){
		return eventService.getEventJSONById(id);
	}
	
	@RequestMapping(value = "/test")
	@ResponseBody
	public JSONObject testAJAX(){
		System.out.println("into test");
		JSONObject json = new JSONObject();
		json.put("test", "test");
		return json;
	}

	public EventService getEventService() {
		return eventService;
	}

	public void setEventService(EventService eventService) {
		this.eventService = eventService;
	}
	
	
	
}
