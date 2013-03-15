package gossip.parse;

import java.io.File;
import java.io.StringReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.lucene.document.DateTools;

import bit.crawl.store.PageStoreReader;
import bit.crawl.store.StoredPage;
import edu.bit.dlde.extractor.CombinedPreciseExtractor;
import edu.bit.dlde.model.DLDEWebPage;
import edu.bit.dlde.utils.DLDEConfiguration;
import edu.bit.dlde.utils.DLDELogger;
import gossip.dao.NewsDAO;
import gossip.model.News;
import gossip.utils.DateException;
import gossip.utils.TypeFactory;

/**
 * 配置文件在 /data/gossip/site/qq/*.xml 定时将抓取的qq新闻移到指定目录
 * /data/gossip/site/qq/*.pages 解析完毕移动到指定目录 /data/gossip/bak/site/qq/*.pages
 * 
 * @author ChenJie
 * 
 */
public class ParseNewsFromWKS {
	private static DLDELogger logger=new DLDELogger();
	/**
	 * 
	 * Jul 12, 2012
	 */
	public void parse() {

		String folderPath = DLDEConfiguration.getInstance("gossip.properties").getValue("NewsPath");
		String bakPath = DLDEConfiguration.getInstance("gossip.properties").getValue("BackupPath");
		File folder = new File(folderPath);
		DLDEWebPage webpage = null;
		for (File file : folder.listFiles()) {
			List<DLDEWebPage> pages = new ArrayList<DLDEWebPage>();
			String filename = file.getName();
			System.out.println(filename);
			PageStoreReader psr = new PageStoreReader(file);
			ArrayList<StoredPage> array = psr.loadAll();
			CombinedPreciseExtractor cpe = new CombinedPreciseExtractor();
			for (StoredPage page : array) {
				int pagesDate = getPagesDate(filename);
				if(pagesDate < 20121031){//20121031新闻改版
					cpe.configWith(new File("src/main/resources/gn-news-qq.xml"));// 只有qq新闻
				}else{
					cpe.configWith(new File("src/main/resources/gn-news-qq-new.xml"));// 只有qq新闻
				}
				String rawContent = page.getContent();
				cpe.setResource(new StringReader(rawContent), "gn-news-qq");
				Map<String, String> map = cpe.extract();
				for (Entry<String, String> entry : map.entrySet()) {
					String key = entry.getKey();
					String value = entry.getValue();
					log("key:" + key + " -- value:" + value);
				}
				map.put("url", page.getHeader("URL"));
				webpage = transfromMap(map);
				if(webpage!=null){
					pages.add(webpage);
				}
			}
			/*
			 * 插入数据
			 */
			DBThreadNews dbtn = new DBThreadNews(pages);
			dbtn.run();
//			new Thread(dbtn).start();
			/*
			 * 移走数据
			 */
			if (!new File(bakPath).exists()) {
				new File(bakPath).mkdirs();
			}
			file.renameTo(new File(bakPath + filename));
		}
	}
	
	
	/**
	 * 根据.pages的文件名得到日期
	 * @return 20121101 格式整数
	 * Dec 6, 2012
	 */
	private static int getPagesDate(String filename){
		String year = filename.substring(10, 14);
		String month = filename.substring(15, 17);
		String day = filename.substring(18, 20);
		String date=year+month+day;
		return Integer.parseInt(date);
	}

	public static DLDEWebPage transfromMap(Map<String, String> map) {
		if (map == null) {
			return null;
		}
		DLDEWebPage webPage = new DLDEWebPage();
		String title = map.get("title");
		String content = map.get("content");
		String from_site = map.get("from_site");
		String author = "";
		String url = map.get("url");
		author = map.get("author");
		String crawl_at = parseDateFromUrl(url);
		
		webPage.setAuthor(author);
		webPage.setBody(content);
		webPage.setDate(modifyDate(crawl_at));
		webPage.setTitle(title);
		webPage.setUrl(url);
		webPage.setFrom_site(from_site);
		webPage.setCrawl_at(crawl_at);

		return webPage;
	}

	private static String parseDateFromUrl(String url){//20121025
		String date = url.substring(21,29);//"http://news.qq.com/a/20121025/000987.htm"
		return date;
	}
	private static String modifyDate(String date){//20121025 -> 2012-10-25
		String newDate = date.substring(0, 4)+"-"+date.substring(4, 6)+"-"+date.substring(6, 8);
		return newDate;
	}
	
	public static String parseDate(String createAt) {
		DateFormat df = new SimpleDateFormat("yyyy年MM月dd日hh:mm");
		String aim = "";
		try {
			Date date = df.parse(createAt);
			aim = DateTools.dateToString(date, DateTools.Resolution.MILLISECOND);
		} catch (Exception e) {
		}
		return aim;
	}

	public static int strintToInt(String createAt) {
		try {
			int year = Integer.parseInt(createAt.substring(0, 4));
			int month = Integer.parseInt(createAt.substring(5, 7));
			int day = Integer.parseInt(createAt.substring(8, 10));
			String date = ""+year+(month<10?("0"+month):month)+(day<10?("0"+day):day);
			return Integer.parseInt(date);
		} catch (Exception e) {
			throw new DateException("新闻时间解析错误");
		}
	}

	public static String extractTime(String str) {
		str = str.substring(str.indexOf("_") + 1, str.lastIndexOf("."));
		str = str.substring(0, 10);
		return str;
	}

	public static void log(String str) {
		// System.out.println(str);
	}

}
class DBThreadNews implements Runnable{
	private List<News> newsList;
	public DBThreadNews(List<DLDEWebPage> webpages){
		
		newsList = new ArrayList<News>(webpages.size());
		News news = null;
		for(DLDEWebPage page : webpages){
			news = TypeFactory.transfromWebPage(page);
			newsList.add(news);
		}
	}

	@Override
	public void run() {
		NewsDAO dao = new NewsDAO();
		dao.insertNews(newsList);
		dao.close();
	}
	
}