package gossip.server.action;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.bit.dlde.utils.DLDELogger;
import gossip.dao.NewsDAO;

/**
 * 访问新闻的action。
 * 
 * @author lins 2012-8-13
 */
@Controller
@RequestMapping("/news")
public class NewsAction {
	private DLDELogger logger;

	public DLDELogger getLogger() {
		return logger;
	}

	public void setLogger(DLDELogger logger) {
		this.logger = logger;
	}
	
	private NewsDAO newsDAO;

	public NewsDAO getNewsDAO() {
		return newsDAO;
	}

	public void setNewsDAO(NewsDAO newsDAO) {
		this.newsDAO = newsDAO;
	}

	/** 每个事件集由X个事件组成 **/
	static final int X = 10;

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
	public JSONObject getNewsById(@PathVariable long id) {
		return newsDAO.getNewsJSONById(id);
	}

	@RequestMapping(value = "/getnew", method = RequestMethod.GET)
	@ResponseBody
	public JSONObject getNewById() {
		int id = 1;
		JSONObject jsonObj = null;
		return jsonObj;
	}

	/**
	 * 获得一个新闻列表。/news?pageNo=5&&limit=15
	 * 
	 * @param pageNo
	 * @param limit
	 * @return {"total":55,//总共的新闻总数 "pageNo":5,//当前是第几页 "limit":15,//每頁15個
	 *         "news":[{},{},{}..]
	 */
	@RequestMapping(value = "/n", method = RequestMethod.GET)
	@ResponseBody
	public JSONObject getNewsList(
			@RequestParam(value = "pageNo", required = false, defaultValue = "0") int pageNo,
			@RequestParam(value = "limit", required = false, defaultValue = "10") int limit) {
		System.out.println(pageNo + "--" + limit);
		JSONArray jsonArray = new JSONArray();
		JSONObject json = null, newsRanking = null, result = new JSONObject();

		/** 起始于page*X，终止于(page + 1) * X - 1 **/
		int begin = (pageNo - 1) * limit;
		/** 先初始化newsRanking **/
		newsRanking = newsDAO.getNewsRankingJSON(begin, limit);
		/** 根据ranking获得新闻 **/
		for (int i = begin; i < begin + limit; i++) {
			Object o = newsRanking.get(i + "");
			if (o == null)
				break;
			json = newsDAO.getNewsJSONById((Integer) o);
			if (json != null) {
				if (!jsonArray.contains(json))
					jsonArray.add(json);
			} else {// 不存在则提示没有
				logger.debug("erro ranking!");
			}
		}

		result.accumulate("total", newsRanking.get("total"));
		result.accumulate("pageNo", pageNo);
		result.accumulate("limit", limit);
		result.accumulate("news", jsonArray);
		logger.info("Succeed to response news list in format of JSON.....");
		System.out.println(result);
		return result;
	}

	/**
	 * 读取组成某一事件的各个新闻
	 * 
	 * @param newsids
	 *            与事件相关的新闻id组成的字符串，以；间隔
	 * @return {"news":[{},{}...]}
	 */
	@RequestMapping(value = "/event-news", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject getEventNews(
			@RequestParam(value = "newsids", required = true) String newsids) {
		System.out.print("get the event-news");
		String[] news_ids = newsids.split(";");
		
		JSONArray jsonArray = new JSONArray();
		JSONObject json = null, result = new JSONObject();

		for (int i = 0; i < news_ids.length; i++) {
			json = newsDAO.getNewsJSONById(Long.parseLong(news_ids[i]));
			if (json != null) {
				if (!jsonArray.contains(json)){
					jsonArray.add(json);
				}
					
			} else {// 不存在则提示没有
				logger.debug("erro!");
			}
		}
		//使用选择排序对新闻按时间逆序排序
		for(int i=0;i<jsonArray.size();i++){
			int k=i;
			for(int j=i+1;j<jsonArray.size();j++){
				JSONObject jsona=JSONObject.fromObject(jsonArray.get(j));
				JSONObject jsonb=JSONObject.fromObject(jsonArray.get(k));
				if(timeToInt(jsona.getString("publish_at"))>timeToInt(jsonb.getString("publish_at"))){
					k=j;
				}
			}
			if(k!=i){
				JSONObject jsonc=JSONObject.fromObject(jsonArray.get(i));
				JSONObject jsonk=JSONObject.fromObject(jsonArray.get(k));
				jsonArray.set(i, jsonk);
				jsonArray.set(k, jsonc);
			}
		}

		result.accumulate("news", jsonArray);
		logger.info("Succeed to response news list in format of JSON...");

		return result;
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
}
