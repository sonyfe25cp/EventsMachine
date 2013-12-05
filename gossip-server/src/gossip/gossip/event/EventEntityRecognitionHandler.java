package gossip.gossip.event;

import gossip.model.Event;
import gossip.model.News;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.ansj.domain.Term;
import org.ansj.recognition.NatureRecognition;
import org.ansj.splitWord.analysis.ToAnalysis;
/**
 * 事件内实体识别
 * Task1: 新闻发生地识别 Location
	 * step1. 找所有新闻的地点词
	 * step2. 判断地点词的可能性,数量最多的就是
 * Task2: 事件发生人识别 People
	 * step1. 找到新闻中人名
	 * step2. 找到最重要的人名
 * @author ChenJie
 *
 * Nov 13, 2013
 */
public class EventEntityRecognitionHandler implements Handler{
	
	private Map<String, HashMap<String, Integer>> entityStore;
	
	@Override
	public void handle(Event event) {
		entityStore = new HashMap<String, HashMap<String, Integer>>();
		List<News> newsList = event.getNewsList();
		for(News news : newsList){
			String title = news.getTitle();
			String body = news.getBody();
			entityRecognition(title);
			entityRecognition(body);
		}
		String people = findMost(People);
		String location = findMost(Location);
		event.setStartedLocation(location);
		event.setImportantPeople(people);
	}
	/**
	 * 没有区分标题和正文，仅从数量判断
	 * @param entityType
	 * @return
	 */
	private String findMost(String entityType){
		String most = "";
		HashMap<String, Integer> entities = entityStore.get(entityType);
		if(entities == null){
			return most;
		}
		int max = 0;
		for(Entry<String, Integer> entry : entities.entrySet()){
			int tmpCount = entry.getValue();
			if(tmpCount > max){
				most = entry.getKey();
			}
		}
		return most;
	}
	
	
	private final static String Location = "location";
	private final static String People = "people";
	
	private void entityRecognition(String content){
		HashMap<String, Integer> locations = entityStore.get(Location);
		HashMap<String, Integer> people = entityStore.get(People);
		List<Term> terms = ToAnalysis.parse(content);
		new NatureRecognition(terms).recognition() ;
        for(Term term : terms){
        	String name = term.getName();
        	if(term.getNatrue().natureStr.equalsIgnoreCase("ns")){//地点名词
        		if(locations == null){
        			locations = new HashMap<String, Integer>();
        		}
        		Integer count = locations.get(name);
        		if(count == null){
        			count = 1;
        		}
        		count = count+1;
        		locations.put(name, count);
        	}else if(term.getNatrue().natureStr.equalsIgnoreCase("nr")){//人名名词
        		if(people == null){
        			people = new HashMap<String, Integer>();
        		}
        		Integer count = people.get(name);
        		if(count == null){
        			count = 1;
        		}
        		count = count+1;
        		people.put(name, count);
        	}
        }
        entityStore.put(Location, locations);
        entityStore.put(People, people);
	}
}
