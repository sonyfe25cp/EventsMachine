package gossip.crawl;

import java.util.List;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

import gossip.urlTemp.ChinaNews;

public class SingleThreadCrawler {

	/**
	 * @param args
	 * Jul 11, 2012
	 */
	public static void main(String[] args) {
		ChinaNews cn=new ChinaNews();
		List<String> urls=cn.baseUrl();
		HttpClient client=new DefaultHttpClient();
		Crawler spider=new Crawler(client);
		
//		for(String url :urls){
			String url="http://www.chinanews.com/scroll-news/gn/2012/0710/news.shtml";
			spider.setUrl(url);
			spider.run();
//		}
	}

}
