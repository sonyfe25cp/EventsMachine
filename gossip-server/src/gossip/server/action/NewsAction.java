package gossip.server.action;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.bit.dlde.utils.DLDELogger;
import gossip.model.News;
import gossip.server.service.NewsService;

/**
 * 访问新闻的action。
 * 
 * @author lins 2012-8-13
 */
@Controller
@RequestMapping("/news")
public class NewsAction {
	private DLDELogger logger = new DLDELogger();;

	@Autowired
	private NewsService newsService;
	
	/**
	 * 获得一整个新闻。 /news/{id}
	 * 
	 * @param id
	 *            新闻的id
	 * @return 
	 *         返回一个由JSONObject表示的新闻{“id”:123,“title”:”xxx”,“desc”:””,“author”:””,
	 *         “body”:””,“publish_at”:””,“source”:””,”started_location”:”””
	 *         keywords”:”[“xx”,”yy”]”}
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	public News getNewsById(@PathVariable int id) {
		return newsService.getNewsById(id);
	}

	/**
	 * 读取组成某一事件的各个新闻
	 * 
	 * @param newsids
	 *            与事件相关的新闻id组成的字符串，以；间隔
	 * @return {"news":[{},{}...]}
	 */
	@RequestMapping(value = "/getEventNews")
	@ResponseBody
	public List<News> getEventNews(@RequestParam(value = "newsIdList", required = true) String newsIdList){
		return newsService.getEventNews(newsIdList);
	}
	
	//将格式为“2012-05-13”的字符串转化为int型，以方便比较大小
	int timeToInt(String time){
		String[] time1=time.split("-");
		String timeString="";
		for(int i=0;i<time1.length;i++){
			timeString+=time1[i];
		}
		return Integer.parseInt(timeString);
	}

	public NewsService getNewsService() {
		return newsService;
	}

	public void setNewsService(NewsService newsService) {
		this.newsService = newsService;
	}

	
	
}
