//package gossip.server.action;
//
//import net.sf.json.JSONArray;
//import net.sf.json.JSONObject;
//
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import edu.bit.dlde.utils.DLDELogger;
//import gossip.dao.RelatedNewsDAO;
//
///**
// * @author jiangxiaotian
// * 
// */
//@Controller
//@RequestMapping("/related-news")
//public class RelatedNewsAction {
//
//	private DLDELogger logger;
//	private RelatedNewsDAO relatedNewsDAO;
//	private NewsAction news;
//
//	public DLDELogger getLogger() {
//		return logger;
//	}
//
//	public void setLogger(DLDELogger logger) {
//		this.logger = logger;
//	}
//
//	public RelatedNewsDAO getRelatedNewsDAO() {
//		return relatedNewsDAO;
//	}
//
//	public void setRelatedNewsDAO(RelatedNewsDAO relatedNewsDAO) {
//		this.relatedNewsDAO = relatedNewsDAO;
//	}
//
//	public NewsAction getNews() {
//		return news;
//	}
//
//	public void setNews(NewsAction news) {
//		this.news = news;
//	}
//
//	/**
//	 * 返回{id}事件的第{pageNo}页的{limit}个相关新闻，默认10个
//	 * 
//	 * @param id
//	 *            需查询相关新闻的新闻Id
//	 * @param pageNo
//	 *            页号,指名返回的是相关新闻（很多）的第几页，从1开始。
//	 * @param limit
//	 *            至多返回的相关新闻数，默认10
//	 * @return 返回一个由JSONArray表示的相关新闻列表,格式如下： { "id":"n-1",//新聞id</br>
//	 *         "pageNo":1,</br> "limit":15,</br> "news":[</br> "id":123,</br>
//	 *         "title":"xxx",</br> "desc":"",</br> "author":"",</br>
//	 *         "body":"",</br> "publish_at":"",</br> "source":"",</br>
//	 *         "started_location":"",</br> "keywords":["xx","yy"]</br>
//	 *         }...]</br> }
//	 */
//	@RequestMapping(method = RequestMethod.GET)
//	@ResponseBody
//	public JSONObject getRelatedNews(
//			@RequestParam(value = "id") int newsId,
//			@RequestParam(value = "pageNo") int pageNo,
//			@RequestParam(value = "limit", required = false, defaultValue = "10") int limit) {
//
//		JSONArray relatedNewsArray = new JSONArray();
//		// relatedNewsIDs应以某种方式排序，比如说相关程度，放置于一张相关度表中
//		// 因为每次读DB时都要对相关度进行排序，所以直接一次排好序全取出来，省的麻烦，内存缓存的时候也省事
//		// ？？问题：在列表页面，用户在跳转页面时新闻相关度排序发生改变（比如加入新新闻）导致第二页又出现相同内容，怎么办？
//		//
//		JSONArray relatedNewsIDs = relatedNewsDAO.getRelatedNewsIds(newsId);
//
//		// 取出表中对应{pageNo}页的{limit}个NewsID
//		int startNo = limit * (pageNo - 1);
//		int endNo = limit * pageNo - 1;
//		for (int i = startNo; i <= endNo&&i<relatedNewsIDs.size(); i++) {
//			Object idObject = relatedNewsIDs.get(i);
//			if (idObject == null)
//				break;
//			else {
//				// 设：getNewsDetail方法返回id对应新闻的具体新闻内容
//				JSONObject newsObject = news.getNewsById((Integer) idObject);
//				if(newsObject!=null)
//					relatedNewsArray.add(newsObject);
//			}
//		}
//
//		JSONObject relatedNewsWithInfo = new JSONObject();
//		relatedNewsWithInfo.accumulate("id", newsId)
//				.accumulate("pageNo", pageNo).accumulate("limit", limit)
//				.accumulate("news", relatedNewsArray);
//		System.out.println("returning:" + relatedNewsWithInfo.toString());
//		return relatedNewsWithInfo;
//	}
//
//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//
//	}
//
//}
