package gossip.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.sql.DataSource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import edu.bit.dlde.utils.DLDELogger;
import gossip.event.Event;
import gossip.utils.DatabaseUtils;

public class EventDAO {

	private DLDELogger logger;

	public DLDELogger getLogger() {
		return logger;
	}

	public void setLogger(DLDELogger logger) {
		this.logger = logger;
	}

	public void init() {
		if (logger == null)
			logger = new DLDELogger();
		if (dataSource == null) {
			dataSource = DatabaseUtils.getInstance();
		}
	}

	private DataSource dataSource;

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	final String SQL_SELECT_EVENT_BY_ID = "select * from event  where id = ?";
	final String SQL_SELECT_EVENT_BY_DATE = "select * from event  where create_time = ?";
	final String SQL_SELECT_EVENT_ORDERED = "select id from event order by recommended desc";
	final String SQL_SELECT_EVENT_ALL = "select * from event";// 从数据库中读出所有的event-by
																// shiyulong
	final String delimiter = ";";

	public JSONArray getEventJSONByDate(Date date) {
		JSONArray result = getEventJSONByDateFromDB(date);
		return result;
	}

	/**
	 * 根据日期从数据库中获得事件
	 * 
	 * @param date
	 * @return
	 */
	private JSONArray getEventJSONByDateFromDB(Date date) {
		JSONObject jsonObj;
		JSONArray jsonArry = new JSONArray();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL_SELECT_EVENT_BY_DATE);
			pstmt.setDate(1, date);
			if (pstmt.execute()) {
				rs = pstmt.getResultSet();
				while (rs.next()) {
					jsonObj = new JSONObject();
					jsonObj.put("id", rs.getInt("id"));
					jsonObj.put("title", rs.getString("title"));
					jsonObj.put("desc", rs.getString("content_abstract"));
					jsonObj.put("img", rs.getString("img"));
					jsonObj.put("recommended", rs.getDouble("recommended"));
					jsonObj.put("started_at", rs.getDate("create_time")
							.getTime());
					jsonObj.put("keywords", rs.getString("keywords"));
					jsonObj.put("started_location",
							rs.getString("started_location"));
					jsonObj.put("news", rs.getString("pages"));

					jsonArry.add(jsonObj);
					logger.info("i found the event in database.");
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

		return jsonArry;

	}

	/**
	 * 获得JSONObject格式的Event
	 * 
	 * @param id
	 * @return
	 */
	public JSONObject getEventJSONById(int id) {
		JSONObject result = getEventJSONByIdFromDB(id);
		return result;
	}

	/**
	 * 从数据库获得JSONObject格式的Event
	 * 
	 * @param id
	 * @return
	 */
	private JSONObject getEventJSONByIdFromDB(int id) {
		JSONObject jsonObj = new JSONObject();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL_SELECT_EVENT_BY_ID);
			pstmt.setInt(1, id);
			if (pstmt.execute()) {
				rs = pstmt.getResultSet();
				while (rs.next()) {
					jsonObj.put("id", rs.getInt("id"));
					jsonObj.put("title", rs.getString("title"));
					jsonObj.put("desc", rs.getString("content_abstract"));
					jsonObj.put("img", rs.getString("img"));
					jsonObj.put("recommended", rs.getDouble("recommended"));
					jsonObj.put("started_at", rs.getDate("create_time")
							.getTime());
					jsonObj.put("keywords", rs.getString("keywords"));
					jsonObj.put("started_location",
							rs.getString("started_location"));
					jsonObj.put("news", rs.getString("pages"));
					logger.info("i found the event in database.");
				}
			}
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return jsonObj;
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	public Event getEventById(int id) {
		Event event = new Event();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL_SELECT_EVENT_BY_ID);
			pstmt.setInt(1, id);
			if (pstmt.execute()) {
				rs = pstmt.getResultSet();
				while (rs.next()) {
					event.setCreateTime(rs.getDate("create_time").getTime());
					String[] tmpStrs;
					tmpStrs = rs.getString("keywords").split(delimiter);
					HashMap<String, Double> map = new HashMap<String, Double>();
					for (String str1 : tmpStrs) {
						String[] str2 = str1.split(":");
						if (str2.length == 2)
							map.put(str2[0], Double.valueOf(str2[1]));
						else
							map.put(str2[0], 0.0);
					}
					event.setKeyWords(map);
					tmpStrs = rs.getString("pages").split(delimiter);
					List<Integer> pages = new LinkedList<Integer>();
					for (String str1 : tmpStrs) {
						pages.add(Integer.valueOf(str1));
					}
					event.setPages(pages);
					event.setTitle(rs.getString("title"));
					event.setId(rs.getInt("id"));
					logger.info("i found the event in database.");
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
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return event;
	}

	// 从数据库中读出所有的event，以供查询扩展使用-by shiyulong

	/**
	 * @return ArrayList<Event>,该功能实现从数据库中读出所有事件以及对应的所有相关信息，并放在一张列表中返回。
	 *         返回形式如：{id
	 *         :1,title:"微软发布win8平板surface"，recommended：0.8，keyWords:{win8
	 *         :0.75,surface:0.6},pages:{1,2,3,4}, createTime:2012-12-23
	 *         00:00:00,desc:
	 *         "微软CEO10月23号在上海发布新一代操作系统windows8，并发布了其第一款采用新系统的平板电脑surface"}
	 */
	public ArrayList<Event> getAllEvent() {
		ArrayList<Event> allEvent = new ArrayList<Event>();
		Statement stmt = null;
		ResultSet rs = null;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(SQL_SELECT_EVENT_ALL);
			while (rs.next()) {
				Event event = new Event();
				event.setCreateTime(rs.getDate("create_time").getTime());
				String[] tmpStrs;
				tmpStrs = rs.getString("keywords").split(delimiter);
				HashMap<String, Double> map = new HashMap<String, Double>();
				for (String str1 : tmpStrs) {
					String[] str2 = str1.split(":");
					if (str2.length == 2)
						map.put(str2[0], Double.valueOf(str2[1]));
					else
						map.put(str2[0], 0.0);
				}
				event.setKeyWords(map);
				tmpStrs = rs.getString("pages").split(delimiter);
				List<Integer> pages = new LinkedList<Integer>();
				for (String str1 : tmpStrs) {
					pages.add(Integer.valueOf(str1));
				}
				event.setPages(pages);
				event.setTitle(rs.getString("title"));
				event.setId(rs.getInt("id"));
				logger.info("i found the event in database.");
				allEvent.add(event);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stmt != null)
					stmt.close();
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return allEvent;
	}

	// 以jsonArray格式返回所有event

	public JSONObject getEventRanking() {
		JSONObject result = getEventRankingFromDB();
		return result;
	}

	/**
	 * 从数据库里面读出按照recommeded排序的event，并且将id和排序号放入jsonObj
	 * 
	 * @return <ranking, event-id>...
	 */
	public JSONObject getEventRankingFromDB() {
		JSONObject jsonObj = new JSONObject();

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL_SELECT_EVENT_ORDERED);
			if (pstmt.execute()) {
				rs = pstmt.getResultSet();
				int i = 0;
				while (rs.next()) {
					int id = rs.getInt("id");
					jsonObj.put(i, id);
					i++;
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
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return jsonObj;
	}

	/**
	 * 将数据入库或者更新，被入库的数据自动添加id
	 */
	public void saveORupdateEvents(HashSet<Event> events) {
		/** 根据id是否为-1将所有数据分为两块一个为要更新的，一个为要插入的 **/
		HashSet<Event> toUpdate = new HashSet<Event>();
		HashSet<Event> toInsert = new HashSet<Event>();
		Iterator<Event> it = events.iterator();
		while (it.hasNext()) {
			Event e = it.next();
			if (e.id == -1)
				toInsert.add(e);
			else
				toUpdate.add(e);
		}
		updateEvents(toUpdate);
		insertEvents(toInsert);
	}

	final String SQL_INSERT_EVENT = "insert into event(title,recommended,img,keywords,pages,create_time,started_location,content_abstract) values(?,?,?,?,?,?,?,?)";

	/**
	 * 将event插入
	 */
	public void insertEvents(HashSet<Event> events) {
		if (events == null || events.isEmpty())
			return;

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL_INSERT_EVENT,
					Statement.RETURN_GENERATED_KEYS);
			Iterator<Event> it = events.iterator();
			Event e;
			while (it.hasNext()) {
				e = it.next();
				try {
					pstmt.setString(1, e.getUTF8Title());
					pstmt.setDouble(2, e.recommended);
					pstmt.setString(3, e.getImg());
					pstmt.setString(4, e.getKeyWordsStr());
					pstmt.setString(5, e.getPagesStr());
					pstmt.setDate(6, new Date(e.createTime));
					pstmt.setString(7, e.getStartedLocation());
					pstmt.setString(8, e.getDesc());
					pstmt.executeUpdate();
					rs = pstmt.getGeneratedKeys();
					if (rs.next()) {
						e.id = rs.getInt(1);
					}
				} catch (SQLException sql) {
					sql.printStackTrace();
					continue;
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
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	final String SQL_EVENT_UPDATE = "update event set ";

	/**
	 * 更新数据库里面的event
	 */
	public void updateEvents(HashSet<Event> events) {
		if (events == null || events.isEmpty())
			return;

		Statement stmt = null;
		ResultSet rs = null;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			stmt = conn.createStatement();
			Iterator<Event> it = events.iterator();
			while (it.hasNext()) {
				String sql = SQL_EVENT_UPDATE + it.next().toSQLString();
				stmt.execute(sql);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stmt != null)
					stmt.close();
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	// 下面几个函数是管理员对event的修改操作，包括修改标题、摘要、关键词、地点，添加、删除新闻，添加图片等；
	final String SQL_UPDATE_TITLE_BY_ID = "update event set title=? where id = ?";

	/**
	 * 修改标题
	 * 
	 * @param id
	 * @param title
	 */
	public void updateTitle(int id, String title) {
		PreparedStatement pstmt = null;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL_UPDATE_TITLE_BY_ID);
			pstmt.setString(1, title);
			pstmt.setInt(2, id);
			pstmt.execute();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

	final String SQL_UPDATE_LOCATION_BY_ID = "update event set started_location=? where id = ?";

	/**
	 * 修改标题
	 * 
	 * @param id
	 * @param location
	 */
	public void updateLocation(int id, String location) {
		PreparedStatement pstmt = null;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL_UPDATE_LOCATION_BY_ID);
			pstmt.setString(1, location);
			pstmt.setInt(2, id);
			pstmt.execute();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

	final String SQL_UPDATE_SUMMARY_BY_ID = "update event set content_abstract=? where id = ?";

	/**
	 * 修改摘要
	 * 
	 * @param id
	 * @param summary
	 */
	public void updateSummary(int id, String summary) {
		PreparedStatement pstmt = null;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL_UPDATE_SUMMARY_BY_ID);
			pstmt.setString(1, summary);
			pstmt.setInt(2, id);
			pstmt.execute();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

	final String SQL_SELECT_NEWS_BY_ID = "select pages from event where id = ?";
	final String SQL_ADD_NEWS_BY_ID = "update event set pages=? where id = ?";

	/**
	 * 添加新闻
	 * 
	 * @param id
	 * @param newsId
	 */
	public void addNews(int id, String newsId) {
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
			ArrayList<String> pagesList = new ArrayList<String>();
			String[] allPages = pages.split(";");
			String[] newsList = newsId.split(";");
			for (String page : allPages) {
				pagesList.add(page);
			}
			for (String news : newsList) {
				if (!pagesList.contains(news))
					pages += news + ";";
			}
			pstmt = conn.prepareStatement(SQL_ADD_NEWS_BY_ID);
			pstmt.setInt(2, id);
			pstmt.setString(1, pages);
			pstmt.execute();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

	final String SQL_SELECT_KEYWORDS_BY_ID = "select keywords from event where id = ?";
	final String SQL_ADD_KEYWORDS_BY_ID = "update event set keywords=? where id = ?";

	/**
	 * 添加新闻
	 * 
	 * @param id
	 * @param keywords
	 */
	public void addKeywords(int id, String keywords) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String oldKeywords = null;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL_SELECT_KEYWORDS_BY_ID);
			pstmt.setInt(1, id);
			pstmt.execute();
			rs = pstmt.getResultSet();
			if (rs.next()) {
				oldKeywords = rs.getString("keywords");
			}
			ArrayList<String> keywordsList = new ArrayList<String>();
			String[] oldKeywordsList = oldKeywords.split(";");
			String[] newKeywordsList = keywords.split(";");
			for (String key : oldKeywordsList) {
				keywordsList.add(key);
			}
			for (String ss : newKeywordsList) {
				if (!keywordsList.contains(ss))
					oldKeywords += ss + ";";
			}
			oldKeywords += ";";
			pstmt = conn.prepareStatement(SQL_ADD_KEYWORDS_BY_ID);
			pstmt.setInt(2, id);
			pstmt.setString(1, oldKeywords);
			pstmt.execute();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

	public static void main(String[] args) {
		EventDAO dao = new EventDAO();
		// Event e = new Event();
		// e.setCreateTime(Calendar.getInstance().getTime().getTime());
		// e.setTitle("q231");
		// e.setRecommended(1.0);
		// e.setId(10);
		// HashSet<Event> set = new HashSet<Event>();
		// set.add(e);
		// dao.updateEvents(set);
		// dao.updateTitle(1, "陕西榆林20辆超标公车被拍卖");
		dao.addKeywords(1, "韩国：1.0");

	}
}
