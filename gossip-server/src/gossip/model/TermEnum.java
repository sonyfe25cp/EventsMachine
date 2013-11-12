package gossip.model;

import java.util.HashMap;
import java.util.Map;

public class TermEnum {
	//单词
	private String words;
	//单词的属性
	private String property;
	//单词的倒排索引，记录单词出现的文档号，以及在文档中出现的频率（title和body分开存储）
	Map<Integer, WordsCount> terms = new HashMap<Integer, WordsCount>();
	
	public TermEnum(){
		
	}
	
	public TermEnum(String words){
		this.words = words;
	}

	public String getWords() {
		return words;
	}

	public void setWords(String words) {
		this.words = words;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public Map<Integer, WordsCount> getTerms() {
		return terms;
	}

	public void setTerms(Map<Integer, WordsCount> terms) {
		this.terms = terms;
	}
	

}
