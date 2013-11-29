package gossip.gossip.service;

import gossip.mapper.NewsMapper;
import gossip.mapper.Page;
import gossip.model.News;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class BatchNewsReader{

	private NewsMapper newsMapper;
	
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



	public NewsMapper getNewsMapper() {
		return newsMapper;
	}



	public void setNewsMapper(NewsMapper newsMapper) {
		this.newsMapper = newsMapper;
	}
	
}
