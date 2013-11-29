package gossip.gossip.event;

import gossip.model.Event;
import gossip.model.News;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.ansj.domain.Term;
import org.ansj.recognition.NatureRecognition;
import org.ansj.splitWord.analysis.ToAnalysis;
/**
 * 事件地点解析
 * step1. 找所有新闻的地点词
 * step2. 判断地点词的可能性,数量最多的就是
 * @author ChenJie
 *
 * Nov 13, 2013
 */
public class EventLocationHandler implements Handler{


	@Override
	public void handle(Event event) {
		List<News> newsList = event.getNewsList();
		HashMap<String, Integer> titleLocation = new HashMap<String, Integer>();
		HashMap<String, Integer> bodyLocation = new HashMap<String, Integer>();
		for(News news : newsList){
			String title = news.getTitle();
			String body = news.getBody();
			locationFind(title, titleLocation);
			locationFind(body, bodyLocation);
		}
		String location = "";
		if(titleLocation.size() != 0){
			int max = 0;
			for(Entry<String, Integer> entry : titleLocation.entrySet()){
				int tmpCount = entry.getValue();
				if(tmpCount > max){
					location = entry.getKey();
				}
			}
		}else{
			if(bodyLocation.size() != 0){
				int max = 0;
				for(Entry<String, Integer> entry : bodyLocation.entrySet()){
					int tmpCount = entry.getValue();
					if(tmpCount > max){
						location = entry.getKey();
					}
				}
			}
		}
		event.setStartedLocation(location);
	}

	private HashMap<String, Integer> locationFind(String content, HashMap<String, Integer> locations){
		List<Term> terms = ToAnalysis.parse(content);
		new NatureRecognition(terms).recognition() ;
        System.out.println(terms);
        for(Term term : terms){
        	if(term.getNatrue().natureStr.equalsIgnoreCase("ns")){
        		if(locations == null){
        			
        		}
        		String name = term.getName();
        		Integer count = locations.get(name);
        		if(count == null){
        			count = 1;
        		}
        		count = count+1;
        		locations.put(name, count);
        	}
        }
        return locations;
	}
}
