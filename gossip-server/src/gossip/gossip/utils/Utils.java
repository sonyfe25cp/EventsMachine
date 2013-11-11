package gossip.gossip.utils;

import org.apache.lucene.document.Document;

import edu.bit.dlde.model.DLDEWebPage;

public class Utils {

	public static DLDEWebPage transfrom(Document doc){
		DLDEWebPage webpage=new DLDEWebPage();
		String title=doc.get("title");
		String body=doc.get("body");
		String url=doc.get("url");
		String creat_at=doc.get("date");
		webpage.setTitle(title);
		webpage.setBody(body);
		webpage.setUrl(url);
		webpage.setDate(creat_at);
		webpage.setCrawl_at(doc.get("crawl_at"));
		return webpage;
	}
	
	
}
