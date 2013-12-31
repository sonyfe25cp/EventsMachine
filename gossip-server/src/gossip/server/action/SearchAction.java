package gossip.server.action;

import gossip.model.Event;
import gossip.server.service.EventService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/q")
public class SearchAction {
	
	@Autowired
	private EventService eventService;
	
	@RequestMapping(value= "")
	@ResponseBody
	public List<Event> searchEvent(
			@RequestParam(value = "queryWords", required = true) String queryWords,
			@RequestParam(value = "pageNo", required = false, defaultValue = "0") int pageNo,
			@RequestParam(value = "limit", required = false, defaultValue = "10") int limit
			){
		System.out.println("queryWords:  " + queryWords + "pageNo: " + pageNo );
		
		return null;
		
	}
	

}
