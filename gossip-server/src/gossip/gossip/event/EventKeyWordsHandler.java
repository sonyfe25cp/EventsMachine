package gossip.gossip.event;

import gossip.model.Event;
import gossip.model.News;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.ansj.app.keyword.KeyWordComputer;
import org.ansj.app.keyword.Keyword;
/**
 * 关键词策略
 * step1. 找出所有新闻的共有词
 * step2. 找出共有词中，idf最大的名词，或者是不在已知词表中的名词
 * step3. 取top10
 * @author ChenJie
 *
 * Nov 13, 2013
 */
public class EventKeyWordsHandler implements Handler{

	@Override
	public void handle(Event event) {
		List<News> newsList = event.getNewsList();
		KeyWordComputer kwc = new KeyWordComputer(5);
		HashSet<String> set = new HashSet<String>();
		for(News news : newsList){
			String title = news.getTitle();
			String body = news.getBody();
			Collection<Keyword> result = kwc.computeArticleTfidf(title, body);
		    for(Keyword kw : result){
		    	System.out.println(kw.toString() + "" + kw.getFreq());
		    	set.add(kw.toString());
		    }
		}
		StringBuilder sb = new StringBuilder();
		for(String tmp : set){
			sb.append(tmp+";");
		}
		event.setKeyWords(sb.toString());
	}

}
