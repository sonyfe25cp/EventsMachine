package gossip.app.data;

import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

/**
 * 
 * @author lins 2012-8-24
 */
public class NewsDAO {
	private DBOpenHelper dbHelper;
	private SQLiteDatabase readableDatabase;
	private SQLiteDatabase writableDatabase;

	public NewsDAO(Context context) {
		dbHelper = new DBOpenHelper(context, "gossip", null, 1);
		readableDatabase = dbHelper.getReadableDatabase();
		writableDatabase = dbHelper.getWritableDatabase();
	}

	public void close() {
		readableDatabase.close();
		writableDatabase.close();
		dbHelper.close();
	}

	public News getNewsByIds(int id) {
		News news = null;
		Cursor cursor = readableDatabase.query("news", new String[] { "id",
				"event_id", "title", "body", "date", "url" }, "id == ?",
				new String[] { String.valueOf(id) }, null, null, null);
		if (cursor.moveToNext()) {
			System.out.println("get news from db whose id = " + id);
			int eventId = cursor.getInt(cursor.getColumnIndex("event_id"));
			String title = cursor.getString(cursor.getColumnIndex("title"));
			String body = cursor.getString(cursor.getColumnIndex("body"));
			String date = cursor.getString(cursor.getColumnIndex("date"));
			String url = cursor.getString(cursor.getColumnIndex("url"));

			news = new News(id, eventId, title, body, date, url);
		}
		return news;
	}

	public Bundle getTopNNewsByEventId(int eventId) {
		Bundle bundle = new Bundle();
		Cursor cursor = readableDatabase
				.query("news", new String[] { "id", "event_id", "title",
						"body", "date", "url" }, "where event_id == ?",
						new String[] { String.valueOf(eventId) }, null, null,
						"id desc");
		News news;
		int i = 0;
		while (cursor.moveToNext()) {
			int id = cursor.getInt(cursor.getColumnIndex("id"));
			System.out.println("get news from db whose id = " + id);
			String title = cursor.getString(cursor.getColumnIndex("title"));
			String body = cursor.getString(cursor.getColumnIndex("body"));
			String date = cursor.getString(cursor.getColumnIndex("date"));
			String url = cursor.getString(cursor.getColumnIndex("url"));

			news = new News(id, eventId, title, body, date, url);
			bundle.putSerializable(String.valueOf(i), news);
		}
		cursor.close();
		return bundle;

	}

	/**
	 * 对给定的输入events，对每一个事件该插入的插入，该更新的更新
	 */
	public void updateOrInsertNews(List<News> news) {
		for (News n : news) {
			writableDatabase.insertWithOnConflict("news", null,
					n.getContentValues(), SQLiteDatabase.CONFLICT_REPLACE);
		}
	}
}
