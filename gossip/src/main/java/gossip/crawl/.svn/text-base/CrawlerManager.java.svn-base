package gossip.crawl;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

import gossip.urlTemp.ChinaNews;

public class CrawlerManager {
	CrawlerQueue quene;//url wait to crawl
	private ExecutorService  executorService;  
	private HttpClient client;
	private boolean work_flag=true;
	
	private BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>();
	public CrawlerManager() {
		
	}

	public void init() {
		quene = new CrawlerQueue();
		executorService=Executors.newFixedThreadPool(16);
		client=new DefaultHttpClient();
	}
	
	public void initSeed(){
		ChinaNews cn=new ChinaNews();
		List<String> urls=cn.baseUrl();
		quene.add_urls_crawl(urls);
	}
	
	public void work() {
		
		
		while(work_flag){
			work_flag=getFlag();
			executorService.submit(new Crawler(client,quene));
		}
	}
	public boolean getFlag(){
		return work_flag;
	}
	public void close(){
		work_flag=false;
		executorService.shutdown();
		quene.close();
		client.getConnectionManager().shutdown();
	}
	
	public static void main(String[] args){
		CrawlerManager cm=new CrawlerManager();
		cm.init();
		cm.initSeed();
		cm.work();
		try {
			Thread.sleep(1000*100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		cm.close();
		System.out.println("over");
	}

}
