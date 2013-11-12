package gossip.server.action;

import gossip.server.service.EventService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

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
	public JSONObject searchEvent(
			@RequestParam(value = "queryWords", required = true) String queryWords,
			@RequestParam(value = "pageNo", required = false, defaultValue = "0") int pageNo
			){
		System.out.println("queryWords:  " + queryWords + "pageNo: " + pageNo );
		return eventService.getEventList(pageNo, 10, 0, 1, 1);
	}
	

}
