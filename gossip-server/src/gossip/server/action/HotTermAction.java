package gossip.server.action;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.bit.dlde.utils.DLDELogger;
import gossip.dao.HotTermDAO;

/**
 * @author jiangxiaotian
 * 
 */
@Controller
public class HotTermAction {

	private DLDELogger logger;
	HotTermDAO hotTermDAO;

	public DLDELogger getLogger() {
		return logger;
	}

	public void setLogger(DLDELogger logger) {
		this.logger = logger;
	}
	
	public HotTermDAO getHotTermDAO() {
		return hotTermDAO;
	}

	public void setHotTermDAO(HotTermDAO hotTermDAO) {
		this.hotTermDAO = hotTermDAO;
	}


	/**
	 * 返回系统当前的热门关键词
	 * 
	 * @param limit
	 *            至多返回的热门关键词个数，可空，默认50
	 * @return 返回一个由JSONObject表示的热门关键词列表，返回数据的格式如下：
	 *         {"hotKeywords":["xx","yy","zz"...]}
	 */
	@RequestMapping(value = "/hot-words", method = RequestMethod.GET)
	@ResponseBody
	public JSONObject getHotKeywords(
			@RequestParam(value = "limit", required = false, defaultValue = "50") int limit) {
		// 这里popKeywordsArray先写成每次返回limit个，而不写成将所有rank全部返回，因为貌似limit不常改动，
		// 而且因为rank可能是不断变化的，所以放在memcached的话需要注意更新
		System.out.println("!!!in<getHotKeywords>1");
		System.out.println("hotTermDAO="+hotTermDAO);
		JSONArray hotKeywordsArray = hotTermDAO.getHotTerms(
				HotTermDAO.HOTKEYWORDS, limit);
		System.out.println(hotKeywordsArray.size());
		System.out.println("!!!in<getHotKeywords>2");
		return new JSONObject().accumulate("hotKeywords", hotKeywordsArray);
	}

	/**
	 * 返回系统当前的热门人名
	 * 
	 * @param limit
	 *            至多返回的热门人名个数，可空，默认50
	 * @return 返回一个由JSONObject表示的热门人名列表，返回数据的格式如下： {"hotPeople":
	 *         [“xx”,"yy","zz"...]}
	 */
	@RequestMapping(value = "/hot-people", method = RequestMethod.GET)
	@ResponseBody
	public JSONObject getHotPeople(
			@RequestParam(value = "limit", required = false, defaultValue = "50") int limit) {
		// 这里popKeywordsArray先写成每次返回limit个，而不写成将所有rank全部返回，因为貌似limit不常改动，
		// 而且牵扯到因为rank可能是不断变化的，所以放在memcached的话需要注意更新
		JSONArray hotPeopleArray = hotTermDAO.getHotTerms(HotTermDAO.HOTPEOPLE,
				limit);
		return new JSONObject().accumulate("hotPeople", hotPeopleArray);
	}

	/**
	 * 返回系统当前的热门地点
	 * 
	 * @param limit
	 *            至多返回的热门地点个数，可空，默认50
	 * @return 返回一个由JSONObject表示的热门地点列表，返回数据的格式如下： {"hotPeople":
	 *         [“xx”,"yy","zz"...]}
	 */
	@RequestMapping(value = "/hot-places", method = RequestMethod.GET)
	@ResponseBody
	public JSONObject getHotLocations(
			@RequestParam(value = "limit", required = false, defaultValue = "50") int limit) {
		// 这里popKeywordsArray先写成每次返回limit个，而不写成将所有rank全部返回，因为貌似limit不常改动，
		// 而且牵扯到因为rank可能是不断变化的，所以放在memcached的话需要注意更新
		JSONArray hotLocationsArray = hotTermDAO.getHotTerms(
				HotTermDAO.HOTLOCATIONS, limit);
		return new JSONObject().accumulate("hotPlaces", hotLocationsArray);
	}

	public static void main(String[] args) {

	}

}
