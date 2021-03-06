package gossip.gossip.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import net.sf.json.JSONArray;
import edu.bit.dlde.utils.DLDELogger;
import gossip.gossip.utils.DatabaseUtils;

/**
 * @author jiangxiaotian
 *
 */
public class RelatedNewsDAO {
	
	private DLDELogger logger;
	private DataSource dataSource;
	
	private final String SQL_getRelatedNewsIds =
			"select * from relatedNews where newsId1 = ? OR newsId2 = ? order by relevancyValue desc";
	private final String SQL_insertRelevancy = 
			"insert into relatedNews(newsId1,newsId2,relevancyValue) values(?,?,?)";
	private final String SQL_updateRelevancy = 
			"update relatedNews set relevancyValue = ? where newsId1 = ? AND newsId2 = ?";
	private final String SQL_deleteRelevancySingle = 
			"delete from relatedNews where newsId1 = ? AND newsId2 = ?";
	private final String SQL_deleteRelevancy = 
			"delete from relatedNews where newsId1 = ? OR newsId2 = ?";
	private final String SQL_deleteRelevancyAll = "delete from relatedNews";
	
	/**	本地数据库初始化	**/
	public void init(){
		if(logger ==null)
			logger = new DLDELogger();
		if(dataSource == null){
			dataSource = DatabaseUtils.getInstance();
		}
	}
	
	/**
	 * 由news的Id得到相关news的Ids
	 * @param newsId 所要查询相关新闻的新闻的Id
	 * @param limit 至多返回limit个相关度最高的新闻
	 * @return 以JSON形式返回相关新闻的Id，格式为：
	 [{</br>
		        "id":123,//新聞id</br>
		        "title":"xxx",//新聞標題</br>
		        "desc":"",//新聞描述</br>
		        "author":"",//新聞作者</br>
		        "body":"",//新聞正文</br>
		        "publish_at":"",//發佈時間</br>
		        "source":"",//新聞來源</br>
		        "started_location":"",//新聞地點</br>
		        "keywords":["xx","yy"]//新聞關鍵詞</br>
				},...]</br>
	 */
	public JSONArray getRelatedNewsIds(int newsId){
		JSONArray relatedNewsIdsArray = new JSONArray();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet result = null;
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL_getRelatedNewsIds);
			pstmt.setInt(1, newsId); 
			pstmt.setInt(2, newsId); 
			pstmt.executeQuery();
			result = pstmt.getResultSet();
			while (result.next())
				if(result.getInt("newsId2")==newsId)
					relatedNewsIdsArray.add(result.getInt("newsId1"));
				else
					relatedNewsIdsArray.add(result.getInt("newsId2"));
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (result != null)
					result.close();
				if (pstmt != null)
					pstmt.close();
				if(conn!=null){
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return relatedNewsIdsArray;
	}
		
	/**
	 * 插入相关度值
	 * @param newsId1 新闻1的Id
	 * @param newsId2 新闻2的Id
	 * @param relevancyValue 两新闻间的相关度值
	 * @return 是否插入成功
	 */
	public Boolean insertRelevancy(int[] newsId1Array,int[] newsId2Array,double[] relevancyValueArray){
		//check
		for(int i=0;i<newsId1Array.length;i++){
			if(relevancyValueArray[i]>1||relevancyValueArray[i]<=0)//relevancyValueArray不在(0,1]内，则返回错误
				return false;
			if(newsId1Array[i]<newsId2Array[i]) //newsId1要>newsId2
			{
				int tmp = newsId1Array[i];
				newsId1Array[i] = newsId2Array[i];
				newsId2Array[i] = tmp;
			}
		}
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		Boolean isSucceed = true;
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL_insertRelevancy);
			for(int i=0;i<newsId1Array.length;i++)
				if(relevancyValueArray[i]!=1)///newsId1=newsId2,正确的value肯定等于1，无视掉
				{
					pstmt.setInt(1, newsId1Array[i]);
					pstmt.setInt(2, newsId2Array[i]);
					pstmt.setDouble(3, relevancyValueArray[i]);
					pstmt.addBatch();
				}
			int[] resultArray = pstmt.executeBatch();
			for(int i=0;i<resultArray.length;i++)
				if(resultArray[i]!=1)
					isSucceed = false;
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
		return isSucceed;
	}
	
	
	/**
	 * 更新相关度值,每个(newsId1[n],newsId2[n],relevancyValue[n])对应一条update数据
	 * @param newsId1 新闻1的Id
	 * @param newsId2 新闻2的Id
	 * @param relevancyValue 两新闻间的相关度值
	 * @return 是否更新成功
	 */
	public Boolean updateRelevancy(int[] newsId1Array,int[] newsId2Array,double[] relevancyValueArray){
		//check
		for(int i=0;i<newsId1Array.length;i++){
			if(relevancyValueArray[i]>1||relevancyValueArray[i]<=0)//relevancyValueArray不在(0,1]内，则返回错误
				return false;
			if(newsId1Array[i]<newsId2Array[i]) //newsId1要>newsId2
			{
				int tmp = newsId1Array[i];
				newsId1Array[i] = newsId2Array[i];
				newsId2Array[i] = tmp;
			}
		}

		Connection conn = null;
		PreparedStatement pstmt = null;
		Boolean isSucceed = true;
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL_updateRelevancy);
			for(int i=0;i<newsId1Array.length;i++)
				if(relevancyValueArray[i]!=1)///newsId1=newsId2,正确的value肯定等于1，无视掉
				{
					pstmt.setDouble(1, relevancyValueArray[i]);
					pstmt.setInt(2, newsId1Array[i]);
					pstmt.setInt(3, newsId2Array[i]);
					pstmt.addBatch();
				}
			int[] resultArray = pstmt.executeBatch();
			for(int i=0;i<resultArray.length;i++)
				if(resultArray[i]!=1)
					isSucceed = false;
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
		return isSucceed;
	}
	
	/**
	 * 删除单一相关度
	 * @param newsId1 新闻1的Id
	 * @param newsId2 新闻2的Id
	 * @return 是否删除成功
	 */
	 public Boolean deleteRelevancySingle(int newsId1, int newsId2){
	  if(newsId1<newsId2){//newsId1要>newsId2
			 int tmp = newsId2;
			 newsId2 = newsId1;
			 newsId1 = tmp;
		 }
		 
		Connection conn = null;
		PreparedStatement pstmt = null;
		Boolean isSucceed = false;
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL_deleteRelevancySingle);
			pstmt.setInt(1, newsId1);
			pstmt.setInt(2, newsId2);
			if(pstmt.executeUpdate() == 1)
				isSucceed = true;
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
		return isSucceed;
	}
	
	/**
	 * 删除与某个news相关的所有相关度值
	 * @param newsId 新闻的Id
	 * @return 删除的行数，-1表示错误。
	 */
	public int deleteRelevancy(int newsId){
		Connection conn = null;
		PreparedStatement pstmt = null;
		int rowDeleted = -1;
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL_deleteRelevancy);
			pstmt.setInt(1, newsId);
			pstmt.setInt(2, newsId);
			rowDeleted = pstmt.executeUpdate();
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
		return rowDeleted;
	}
	
	/**
	 * 删除与表中所有相关度值
	 * @return 删除的行数，-1表示错误。
	 */
	public int deleteRelevancyAll(){
		Connection conn = null;
		PreparedStatement pstmt = null;
		int rowDeleted = -1;
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL_deleteRelevancyAll);
			rowDeleted = pstmt.executeUpdate();
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
		return rowDeleted;
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
	
}
