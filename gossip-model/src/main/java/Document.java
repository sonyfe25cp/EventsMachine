

import java.util.HashMap;

public class Document {
	//文档号（新闻newsId）
	private int id;
	//文档中单词总数
	private int totalWordsCount;
	//文档标题单词总数
	private int titleWordsCount;
	//文档内容中单词总数
	private int bodyWordsCount;
	//文档标题的单词信息，表示为： 北理工:3;北航:2;
	private String titleWords;
	//文档内容中单词信息，表示为： 北理工:3;北航:2;
	private String bodyWords;
	//每篇文档对应的BM25值，便于排序
	private double BM25;
	private HashMap<String, Integer> titleWordsMap = new HashMap<String, Integer>(); 
	private HashMap<String, Integer> bodyWordsMap = new HashMap<String, Integer>();
	
	
	public Document(){
		
	}
	
	public Document(int id){
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getTotalWordsCount() {
		return totalWordsCount;
	}

	public void setTotalWordsCount(int totalWordsCount) {
		this.totalWordsCount = totalWordsCount;
	}

	public int getTitleWordsCount() {
		return titleWordsCount;
	}

	public void setTitleWordsCount(int titleWordsCount) {
		this.titleWordsCount = titleWordsCount;
	}

	public int getBodyWordsCount() {
		return bodyWordsCount;
	}

	public void setBodyWordsCount(int bodyWordsCount) {
		this.bodyWordsCount = bodyWordsCount;
	}

	public String getTitleWords() {
		return titleWords;
	}

	public void setTitleWords(String titleWords) {
		this.titleWords = titleWords;
		//解析字符串，保存成“单词-频率”的map形式
		String words[] = titleWords.split(";");
		for(String word: words){
			String[] keyword = word.split(":");
			if(keyword.length < 2){
				continue;
			}
			titleWordsMap.put(keyword[0], Integer.parseInt(keyword[1])); 
		}
	}

	public String getBodyWords() {
		return bodyWords;
	}

	public void setBodyWords(String bodyWords) {
		this.bodyWords = bodyWords;
		//解析字符串，保存成“单词-频率”的map形式
		String words[] = bodyWords.split(";");
		for(String word: words){
			String[] keyword = word.split(":");
			if(keyword.length !=2){
				continue;
			}
			bodyWordsMap.put(keyword[0], Integer.parseInt(keyword[1])); 
		}
	}

	public HashMap<String, Integer> getTitleWordsMap() {
		return titleWordsMap;
	}

	public void setTitleWordsMap(HashMap<String, Integer> titleWordsMap) {
		this.titleWordsMap = titleWordsMap;
	}

	public HashMap<String, Integer> getBodyWordsMap() {
		return bodyWordsMap;
	}

	public void setBodyWordsMap(HashMap<String, Integer> bodyWordsMap) {
		this.bodyWordsMap = bodyWordsMap;
	}

	public double getBM25() {
		return BM25;
	}

	public void setBM25(double bM25) {
		BM25 = bM25;
	}
	
	
	
}
