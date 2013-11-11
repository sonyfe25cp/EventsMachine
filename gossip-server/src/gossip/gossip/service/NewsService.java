package gossip.gossip.service;

import gossip.gossip.utils.DateTrans;
import gossip.mapper.NewsMapper;
import gossip.model.News;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class NewsService {
	
	private NewsMapper newsMapper;
	
	private double lambda = 0.5;

	public double compairNews(News n1, News n2){
		return SimCompute.cosineSim(n1, n2);
	}
	
	public boolean isSimilar(News n1, News n2){
		double sim = SimCompute.cosineSim(n1, n2);
		if(sim > lambda){
			return true;
		}else
			return false;
	}
	
	
	public List<News> getNewsLast7Days(Date date){
		
		Date begin = DateTrans.getDateBefore(date, 7);
		
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		
		String beginDate = df.format(begin);
		
		System.out.println("begin Date : " + beginDate);
		
		return newsMapper.getNewsAfterDate(beginDate, News.UnEvented);
	}
	
	public void batchUpdateNewsStatus(List<Integer> ids, int eventStatus){
		if(ids !=null && ids.size() > 0){
			newsMapper.batchUpdateNewsStatus(ids, eventStatus);
		}
	}
}
