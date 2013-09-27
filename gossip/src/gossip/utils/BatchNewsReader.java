package gossip.utils;

import gossip.mapper.NewsMapper;
import gossip.mapper.Page;
import gossip.model.News;
import gossip.service.Service;

import java.util.List;

public class BatchNewsReader extends Service{

	private NewsMapper newsMapper = session.getMapper(NewsMapper.class) ;
	
	int total = 90000;
	int pageSize = 1000;
	int run = total / 100;
	int current = 0;
	
	public BatchNewsReader(){
	}
	
	
	
	public List<News> next(){
		List<News> newsList = newsMapper.getNewsByPage(new Page(current,pageSize));
		if(newsList.size() == 0){
			return null;
		}
		current++;
		return newsList;
	}
	
	
}
