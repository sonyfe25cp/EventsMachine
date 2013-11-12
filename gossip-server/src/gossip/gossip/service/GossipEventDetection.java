package gossip.gossip.service;

import gossip.model.Event;
import gossip.model.News;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class GossipEventDetection {
	private static final double lambda = 0.5;//yuzhi
	
	/**
	 * if a sim b > lambda , a b belong to same event
	 * 参数1：空的events表
	 * 参数2：新闻列表
	 * @param newsList
	 * @return
	 */
	public static List<Event> simpleDetect(List<News> newsList){
		Map<Integer, Integer> mark = new HashMap<Integer, Integer>();
		Map<Integer, Event> eventStore = new HashMap<Integer, Event>();
		int storeCount = 0;
		
		for(int i = 0; i < newsList.size(); i++){
			News n1 = newsList.get(i);
			for(int j = i+1; j < newsList.size(); j++){
				News n2 = newsList.get(j);
				double sim = GossipSimCompute.cosineSim(n1, n2);
				if(sim > lambda){
//					int cusor = mark.get(i) == 0 ? i : mark.get(i);
					int cusor = 0;
					Integer cTmp = mark.get(i);
					if(cTmp !=null ){
						cusor = cTmp;
					}else{
						cusor = i;
					}
					
					Event event = eventStore.get(cusor);
					if(event == null){
						event = new Event();
						event.add(n1);
						eventStore.put(storeCount, event);
						mark.put(i, storeCount);
						storeCount++;
					}
					event.add(n2);
					mark.put(j, i);
				}
			}
			System.out.println("computing the news : "+n1.getId());
		}
		List<Event> events = new ArrayList<Event>();
		for(Entry<Integer, Event> entry : eventStore.entrySet()){
			events.add(entry.getValue());
		}
		return events;
	}
	
}
