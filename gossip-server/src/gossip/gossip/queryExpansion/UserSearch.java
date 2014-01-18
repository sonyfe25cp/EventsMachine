package gossip.gossip.queryExpansion;

import gossip.gossip.utils.DatabaseUtils;
import gossip.model.News;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.sql.DataSource;

import org.ansj.domain.Term;
import org.ansj.recognition.NatureRecognition;
import org.ansj.splitWord.analysis.ToAnalysis;

/**
 * 模拟用户查询的过程，得到用户查询词后，先进行分词，然后进行第一次查询，根据BM25得到前20篇文档，然后从这20篇文档中查找扩展词
 * @author yulong
 *
 */
public class UserSearch {
	private DataSource dataSource = DatabaseUtils.getInstance();
	
	
	/**
	 * 检索的过程，先分词，然后使用BM25评分选择前20篇文档，然后进行扩展，再进行第二次检索
	 * @param query
	 * @return
	 */
	public List<News> search(String query){
		System.out.println("开始查询： " + query);
		//将用户查询分词
		List<String> queryWords = new ArrayList<String>();
		List<Term> terms = ToAnalysis.parse(query);
		new NatureRecognition(terms).recognition();
		for (Term term : terms) {
			String keyword = term.getName();
			String property = term.getNatrue().natureStr;
			if (!WordSplit.wordFilter(keyword))
				continue;
			if((!(queryWords.contains(keyword)))&&property.startsWith("n")){
				queryWords.add(keyword);
			}
			
		}// for queryTerms
		System.out.println("分词完毕");
		List<DocTerm> queryTerms = getTermsByQuery(queryWords);
		for(DocTerm term : queryTerms){
			System.out.println("查询词： " + term.getWords() + "   docid:" + term.getDocIdString());
		}
		//将包含该词的文档选出，在这些文档上计算BM25并排序
		List<Document> documents = getDocuments(queryTerms);
//		System.out.println("包含查询词的文档集为： " + documents.size());
//		for(Document doc : documents){
//			System.out.println(doc.getId());
//		}
		//第一次仅使用query进行查询，返回相关文档列表
		//List<Integer> relatedDocs = new ArrayList<Integer>();
		List<Document> relatedDocs = new ArrayList<Document>();
		//计算文档的BM25值并排序，返回得分最高的10篇文档
		relatedDocs = getRelatedDocs(queryTerms, documents);
		if(relatedDocs.size()>20){
			relatedDocs = relatedDocs.subList(0, 20);
		}
//		System.out.println("第一次检索返回的相关文档集为： ");
//		for(Document doc : relatedDocs){
//			System.out.println(doc.getId() + "-----bm25: " + doc.getBM25());
//		}
		QueryExpansion qe = new QueryExpansion();
		List<DocTerm> expansionTerms = qe.getExpansionTerms(queryTerms, relatedDocs);
		List<Integer> relatedDocsId = searchSecond(queryTerms, expansionTerms);
		List<News> news = getNewsFromDB(relatedDocsId);
		return news;
	}
	
	
	/**
	 * 根据BM25评分得到相关文档
	 * @param queryTerms
	 * @param documents
	 * @return
	 */
	public List<Document> getRelatedDocs(List<DocTerm> queryTerms, List<Document> documents){
		System.out.println("开始计算文档的bm25值");
		double avgdl = 0.0;
		for(Document doc : documents){
			avgdl += doc.getBodyWordsCount();
		}
		avgdl = (double)avgdl/documents.size();
		
		for(Document doc : documents){
			double BM25 = 0.0;
			for(DocTerm queryTerm : queryTerms){
				String word = queryTerm.getWords();
				//如果文档标题中含有该词，则权重提高
				if(doc.getTitleWordsMap().containsKey(word)){
					BM25 = BM25 + (double)1/queryTerms.size();
				}
				
				if(doc.getBodyWordsMap().containsKey(word)){
					int tf = doc.getBodyWordsMap().get(word);
					int dl = doc.getBodyWordsCount();
					BM25 = BM25 + queryTerm.getWeight()*computeBM25(tf, dl, avgdl);
				}
			}
			doc.setBM25(BM25);
		}
		Collections.sort(documents, new Comparator<Document>(){
			public int compare(Document doc1, Document doc2){
				if(doc1.getBM25() < doc2.getBM25())
					return 1;
				else if(doc1.getBM25() > doc2.getBM25())
					return -1;
				else
					return 0;
			}
		});
//		//取bm25最大的10篇文档返回
//		if(documents.size()>10){
//			documents = documents.subList(0, 9);
//		}
		System.out.println("bm25计算完毕，并进行排序");
		return documents;
		
	}
	
