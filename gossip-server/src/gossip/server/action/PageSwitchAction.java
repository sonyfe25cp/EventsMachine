package gossip.server.action;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * 控制页面跳转的action
 * @author shiyulong
 *
 */
@Controller
public class PageSwitchAction {
	@RequestMapping(value="/event",method=RequestMethod.GET)
	public ModelAndView toEvent(@RequestParam("eventId") String eventId){
		ModelAndView mav = new ModelAndView("event");
		mav.addObject("eventId", eventId);
		return mav;
	}
	@RequestMapping(value="/eventmodify",method=RequestMethod.GET)
	public ModelAndView toEventModify(@RequestParam("eventId") String eventId){
		ModelAndView mav = new ModelAndView("event_modify");
		mav.addObject("eventId", eventId);
		return mav;
	}
	
	@RequestMapping(value="/newsContent",method=RequestMethod.GET)
	public ModelAndView toNews(@RequestParam("newsId") String newsId){
		ModelAndView mav = new ModelAndView("news");
		mav.addObject("newsId", newsId);
		return mav;
	}
	@RequestMapping(value="/")
	public ModelAndView toIndex(){
		ModelAndView mav = new ModelAndView("index");
		return mav;
	}
	//跳转到事件修改页面
	@RequestMapping(value="/eventManage",method=RequestMethod.GET)
	public ModelAndView toEventManage(@RequestParam("eventId") String eventId,
			@RequestParam(value="status",required=false,defaultValue="") String status){
		ModelAndView mav = new ModelAndView("eventManage");
		mav.addObject("eventId", eventId);
		mav.addObject("status", status);
		return mav;
	}
	
	//跳转到搜索界面
	@RequestMapping(value = "/search")
	public ModelAndView toSearch(){
		ModelAndView mav = new ModelAndView("search");
		return mav;
	}
	
	//跳转到搜索界面
	@RequestMapping(value = "/wordtagging")
	public ModelAndView toWordTagging(){
		ModelAndView mav = new ModelAndView("wordtagging");
		return mav;
	}
	
	//跳转到后台
	@RequestMapping("/admin")
	public ModelAndView toAdmin(){
		ModelAndView mav = new ModelAndView("/admin/login");
		return mav;
	}
	
}
