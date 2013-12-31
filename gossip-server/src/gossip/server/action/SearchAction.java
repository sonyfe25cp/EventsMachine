package gossip.server.action;

import gossip.mapper.Page;
import gossip.model.Event;
import gossip.server.service.EventService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class SearchAction {
	
	@Autowired
	private EventService eventService;
	
	@RequestMapping(value= "/q")
	public ModelAndView searchEvent(
			@RequestParam(value = "keyword", required = false) String keyword,
			@RequestParam(value="date", required = false) String date,
			@RequestParam(value = "pageNo", required = false, defaultValue = "0") int pageNo,
			@RequestParam(value = "limit", required = false, defaultValue = "10") int limit
			){
		System.out.println("queryWords:  " + keyword + "pageNo: " + pageNo );
		List<Event> events = null;
		if(keyword == null && date == null){
			return null;
		}else{
			
			events = eventService.getEventListSelectWhat(new Page(pageNo, limit), date, keyword);
		}
		return new ModelAndView("/event/event-search-result")
			.addObject("events", events)
			.addObject("keyword", keyword)
			.addObject("date", date);
		
	}
	

}
