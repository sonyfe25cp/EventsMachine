package gossip.server.mapper;

import gossip.server.model.News;

import java.util.List;

public interface NewsMapper {
	
	public News getNewsById(int id);
	
	public List<News> getNews();

}
