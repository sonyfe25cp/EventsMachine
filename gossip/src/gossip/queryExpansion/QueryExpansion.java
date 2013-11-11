package gossip.queryExpansion;

import gossip.dao.EventDAO;
import gossip.dao.NewsDAO;
import gossip.dao.QueryExpansionDAO;
import gossip.model.Event;
import gossip.model.News;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.TermFreqVector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.store.RAMDirectory;
import org.wltea.analyzer.lucene.IKAnalyzer;

/**
 * @author syl
 * 
 */


/**
 * 计算查询扩展的主类，目前只是简单地实现了利用event和news的标题实现查询扩展，即只要某个词存在event或news的title中，就认为该词和该event或news是相关的
 * 此程序是在离线状态下进行的，因此需要每隔固定的时间计算一次，以使查询扩展保持最新
 *
 */

public class QueryExpansion {
	
	private ArrayList<Expansion> expansions = new ArrayList<Expansion>();
	
	

	public ArrayList<Expansion> getExpansions() {
		return expansions;
	}


	public void setExpansions(ArrayList<Expansion> expansions) {
		this.expansions = expansions;
	}


	/**
	 * @param ArrayList<Event> events
	 * 通过event的title进行扩展，首先对每个event的title进行分词，认为存在同一个title中的词是相关的，并且该event和event下的news与该词也是相关的
	 *
	 */
	public void expansionByEvents(ArrayList<Event> events)
			throws CorruptIndexException, LockObtainFailedException,
			IOException {
		if (events.isEmpty()||events==null)
			return;
		else {
			for (int i = 0; i < events.size(); i++) {
				// Term term = new Term();
				String eventTitle = events.get(i).getTitle();
				int id = events.get(i).getId();

				/* 对事件的title进行分词 */
				ArrayList<String> eventTitleTerms = segmentation(eventTitle);

				for (String s : eventTitleTerms) {
					List<Integer> newsId = new ArrayList<Integer>();
					newsId = events.get(i).getPages();
					Expansion expansion = new Expansion();
					expansion.setTerm(s);
					expansion.addEventId(id);
					expansion.addNewsId(newsId);
					expansion.addExpansionTerms(eventTitleTerms);
					if (expansions.isEmpty()) {
						expansions.add(expansion);
					}// if
					else {
						boolean tag = false;// 设置标志，确定原expansions集合中是否已经有该关键词
						for (Expansion expan : expansions) {
							if (expan.getTerm().equals(s)) {/* 如果该expansion存在原集合中，则进行更新 */
								expan.addEventId(id);
								expan.addNewsId(newsId);
								expan.addExpansionTerms(eventTitleTerms);
								tag = true;
							}// if
						}// for
						if (tag == false) {/* 如果原term集中没有，则新建一个term，并加入到集合中 */
							expansions.add(expansion);
						}
					}// else
				}// for
			}// for
		}// else
			// System.out.println(terms.size());
	}// expansionByEvents()

	
	/**
	 * @param ArrayList<News> news
	 * 通过news的title进行扩展，首先对每个news的title进行分词，认为存在同一个title中的词是相关的，并且该news和对应的event与该词也是相关的
	 *
	 */
	public void expansionByNews(ArrayList<News> news) throws CorruptIndexException, LockObtainFailedException, IOException {
		if(news.isEmpty()||news==null)
			return ;
		else{
			for(News inews:news){
				String newsTitle=inews.getTitle();
				int newsId=inews.getId();
				//int eventId=getEventByNewsId(newsId);/*通过新闻id查找事件id，因为还不知道新闻的存储结构，放在后面实现*/
				ArrayList<String> newsTitleTerms = segmentation(newsTitle);//分词
				for(String s:newsTitleTerms){
					Expansion expansion = new Expansion();
					expansion.setTerm(s);
					//expansion.addEventId(eventId);
					expansion.addNewsId(newsId);
					expansion.addExpansionTerms(newsTitleTerms);
					if (expansions.isEmpty()) {
						expansions.add(expansion);
					}// if
					else{
						boolean tag = false;// 设置标志，确定原Terms集合中是否已经有该关键词
						for (Expansion expan : expansions) {
							if (expan.getTerm().equals(s)) {/* 如果该term存在原集合中，则进行更新 */
								//t.addEventId(eventId);
								expan.addNewsId(newsId);
								expan.addExpansionTerms(newsTitleTerms);
								tag = true;
							}// if
						}// for
						if (tag == false) {/* 如果原term集中没有，则新建一个term，并加入到集合中 */
							expansions.add(expansion);
						}
					}//else
				}//for
			}//for
		}//else
	}

