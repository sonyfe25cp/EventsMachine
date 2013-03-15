package gossip.model;

import gossip.utils.DateTimeUtil;

import java.util.Calendar;
import java.util.Date;

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
	private String description="";//摘要
	private String author="";
	private String url;
	private String date="";
	private String fromSite="";
	private String crawlAt="";
	private NewsStatus status=NewsStatus.NEW;
	
	
	public Document toDocument(){
		Document doc = new Document();
		Field uniqueField = new Field("id", id + "", Store.YES, Index.NOT_ANALYZED);
		if (title == null || body == null || url == null || date == null) {
			return null;
		}

		Field titleField = new Field("title", title, Store.YES, Index.ANALYZED, TermVector.YES);
		Field contentField = new Field("body", body, Store.YES, Index.ANALYZED, TermVector.YES);
		Field authorField = new Field("author",author == null ? "" : author, Store.YES, Index.NOT_ANALYZED);
		Field urlField = new Field("url", url, Store.YES, Index.NOT_ANALYZED);
		Field dateField = new Field("date", date, Store.YES, Index.NOT_ANALYZED);// 显示用
		Field siteField = new Field("site",	fromSite == null ? "" : fromSite, Store.YES, Index.ANALYZED);
		NumericField crawlAtField = new NumericField("crawlAt",	Field.Store.YES, true).setIntValue(0);// 便于范围查找
		crawlAtField.setIntValue(transTodayToInt());
		doc.add(uniqueField);
		doc.add(titleField);
		doc.add(contentField);
		doc.add(authorField);
		doc.add(urlField);
		doc.add(dateField);
		doc.add(siteField);
		doc.add(crawlAtField);
		return doc;
	}
	
	private int transTodayToInt(){
		Date date = Calendar.getInstance().getTime();
		String time = DateTimeUtil.getFormatDay(date);
		return Integer.parseInt(time);
	}
	
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

	public NewsStatus getStatus() {
		return status;
	}

	public void setStatus(NewsStatus status) {
		this.status = status;
	}
}