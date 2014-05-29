package gossip.server.service.local;

import gossip.mapper.NewsMapper;
import gossip.model.News;

import java.util.List;

public class NewsService extends LocalService{
	
	
	NewsMapper newsMapper = session.getMapper(NewsMapper.class);
	
	public List<News> findALL(){
		return newsMapper.getAllNews();
	}
	
}
