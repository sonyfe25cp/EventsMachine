package gossip.app.data;

import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

/**
 *用来操作数据库EVENT逐个表的DAO。不支持多线程，因为可能引发死锁。
 *@author lins 2012-8-23
 */
public class EventDAO {
	private DBOpenHelper dbHelper;
	private SQLiteDatabase readableDatabase;
	private SQLiteDatabase writableDatabase;
	
	public EventDAO(Context context){
		dbHelper = new DBOpenHelper(context, "gossip", null, 1);
		readableDatabase = dbHelper.getReadableDatabase();
		writableDatabase = dbHelper.getWritableDatabase();
	}
	
	/**
	 * 根据事件获得前N个事件
	 */
	public Bundle getTopNEventsByTime(int n){
		Bundle bundle = new Bundle();
		Cursor cursor = readableDatabase.query("event", new String[]{"id","title","recommended","keywords","pages","create_time","content_abstract"}, null, null, null, null, "create_time");
		Event e;//事件
		int i =0;
		while(cursor.moveToNext()){
			int id = cursor.getInt(cursor.getColumnIndex("id"));
			System.out.println("get event from db whose id = "+id);
			String title = cursor.getString(cursor.getColumnIndex("title"));
			float recommended = cursor.getFloat(cursor.getColumnIndex("recommended"));
			String keywords = cursor.getString(cursor.getColumnIndex("keywords"));
			String pages = cursor.getString(cursor.getColumnIndex("pages"));
			long createTime = cursor.getLong(cursor.getColumnIndex("create_time"));
			String contentAbstract = cursor.getString(cursor.getColumnIndex("content_abstract"));
			
			e = new Event(id, title, recommended, keywords, pages, createTime, contentAbstract);
			bundle.putSerializable(String.valueOf(i), e);
			i++;
		}
		cursor.close();
		return bundle;
	}
	
	/**
	 * 对给定的输入events，对每一个事件该插入的插入，该更新的更新
	 */
	public void updateOrInsertEvents(List<Event> events){
		for(Event e : events){
			writableDatabase.insertWithOnConflict("event", null, e.getContentValues(), SQLiteDatabase.CONFLICT_REPLACE);
		}
	}
	
	final long EXPIRATION = 3*24*3600;
	/**
	 * 移除数据库里面过期的记录，期限为3天
	 */
	public void removeExpired(){
		
	}
	
	public void close(){
		readableDatabase.close();
		writableDatabase.close();
		dbHelper.close();
	}
}
