package gossip.model;

public class TermInDoc {
	private String words;//单词
	private int newsId;//单词所在新闻文档号
	private String property;//单词的词性
	private int totalCount;//在新闻中出现的总次数
	private int titleCount;//在新闻标题中出现的次数
	private int bodyCount;//在新闻内容中出现的次数

	public TermInDoc() {

	}

	public TermInDoc(String words) {
		this.words = words;
	}

	public TermInDoc(String words, int newsId) {
		this.words = words;
		this.newsId = newsId;
	}

	public String getWords() {
		return words;
	}

	public void setWords(String words) {
		this.words = words;
	}

	public int getNewsId() {
		return newsId;
	}

	public void setNewsId(int newsId) {
		this.newsId = newsId;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public int getTitleCount() {
		return titleCount;
	}

	public void setTitleCount(int titleCount) {
		this.titleCount = titleCount;
	}

	public int getBodyCount() {
		return bodyCount;
	}

	public void setBodyCount(int bodyCount) {
		this.bodyCount = bodyCount;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}
	
	
	


}
