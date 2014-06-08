package gossip.gossip.service;

import gossip.model.Event;
import gossip.model.News;
import gossip.model.SimilarityCache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GossipEventDetection {
	private static final double lambda = 0.5;//yuzhi
	
	@Autowired
	private GossipSimCompute gossipSimCompute;
	
	@Autowired
	private GossipSimilarityCacheService gossipSimilarityCacheService;
	
	/**
	 * if a sim b > lambda , a b belong to same event
	 * 参数1：空的events表
	 * 参数2：新闻列表
	 * @param newsList
	 * @return
	 */
	public List<Event> simpleDetect(List<News> newsList){
		Map<Integer, Integer> mark = new HashMap<Integer, Integer>();//新闻所在的事件号，news.id : event.id
		Map<Integer, Event> eventStore = new HashMap<Integer, Event>();//事件的序号，1-n 与新闻号无关
		int storeCount = 0;
		
		for(int i = 0; i < newsList.size(); i++){
			News n1 = newsList.get(i);
			int id1 = n1.getId();
			Map<String, SimilarityCache> newsSimilarityCache = gossipSimilarityCacheService.getSimilarityCacheMapByPartIdAndCacheType(id1, SimilarityCache.News);
//			System.out.println("computing the news : "+n1.getId());
			for(int j = i+1; j < newsList.size(); j++){
				News n2 = newsList.get(j);
				int id2 = n2.getId();
				
				double sim = 0;
				/**
				 * 缓存n1计算过的所有结果
				 */
				String pair = SimilarityCache.generatePair(id1, id2);
				if(newsSimilarityCache != null){
					SimilarityCache cache = newsSimilarityCache.get(pair);
					if(cache != null){
						sim = cache.getSimilarity();
					}else{
						sim = gossipSimCompute.cosineSim(n1, n2);
					}
				}else{
					sim = gossipSimCompute.cosineSim(n1, n2);
				}
				
				if(sim > lambda && sim != 1.0){
					//n1与n2相似
					//step1 找出n1所在的事件
					//	若不存在，则新建一个事件，并记录n1在该事件中
					//step2 把n2放入该事件
					//step3 记录n2所在的事件
					Event event = null;
					int cusor = 0;
					Integer cTmp = mark.get(i);
					if(cTmp !=null ){//说明该n1 已经有所属的事件
						cusor = cTmp;
						event = eventStore.get(cusor);
						event.add(n2);
						mark.put(j, cusor);
					}else{//该n1是个新事件
						event = new Event();
						event.add(n1);
						event.add(n2);
						eventStore.put(storeCount, event);
						mark.put(i, storeCount);
						mark.put(j, storeCount);
						storeCount++;
					}
				}
			}
		}
		List<Event> events = new ArrayList<Event>();
		for(Entry<Integer, Event> entry : eventStore.entrySet()){
			events.add(entry.getValue());
		}
		return events;
	}
	
	public List<Event> KMeansDetect(List<News> newsList){//基本的kmeans方法
		return null;
	}
	public List<Event> DBScanDetect(List<News> newsList){//基本的dbscan方法
		return null;
	}
	public List<Event> HDPDetect(List<News> newsList){//HDP方法
		return null;
	}
}
