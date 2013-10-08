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
//import gossip.dao.RelatedEventDAO;
//
///**
// * @author jiangxiaotian
// * 
// */
//@Controller
//@RequestMapping("/related-events")
//public class RelatedEventAction {
//
//	private DLDELogger logger;
//	private RelatedEventDAO relatedEventDAO;
//	private EventAction event;
//
//	public DLDELogger getLogger() {
//		return logger;
//	}
//
//	public void setLogger(DLDELogger logger) {
//		this.logger = logger;
//	}
//
//	public RelatedEventDAO getRelatedEventDAO() {
//		return relatedEventDAO;
//	}
//
//	public void setRelatedEventDAO(RelatedEventDAO relatedEventDAO) {
//		this.relatedEventDAO = relatedEventDAO;
//	}
//
//	public EventAction getEvent() {
//		return event;
//	}
//
//	public void setEvent(EventAction event) {
//		this.event = event;
//	}
//
//	/**
//	 * 返回{id}事件的第{pageNo}页的{limit}个相关事件，默认10个
//	 * 
//	 * @param id
//	 *            事件的Id
//	 * @param pageNo
//	 *            页号,指名返回的是相关事件（很多）的第几页，从1开始。
//	 * @param limit
//	 *            至多返回的相关事件数，默认为10
//	 * @return 返回一个由JSONArray表示的相关事件列表，其中每项的示例如下： {<BR/>
//	 *         "id":"e-1",//当前事件的id<BR/>
//	 *         "total":15,//总共的事件总数<BR/>
//	 *         "pageNo":1,//当前是第几页<BR/>
//	 *         "limit":15,//每頁15個<BR/>
//	 *         "events" : [{<BR/>
//	 *         "id" : "e-1",//事件id<BR/>
//	 *         "title" : "",//事件标题<BR/>
//	 *         "desc" : "",//事件描述<BR/>
//	 *         "img":"/images/e-1.jpg",//事件焦點圖<BR/>
//	 *         "started_at" : "2012-10-1",//起始时间<BR/>
//	 *         "started_location" : "",//起始地点<BR/>
//	 *         "keywords" : ["xx", "yy"],//关键词集合<BR/>
//	 *         "news" : ["n-1", "n-2"]//新闻id集合<BR/>
//	 *         }.....]<BR/>
//	 *         }<BR/>
//	 */
//	@RequestMapping(method = RequestMethod.GET)
//	@ResponseBody
//	public JSONObject getRelatedEvents(
//			@RequestParam(value = "id") int eventId,
//			@RequestParam(value = "pageNo") int pageNo,
//			@RequestParam(value = "limit", required = false, defaultValue = "10") int limit) {
//
//		JSONArray relatedEventsArray = new JSONArray();
//		// relatedEventIDs应以某种方式排序，比如说相关程度，设放置于一张相关度表中
//		// 因为每次读DB时都要对相关度进行排序，所以直接一次排好序全取出来，省的麻烦
//		JSONArray relatedEventIDs = relatedEventDAO.getRelatedEventIds(eventId);
//		System.out.println("relatedEventIDs="+relatedEventIDs);
//		
//		// 取出表中对应{pageNo}页的{limit}个EventID
//		int startNo = limit * (pageNo - 1);
//		int endNo = limit * pageNo - 1;
//		for (int i = startNo; i <= endNo&&i<relatedEventIDs.size(); i++) {
//			Object idObject = relatedEventIDs.get(i);
//			if (idObject == null)
//				break;
//			else {
//				// 调用EventAction中以Eventid来获取Event的方法：getEventByDBid *
//				// 设这个方法只返回id对应的Event，并不返回此Event对应的News
//				JSONObject eventAndItsNews = event.getEventByDBid((Integer) idObject);
//				if(eventAndItsNews!=null)
//					relatedEventsArray.add(eventAndItsNews);
//			}
//		}
//		System.out.println("relatedEventsArray=" +relatedEventsArray);
//		JSONObject relatedEventsWithInfo = new JSONObject();
//		relatedEventsWithInfo.accumulate("id", eventId)
//				.accumulate("total", relatedEventIDs.size())
//				.accumulate("pageNo", pageNo).accumulate("limit", limit)
//				.accumulate("events", relatedEventsArray);
//		System.out.println("returning=" + relatedEventsWithInfo);
//		return relatedEventsWithInfo;
//	}
//
//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//
//	}
//
//}
