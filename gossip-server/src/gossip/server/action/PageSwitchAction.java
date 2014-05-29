package gossip.server.action;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * 控制页面跳转的action
 * @author shiyulong
 *
 */
@Controller
public class PageSwitchAction {
	
	@RequestMapping(value="/")
	public ModelAndView toIndex(){
		ModelAndView mav = new ModelAndView("index");
		return mav;
	}
	
	//跳转到搜索界面
		@RequestMapping(value = "/talks")
		public ModelAndView toTalks(){
			ModelAndView mav = new ModelAndView("/talks/index");
			return mav;
		}
	
	//跳转到搜索界面
	@RequestMapping(value = "/wordtagging")
	public ModelAndView toWordTagging(){
		ModelAndView mav = new ModelAndView("/wordtag/wordtag");
		return mav;
	}
	
	//跳转到后台
	@RequestMapping("/admin")
	public ModelAndView toAdmin(){
		ModelAndView mav = new ModelAndView("/admin/auth/login");
		return mav;
	}
	
	@RequestMapping("/about")
	public String about(){
		return "/common/about";
	}
	
	@RequestMapping("/members")
	public String member(){
		return "/common/member";
	}
	
	@RequestMapping("/user-login")
	public String login(){
		return "/common/login";
	}
	
	@RequestMapping("/user-register")
	public String register(){
		return "/common/register";
	}
	
	@RequestMapping("/readme")
	public String readme(){
		return "/common/readme";
	}
	@RequestMapping("/framework")
	public String framework(){
		return "/common/framework";
	}
	
}
