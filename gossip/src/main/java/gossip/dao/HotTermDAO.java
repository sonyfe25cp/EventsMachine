package gossip.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;

import net.sf.json.JSONArray;

import edu.bit.dlde.utils.DLDELogger;
import gossip.utils.DatabaseUtils;

/**
 * 
 * @author jiangxiaotian
 */
public class HotTermDAO {
	
	private DLDELogger logger;
	private DataSource dataSource;
	
	public static final String HOTKEYWORDS ="hotKeywords"; 
	public static final String HOTPEOPLE ="hotPeople";
	public static final String HOTLOCATIONS ="hotLocations";
	
	
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
	 * 取得hotTerm，type需填写HotTermDAO中的几种类型：如keywords,people,locations
	 *@param type HotTermDAO中出现过的几种类型：如keywords,people,locations
	 *@param limit 返回的top数量限制
	 *@return 所需的hotTerm数组
	 */
	public JSONArray getHotTerms(String type,int limit){
		String sqlSent = "select termName from "+ type +" order by hotValue desc limit ?"; 
		JSONArray hotResultArray = new JSONArray();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet result = null;
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(sqlSent);
			pstmt.setInt(1, limit);
			pstmt.executeQuery();
			result = pstmt.getResultSet();
			while (result.next()) 
				hotResultArray.add(result.getString(1));
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (result != null)
					result.close();
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return hotResultArray;
	}
	
	/**
	 * 更新热度值，(termArray[i],valueArray[i])构成一组新数据
	 * @param type 类型，如HotTermDAO.HOTKEYWORDS等...
	 * @param termArray 要被更新的term的数组，term可以是keyword,people或location的值
	 * @param value term的新值数组
	 * @return 是否更新成功
	 */
	public Boolean updateHotValue(String type ,String[] termArray ,double[] valueArray){
		if(termArray.length!=valueArray.length)
			return false;
		String sqlSent = "update " + type + " set hotValue = ? where termName = ?";
		Connection conn = null;
		PreparedStatement pstmt = null;
		Boolean isSucceed = true;
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(sqlSent);
			for(int i=0;i<termArray.length;i++)
			{
				pstmt.setDouble(1, valueArray[i]);
				pstmt.setString(2, termArray[i]);
				pstmt.addBatch();
			}
		int[] resultArray = pstmt.executeBatch();
		for(int i=0;i<resultArray.length;i++)
			if(resultArray[i]!= 1)
				isSucceed = false;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return isSucceed;
	}
	
	/**
	 * 删除hotTerm
	 * @param type 类型，如HotTermDAO.HOTKEYWORDS等...
	 * @param term 要被更新的term，term可以使keyword,people或location
	 * @return 是否删除成功
	 */
	public Boolean removeHotTerm(String type ,String term){
		String sqlSent = "delete from " + type + " where termName = ?";
		Connection conn = null;
		PreparedStatement pstmt = null;
		Boolean isSucceed = false;
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(sqlSent);
			pstmt.setString(1, term);
			if(pstmt.executeUpdate()== 1)
				isSucceed = true;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return isSucceed;
	}
	
	/**
	 * 更新HotTerm，(termArray[i],valueArray[i])构成一组欲插入的新数据
	 * @param type 类型，如HotTermDAO.HOTKEYWORDS等...
	 * @param termArray 要被插入的term的数组，term可以是keyword,people或location的值
	 * @param value term的值数组
	 * @return 是否插入成功
	 */
	public Boolean insertHotTerm(String type, String[] termArray, double[] valueArray){
		if(termArray.length!=valueArray.length)
			return false;
		String sqlSent = "insert into " + type + "(termName,hotValue) values(?,?)";
		Connection conn = null;
		PreparedStatement pstmt = null;
		Boolean isSucceed = true;
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(sqlSent);
			for(int i=0;i<termArray.length;i++)
			{
				pstmt.setString(1, termArray[i]);
				pstmt.setDouble(2, valueArray[i]);
				pstmt.addBatch();
			}
			int[] resultArray = pstmt.executeBatch();
			for(int i=0;i<resultArray.length;i++)
				if(resultArray[i]!= 1)
					isSucceed = false;
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return isSucceed;
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
		//测试完成:dao.insertHotTerm(HOTKEYWORDS,a ,b);
		//测试完成:dao.getHotTerms(HOTKEYWORDS, 5)
		//测试完成:dao.updateHotValue(HOTKEYWORDS, a, b)
		//测试完成:dao.removeHotTerm(HOTKEYWORDS, "蛋疼")
		//完毕
		HotTermDAO dao = new HotTermDAO();
		dao.init();
		String []a = {"喵喵","汪汪"};
		double []b = {0.31,0.32};
		//dao.insertHotTerm(HOTKEYWORDS,a ,b);
		//System.out.println(dao.getHotTerms(HOTKEYWORDS, 5));
		System.out.println(dao.removeHotTerm(HOTKEYWORDS, "蛋疼"));
	}

}
