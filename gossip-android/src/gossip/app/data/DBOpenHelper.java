package gossip.app.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHelper extends SQLiteOpenHelper {
	final String CREATE_TABLE_EVENT = "create table event(id int primary key,title varchar(50) not null,recommended float not null , keywords varchar(50),pages varchar(999),create_time long, content_abstract varvhar(999));";
	final String CREATE_TABLE_NEWS = "create table news(id int primary key, event_id int, title varchar(50) not null, body varchar(5000), date varchar(20), url varchar(100))";
	
	public DBOpenHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase arg0) {
		// 第一次建立数据库则建表
		System.out.println("i'll create table event on create db.");
		arg0.execSQL(CREATE_TABLE_EVENT);
		arg0.execSQL(CREATE_TABLE_NEWS);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
