package gossip.app.data;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.text.format.DateFormat;

/**
 * 表示一个事件
 * 
 * @author lins 2012-8-23
 */
public class Event implements Serializable {
	private static final long serialVersionUID = -5667032665653329568L;

	final String delimiter = ";";

	private int id = -1;// 事件id
	private String title; // 事件主标题
	private double recommended = 0.0;
	private Map<String, Double> keyWords = new HashMap<String, Double>(); // 关键词-权重
	private String keyWordsStr;
	private List<Integer> pages = new LinkedList<Integer>(); // 新闻id,已经排过序的
	private String pagesStr;
	private String createTime;
	private long createTimeLong;
	private String contentAbstract;

	public Event(int id, String title, float recommended, String keywords,
			String pages, long createTime, String contentAbstract) {
		this.id = id;
		this.title = title;
		this.recommended = recommended;
		String[] tmpStrs;
		// keywords
		if (keywords != null) {
			tmpStrs = keywords.split(delimiter);
			for (String str1 : tmpStrs) {
				String[] str2 = str1.split(":");
				if (str2.length == 2)
					this.keyWords.put(str2[0], Double.valueOf(str2[1]));
				else
					this.keyWords.put(str2[0], 0.0);
			}
			keyWordsStr = keywords;
		}
		// pgaes
		if (pages != null) {
			tmpStrs = pages.split(delimiter);
			for (String str1 : tmpStrs) {
				this.pages.add(Integer.valueOf(str1));
			}
			pagesStr = pages;
		}
		this.createTime = DateFormat.format("yyyy-MM-dd hh:mm",
				new Date(createTime)).toString();
		createTimeLong = createTime;
		this.contentAbstract = contentAbstract;
	}

	public ContentValues getContentValues() {
		ContentValues values = new ContentValues();
		values.put("id", id);
		values.put("title", title);
		values.put("recommended", recommended);
		values.put("keywords", keyWordsStr);
		values.put("pages", pagesStr);
		values.put("create_time", createTimeLong);
		values.put("content_abstract", contentAbstract);
		return values;
	}

	public static Event fromJSONObject(JSONObject jsonObj) {
		int id;
		try {
			id = jsonObj.getInt("id");String title = jsonObj.getString("title");
			float recommended = (float) jsonObj.getDouble("recommended");
			String keywords = jsonObj.getString("keywords");
			String pages = jsonObj.getString("pages");
			long createTime = jsonObj.getLong("create_time");
			// String contentAbstract
			return new Event(id, title, recommended, keywords, pages, createTime, null);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @return 返回新闻在索引的id的列表，该列表里面的新闻是已经排过序的
	 */
	public List<Integer> getPages() {
		return pages;
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

	public double getRecommended() {
		return recommended;
	}

	public void setRecommended(double recommended) {
		this.recommended = recommended;
	}

	public Map<String, Double> getKeyWords() {
		return keyWords;
	}

	public void setKeyWords(Map<String, Double> keyWords) {
		this.keyWords = keyWords;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getDelimiter() {
		return delimiter;
	}

	public void setPages(List<Integer> pages) {
		this.pages = pages;
	}

	public String getContentAbstract() {
		return contentAbstract;
	}

	public void setContentAbstract(String contentAbstract) {
		this.contentAbstract = contentAbstract;
	}
}
