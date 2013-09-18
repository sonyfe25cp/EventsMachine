package gossip.mapper;

import gossip.model.News;

public interface NewsMapper {
	
	public void insertNews(News news);
	
	public void updateNews(News news);

}
