package gossip.server.action;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.bit.dlde.utils.DLDELogger;
import gossip.dao.QueryExpansionDAO;

/**
 * @author jiangxiaotian
 * 
 */
@Controller
@RequestMapping("/query-expan")
public class QueryExpansionAction {

	private DLDELogger logger;
	private QueryExpansionDAO qeDAO;

	public DLDELogger getLogger() {
		return logger;
	}

	public void setLogger(DLDELogger logger) {
		this.logger = logger;
	}

	public QueryExpansionDAO getQeDAO() {
		return qeDAO;
	}

	public void setQeDAO(QueryExpansionDAO qeDAO) {
		this.qeDAO = qeDAO;
	}
	
	/**
	 * 返回系统中与q相关的查询扩展10个
	 * 
	 * @param query
	 *            欲进行查询扩展的词
	 * @return 返回一个由JSONArray表示的相关新闻列表,格式如下： {"q":{q}, "expan":{
	 *         "events":[{"id":
	 *         "","desc":"","title":""},{"id":"","desc":"","title":""}...],
	 *         "news"
	 *         :[{"id":"","desc":"","title":""},{"id":"","desc":"","title":
	 *         ""}...], "keywords":["",""...]} }
	 */
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public JSONObject getExpansionResult(@RequestParam(value = "q") String query) {
		return qeDAO.getExpansion(query);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
