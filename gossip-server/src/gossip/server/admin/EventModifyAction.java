package gossip.server.admin;

import gossip.mapper.Page;
import gossip.model.Event;
import gossip.server.service.EventService;

import java.util.List;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/admin/events")
public class EventModifyAction {
	
	private EventService eventService;

	
	@RequestMapping("/list")
	public ModelAndView list(
			@RequestParam(value="pageNo", required=false, defaultValue="0") int pageNo,
			@RequestParam(value="pageSize", required=false, defaultValue="15") int pageSize
			){
		List<Event> events = eventService.getEventListSimply(new Page(pageNo, pageSize));
		
		return new ModelAndView("/admin/event-list").addObject("events", events).addObject("pageNo", pageNo);
	}
	
	@RequestMapping("/show/{id}")
	public ModelAndView show(@PathVariable int id){
		Event event = eventService.getEventById(id);
		return new ModelAndView("/admin/event-show").addObject("event", event);
	}
	
	@RequestMapping("/edit/{id}")
	public ModelAndView edit(@PathVariable int id){
		Event event = eventService.getEventById(id);
		return new ModelAndView("/admin/event-edit").addObject("event", event);
	}
	
	
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String updateEvent(
			@RequestParam(value = "eventId", required = false) int eventId,
			@RequestParam(value = "title", required = false) String title,
			@RequestParam(value = "summary", required = false) String summary,
	        @RequestParam(value = "keyWords", required = false) String keyWords,
	        @RequestParam(value = "location", required = false) String location){
		
		Event event = eventService.getEventById(eventId);
		event.setTitle(title);
		event.setDesc(summary);
		event.setKeyWords(keyWords);
		event.setStartedLocation(location);
		eventService.update(event);
		
		return "redirect:/admin/event/show/"+eventId;

	}
	@ResponseBody
	@RequestMapping(value = "/deleteNews", method = RequestMethod.POST)
	public boolean deleteNews(
			@RequestParam(value = "eventId", required = false) int eventId,
			@RequestParam(value = "newsId", required = false) String newsId){
		
		Event event = eventService.getEventById(eventId);
		List<Integer> pages = event.getPagesList();
		pages.remove(newsId);
		event.setPagesList(pages);
		eventService.update(event);
		
		return true;
        
	}
	
	@RequestMapping(value = "/test", method = RequestMethod.POST)
	public JSONObject test(
			@RequestParam(value = "eventId", required = false) int eventId,
			@RequestParam(value = "title", required = false) String title,
			@RequestParam(value = "summary", required = false) String summary,
			@RequestParam(value = "newsId", required = false) String newsId,
	        @RequestParam(value = "keyWords", required = false) String keyWords,
	        @RequestParam(value = "location", required = false) String location){
		
		System.out.println(title);
		System.out.println(summary);
		System.out.println(keyWords);
		System.out.println(location);
		String status="success";
		JSONObject json=new JSONObject();
		json.put("status", status);
		return json;

	}
	public EventService getEventService() {
		return eventService;
	}
	public void setEventService(EventService eventService) {
		this.eventService = eventService;
	}

}
