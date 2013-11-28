package gossip.server.service;

import edu.bit.dlde.utils.DLDELogger;
import gossip.mapper.NewsMapper;
import gossip.model.News;

import java.util.ArrayList;
import java.util.List;

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
	/**
	 * 根据新闻id号从数据库中读取news信息，并以json的格式返回
	 * @param id
	 * @return
	 */
	public News getNewsById(int id){
		News news = newsMapper.getNewsById(id);
		return news;
	}
	/**
	 * 根据事件的一组新闻id从数据库中读取新闻并返回
	 * @param newsIdList
	 * @return
	 */
	public List<News> getEventNews(String newsIdList){
		String[] ids = newsIdList.split(",");
		List<Integer> idArray = new ArrayList<Integer>();
		for(String id : ids){
			if(id!=null && id.length() > 0){
				int tmp = Integer.parseInt(id);
				idArray.add(tmp);
			}
		}
		return newsMapper.getNewsListByIds(idArray);
	}
	
	public List<News> getAllNews(){
		return newsMapper.getAllNews();
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
