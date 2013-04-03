package gossip.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import net.sf.json.JSONArray;
import edu.bit.dlde.utils.DLDELogger;
import gossip.utils.DatabaseUtils;

/**
 * 
 * @author jiangxiaotian
 */
public class RelatedEventDAO {
	
	private DLDELogger logger;
	private DataSource dataSource;
	
	private final String SQL_getRelatedEventIds =
			"select * from relatedEvent where eventId1 = ? OR eventId2 = ? order by relevancyValue desc";
	private final String SQL_insertRelevancy = 
			"insert into relatedEvent(eventId1,eventId2,relevancyValue) values(?,?,?)";
	private final String SQL_updateRelevancy = 
			"update relatedEvent set relevancyValue = ? where eventId1 = ? AND eventId2 = ?";
	private final String SQL_deleteRelevancySingle = 
			"delete from relatedEvent where eventId1 = ? AND eventId2 = ?";
	private final String SQL_deleteRelevancy = 
			"delete from relatedEvent where eventId1 = ? OR eventId2 = ?";
	private final String SQL_deleteRelevancyAll = "delete from relatedEvent";
	
	
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
	 * 由给定事件的Id获得相关事件的Ids，使用表：relatedEvents
	 * @param eventId 事件Id
	 * @param limit 至多返回limit个相关度最高的事件
	 * @return 返回一个由JSONArray表示的相关新闻列表
	 */
	public JSONArray getRelatedEventIds(int eventId){
		JSONArray relatedEventIdsArray = new JSONArray();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet result = null;
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL_getRelatedEventIds);
			pstmt.setInt(1, eventId); 
			pstmt.setInt(2, eventId); 
			pstmt.executeQuery();
			result = pstmt.getResultSet();
			while (result.next())
				if(result.getInt("eventId2")==eventId)
					relatedEventIdsArray.add(result.getInt("eventId1"));
				else
					relatedEventIdsArray.add(result.getInt("eventId2"));
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
		return relatedEventIdsArray;
	}
	
	/**
	 * 插入相关度值
	 * @param eventId1 事件1的Id
	 * @param eventId2 事件2的Id
	 * @param relevancyValue 两事件间的相关度值
	 * @return 是否插入成功
	 */
	public Boolean insertRelevancy(int[] eventId1Array,int[] eventId2Array,double[] relevancyValueArray){
		//check
		for(int i=0;i<eventId1Array.length;i++){
			if(relevancyValueArray[i]>1||relevancyValueArray[i]<=0)//relevancyValueArray不在(0,1]内，则返回错误
				return false;
			if(eventId1Array[i]<eventId2Array[i]) //eventId1要>eventId2
			{
				int tmp = eventId1Array[i];
				eventId1Array[i] = eventId2Array[i];
				eventId2Array[i] = tmp;
			}
		}
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		Boolean isSucceed = true;
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL_insertRelevancy);
			for(int i=0;i<eventId1Array.length;i++)
				if(relevancyValueArray[i]!=1)///eventId1=eventId2,正确的value肯定等于1，无视掉
				{
					pstmt.setInt(1, eventId1Array[i]);
					pstmt.setInt(2, eventId2Array[i]);
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
	 * 更新相关度值,每个(eventId1[n],eventId2[n],relevancyValue[n])对应一条update数据
	 * @param eventId1 新闻1的Id
	 * @param eventId2 新闻2的Id
	 * @param relevancyValue 两新闻间的相关度值
	 * @return 是否更新成功
	 */
	public Boolean updateRelevancy(int[] eventId1Array,int[] eventId2Array,double[] relevancyValueArray){
		//check
		for(int i=0;i<eventId1Array.length;i++){
			if(relevancyValueArray[i]>1||relevancyValueArray[i]<=0)//relevancyValueArray不在(0,1]内，则返回错误
				return false;
			if(eventId1Array[i]<eventId2Array[i]) //eventId1要>eventId2
			{
				int tmp = eventId1Array[i];
				eventId1Array[i] = eventId2Array[i];
				eventId2Array[i] = tmp;
			}
		}

		Connection conn = null;
		PreparedStatement pstmt = null;
		Boolean isSucceed = true;
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL_updateRelevancy);
			for(int i=0;i<eventId1Array.length;i++)
				if(relevancyValueArray[i]!=1)///eventId1=eventId2,正确的value肯定等于1，无视掉
				{
					pstmt.setDouble(1, relevancyValueArray[i]);
					pstmt.setInt(2, eventId1Array[i]);
					pstmt.setInt(3, eventId2Array[i]);
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
	 * @param eventId1 新闻1的Id
	 * @param eventId2 新闻2的Id
	 * @return 是否删除成功
	 */
	 public Boolean deleteRelevancySingle(int eventId1, int eventId2){
	  if(eventId1<eventId2){ //eventId1要>eventId2 
			 int tmp = eventId2;
			 eventId2 = eventId1;
			 eventId1 = tmp;
		 }
		 
		Connection conn = null;
		PreparedStatement pstmt = null;
		Boolean isSucceed = false;
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL_deleteRelevancySingle);
			pstmt.setInt(1, eventId1);
			pstmt.setInt(2, eventId2);
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
	 * 删除与某个Event相关的所有相关度值
	 * @param eventId 事件的Id
	 * @return 删除的行数，-1表示错误。
	 */
	public int deleteRelevancy(int eventId){
		Connection conn = null;
		PreparedStatement pstmt = null;
		int rowDeleted = -1;
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL_deleteRelevancy);
			pstmt.setInt(1, eventId);
			pstmt.setInt(2, eventId);
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
