package gossip.server.admin;

import gossip.gossip.dao.EventDAO;
import gossip.gossip.dao.NewsDAO;
import gossip.mapper.Page;
import gossip.model.Event;
import gossip.server.service.EventService;

import java.util.List;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * 
 * @author shiyulong
 *
 */
@Controller
@RequestMapping("/admin/events")
public class EventModifyAction {
	private EventDAO eventDAO;
	private NewsDAO newsDAO;
	
	private EventService eventService;

	public EventDAO getEventDAO() {
		return eventDAO;
	}
	public void setEventDAO(EventDAO eventDAO) {
		this.eventDAO = eventDAO;
	}
	
	@RequestMapping("/list")
	public ModelAndView list(
			@RequestParam(value="pageNo", required=false, defaultValue="0") int pageNo,
			@RequestParam(value="pageSize", required=false, defaultValue="15") int pageSize
			){
		List<Event> events = eventService.getEventRanking(new Page(pageNo, pageSize));
		
		return new ModelAndView("/admin/event_list").addObject("events", events);
	}
	
	
	
	
	
	@RequestMapping(value = "/content", method = RequestMethod.GET)
	public String updateEvent(
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
		if((!title.equals("")||!(title==null)))
			eventDAO.updateTitle(eventId, title);
		if((!summary.equals("")||!(summary==null)))
			eventDAO.updateSummary(eventId, summary);
		if((!newsId.equals("")||!(newsId==null)))
			eventDAO.addNews(eventId, newsId);
		if((!keyWords.equals("")||!(keyWords==null)))
			eventDAO.addKeywords(eventId, keyWords);
		if((!location.equals("")||!(location==null)))
			eventDAO.updateLocation(eventId, location);
		String status="";
		return "redirect:/eventManage?eventId="+eventId+"&status="+status;

	}
	@RequestMapping(value = "/deleteNews", method = RequestMethod.GET)
	public String deleteNews(
			@RequestParam(value = "eventId", required = false) int eventId,
			@RequestParam(value = "newsId", required = false) String newsId){
		String[] newsIdList=newsId.split(";");
		for(String news:newsIdList){
			newsDAO.deleteNews(eventId, news);
		}
		
		String status="";
		return "redirect:/eventManage?eventId="+eventId+"&status="+status;
        
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
	public NewsDAO getNewsDAO() {
		return newsDAO;
	}
	public void setNewsDAO(NewsDAO newsDAO) {
		this.newsDAO = newsDAO;
	}
	public EventService getEventService() {
		return eventService;
	}
	public void setEventService(EventService eventService) {
		this.eventService = eventService;
	}

}
