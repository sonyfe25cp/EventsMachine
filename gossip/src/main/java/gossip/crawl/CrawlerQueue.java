package gossip.crawl;

import java.util.Collection;
import java.util.concurrent.ConcurrentLinkedQueue;

import edu.bit.dlde.model.DLDEWebPage;

public class CrawlerQueue {

	ConcurrentLinkedQueue<String> urlQueue;// url wait to crawl
	ConcurrentLinkedQueue<String> url_over_Queue;// url crawl over
	ConcurrentLinkedQueue<String> url_error_Queue;// url error
	
	ConcurrentLinkedQueue<DLDEWebPage> pages;//html

	public CrawlerQueue() {
		init();
	}
	
	public void init(){
		urlQueue=new ConcurrentLinkedQueue<String>();
		url_over_Queue=new ConcurrentLinkedQueue<String>();
		url_error_Queue=new ConcurrentLinkedQueue<String>();
		pages=new ConcurrentLinkedQueue<DLDEWebPage>();
	}

	public void savePage(DLDEWebPage html) {
		pages.add(html);
	}
	
	public void add_url_crawl(String url) {
		urlQueue.add(url);
	}
	public void add_urls_crawl(Collection urls) {
		urlQueue.addAll(urls);
	}
	public String get_url_crawl() {
		return urlQueue.poll();
	}

	public void add_url_over(String url) {
		url_over_Queue.add(url);
	}

	public void add_url_error(String url) {
		url_error_Queue.add(url);
	}

	public void close(){
		
	}
	
}
