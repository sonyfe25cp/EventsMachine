package gossip.model;

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
	}

	public String getBodyWords() {
		return bodyWords;
	}

	public void setBodyWords(String bodyWords) {
		this.bodyWords = bodyWords;
	}
	
}