	/* 以下两个函数都是判断字符串类型的函数，在扩展词的过滤中用到 */

	private static final boolean isChinese(char c) {
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
		if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
				|| ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
				|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
				|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
			return true;
		}
		return false;
	}

	/* 准确判断字符串是否为中文 */
	public static final boolean isChinese(String strName) {
		char[] ch = strName.toCharArray();
		for (int i = 0; i < ch.length; i++) {
			char c = ch[i];
			if (!isChinese(c)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * @param s
	 *            ，s是要进行分词的字符串
	 * @return 
	 *         ArrayList<String>，返回分词后的结果，该方法使用lucene进行索引分词，但是并没有在磁盘上建立索引，而是将索引建在内存中
	 *         ，增加了读取速度
	 */
	public ArrayList<String> segmentation(String s)
			throws CorruptIndexException, LockObtainFailedException,
			IOException {
		Analyzer analyzer = new IKAnalyzer(true);
		Directory directory = null;
		IndexWriter iwriter = null;
		directory = new RAMDirectory();
		iwriter = new IndexWriter(directory, analyzer, true,
				IndexWriter.MaxFieldLength.LIMITED);
		Document doc = new Document();
		doc.add(new Field("title", s, Store.YES, Index.ANALYZED,
				Field.TermVector.WITH_POSITIONS_OFFSETS));
		iwriter.addDocument(doc);
		iwriter.close();
		IndexReader reader = null;
		TermFreqVector tfv = null;
		reader = IndexReader.open(directory);
		tfv = reader.getTermFreqVector(0, "title");
		String terms[] = tfv.getTerms();
		// int termfre[] = tfv.getTermFrequencies();
		ArrayList<String> words = new ArrayList<String>();
		/* 对分词进行简单过滤，这里只处理中文词语，并且过滤掉长度小于2的词组（后期还可以添加过滤规则，如使用停用词表等） */
		for (String term : terms) {
			if (isChinese(term) && term.length() > 1)
				words.add(term);
		}
		return words;

	}



	//简单输出扩展信息，用于测试
	public void display() {
		for (Expansion expansion : expansions) {
			System.out.println("term:" + expansion.getTerm());
			System.out.println("expansionTerms:");
			for (String s : expansion.getExpansionTerms())
				System.out.println(s + "  ");
			System.out.println("eventsId:");
			for (int id : expansion.getEventId())
				System.out.println(id + "  ");
			System.out.println("newsId:");
			for (int nid : expansion.getNewsId())
				System.out.println(nid + "  ");
			System.out.println("-----------------------------------");
		}
	}

	public static void main(String args[]) throws CorruptIndexException,
			LockObtainFailedException, IOException {
//		ArrayList<Event> events = new ArrayList<Event>();
//		Event event = new Event();
//		event.setId(132);
//		Map<String, Double> keyWords = new HashMap<String, Double>();
//		keyWords.put("十八大", 1.0);
//		event.setKeyWords(keyWords);
//		event.setTitle("全国各地喜迎十八大");
//		List<Integer> pages = new ArrayList<Integer>();
//		pages.add(1);
//		pages.add(2);
//		pages.add(3);
//		event.setPages(pages);
//		events.add(event);
//		QueryExpansion qe = new QueryExpansion();
//		qe.expansionByEvents(events);
//		qe.display();
		QueryExpansion qe=new QueryExpansion();
		NewsDAO newsDao=new NewsDAO();
		ArrayList<News> newsList=newsDao.getAllNewsFromDB();
		qe.expansionByNews(newsList);
		
		EventDAO eventDao=new EventDAO();
		ArrayList<Event>eventList=eventDao.getAllEvent();
		qe.expansionByEvents(eventList);
		qe.display();
		QueryExpansionDAO queryExpansion=new QueryExpansionDAO();
		queryExpansion.insertExpansion(qe.getExpansions());
		
		
	}

}
