package gossip.gossip.utils;

import gossip.model.Event;
import gossip.model.News;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;

public class DatabaseUtils {

	private static DataSource dataSource;

	static String driver = "com.mysql.jdbc.Driver";
	static String dbUser = "root";
	static String dbPassword = "123iop";
	static String dbUrl = "jdbc:mysql://localhost:3306/gossip.gossip";

	public static DataSource getInstance() {
		if (dataSource == null) {
			BasicDataSource ds = new BasicDataSource();
			ds.setDriverClassName(driver);
			ds.setUrl(dbUrl);
			ds.setUsername(dbUser);
			ds.setPassword(dbPassword);
			ds.setInitialSize(20);
			ds.setMaxActive(50);
			ds.setMaxIdle(30);
			ds.setMaxWait(100);
			dataSource = ds;
		}
		return dataSource;
	}
	

	public static Connection getConnection() {
		Connection conn = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://10.1.0.127:3306/gossip.gossip", "root", "123iop");

			// conn = dataSource.getConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}

	public static Event eventfromResultSet(ResultSet rs) {
		Event event = new Event();
		try {
			event.setId(rs.getInt("id"));
			event.setTitle(rs.getString("title"));
			event.setDesc(rs.getString("content_abstract"));
			event.setImg(rs.getString("img"));
			event.setRecommended(rs.getDouble("recommended"));
			event.setCreateTime(rs.getDate("create_time").getTime());

			Map<String, Double> keywordsMap = new HashMap<String, Double>();
			String[] tmps = rs.getString("keywords").split(";");
			for (String tmp : tmps) {
				String[] pair = tmp.split(":");
				keywordsMap.put(pair[0], Double.parseDouble(pair[1]));
			}
			event.setKeyWords(keywordsMap);

			event.setStartedLocation(rs.getString("started_location"));
			List<Integer> pages = new ArrayList<Integer>();
			String[] pages_tmp = rs.getString("pages").split(";");
			for (String page : pages_tmp) {
				pages.add(Integer.parseInt(page));
			}
			event.setPagesList(pages);
		} catch (SQLException e) {
			e.printStackTrace();
			event = null;
		}
		return event;
	}

	public static News newsfromResultSet(ResultSet rs) {
		News news = new News();

		try {
			news.setId(rs.getInt("id"));
			news.setTitle(rs.getString("title"));
			news.setAuthor(rs.getString("author"));
			news.setBody(rs.getString("body"));
			news.setUrl(rs.getString("url"));
			news.setDescription(rs.getString("description"));
			news.setDate(rs.getString("date"));
			news.setCrawlAt(rs.getString("crawlat"));
			news.setFromSite(rs.getString("fromsite"));
			news.setStatus(rs.getString("status"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return news;
	}

}
