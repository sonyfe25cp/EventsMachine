package gossip.crawl;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import edu.bit.dlde.model.DLDEWebPage;
import edu.bit.dlde.utils.DLDELogger;

public class Crawler implements Runnable {
	private DLDELogger logger = new DLDELogger();
	private String url;
	private DLDEWebPage webPage;
	private CrawlerQueue queue;
	private HttpClient client;
	
	public Crawler(HttpClient client) {
		this.client=client;
		webPage=new DLDEWebPage();
	}
	
	public Crawler(HttpClient client,String url,CrawlerQueue queue) {
		this.url = url;
		this.queue=queue;
		this.client=client;
		webPage=new DLDEWebPage();
	}
	
	public Crawler(HttpClient client,CrawlerQueue queue) {
		this.queue=queue;
		this.client=client;
		webPage=new DLDEWebPage();
	}

	public static void main(String[] args) {
		CrawlerQueue queue = new CrawlerQueue();
		HttpClient client = new DefaultHttpClient();
		Crawler crawl = new Crawler(client,"http://www.chinanews.com/scroll-news/gn/2012/0710/news.shtml",queue);
		crawl.run();
		client.getConnectionManager().shutdown();
		
	}

	public void run() {
		try {
			if(queue!=null){
				url=queue.get_url_crawl();
			}
			if(url==null){
				logger.error("please input the url to crawl");
				return;
			}
			HttpGet get = new HttpGet(url);
			logger.info("crawl url:" + get.getURI());
			HttpResponse response = client.execute(get);
			HttpEntity entity = response.getEntity();
			int code = response.getStatusLine().getStatusCode();
			switch (code) {
			case 200:
//				String encoding = "GB2312";
//				String html = EntityUtils.toString(entity, encoding);
				String html = EntityUtils.toString(entity);
				webPage.setBody(html);
				webPage.setUrl(url);
				if(queue!=null){
					queue.savePage(webPage);
					logger.info("save over");
				}else{
					logger.info(webPage.toString());
				}
				break;
			case 404:
				logger.error("url 404");
				break;
			case 500:
				logger.error("url 500");
				break;
			default:
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public CrawlerQueue getQueue() {
		return queue;
	}

	public void setQueue(CrawlerQueue queue) {
		this.queue = queue;
	}

	public HttpClient getClient() {
		return client;
	}

	public void setClient(HttpClient client) {
		this.client = client;
	}
}
