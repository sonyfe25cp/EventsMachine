

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TermEnum {
	//单词
	private String words;
	//单词的属性
	private String property;
	//单词的倒排索引，记录单词出现的文档号，以及在文档中出现的频率（title和body分开存储）
	private String docIdString;
	private String titleCountString;
	private String bodyCountString;
	private List<Integer> docIds = new ArrayList<Integer>();
	private List<Integer> titleCounts = new ArrayList<Integer>();
	private List<Integer> bodyCounts  = new ArrayList<Integer>();
	
	public TermEnum(){
		
	}
	
	public TermEnum(String words){
		this.words = words;
	}
	
	public void addCount(int docId, int titleCount, int bodyCount){
		if((docIdString != null)&&(!docIdString.equals(""))){
			docIdString = docIdString + docId + ";";
			titleCountString = titleCountString + titleCount + ";";
			bodyCountString = bodyCountString + bodyCount + ";";
		}
		else
		{
			docIdString = docId + ";";
			titleCountString = titleCount + ";";
			bodyCountString = bodyCount + ";";
		}
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
	
	
	public List<Integer> getDocIds() {
		return docIds;
	}

	public void setDocIds(List<Integer> docIds) {
		this.docIds = docIds;
	}

	public List<Integer> getTitleCounts() {
		return titleCounts;
	}

	public void setTitleCounts(List<Integer> titleCounts) {
		this.titleCounts = titleCounts;
	}

	public List<Integer> getBodyCounts() {
		return bodyCounts;
	}

	public void setBodyCounts(List<Integer> bodyCounts) {
		this.bodyCounts = bodyCounts;
	}

	public String getDocIdString() {
		return docIdString;
	}

	public void setDocIdString(String docIdString) {
		this.docIdString = docIdString;
		String docId[] = docIdString.split(";");
		for(String s : docId){
			docIds.add(Integer.parseInt(s));
		}
	}

	public String getTitleCountString() {
		return titleCountString;
	}

	public void setTitleCountString(String titleCountString) {
		this.titleCountString = titleCountString;
		String titleCount[] = titleCountString.split(";");
		for(String s : titleCount){
			titleCounts.add(Integer.parseInt(s));
		}
	}

	public String getBodyCountString() {
		return bodyCountString;
	}

	public void setBodyCountString(String bodyCountString) {
		this.bodyCountString = bodyCountString;
		String bodyCounts[] = bodyCountString.split(";");
		for(String s : bodyCounts){
			docIds.add(Integer.parseInt(s));
		}
	}
	
}
