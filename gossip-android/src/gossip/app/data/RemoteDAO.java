package gossip.app.data;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * 方便获得服务器端数据的DAO
 * 
 * @author lins 2012-8-27
 */
public class RemoteDAO {
	final String urlBaseStr = "http://10.1.10.203:8080/gossip-server/";
	final int timeout = 3000;

	public RemoteDAO() {

	}

	// 检测网络是否可用
	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null) {
			return false;
		} else {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * 从服务器获得event
	 * 
	 * @param localEventId
	 *            当前本地最新的event的id
	 */
	public List<Event> getEventsFromServer(int localEventId) {
		List<Event> events = new ArrayList<Event>();

		InputStreamReader isr = null; // 当前是字符流
		URL url;
		try {
			/** 连接到服务器，读取响应放入sb **/
			url = new URL(urlBaseStr +"events/local?id="+localEventId);
			Log.i("connect", urlBaseStr +"events/local?id="+localEventId);
//			url = new URL("http://www.bitren.com");
			URLConnection urlConn = url.openConnection();
			urlConn.setReadTimeout(timeout);
			urlConn.setConnectTimeout(timeout);
			isr = new InputStreamReader(urlConn.getInputStream(), "UTF-8");
			urlConn.connect();
			char[] buffer = new char[10 * 1024];// 一次读10k的数据
			StringBuilder sb = new StringBuilder();
			while (isr.read(buffer) != -1) {
				sb.append(buffer);
			}
			//System.out.println(sb);

			/** 将sb转换成JSONArray,然后变成JSONObject然后变成event加入结果 **/
			JSONArray jsonArry = new JSONArray(sb.toString());
			for (int i = 0; i < jsonArry.length(); i++) {
				events.add(Event.fromJSONObject(jsonArry.getJSONObject(i)));
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		} finally {
			if (isr != null) {// 关闭inputStreamReader
				try {
					isr.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return events;
	}

	/**
	 * 从服务器获得news
	 * 
	 * @param newsId
	 *            新闻归属的event
	 */
	public List<News> getNewsFromServerByEventId(int newsId) {
		List<News> news = new ArrayList<News>();

		InputStreamReader isr = null; // 当前是字符流
		URL url;
		try {
			/** 连接到服务器，读取响应放入sb **/
			 url = new URL(urlBaseStr +"news/detail?id="+newsId);
//			url = new URL("http://www.bitren.com");
			URLConnection urlConn = url.openConnection();
			urlConn.setReadTimeout(timeout);
			urlConn.setConnectTimeout(timeout);
			isr = new InputStreamReader(urlConn.getInputStream(), "UTF-8");
			urlConn.connect();
			char[] buffer = new char[10 * 1024];// 一次读10k的数据
			StringBuilder sb = new StringBuilder();
			while (isr.read(buffer) != -1) {
				sb.append(buffer);
			}
			System.out.println(sb);

			/** 将sb转换成JSONArray,然后变成JSONObject然后变成event加入结果 **/
			JSONArray jsonArry = new JSONArray(sb.toString());
			for (int i = 0; i < jsonArry.length(); i++) {
				news.add(News.fromJSONObject(jsonArry.getJSONObject(i)));
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		} finally {
			if (isr != null) {// 关闭inputStreamReader
				try {
					isr.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return news;
	}

	/**
	 * 从服务器获得news
	 * 
	 * @param eventId
	 *            新闻归属的event
	 */
	public News getNewsFromServer(int id) {
		News news = null;
		
		InputStreamReader isr = null; // 当前是字符流
		URL url;
		try {
			/** 连接到服务器，读取响应放入sb **/
			url = new URL(urlBaseStr +"news/detail?id="+id);
			Log.i("connect", urlBaseStr +"news/detail?id="+id);
//			url = new URL("http://www.bitren.com");
			URLConnection urlConn = url.openConnection();
			urlConn.setReadTimeout(timeout);
			urlConn.setConnectTimeout(timeout);
			isr = new InputStreamReader(urlConn.getInputStream(), "UTF-8");
			urlConn.connect();
			char[] buffer = new char[10 * 1024];// 一次读10k的数据
			StringBuilder sb = new StringBuilder();
			while (isr.read(buffer) != -1) {
				sb.append(buffer);
			}
			System.out.println(sb);

			/** 将sb转换成JSONObject,然后变成news **/
			JSONObject jsonObj = new JSONObject(sb.toString());
			news = News.fromJSONObject(jsonObj);		
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		} finally {
			if (isr != null) {// 关闭inputStreamReader
				try {
					isr.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return news;
	}
	
	public static void main(String[] args) {
		RemoteDAO remoteDAO = new RemoteDAO();
		remoteDAO.getEventsFromServer(1);
	}
}
