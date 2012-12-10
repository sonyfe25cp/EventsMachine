package gossip.server.action;

import java.sql.Date;
import java.util.concurrent.TimeoutException;

import net.rubyeye.xmemcached.exception.MemcachedException;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.bit.dlde.utils.DLDEConfiguration;
import edu.bit.dlde.utils.DLDELogger;
import gossip.dao.EventDAO;

/**
 * 访问事件的action。各种事件被缓存在memcached里面。
 * 
 * @author lins
 */
@Controller
@RequestMapping("/events")
public class EventAction {
	private DLDELogger logger;

	public DLDELogger getLogger() {
		return logger;
	}

	public void setLogger(DLDELogger logger) {
		this.logger = logger;
	}

	private EventDAO eventDAO;

	public EventDAO getEventDAO() {
		return eventDAO;
	}

	public void setEventDAO(EventDAO eventDAO) {
		this.eventDAO = eventDAO;
	}

	/** 每一页由X个事件组成 **/
	static final int X = 10;
	/** memcached里面数据的过期时间 **/
	static final int expiration = Integer.valueOf(DLDEConfiguration
			.getInstance("gossip-server.properties").getValue("expiration"));

	/**
	 * 两种途径获得第N个事件集，每个事件集由X个事件组成。 第一种途径是/events?pageNo={pageNo}&limit={limit}
	 * 第二种途径是
	 * /events?year={year}&month={month}&day={day}pageNo={pageNo}&limit={limit}
	 * 
	 * @param pageNo
	 *            page从0开始，没一页含有X个事件。
	 * @return 返回一个由JSONArray表示的事件集
	 */
	@RequestMapping(value = "", method = RequestMethod.GET)
	@ResponseBody
	public JSONObject getEventList(
			@RequestParam(value = "pageNo", required = false, defaultValue = "0") int pageNo,
			@RequestParam(value = "limit", required = false, defaultValue = "10") int limit,
			@RequestParam(value = "year", required = false, defaultValue = "0") int year,
			@RequestParam(value = "month", required = false, defaultValue = "1") int month,
			@RequestParam(value = "day", required = false, defaultValue = "1") int day) {
		if (year != 0) {
			return getEventListTimeConstrain(pageNo, limit, year, month, day);
		} else {
			return getEventListSimply(pageNo, limit);
		}
	}

	private JSONObject getEventListTimeConstrain(int pageNo, int limit,
			int year, int month, int day) {
		JSONObject result = new JSONObject();

		JSONArray jsonArry = null, tmp = new JSONArray();
		@SuppressWarnings("deprecation")
		Date date = new Date(year - 1900, month - 1, day);
		/** 先初始化某天的所有events，最多尝试5次，否则返回空 **/
		jsonArry = eventDAO.getEventJSONByDate(date);
		/** 起始于page*limit，终止于(page + 1) * limit - 1 **/
		int begin = (pageNo - 1) * limit;
		for (int i = begin; i < begin + limit && i < jsonArry.size(); i++) {
			tmp.add(jsonArry.get(i));
		}
		result.accumulate("total", jsonArry.size());
		result.accumulate("pageNo", pageNo);
		result.accumulate("limit", limit);
		result.accumulate("events", tmp);
		logger.info("Succeed to response events in format of JSON...");
		return result;
	}

	private JSONObject getEventListSimply(int pageNo, int limit) {
		JSONArray jsonArray = new JSONArray();
		JSONObject json = null, eventRanking = eventDAO.getEventRanking(), result = new JSONObject();

		/** 起始于page*limit，终止于(page + 1) * limit - 1 **/
		int begin = (pageNo - 1) * limit;
		for (int i = begin; i < begin + limit; i++) {
			Object o = eventRanking.get(i + "");
			if (o == null)
				break;
			json = eventDAO.getEventJSONById((Integer) o);
			if (json != null) {
				if (!jsonArray.contains(json))
					jsonArray.add(json);
			} else {// 不存在则提示没有
				logger.debug("erro ranking!");
			}
		}
		result.accumulate("total", eventRanking.size());
		result.accumulate("pageNo", pageNo);
		result.accumulate("limit", limit);
		result.accumulate("events", jsonArray);
		logger.info("Succeed to response events in format of JSON...");

		return result;
	}

	final int LOCAL_MAX = 30;

	// @RequestMapping(value = "/local", method = RequestMethod.GET)
	// @ResponseBody
	// public JSONArray getEventByLocalID(
	// @RequestParam(value = "id", required = true) int id) {
	// JSONArray jsonArray = new JSONArray();
	// JSONObject json = null, rankJsonObj;
	// try {
	// rankJsonObj = memcachedDaemon.getMemcachedClient().get(
	// MemcachedKeyUtils.EVENT_RANKING_KEY);
	// if (rankJsonObj == null) {
	// logger.debug("no ranking in memcached!");
	// memcachedDaemon.fireEventRankingNotFoundEvent();// 这个步骤需要多线程
	// return jsonArray;// 先返回空，因为更新rank需要的数据库查询可能较多
	// }
	// // 从排名第0的event开始，直到找到localid或者max
	// for (int i = 0; i < LOCAL_MAX; i++) {
	// Object o = rankJsonObj.get(i + "");
	// if (o == null)
	// break;
	// json = getEventByDBid((Integer) o);
	// if (json == null || json.getInt("id") == id)
	// break;
	// jsonArray.add(json);
	// }
	// } catch (TimeoutException e) {
	// e.printStackTrace();
	// } catch (InterruptedException e) {
	// e.printStackTrace();
	// } catch (MemcachedException e) {
	// e.printStackTrace();
	// }
	// return jsonArray;
	// }

	/**
	 * 获得event
	 * 
	 * @param id
	 *            对应数据库里面event的id
	 * @return 返回一个由JSONArray表示的事件集
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	public JSONObject getEventByDBid(@PathVariable int id) {
		return eventDAO.getEventJSONById(id);
	}

	/**
	 * 获得event，底层会调用{@link getEventByDBid()}
	 * 
	 * @param rank
	 *            对应事件的排名
	 * @return event 事件在数据库中的id
	 */
	@RequestMapping(value = "/rank", method = RequestMethod.GET)
	@ResponseBody
	public JSONObject getEventByRankNum(
			@RequestParam(value = "id", required = true) int rank) {
		JSONObject eventRanking = eventDAO.getEventRanking();
		int id;

		if (!eventRanking.containsKey(rank + "")) {// 不存在则提示没有
			logger.debug("erro ranking!");
			return null;
		}
		id = (Integer) eventRanking.get(rank + "");
		
		return eventDAO.getEventJSONById(id);
	}

	@RequestMapping(value = "/search", method = RequestMethod.GET)
	@ResponseBody
	public JSONObject searchEvents(
			@RequestParam(value = "q", required = true) String query) {
		return null;
	}

	// public static void main(String[] args) {
	// EventAction action = new EventAction();
	// HashSet<EventModel> eventSet = action.listEvents();
	// for (EventModel model : eventSet) {
	// System.out.println("********************************");
	// Compare.doc_compare(true, model.getPages());
	// }
	// System.out.println("" + eventSet.size());
	// }
	public static void main(String[] args) {
		JSONObject json = new JSONObject();
		json = null;
		EventAction action = new EventAction();
		json = action.getEventListSimply(1, 15);
	}

}
