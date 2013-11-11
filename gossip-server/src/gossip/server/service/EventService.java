package gossip.server.service;

import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.bit.dlde.utils.DLDELogger;
import gossip.mapper.EventMapper;
import gossip.model.Event;

@Service
public class EventService {
	@Autowired
	private EventMapper eventMapper;
	
	private DLDELogger logger = new DLDELogger();

	public Event getEventById(int id) {
		Event event = eventMapper.getEventById(id);
		System.out.println("create time: " + event.getCreateTime());
		event.setCreateTime(event.getCreateAt().getTime());
		return event;
	}

	public JSONObject getEventJSONById(int id) {
		JSONObject json = new JSONObject();
		Event event = getEventById(id);
		json.put("id", event.getId());
		json.put("title", event.getTitle());
		json.put("desc", event.getDesc());
		json.put("img", event.getImg());
		json.put("recommended", event.getRecommended());
		json.put("started_at", event.getCreateTime());
		json.put("keywords", event.getKeyWords());
		json.put("started_location", event.getStartedLocation());
		json.put("news", event.getPages());
		logger.info("i found the event in database.");
		return json;
	}

	public JSONObject getEventList(int pageNo, int limit, int year, int month,
			int day) {
		if (year != 0) {
			return getEventListTimeConstrain(pageNo, limit, year, month, day);
		} else {
			return getEventListSimply(pageNo, limit);
		}
	}

	private JSONObject getEventListTimeConstrain(int pageNo, int limit,
			int year, int month, int day) {
		JSONObject result = new JSONObject();
		return result;
	}

	private JSONObject getEventListSimply(int pageNo, int limit) {
		JSONObject result = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONObject json = null;
		/** 按照recommended降序搜索 **/
		JSONObject eventRanking = getEventRanking();

		/** 起始于page*limit，终止于(page + 1) * limit - 1 **/
		int begin = (pageNo - 1) * limit;
		for (int i = begin; i < begin + limit; i++) {
			Object o = eventRanking.get(i + "");
			if (o == null)
				break;
			json = getEventJSONById((Integer)o);
			if (json != null) {
				if (!jsonArray.contains(json))
					jsonArray.add(json);
			} else {// 不存在则提示没有
				logger.debug("erro ranking!");
			}
		}
		/** total存放所有事件的个数**/
		int total = eventRanking.size();
		/** totalpage存放页面的个数 **/
		int totalPage = total % 10 == 0 ? total / 10 : total / 10 + 1;
		int pageBegin = pageNo - 5 > 0 ? pageNo - 5 : 1;
		int pageEnd;
		if (totalPage <= 11)
			pageEnd = pageNo;
		else if (pageNo <= 6)
			pageEnd = 10;
		else {
			pageEnd = pageNo + 4;
			if (pageEnd >= totalPage) {
				pageBegin = totalPage - limit - 1;
				pageEnd = totalPage - 1;
			}

		}
		System.out.println("total: " + total);
		result.accumulate("total", total);
		result.accumulate("totalPage", totalPage);
		result.accumulate("pageBegin", pageBegin);
		result.accumulate("pageEnd", pageEnd);
		result.accumulate("pageNo", pageNo);
		result.accumulate("limit", limit);
		result.accumulate("events", jsonArray);
		logger.info("Succeed to response events in format of JSON...");

		return result;
	}

	/**
	 * 从数据库里面读出按照recommeded排序的event，并且将id和排序号放入jsonObj
	 * 
	 * @return <ranking, event-id>...
	 */
	public JSONObject getEventRanking() {
		JSONObject jsonObj = new JSONObject();
		List<Integer> eventIdRanking = eventMapper.getEventRanking();
		if (eventIdRanking == null) {
			return null;
		}
		for (int i = 0; i < eventIdRanking.size(); i++) {
			int id = eventIdRanking.get(i);
			jsonObj.put(i, id);
		}
		return jsonObj;
	}
	
	

	public EventMapper getEventMapper() {
		return eventMapper;
	}

	public void setEventMapper(EventMapper eventMapper) {
		this.eventMapper = eventMapper;
	}

	public static void main(String[] args) {
		EventService eventService = new EventService();
		Event event = eventService.getEventById(7);
		System.out.println(event.getTitle() + "   " + event.getKeyWords());
	}
}
