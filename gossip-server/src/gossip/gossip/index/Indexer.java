package gossip.gossip.index;

import gossip.gossip.dao.NewsDAO;
import gossip.gossip.utils.DatabaseUtils;
import gossip.model.News;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.sql.DataSource;

import org.ansj.lucene3.AnsjAnalysis;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.MMapDirectory;
import org.apache.lucene.util.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 临时建索引方案
 * 
 * @author ChenJie
 * 
 */
public class Indexer {
	Logger logger = LoggerFactory.getLogger(Indexer.class);

	IndexWriter iw = null;

	public static void main(String[] args) {
		Indexer index = new Indexer();
		try {
			index.run();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	String indexPath = "/data/index";

	public void run() throws IOException {
		Analyzer analyzer = new AnsjAnalysis();
		Directory dir = new MMapDirectory(new File(indexPath));
		IndexWriterConfig conf = new IndexWriterConfig(Version.LUCENE_35,
				analyzer);
		iw = new IndexWriter(dir, conf);

		int cpu = Runtime.getRuntime().availableProcessors();
		service = new ThreadPoolExecutor(cpu - 1, cpu - 1, 1, TimeUnit.DAYS,
				new ArrayBlockingQueue<Runnable>(6000),
				new ThreadPoolExecutor.CallerRunsPolicy());

		new Worker().run();

	}

	ThreadPoolExecutor service;

	class Worker {
		NewsDAO nd = null;

		public Worker() {
			DataSource source = DatabaseUtils.getInstance();
			nd =  new NewsDAO();
			nd.setDataSource(source);
		}

		public void run() {
			List<News> newsList = nd.getAllNewsFromDB();
			int i = 0;
			for (News news : newsList) {
				service.submit(new Farmer(news));
				i++;
				if(i%500 == 0){
					logger.info("working ~~" + i);
				}
			}
			service.shutdown();
			try {
				iw.commit();
				iw.forceMerge(1);
				iw.close();
			} catch (CorruptIndexException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}

	}

	class Farmer implements Runnable {

		News news = null;

		public Farmer(News news) {
			this.news = news;
		}

		public void run() {
			Document doc = news.toDocument();
			try {
				iw.addDocument(doc);
			} catch (CorruptIndexException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	// void index() {
	// try {
	// DataSource source = DatabaseUtils.getInstance();
	// NewsDAO newsDao = new NewsDAO();
	// newsDao.setDataSource(source);
	// List<News> list = newsDao.getFreshNews();
	// int count = 0;
	// for (News news : list) {
	// iw.addDocument(news.toDocument());
	// count++;
	// if (count % 100 == 0)
	// iw.commit();
	// }
	// iw.commit();
	// iw.close();
	// newsDao.batchUpdateNews(list, News.INDEX);
	// } catch (Exception e) {
	// e.printStackTrace();
	// } finally {
	// if (iw != null) {
	// try {
	// iw.close();
	// } catch (CorruptIndexException e) {
	// e.printStackTrace();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }
	// }
	//
	// }

}
