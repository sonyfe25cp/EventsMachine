package gossip.server.model;

import java.io.Serializable;
import java.sql.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author yulong
 * 
 */
public class Event implements Serializable {
	private static final long serialVersionUID = 8954198578600418283L;

	final String delimiter = ";";

	private int id = -1;// 事件id
	private String title; // 事件主标题
	private double recommended = 0.0;
	// 下面这个属性感觉不要再建一个表，干脆就直接合一下放event这个表里面，表连接太耗时了
	private String keyWords;//
	private Map<String, Double> keyWordsMap = new HashMap<String, Double>(); // 关键词-权重
	private String pages;
	private List<Integer> pagesList = new LinkedList<Integer>();// 页面id,已经排过序的
	private Date create_time;
	private long started_at;
	private String content_abstract;// 事件描述
	private String img;
	private String started_location;

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

	public double getRecommended() {
		return recommended;
	}

	public void setRecommended(double recommended) {
		this.recommended = recommended;
	}

	public String getKeyWords() {
		return keyWords;
	}

	public void setKeyWords(String keyWords) {
		this.keyWords = keyWords;
	}

	public Map<String, Double> getKeyWordsMap() {
		return keyWordsMap;
	}

	public void setKeyWordsMap(Map<String, Double> keyWordsMap) {
		this.keyWordsMap = keyWordsMap;
	}

	public String getPages() {
		return pages;
	}

	public void setPages(String pages) {
		this.pages = pages;
	}

	public List<Integer> getPagesList() {
		return pagesList;
	}

	public void setPagesList(List<Integer> pagesList) {
		this.pagesList = pagesList;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	public String getContent_abstract() {
		return content_abstract;
	}

	public void setContent_abstract(String content_abstract) {
		this.content_abstract = content_abstract;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getStarted_location() {
		return started_location;
	}

	public void setStarted_location(String started_location) {
		this.started_location = started_location;
	}

	public String getDelimiter() {
		return delimiter;
	}

	public long getStarted_at() {
		return started_at;
	}

	public void setStarted_at(long started_at) {
		this.started_at = started_at;
	}
	
	

}
