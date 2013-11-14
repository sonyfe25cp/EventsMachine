package gossip.gossip.queryExpansion;

import gossip.gossip.utils.DatabaseUtils;
import gossip.gossip.utils.TokenizerUtils;
import gossip.model.Document;
import gossip.model.News;
import gossip.model.TermEnum;
import gossip.model.WordTag;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.ansj.domain.Term;
import org.ansj.recognition.NatureRecognition;
import org.ansj.splitWord.analysis.ToAnalysis;

public class WordSplit {

	private static HashSet<String> stopwords = TokenizerUtils.getStopWords();

	/**
	 * 从数据库中读取所有的新闻文档，仅包含id，title和body
	 * 
	 * @return
	 */
	public List<News> getAllNews() {
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
			while (rs.next()) {
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
	 * 
	 * @param args
	 */
	public void computeWordsFre() {

	}

	/**
	 * 得到所有分词的词性
	 * 
	 * @param args
	 */
	public void getCiXing() {
		Map<String, String> words = new HashMap<String, String>();
		List<News> newsList = getAllNews();
		int count = 0;
		for (News news : newsList) {
			if (count % 1000 == 0) {
				System.out.println("document--------------" + count);
			}
			String title = news.getTitle();
			List<Term> titleTerms = ToAnalysis.parse(title);
			new NatureRecognition(titleTerms).recognition();
			for (Term term : titleTerms) {
				if (term.getName().trim() == "")
					continue;
				if (!words.containsKey(term.getName().trim())) {
					words.put(term.getName().trim(), term.getNatrue().natureStr);
				}
			}
			String body = news.getBody();
			List<Term> bodyTerms = ToAnalysis.parse(body);
			new NatureRecognition(bodyTerms).recognition();
			for (Term term : bodyTerms) {
				if (term.getName().trim() == "")
					continue;
				if (!words.containsKey(term.getName().trim())) {
					words.put(term.getName().trim(), term.getNatrue().natureStr);
				}
			}
			count++;
		}
		// 将词性写入文件
		File file = new File("library/words.dic");
		BufferedWriter br = null;
		try {
			br = new BufferedWriter(new FileWriter(file));
			String str = "";
			Set<Map.Entry<String, String>> mapSet = words.entrySet();
			Iterator<Map.Entry<String, String>> it = mapSet.iterator();
			while (it.hasNext()) {
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

	/**
	 * 得到文档的所有分词，并按照倒序进行存储
	 */
	public void getAllWords() {
		List<News> newsList = getAllNews();
		List<Document> docs = new ArrayList<Document>();
		Map<String, TermEnum> allWords = new HashMap<String, TermEnum>();
		int count = 0;
		for (News news : newsList) {
			if(count%1000 == 0){
				System.out.println(count);
			}
//			if (count > 10000)
//				break;
			Map<String, String> wordsProperty = new HashMap<String, String>();
			Map<String, Integer> titleWordsMap = new HashMap<String, Integer>();
			Map<String, Integer> bodyWordsMap = new HashMap<String, Integer>();
			String title = news.getTitle();
			int titleWordsCount = 0;
			// 对title进行分词
			List<Term> titleTerms = ToAnalysis.parse(title);
			new NatureRecognition(titleTerms).recognition();
			for (Term term : titleTerms) {
				String keyword = term.getName();
				String property = term.getNatrue().natureStr;
				if (!wordFilter(keyword))
					continue;
				titleWordsCount++;
				wordsProperty.put(keyword, property);
				if (titleWordsMap.containsKey(keyword)) {
					titleWordsMap.put(keyword, titleWordsMap.get(keyword) + 1);
				} else
					titleWordsMap.put(keyword, 1);
			}// for title
				// 对body进行分词
			String body = news.getBody();
			int bodyWordsCount = 0;
			List<Term> bodyTerms = ToAnalysis.parse(body);
			new NatureRecognition(bodyTerms).recognition();
			for (Term term : bodyTerms) {
				String keyword = term.getName();
				String property = term.getNatrue().natureStr;
				if (!wordFilter(keyword))
					continue;
				bodyWordsCount++;
				wordsProperty.put(keyword, property);
				if (bodyWordsMap.containsKey(keyword)) {
					bodyWordsMap.put(keyword, bodyWordsMap.get(keyword) + 1);
				} else
					bodyWordsMap.put(keyword, 1);
			}// for body
				// 构建document和TermInDoc结构，包含docid，titlewords和bodywords等信息
			int id = news.getId();
			Document doc = new Document(id);
			docs.add(doc);
			doc.setTitleWordsCount(titleWordsCount);
			doc.setBodyWordsCount(bodyWordsCount);
			doc.setTotalWordsCount(titleWordsCount + bodyWordsCount);
			StringBuffer titleWords = new StringBuffer();
			StringBuffer bodyWords = new StringBuffer();
			for (String word : wordsProperty.keySet()) {
				int countInTitle = 0;
				if (titleWordsMap.containsKey(word)) {
					countInTitle = titleWordsMap.get(word);
					titleWords.append(word).append(":").append(countInTitle)
							.append(";");
				}
				int countInBody = 0;
				if (bodyWordsMap.containsKey(word)) {
					countInBody = bodyWordsMap.get(word);
					bodyWords.append(word).append(":").append(countInBody)
							.append(";");
				}
				if (allWords.containsKey(word)) {
					allWords.get(word).addCount(id, countInTitle, countInBody);
				} else {
					TermEnum te = new TermEnum(word);
					te.setProperty(wordsProperty.get(word));
					te.addCount(id, countInTitle, countInBody);
					allWords.put(word, te);
				}
			}
			doc.setTitleWords(titleWords.toString());
			doc.setBodyWords(bodyWords.toString());
			count++;
		}// for news
		BufferedWriter br = null;
		try {
			br = new BufferedWriter(new FileWriter(new File(
					"/home/yulong/Desktop/doc")));
			for (Document doc : docs) {
				String str = doc.getId() + "     " + doc.getTitleWords()
						+ "     " + doc.getTitleWordsCount() + "     "
						+ doc.getBodyWords() + "     "
						+ doc.getBodyWordsCount();
				br.write(str);
				br.newLine();
			}
			br.close();
			br = new BufferedWriter(new FileWriter(new File(
					"/home/yulong/Desktop/term")));
			for (String word : allWords.keySet()) {
				TermEnum te = allWords.get(word);
				String str = te.getWords() + "     pro:" + te.getProperty()
						+ "     newsId:" + te.getDocIdString()
						+ "     titleCount:" + te.getTitleCountString()
						+ "     bodyCount:" + te.getBodyCountString();
				br.write(str);
				br.newLine();
			}
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("done!!!!!");
	}
	
	/**
	 * 将分完的词存到wordtag中，待标注，人名、地名单独存放，不用再标注，单个字的词语也单独存放，以便于人工筛选。
	 * 现在是从文件中读取，可以修改成从数据库中读取
	 */
	public void storeWords(){
		DataSource dataSource = DatabaseUtils.getInstance();
		Connection conn = null;
		PreparedStatement pstat = null;
		String sql1 = "insert into nameword(keyword, property) values(?, ?)";
		String sql2 = "insert into wordtag(keywords, property) values(?, ?)";
		BufferedReader br = null;
		List<WordTag> tagWords = new ArrayList<WordTag>();
		List<WordTag> nameWords = new ArrayList<WordTag>();
		List<WordTag> singleWords = new ArrayList<WordTag>();
		int i = 0;
		try {
			br = new BufferedReader(new FileReader(new File("/home/yulong/Desktop/term")));
			String str = null;
			while((str = br.readLine())!= null){
				String word[] = str.split("\\s{5}");
				String keyword = word[0];
				String property = (word[1].split(":"))[1];
				WordTag wordTag = new WordTag(keyword, property);
				if(keyword.trim().length() == 1){
					singleWords.add(wordTag);
				}
				else if(property.equals("nr")||property.equals("ns") ){
					nameWords.add(wordTag);
				}
				else
					tagWords.add(wordTag);
				if(i%1000 == 0){
					System.out.println(i);
				}
				i++;
			}
			br.close();
			//将只有一个字的词语存储到singleword文件中
			System.out.println("---------------------开始存放singleword-----------------------");
			BufferedWriter bw = null; 
			bw = new BufferedWriter(new FileWriter(new File("/home/yulong/Desktop/singleword")));
			for(WordTag word : singleWords){
				String str1 = word.getKeywords() + "     " + word.getProperty();
				bw.write(str1);
				bw.newLine();
			}
			bw.close();/*存储singleword 完毕*/
			
			
			conn = dataSource.getConnection();
			//将人名、地名存储到nameword表格中
			System.out.println("---------------------开始存放nameWord-----------------------");
			System.out.println("nameWord count: " + nameWords.size());
			int count = 0;
			pstat = conn.prepareStatement(sql1);
			for(WordTag word : nameWords){
				pstat.setString(1, word.getKeywords());
				pstat.setString(2, word.getProperty());
				pstat.addBatch();
				count++;
				if(count%2000 == 0){
					System.out.println("nameWord: " + count);
					pstat.executeBatch();
					pstat.clearBatch();
				}
				pstat.executeBatch();
			}
			pstat.close();/*存储人名、地名结束*/
			
			//将待标注的词存到wordtag表格中
			pstat = conn.prepareStatement(sql2);
			System.out.println("---------------------开始存放tagWords-----------------------");
			System.out.println(tagWords.size());
			count = 0;
			for(WordTag word : tagWords){
				pstat.setString(1, word.getKeywords());
				pstat.setString(2, word.getProperty());
				pstat.addBatch();
				count++;
				if(count%2000 == 0){
					System.out.println("tagWord: " + count);
					pstat.executeBatch();
					pstat.clearBatch();
				}
				pstat.executeBatch();
			}
			pstat.close();
			conn.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	

	public boolean wordFilter(String word) {
		boolean flag = true;
		// 去掉空格
		word = word.replaceAll("\\s{1,}", "");
		if ((!isChinese(word)) && (!isEnglish(word))) {
			return false;
		}
		// 去掉停用词
		if (stopwords.contains(word)) {
			return false;
		}
		return flag;
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
		strName = strName.replaceAll("\\s{1,}", "");
		char[] ch = strName.toCharArray();
		for (int i = 0; i < ch.length; i++) {
			char c = ch[i];
			if (!isChinese(c)) {
				return false;
			}
		}
		return true;
	}

	public boolean isEnglish(String str) {
		return str.replaceAll("\\s{1,}", "").matches("[a-zA-Z]");
	}

	// 判断字符串为数字
	public static boolean isNum(String str) {
		return str.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
	}

	public static void main(String[] args) {
		WordSplit ws = new WordSplit();
		// List<News> newsList = ws.getAllNews();
		// System.out.println(newsList.size());
		// ws.getCiXing();
		//ws.getAllWords();
		ws.storeWords();
	}

}
