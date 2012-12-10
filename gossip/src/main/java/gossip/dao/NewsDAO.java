package gossip.dao;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

import javax.sql.DataSource;

import net.rubyeye.xmemcached.exception.MemcachedException;
import net.sf.json.JSONObject;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import edu.bit.dlde.utils.DLDEConfiguration;
import edu.bit.dlde.utils.DLDELogger;
import gossip.index.GossipMessager;
import gossip.queryExpansion.News;

/**
 * @author syl
 * 
 *         该类实现对新闻的读取和存储功能，并可以根据不同的条件对数据库进行操作
 */
public class NewsDAO {
	String indexPath = DLDEConfiguration.getInstance("gossip.properties")
			.getValue("IndexPath");
	private DataSource dataSource;
	private DLDELogger logger;
	final String SQL_SELECT_NEWS_ALL = "select * from news";
	final String SQL_SELECT_NEWS_BY_ID = "select * from news  where id = ?";
	final String SQL_SELECT_NEWS_BY_TITLE = "select * from news  where title = ?";
	final String SQL_INSERT_NEWS = "insert into news(id,title,body,url,author,description) values(?,?,?,?,?,?)";

	public DLDELogger getLogger() {
		return logger;
	}

	public void setLogger(DLDELogger logger) {
		this.logger = logger;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	MemcachedDaemon memcachedDaemon;

	public MemcachedDaemon getMemcachedDaemon() {
		return memcachedDaemon;
	}

	public void setMemcachedDaemon(MemcachedDaemon memcachedDaemon) {
		this.memcachedDaemon = memcachedDaemon;
	}

	/** 主要用来读索引 **/
	private GossipMessager messager;

	public GossipMessager getMessager() {
		return messager;
	}

	public void setMessager(GossipMessager messager) {
		this.messager = messager;
	}

	public void init() {
		if (logger == null)
			logger = new DLDELogger();
		if (dataSource == null) {
			BasicDataSource basicDataSource = new BasicDataSource();
			basicDataSource.setDriverClassName("com.mysql.jdbc.Driver");
			basicDataSource
					.setUrl("jdbc:mysql://localhost:3306/gossip?useUnicode=true");
			basicDataSource.setUsername("root");
			basicDataSource.setPassword("123iop");
			dataSource = basicDataSource;
		}
	}

	/**
	 * @deprecated
	 * @return ArrayList<News>
	 *         此方法为从索引中读取news的信息,这里值用到了id,title,body,author和url属性
	 *         返回形式如：{id:1,author:"***",body:"……",url:"……"，description:"……"}
	 */
	/* 此方法为从索引中读取news的信息,这里值用到了id,title,body,author和url属性 */
	private ArrayList<News> allNewsFromIndex() {
		init();
		ArrayList<News> allNews = new ArrayList<News>();
		try {
			Directory dir = FSDirectory.open(new File(indexPath));
			IndexReader reader = IndexReader.open(dir);
			for (int i = 0; i < reader.numDocs(); i++) {
				System.out.println(i + "------------------------------");
				News news = new News();
				String id = reader.document(i).getField("id").stringValue();
				System.out.println(id);
				news.setId(Integer.parseInt(id));
				String title = reader.document(i).getField("title")
						.stringValue();
				System.out.println(title);
				news.setTitle(title);
				String body = reader.document(i).getField("body").stringValue();
				System.out.println(body);
				news.setBody(body);
				String author = reader.document(i).getField("author")
						.stringValue();
				System.out.println(author);
				news.setAuthor(author);
				// String
				// description=reader.document(i).getField("description").stringValue();
				// news.setDescription(description);
				allNews.add(news);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return allNews;
	}

	public JSONObject getNewsJSONById(long id) {
		JSONObject result = null;
		if (memcachedDaemon.isOn()) {// 当memcached是开着的时候
			try {
				for (int i = 0; i < 5; i++) {// 最多尝试5次读memcached
					result = memcachedDaemon.getMemcachedClient().get(
							"news:" + id, 500);
					if (result == null) {
						logger.info("i found nothing in memcacehd...");
						// 查索引
						result = getNewsJSONByIdFromIndex(id);
						// 添加
						if (result != null) {
							String key = MemcachedKeyUtils.generateKey(
									MemcachedKeyUtils.NEWS, id);
							memcachedDaemon.getMemcachedClient().add(key,
									MemcachedDaemon.expiration, result);
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {// 当memcached是关着的时候
			result = getNewsJSONByIdFromIndex(id);
		}
		return result;
	}

	/**
	 * 从索引读取新闻,该方法被getNewsJSONById(long id)调用
	 * 
	 * @param id
	 * @return
	 */
	private JSONObject getNewsJSONByIdFromIndex(long id) {
		/** socket到索引进程，等待响应 **/
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("id", id);
		String response = messager.sendMessage(jsonObj,
				GossipMessager.GET_DOC_BY_ID);

		if (response == null || response.equals(""))
			return null;

		// 进行一次类型转换
		return JSONObject.fromObject(response);
	}

	/**
	 * 获得新闻的排列
	 * 
	 * @param begin
	 * @param limit
	 * @return
	 */
	public JSONObject getNewsRankingJSON(int begin, int limit) {
		JSONObject result = null;
		if (memcachedDaemon.isOn()) {// 当memcached是开着的时候
			try {
				for (int i = 0; i < 5; i++) {// 最多尝试5次读memcached
					result = memcachedDaemon.getMemcachedClient().get(
							MemcachedKeyUtils.generateKey(
									MemcachedKeyUtils.NEWS_RANKING, begin,
									limit), 500);
					if (result == null) {
						logger.info("i found nothing in memcacehd...");
						// 查索引
						result = getNewsRankingJSONFromIndex(begin, limit);
						// 添加
						if (result != null) {
							String key = MemcachedKeyUtils.generateKey(
									MemcachedKeyUtils.NEWS_RANKING, begin,
									limit);
							memcachedDaemon.getMemcachedClient().add(key,
									MemcachedDaemon.expiration, result);
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {// 当memcached是关着的时候
			result = getNewsRankingJSONFromIndex(begin, limit);
		}
		return result;
	}

	/**
	 * 从索引读取新闻的排列，该方法被getNewsRanking(int begin, int limit)调用
	 * 
	 * @return
	 */
	private JSONObject getNewsRankingJSONFromIndex(int begin, int limit) {
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("begin", begin);
		jsonObj.put("offset", limit);
		String response = messager.sendMessage(jsonObj,
				GossipMessager.GET_NEWS_RANKING);

		if (response == null || response.equals(""))
			return null;

		// 进行一次类型转换
		return JSONObject.fromObject(response);
	}

	/**
	 * @param id
	 * @return News,该方法实现根据新闻的id号从数据库中读取出对应的新闻及其相关内容
	 *         返回形式如：{id:1,author:"***",body:"……",url:"……"，description:"……"}
	 */
	public News getNewsById(int id) {
		init();
		News news = new News();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL_SELECT_NEWS_BY_ID);
			pstmt.setInt(1, id);
			if (pstmt.execute()) {
				rs = pstmt.getResultSet();
				while (rs.next()) {
					news.setId(id);
					news.setTitle(rs.getString("title"));
					news.setAuthor(rs.getString("author"));
					news.setBody(rs.getString("body"));
					news.setUrl(rs.getString("url"));
					news.setDescription(rs.getString("description"));
					logger.info("i found the news in database.");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return news;
	}

	/**
	 * @param String
	 *            title，
	 * @return News 使用该方法通过标题控制新闻存储时不能出现重复的问题，因为id是自增的，不太好控制重复
	 */
	public News getNewsByTitle(String title) {
		News news = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL_SELECT_NEWS_BY_TITLE);
			pstmt.setString(1, title);
			if (pstmt.execute()) {
				rs = pstmt.getResultSet();
				while (rs.next()) {
					news = new News();
					// System.out.println("I found a new by this title"+"id="+rs.getInt("id"));
					news.setId(rs.getInt("id"));
					news.setTitle(rs.getString("title"));
					news.setAuthor(rs.getString("author"));
					news.setBody(rs.getString("body"));
					news.setUrl(rs.getString("url"));
					news.setDescription(rs.getString("description"));
					// logger.info("i found the news in database.");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return news;
	}

	/**
	 * @return ArrayList<News>,该方法是从数据库中读出所有的新闻及其相关内容
	 *         返回形式如：{id:1,author:"***",body
	 *         :"……",url:"……"，description:"……"},{},{},……
	 */
	public ArrayList<News> getAllNewsFromDB() {
		ArrayList<News> allNews = new ArrayList<News>();
		init();
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			conn = dataSource.getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(SQL_SELECT_NEWS_ALL);
			while (rs.next()) {
				News news = new News();
				news.setId(rs.getInt("id"));
				news.setTitle(rs.getString("title"));
				news.setBody(rs.getString("body"));
				news.setAuthor(rs.getString("author"));
				news.setDescription(rs.getString("description"));
				allNews.add(news);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return allNews;
	}

	// insert into news(title,body,url,author,description) values(?,?,?,?,?)";
	public void insertNews(ArrayList<News> newsList) {
		if (newsList.isEmpty() || newsList == null) {
			return;
		}
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL_INSERT_NEWS,
					Statement.RETURN_GENERATED_KEYS);
			for (News news : newsList) {
				//System.out.println("11111111111111111");
				News newsByTitle = null;
				// 插入之前先根据title执行查询操作，如果查询结果为空，说明数据库中还没有这条新闻，可以插入，否则说明数据库中已经有了，不能重复插入
				newsByTitle = getNewsByTitle(news.getTitle());
				if (newsByTitle == null) {
					pstmt.setInt(1, news.getId());
					pstmt.setString(2, news.getTitle());
					pstmt.setString(3, news.getBody());
					pstmt.setString(4, news.getUrl());
					pstmt.setString(5, news.getAuthor());
					pstmt.setString(6, news.getDescription());
					pstmt.executeUpdate();
					rs = pstmt.getGeneratedKeys();
				}// if
			}// for
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}
}
