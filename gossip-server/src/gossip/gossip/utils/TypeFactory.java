package gossip.gossip.utils;

import edu.bit.dlde.model.DLDEWebPage;
import gossip.model.News;

public class TypeFactory {

	public static News transfromWebPage(DLDEWebPage webpage){
		if(webpage == null || webpage.getTitle() == null || webpage.getTitle().length()==0){
			return null;
		}
		News news = new News();
		news.setTitle(webpage.getTitle());
		news.setBody(webpage.getBody());
		news.setDescription("");
		news.setUrl(webpage.getUrl());
		news.setAuthor(webpage.getAuthor());
		
		return news;
	}

}
