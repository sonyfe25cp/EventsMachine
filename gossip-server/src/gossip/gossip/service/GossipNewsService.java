package gossip.gossip.service;

import gossip.gossip.utils.DateTrans;
import gossip.mapper.NewsMapper;
import gossip.model.News;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GossipNewsService {
	@Autowired
	private NewsMapper newsMapper;
	@Autowired
	private GossipSimCompute gossipSimCompute;
	
	public News getNewsById(int id){
		return newsMapper.getNewsById(id);
	}
	
	private double lambda = 0.5;

	public double compairNews(News n1, News n2){
		return gossipSimCompute.cosineSim(n1, n2);
	}
	
	public boolean isSimilar(News n1, News n2){
		double sim = gossipSimCompute.cosineSim(n1, n2);
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
