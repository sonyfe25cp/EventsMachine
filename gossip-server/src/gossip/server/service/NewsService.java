package gossip.server.service;

import edu.bit.dlde.utils.DLDELogger;
import gossip.server.mapper.NewsMapper;
import gossip.server.model.News;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author yulong
 *
 */
@Service
public class NewsService {
	
	@Autowired
	private NewsMapper newsMapper;
	
	DLDELogger logger = new DLDELogger();
	public JSONObject getNewsById(int id){
		News news = newsMapper.getNewsById(id);
		return JSONObject.fromObject(news);
	}
	
	public JSONObject getEventNews(String newsIdList){
		JSONObject json = new JSONObject();
		JSONObject result = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		String[] ids = newsIdList.split(";");
		for(String id : ids)
		{
			News news = newsMapper.getNewsById(Integer.parseInt(id));
			json = JSONObject.fromObject(news);
			if(json != null){
				jsonArray.add(json);
			}
			else
				logger.debug("have not the news error!");
		}//for
		
		//使用选择排序对新闻按时间逆序排序
		for(int i=0;i<jsonArray.size();i++)
		{
			int k=i;
			for(int j=i+1;j<jsonArray.size();j++){
				JSONObject jsona=JSONObject.fromObject(jsonArray.get(j));
				JSONObject jsonb=JSONObject.fromObject(jsonArray.get(k));
				if(timeToInt(jsona.getString("date"))>timeToInt(jsonb.getString("date"))){
					k=j;
				}
			}
			if(k!=i){
				JSONObject jsonc=JSONObject.fromObject(jsonArray.get(i));
				JSONObject jsonk=JSONObject.fromObject(jsonArray.get(k));
				jsonArray.set(i, jsonk);
				jsonArray.set(k, jsonc);
			}
		}//for
		
		result.accumulate("news", jsonArray);
		logger.info("Succeed to response news list in format of JSON...");
		return result;
	}
	
	int timeToInt(String time){
		String[] time1=time.split("-");
		String timeString="";
		for(int i=0;i<time1.length;i++){
			timeString+=time1[i];
		}
		return Integer.parseInt(timeString);
	}

	public NewsMapper getNewsMapper() {
		return newsMapper;
	}

	public void setNewsMapper(NewsMapper newsMapper) {
		this.newsMapper = newsMapper;
	}
	
	

}
