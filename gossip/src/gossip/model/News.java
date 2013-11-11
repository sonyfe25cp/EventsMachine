package gossip.model;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.Field.TermVector;
import org.apache.lucene.document.NumericField;

public class News {

	private int id;//id
	private String title;//标题
	private String body;//正文
	private String url;
	private String author="";
	private String description="";//摘要
	private String date="";
	private String fromSite="";
	private String crawlAt="";
	private String status=NEW;//新闻的状态，新建，被索引，更新，删除
	private String startedLocation = "";
	private String keywords ="";
	private String titleWords;
	private String bodyWords;
	private String eventStatus;//跟事件的关系，已属于事件和不属于事件，默认为0，标志没有被某事件包含，若为1，则已经被包含在某事件
	
	public final static int Evented = 1;
	public final static int UnEvented = 0;
	
	public final static String NEW = "new";
	public final static String INDEX = "index";
	public final static String DELETE = "delete";
	public final static String UPDATE = "update";
	
	public String toJson(){
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		sb.append("\"id\":" + id);
		sb.append(",");
		sb.append("\"title\":\"" + title+"\"");
		sb.append(",");
		sb.append("\"desc\":\"" + description+"\"");
		sb.append(",");
		sb.append("\"author\":\"" + author+"\"");
		sb.append(",");
		sb.append("\"body\":\"" + body+"\"");
		sb.append(",");
		sb.append("\"publish_at\":\""+date+"\"");
		sb.append(",");
		sb.append("\"source\":\"" + url+"\"");
		sb.append(",");
		sb.append("\"started_location\":\"" + startedLocation+"\"");
		sb.append(",");
		sb.append("\"keywords\":\"" + keywords+"\"");
		sb.append("}");
		return sb.toString();
	}
	
	public static News fromDocument(Document doc){
		News news = new News();
		
		String title = doc.get("title");
		news.setTitle(title);
		
		String crawlat = doc.get("crawlat");
		news.setCrawlAt(crawlat);
		
		return news;
	}
	
	public String toString(){
		return "title:"+title+"\n"+"crawlat:"+crawlAt+"\n";
	}
	
	public Document toDocument(){
		Document doc = new Document();
		if (title == null || body == null || url == null || date == null) {
			return null;
		}
		Field uniqueField = new Field("id", id + "", Store.YES, Index.NOT_ANALYZED);
		Field titleField = new Field("title", title, Store.YES, Index.ANALYZED, TermVector.YES);
		Field contentField = new Field("body", body, Store.YES, Index.ANALYZED, TermVector.YES);
		Field authorField = new Field("author",author == null ? "" : author, Store.YES, Index.NOT_ANALYZED);
		Field urlField = new Field("url", url, Store.YES, Index.NOT_ANALYZED);
		Field dateField = new Field("date", date, Store.YES, Index.NOT_ANALYZED);// 显示用
		Field siteField = new Field("site",	fromSite == null ? "" : fromSite, Store.YES, Index.ANALYZED);
		NumericField crawlAtField = new NumericField("crawlat",	Field.Store.YES, true).setIntValue(0);// 便于范围查找
		crawlAtField.setIntValue(Integer.parseInt(crawlAt));
		Field statusField = new Field("status", status, Store.NO, Index.NOT_ANALYZED);
		doc.add(uniqueField);
		doc.add(titleField);
		doc.add(contentField);
		doc.add(authorField);
		doc.add(urlField);
		doc.add(dateField);
		doc.add(siteField);
		doc.add(crawlAtField);
		doc.add(statusField);
		return doc;
	}
	
//	private int transTodayToInt(){
//		Date date = Calendar.getInstance().getTime();
//		String time = DateTimeUtil.getFormatDay(date);
//		return Integer.parseInt(time);
//	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}


	public String getDate() {
		return date;
	}


	public void setDate(String date) {
		this.date = date;
	}


	public String getFromSite() {
		return fromSite;
	}


	public void setFromSite(String fromSite) {
		this.fromSite = fromSite;
	}


	public String getCrawlAt() {
		return crawlAt;
	}
	public void setCrawlAt(String crawlAt) {
		this.crawlAt = crawlAt;
	}

	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	public String getStartedLocation() {
		return startedLocation;
	}

	public void setStartedLocation(String startedLocation) {
		this.startedLocation = startedLocation;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
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

	public String getEventStatus() {
		return eventStatus;
	}

	public void setEventStatus(String eventStatus) {
		this.eventStatus = eventStatus;
	}


}