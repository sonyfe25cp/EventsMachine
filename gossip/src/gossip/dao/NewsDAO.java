package gossip.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import net.sf.json.JSONObject;
import edu.bit.dlde.utils.DLDEConfiguration;
import edu.bit.dlde.utils.DLDELogger;
import gossip.index.GossipMessager;
import gossip.model.News;
import gossip.utils.DatabaseUtils;

/**
 * @author syl
 * 
 *         该类实现对新闻的读取和存储功能，并可以根据不同的条件对数据库进行操作
 */
public class NewsDAO{
	String indexPath = DLDEConfiguration.getInstance("gossip.properties")
			.getValue("IndexPath");
	private DataSource dataSource;
	private DLDELogger logger;
	final String SQL_SELECT_NEWS_ALL = "select * from news";
	final String SQL_SELECT_NEWS_BY_ID = "select * from news  where id = ?";
	final String SQL_SELECT_NEWS_BY_TITLE = "select * from news  where title = ?";

	public NewsDAO() {
	}

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

	/** 主要用来读索引 **/
	private GossipMessager messager;

	public GossipMessager getMessager() {
		return messager;
	}

	public void setMessager(GossipMessager messager) {
		this.messager = messager;
	}

	public void init() {
		
		System.out.println("logger:"+logger);
		System.out.println("dataSource:"+dataSource);
		
		if (logger == null)
			logger = new DLDELogger();
		if (dataSource == null) {
			dataSource = DatabaseUtils.getInstance();
		}
	}
	public JSONObject getNewsJSONById(long id) {
		JSONObject result = null;
		result = getNewsJSONByIdFromIndex(id);
		
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
		result = getNewsRankingJSONFromIndex(begin, limit);
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
		News news = new News();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL_SELECT_NEWS_BY_ID);
			pstmt.setInt(1, id);
			if (pstmt.execute()) {
				rs = pstmt.getResultSet();
				while (rs.next()) {
					news = DatabaseUtils.newsfromResultSet(rs);
					news.setId(id);
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
				if(conn!=null){
					conn.close();
				}
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
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection conn = null;
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
				if(conn!=null){
					conn.close();
				}
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
		Statement stmt = null;
		ResultSet rs = null;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(SQL_SELECT_NEWS_ALL);
			while (rs.next()) {
				News news = new News();
				news = DatabaseUtils.newsfromResultSet(rs);
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
				if(conn!=null){
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return allNews;
	}

	/*
	 * 批量更新新闻状态
	 */
	final String SQL_UPDATE_STATUS_BATCH = "update news set status = ? where id = ?";
	public void batchUpdateNews(List<News> newsList, String status) {
		PreparedStatement pstmt = null;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			conn.setAutoCommit(false);
			for (News news : newsList) {
				pstmt = conn.prepareStatement(SQL_UPDATE_STATUS_BATCH);
				pstmt.setString(1, status);
				pstmt.setInt(2, news.getId());
				pstmt.addBatch();
			}
			pstmt.executeBatch();
			conn.commit();
			pstmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 更新新闻的分词
	 */
	final String SQL_UPDATE_WORDS_BATCH = "update news set titleWords = ?, bodyWords = ? where id = ?";
	public void batchUpdateTokenizerWords(List<News> newsList, String status) {
		PreparedStatement pstmt = null;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			conn.setAutoCommit(false);
			for (News news : newsList) {
				pstmt = conn.prepareStatement(SQL_UPDATE_STATUS_BATCH);
				pstmt.setString(1, news.getTitleWords());
				pstmt.setString(2, news.getBodyWords());
				pstmt.setInt(3, news.getId());
				pstmt.addBatch();
			}
			pstmt.executeBatch();
			conn.commit();
			pstmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	/**
	 * true is exist
	 * false is not exist
	 * @param title
	 * @return
	 */
	public boolean isExist(String title) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL_SELECT_NEWS_BY_TITLE);
			pstmt.setString(1, title);
			if (pstmt.execute()) {
				rs = pstmt.getResultSet();
				if(rs.next()){
					return true;
				}else{
					return false;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if(conn!=null){
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}
		}
		return false;
	}

	final String SQL_INSERT_NEWS = "insert into news" +
			"(title,body,url,author,description,date,fromsite,crawlat,status, titleWords, bodyWords) " +
			"values " +
			"(?,?,?,?,?,?,?,?,?,?,?)";
	public void insertNews(List<News> newsList) {
		if (newsList.isEmpty() || newsList == null) {
			return;
		}
		PreparedStatement pstmt = null;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			conn.setAutoCommit(false);
			pstmt = conn.prepareStatement(SQL_INSERT_NEWS);
			for (News news : newsList) {
				if (news == null || news.getTitle() == null
						|| news.getBody() == null)
					continue;
//				boolean existFlag = isExist(news.getTitle());
//				if (!existFlag) {
					System.out.println("insert " + news.getTitle());
					pstmt.setString(1, news.getTitle());
					pstmt.setString(2, news.getBody());
					pstmt.setString(3, news.getUrl());
					pstmt.setString(4, news.getAuthor());
					pstmt.setString(5, news.getDescription());
					pstmt.setString(6, news.getDate());
					pstmt.setString(7, news.getFromSite());
					pstmt.setString(8, news.getCrawlAt());
					pstmt.setString(9, news.getStatus());
					pstmt.setString(10, news.getTitleWords());
					pstmt.setString(11, news.getBodyWords());
					pstmt.addBatch();
//				}// if
			}// for
			pstmt.executeBatch();
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
				if(conn!=null){
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * 返回状态为NEW的
	 * 
	 * @return
	 */
	final String SQL_FRESH_NEWS = "select * from news where status = 'new'";

	public List<News> getFreshNews() {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<News> newsList = new ArrayList<News>();
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL_FRESH_NEWS);
			pstmt.execute();
			rs = pstmt.getResultSet();
			News news = null;
			while (rs.next()) {
				news = new News();
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
				newsList.add(news);
			}
			rs.close();
			pstmt.close();
		} catch (Exception e) {

		}
		return newsList;
	}

	final String SQL_DELETE_NEWS_BY_ID = "update event set pages=? where id = ?";

	/**
	 * 删除不相关新闻
	 * 
	 * @param id
	 * @param newsId
	 */
	public void deleteNews(int id, String newsId) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String pages = null;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL_SELECT_NEWS_BY_ID);
			pstmt.setInt(1, id);
			pstmt.execute();
			rs = pstmt.getResultSet();
			if (rs.next()) {
				pages = rs.getString("pages");
			}
			String newPages = null;
			String[] allPages = pages.split(";");
			for (String page : allPages) {
				if (!page.equals(newsId)) {
					if (newPages == null)
						newPages = page + ";";
					else
						newPages += page + ";";
				}

			}

			pstmt = conn.prepareStatement(SQL_DELETE_NEWS_BY_ID);
			pstmt.setInt(2, id);
			pstmt.setString(1, newPages);
			pstmt.execute();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
				if(conn!=null){
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