	/**
	 * BM25计算公式
	 * @param tf
	 * @param dl
	 * @param avgdl
	 * @return
	 */
	public double computeBM25(int tf, int dl, double avgdl){
		int k =2;//参数k
		double b = 0.75;//参数b
		double divideA = (double)tf*(k+1);//分子
		double divideB = b*((double)dl/avgdl);//分母的一部分
		double bm25 = divideA/(tf+k*(1-b+b*divideB));
		return bm25;
	}
	
	
	/**
	 * 将包含查询词的文档从数据库中取出，计算BM25时仅考虑这些文档，不必计算整个文档集
	 * @param queryTerms
	 * @return
	 */
	public List<Document> getDocuments(List<DocTerm> queryTerms){
		System.out.println("开始读取包含查询词的文档");
		if(queryTerms==null){
			return null;
		}
		List<Document> documents = new ArrayList<Document>();
		List<Document> docs = new ArrayList<Document>();
		Set<Integer> docIds = new HashSet<Integer>();
		for(DocTerm term : queryTerms){
			String[] ids = term.getDocIdString().split(";");
			for(String id : ids){
				docIds.add(Integer.parseInt(id));
			}
		}
		if(docIds==null||docIds.size()==0)
			return null;
			
		Connection conn = null;
		Statement stat = null;
		ResultSet rs = null;
		String docSql = "select * from document where id in (";
		Iterator<Integer> it = docIds.iterator();
		while(it.hasNext()){
			docSql = docSql + "'" + it.next() + "',";
		}
		docSql = docSql.substring(0, docSql.length()-1);
		docSql = docSql + ")";
		//System.out.println(docSql);
		
		try {
			conn = dataSource.getConnection();
			stat = conn.createStatement();
			rs = stat.executeQuery(docSql);
			while(rs.next()){
				Document doc = new Document();
				doc.setId(rs.getInt("id"));
				doc.setTotalWordsCount(rs.getInt("totalwordscount"));
				doc.setTitleWordsCount(rs.getInt("titlewordscount"));
				if(rs.getInt("bodywordscount")>300){
					continue;
				}
				doc.setBodyWordsCount(rs.getInt("bodywordscount"));
				doc.setBodyWords(rs.getString("bodywords"));
				doc.setTitleWords(rs.getString("titlewords"));
				documents.add(doc);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			try {
				System.out.println(rs.getInt("bodywordscount"));
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		//进行一次简单筛选，只将包含所有查询词的文档选出
		for(Document doc : documents){
			boolean flag = true;
			for(DocTerm term : queryTerms){
				if(!doc.getBodyWordsMap().containsKey(term.getWords())){
					flag = false;
					break;
				}
			}
			if(flag==true){
				docs.add(doc);
			}
		}
		int i = 0;
		System.out.println(docs.size());
		if(docs.size()<10){
			if(i<documents.size()){
				docs.add(documents.get(i));
				i++;
			}
		}
		System.out.println("读取查询词文档完毕");
		for(Document doc : docs){
			System.out.println(doc.getId());
		}
		return docs;
	}
	
	/**
	 * 得到扩展词以后将包含扩展词的所有文档读出，以便对这些文档进行第二次检索，防止对所有文档检索浪费时间
	 * @param queryTerms
	 * @return
	 */
	public List<Document> getDocumentsByExpansion(List<DocTerm> queryTerms){
		System.out.println("开始读取包含查询词的文档");
		if(queryTerms==null){
			return null;
		}
		List<Document> documents = new ArrayList<Document>();
		List<Document> docs = new ArrayList<Document>();
		Set<Integer> docIds = new HashSet<Integer>();
		for(DocTerm term : queryTerms){
			String[] ids = term.getDocIdString().split(";");
			for(String id : ids){
				docIds.add(Integer.parseInt(id));
			}
		}
		if(docIds==null||docIds.size()==0)
			return null;
			
		Connection conn = null;
		Statement stat = null;
		ResultSet rs = null;
		String docSql = "select * from document where id in (";
		Iterator<Integer> it = docIds.iterator();
		while(it.hasNext()){
			docSql = docSql + "'" + it.next() + "',";
		}
		docSql = docSql.substring(0, docSql.length()-1);
		docSql = docSql + ")";
		//System.out.println(docSql);
		
		try {
			conn = dataSource.getConnection();
			stat = conn.createStatement();
			rs = stat.executeQuery(docSql);
			while(rs.next()){
				Document doc = new Document();
				doc.setId(rs.getInt("id"));
				doc.setTotalWordsCount(rs.getInt("totalwordscount"));
				doc.setTitleWordsCount(rs.getInt("titlewordscount"));
				if(rs.getInt("bodywordscount")>300){
					continue;
				}
				doc.setBodyWordsCount(rs.getInt("bodywordscount"));
				doc.setBodyWords(rs.getString("bodywords"));
				doc.setTitleWords(rs.getString("titlewords"));
				documents.add(doc);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			try {
				System.out.println(rs.getInt("bodywordscount"));
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		}

		System.out.println("读取查询词文档完毕");
		return documents;
	}
	
	/**
	 * 根据查询词从数据库中取出每个词的详细信息
	 * @param queyWords
	 * @return
	 */
	public List<DocTerm> getTermsByQuery(List<String> queryWords){
		List<DocTerm> queryTerms = new ArrayList<DocTerm>();
		Connection conn = null;
		Statement stat = null;
		ResultSet rs = null;
		String TermSql = "select * from termenum where keywords in (";
		for(int i=0;i<queryWords.size();i++){
			if(i<queryWords.size()-1){
				TermSql =TermSql + "'" + queryWords.get(i) + "',";
			}
			else
				TermSql =TermSql + "'" + queryWords.get(i) + "')";
		}
		//System.out.println(TermSql);
		System.out.println("开始从数据库读取查询词信息");
		try {
			conn = dataSource.getConnection();
			stat = conn.createStatement();
			rs = stat.executeQuery(TermSql);
			while(rs.next()){
				DocTerm docTerm = new DocTerm();
				docTerm.setWords(rs.getString("keywords"));
				docTerm.setProperty(rs.getString("property"));
				docTerm.setDocIdString(rs.getString("docIds"));
				docTerm.setBodyCountString(rs.getString("bodycounts"));
				docTerm.setTitleCountString(rs.getString("titlecounts"));
				docTerm.getCountMap();
				docTerm.setWeight(1.0);
				queryTerms.add(docTerm);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("读取查询词信息完毕");
		return queryTerms;
		
	}
	
	/**
	 * 第二次检索的过程，检索条件包含初始查询词和扩展词
	 * @param queryTerm
	 * @param expansionTerms
	 * @return
	 */
	public List<Integer> searchSecond(List<DocTerm> queryTerm, List<DocTerm> expansionTerms){
		System.out.println("第二次查询开始");
		List<DocTerm> terms = new ArrayList<DocTerm>();
		terms.addAll(queryTerm);
		terms.addAll(expansionTerms);
		List<Document> documents = getDocumentsByExpansion(terms);
		List<Document> relatedDocument = getRelatedDocs(terms, documents);
		List<Integer> relatedDocs = new ArrayList<Integer>();
		int count = 0;
		for(Document doc : relatedDocument){
			relatedDocs.add(doc.getId());
			count++;
			if(count>=30){
				break;
			}
		}
		return relatedDocs;
	}

	/**
	 * 根据新闻的id读取新闻内容，每次可以读取一系列新闻
	 * @param newsId
	 * @return
	 */
	public List<News> getNewsFromDB(List<Integer> newsId){
		System.out.println("从数据库中读取新闻信息");
		List<News> newsList = new ArrayList<News>();
		Connection conn = null;
		Statement stat = null;
		ResultSet rs = null;
		String newsSql = "select * from news where id in (";
		for(int i=0;i<newsId.size();i++){
			if(i<newsId.size()-1){
				newsSql = newsSql + "'" + newsId.get(i) + "',";
			}
			else
				newsSql = newsSql + "'" + newsId.get(i) + "')";
		}
		//System.out.println(newsSql);
		try {
			conn = dataSource.getConnection();
			stat = conn.createStatement();
			rs = stat.executeQuery(newsSql);
			while(rs.next()){
				News news = new News();
				news.setId(rs.getInt("id"));
				news.setTitle(rs.getString("title"));
				news.setBody(rs.getString("body"));
				news.setUrl(rs.getString("url"));
				news.setAuthor(rs.getString("author"));
				news.setDescription(rs.getString("description"));
				newsList.add(news);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("新闻信息读取完毕");
		return newsList;
	}
	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	public static void main(String[] args){
		UserSearch us = new UserSearch();
		List<News> newsList = us.search("雅安地震是汶川地震余震吗");
		for(News news :newsList){
			System.out.println(news.getTitle());
		}
	}
	

}
