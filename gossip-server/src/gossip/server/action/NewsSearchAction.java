package gossip.server.action;

import gossip.gossip.service.UserSearchService;
import gossip.model.News;
import gossip.server.service.NewsService;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class NewsSearchAction {
	
	@Autowired
	private NewsService newsService;
	
	@Autowired
	private UserSearchService userSearchService;
	
	
	//跳转到搜索界面
	@RequestMapping(value = "/news-search")
	public ModelAndView toSearch(){
		ModelAndView mav = new ModelAndView("/search/search");
		return mav;
	}
	
	@RequestMapping(value= "/news-q")
	public ModelAndView search(
			@RequestParam(value = "queryWords", required = true) String queryWords
			){
		System.out.println("queryWords:  " + queryWords  );
		if(queryWords==null||queryWords.trim().equals("")){
			return new ModelAndView("/search/search");
		}
		List<News> newsList= userSearchService.search(queryWords);
		return new ModelAndView("/search/result").addObject("newsList", newsList);
	}
	

}
