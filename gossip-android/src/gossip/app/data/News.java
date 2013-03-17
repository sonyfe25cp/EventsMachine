package gossip.app.data;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;

/**
 * 存放新闻
 * 
 * @author lins 2012-8-24
 */
public class News implements Serializable {
	private static final long serialVersionUID = 7073494483311116241L;

	private int id = -1;
	private int eventId = -1;
	private String title;
	private String body;
	private String date;
	private String url;

	public News(int id, int eventId, String title, String body, String date,
			String url) {
		this.id = id;
		this.eventId = eventId;
		this.title = title;
		this.body = body;
		this.date = date;
		this.url = url;
	}

	public static News fromJSONObject(JSONObject jsonObject) {
		int id = -1;
		int eventId = -1;
		String title = "";
		String body = "";
		String date = "";
		String url = "";
		try {
			title = jsonObject.getString("title");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		try {
			body = jsonObject.getString("body");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		try {
			date = jsonObject.getString("date");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		try {
			url = jsonObject.getString("url");
		} catch (JSONException e) {
			//e.printStackTrace();
		}
		return new News(id, eventId, title, body, date, url);
	}

	public ContentValues getContentValues() {
		ContentValues values = new ContentValues();
		values.put("id", id);
		values.put("event_id", eventId);
		values.put("title", title);
		values.put("body", body);
		values.put("date", date);
		values.put("url", url);
		return values;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getEventId() {
		return eventId;
	}

	public void setEventId(int eventId) {
		this.eventId = eventId;
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

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
