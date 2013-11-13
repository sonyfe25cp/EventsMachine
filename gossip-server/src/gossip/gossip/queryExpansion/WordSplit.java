package gossip.gossip.queryExpansion;

import gossip.gossip.utils.DatabaseUtils;
import gossip.model.News;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.ansj.domain.Term;
import org.ansj.recognition.NatureRecognition;
import org.ansj.splitWord.analysis.ToAnalysis;

public class WordSplit {
	
	/**
	 * 从数据库中读取所有的新闻文档，仅包含id，title和body
	 * @return
	 */
	public List<News> getAllNews(){
		DataSource dataSource = DatabaseUtils.getInstance();
		Connection conn = null;
		Statement state = null;
		ResultSet rs = null;
		List<News> newsList = new ArrayList<News>();
		String sql = "select id, title, body from news";
		try {
			conn = dataSource.getConnection();
			state = conn.createStatement();
			rs = state.executeQuery(sql);
			while(rs.next()){
				int id = rs.getInt("id");
				String title = rs.getString("title");
				String body = rs.getString("body");
				News news = new News();
				news.setId(id);
				news.setTitle(title);
				news.setBody(body);
				newsList.add(news);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(newsList.size());
		return newsList;
	}
	
	/**
	 * 对title和body分别进行分词，并统计title和body中的单词及其出现的频率
	 * @param args
	 */
	public void computeWordsFre(){
		
	}
	
	/**
	 * 得到所有分词的词性
	 * @param args
	 */
	public void getCiXing(){
		Map<String, String> words = new HashMap<String, String>();
		List<News> newsList = getAllNews();
		int count = 0;
		for(News news : newsList){
			if(count%1000==0){
				System.out.println("document--------------" + count);
			}
			String title = news.getTitle();
			List<Term> titleTerms = ToAnalysis.parse(title);
			new NatureRecognition(titleTerms).recognition() ;
			for(Term term : titleTerms){
				if(term.getName().trim()=="")
					continue;
				if(!words.containsKey(term.getName().trim())){
					words.put(term.getName().trim(), term.getNatrue().natureStr);
				}
			}
			String body = news.getBody();
			List<Term> bodyTerms = ToAnalysis.parse(body);
			new NatureRecognition(bodyTerms).recognition() ;
			for(Term term : bodyTerms){
				if(term.getName().trim()=="")
					continue;
				if(!words.containsKey(term.getName().trim())){
					words.put(term.getName().trim(), term.getNatrue().natureStr);
				}
			}
			count++;
		}
		//将词性写入文件
		File file = new File("library/words.dic");
		BufferedWriter br = null;
		try {
			br = new BufferedWriter(new FileWriter(file));
			String str = "";
			Set<Map.Entry<String, String>> mapSet = words.entrySet();
			Iterator<Map.Entry<String, String>> it = mapSet.iterator();
			while(it.hasNext()){
				Map.Entry<String, String> entry = it.next();
				str = entry.getKey() + "     " + entry.getValue();
				br.write(str);
				br.newLine();
			}
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] args){
		WordSplit ws = new WordSplit();
//		List<News> newsList = ws.getAllNews();
//		System.out.println(newsList.size());
		ws.getCiXing();
	}

}
