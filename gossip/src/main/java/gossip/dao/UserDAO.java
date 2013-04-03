package gossip.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import javax.sql.DataSource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import edu.bit.dlde.utils.DLDELogger;
import gossip.utils.DatabaseUtils;

/**
 * @author jiangxiaotian
 *
 */
public class UserDAO {

	private DLDELogger logger;
	private DataSource dataSource;
	
	private final String SQL_getUserLoginInfo = "select name,role,last_login_time,last_login_ip from userInfo where newsId = ? AND password = ?";
	private final String SQL_insertRegistationInfo = "insert into userInfo(username,password,email) values(?,?,?)";
	private final String SQL_verifyString  = "select count(*) from userInfo where ? = ?";
	private final String SQL_storeClicklog = "insert into clicklog(username,clickTime,userQuery,id) values(?,?,?,?)";
	private final String SQL_storeComment = "insert into comments(username,commentTime,id,commentText) values(?,?,?,?)";
	
	public static final String USER = "user";
	public static final String EMAIL = "email";
	
	
	/**
	 * 本地数据库初始化
	 */
	public void init(){
		if(logger ==null)
			logger = new DLDELogger();
		if(dataSource == null){
			dataSource = DatabaseUtils.getInstance();
		}
	}
	
	/**
	 * 用户登录
	 * @param username 用户名
	 * @param password 用户密码
	 * @return 以JSON回登录结果，格式为：
	 * 成功：{"status":true,"info":{"name":"","role":"","last_login_time":"","last_login_ip":""}};
	 * 失败：{"status":false}
	 */
	public JSONObject login(String username, String password){
		JSONObject userInfo = new JSONObject();
		Boolean loginState = false;  
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet result = null;
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL_getUserLoginInfo);
			pstmt.setString(1, username);
			pstmt.setString(2, password);
			pstmt.executeQuery();
				result = pstmt.getResultSet();
				while (result.next()) {
					//取出来填充到userInfo中
					int columnsSize = result.getMetaData().getColumnCount();
					for(int i=1;i<=columnsSize;i++)
						userInfo.element(result.getMetaData().getColumnName(i+1), result.getObject(i));
					loginState = true;
				}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (result != null)
					result.close();
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		JSONObject resultObj =  new JSONObject();
		resultObj.element("status", loginState);
		resultObj.element("info", "");
		if(loginState)
			resultObj.put("info", userInfo);
		return resultObj;
	}
	
	/**
	 * 录入用户注册信息
	 * @param username 用户名
	 * @param password 用户密码
	 * @param email 用户email
	 * @return 是否录入成功
	 */
	public Boolean insertRegistationInfo(String username, String password,String email){
		Boolean resultState = false;  
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL_insertRegistationInfo);
			pstmt.setString(1, username);
			pstmt.setString(2, password);
			pstmt.setString(3, email);
			if (pstmt.executeUpdate()==1) {
				resultState=true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return resultState;
	}
	
	/**
	 * 验证用户属性的存在性
	 * @param name 需要验证的属性，格式如：USERDAO.EMAIL
	 * @param userPropertyValue 需要在数据库内验证是否存在的属性值
	 * @return 用户属性是否存在
	 */
	public Boolean verifyExistance(String type, String value){
		Boolean resultState = false;  
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet result = null;
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL_verifyString);
			pstmt.setString(1, type);
			pstmt.setString(2, value);
			result =  pstmt.executeQuery();
			resultState=(result.getInt(0)==0?true:false);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(result != null)
					result.close();
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return resultState;
	}	
	
	/**
	 * 存储用户点击日志
	 * @param username 用户名
	 * @param date 点击事件
	 * @param query 查询，有则写，无则填null
	 * @param id 用户点击的事件/新闻的id
	 */
	public void storeClicklog(String username,Date date, String query, int id){
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL_storeClicklog);
			pstmt.setString(1, username);
			pstmt.setDate(2, (java.sql.Date) date);
			pstmt.setString(3, query);
			pstmt.setInt(4, id);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 查询用户日志
	 * @param SQLString SQL查询语句
	 * @return JSONArray格式返回的点击日志，形式为：
	 * [{"username":"xxx","time":"2012-10-24 10:22:12","q":"北京","id":yyy},...]
	 */
	public JSONArray getClicklog(String SQLString){
		JSONArray clicklogArray = new JSONArray();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet result = null;
		try {
			JSONObject obj = new JSONObject();
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQLString);
			pstmt.executeQuery();
			result = pstmt.getResultSet();
			while (result.next()){
				obj.element("username",result.getString(1));
				obj.element("time",result.getDate(2));
				obj.element("uqery",result.getString(3));
				obj.element("id",result.getInt(4));
				clicklogArray.add(obj);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (result != null)
					result.close();
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return clicklogArray;
	}
	
	/**
	 * 存储用户的评论
	 * @param username 用户名
	 * @param time 评论时间
	 * @param id 所评论的事件id
	 * @return 是否存储成功
	 */
	public Boolean storeComment(String username,Date time, int id, String commentText){
		Connection conn = null;
		PreparedStatement pstmt = null;
		Boolean isSucceed = false;
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL_storeComment);
			pstmt.setString(1, username);
			pstmt.setDate(2, (java.sql.Date) time);
			pstmt.setInt(3, id);
			pstmt.setString(4, commentText);
			if(pstmt.executeUpdate()==1)
				isSucceed = true;
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return isSucceed;
	}
	
	/**
	 * 查询用户评论
	 * @param selectSent SQL查询语句
	 * @return JSONArray格式返回的用户评论，形式为：
	 * [{"username":"xxx","time":"2012-10-24 10:22:12","id":yyy,"comment":"mmmmmmmm"},...]
	 */
	public JSONArray getComment(String selectSent){
		JSONArray clicklogArray = new JSONArray();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet result = null;
		try {
			JSONObject obj = new JSONObject();
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(selectSent);
			pstmt.executeQuery();
			result = pstmt.getResultSet();
			while (result.next()){
				obj.element("username",result.getString(1));
				obj.element("time",result.getDate(2));
				obj.element("id",result.getInt(3));
				obj.element("comment",result.getString(4));
				clicklogArray.add(obj);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (result != null)
					result.close();
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return clicklogArray;
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
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
	}

}
