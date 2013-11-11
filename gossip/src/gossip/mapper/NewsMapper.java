package gossip.mapper;

import gossip.model.News;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface NewsMapper {
	
	public void insertNews(News news);
	
	public void updateNews(News news);
	
	public List<News> getNewsByDate(String date);//dateFormat: 20130312
	
	public List<News> getNewsAfterDate(@Param("crawlAt")String crawlAt, @Param("eventStatus")int eventStatus);
	
	public News getNewsById(int id);
	
	public List<News> getAllNews();
	
	public List<News> getNewsByPage(@Param("page")Page page);

	public void batchUpdateNewsStatus(@Param("idArray")List<Integer> idArray, @Param("eventStatus")int eventStatus);
}
