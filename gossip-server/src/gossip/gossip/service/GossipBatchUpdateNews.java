package gossip.gossip.service;

import gossip.gossip.utils.TokenizerUtils;
import gossip.mapper.NewsMapper;
import gossip.mapper.Page;
import gossip.model.News;

import java.util.List;

/**
 * 用于批量更新数据库中新闻的分词
 * @author ChenJie
 *
 * Oct 8, 2013
 */
public class GossipBatchUpdateNews extends Service{

	private NewsMapper newsMapper = session.getMapper(NewsMapper.class) ;
	
	public void testUpdate(){
		News news= newsMapper.getNewsById(162651);
		tokenizerNews(news);
	}
	
	
	public void batchUpdate(){
		int total = 90000;
		int pageSize = 1000;
		int run = total / 100;
		int count = 0;
		for(int i = 0; i<= run ; i++){
			List<News> newsList = newsMapper.getNewsByPage(new Page(i,pageSize));
			for(News news : newsList){
				tokenizerNews(news);
				count++;
				if(count/100==0)
					System.out.println("count is :"+count);
			}
		}
	}
	
	private void tokenizerNews(News news){
		String title = news.getTitle();
		String body = news.getBody();
		String[] titleWords = TokenizerUtils.tokenizer(title);
		String[] bodyWords = TokenizerUtils.tokenizer(body);
		
		String twords = TokenizerUtils.arrayToString(titleWords);
		String bwords = TokenizerUtils.arrayToString(bodyWords);
		
		news.setTitleWords(twords);
		news.setBodyWords(bwords);
		newsMapper.updateNews(news);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		GossipBatchUpdateNews bun = new GossipBatchUpdateNews();
		bun.batchUpdate();
//		bun.testUpdate();
	}

}
