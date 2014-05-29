package gossip.server.test;

import gossip.gossip.event.EventSummaryHandler;
import gossip.gossip.utils.Utils;
import gossip.model.News;

import java.util.List;
import java.util.Map.Entry;

public class TestTmpSummary {
	public static void main(String[] args) {
		String file = "news.txt";
		String content = Utils.getResouce(file);
		
		EventSummaryHandler esh = new EventSummaryHandler();
		News news = new News();
		news.setBody(content);
		List<Entry<String, Double>> findImportantSentences = esh.findImportantSentences(news);
		for(Entry<String, Double> entry : findImportantSentences){
			System.out.println(entry.getKey() +" -- "+ entry.getValue());
		}
	}
}
