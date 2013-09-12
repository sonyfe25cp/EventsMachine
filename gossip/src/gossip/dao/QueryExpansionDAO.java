package gossip.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.sql.DataSource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import edu.bit.dlde.utils.DLDELogger;
import gossip.event.Event;
import gossip.model.News;
import gossip.queryExpansion.Expansion;
import gossip.utils.DatabaseUtils;

/**
 * @author shiyulong
 *
 */


/**
 * 该类实现查询扩展的读取和存储操作
 *
 */

public class QueryExpansionDAO {
	
	private DLDELogger logger;
	private DataSource dataSource;
	final String SQL_INSERT_EXPANSION = "insert into expansion(keyWords,eventsId,newsId,expansionTerms) values(?,?,?,?)";
	final String SQL_SELECT_EXPANSION_BY_KEYWORDS = "select * from expansion  where keyWords = ?";
	final String SQL_UPDATE_EXPANSION="update expansion set eventsId = ? , newsId = ? , expansionTerms = ? where keyWords = ?";
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
	
	/**	初始化	**/
	public void init(){
		if(logger ==null)
			logger = new DLDELogger();
		if(dataSource == null){
			dataSource = DatabaseUtils.getInstance();
		}
	}
	
	/**
	 * 查询query对应的扩展事件，扩展新闻，扩展关键词，并返回结果
	 * @param query 
	 * @return 返回一个由JSONArray表示的相关新闻列表,格式如下：
	 * {"q":{q},
	 *  "expan":{
	 * 		"events":[{"id":"","desc":"","title":""},{"id":"","desc":"","title":""}...],
	 * 		"news":[{"id":"","desc":"","title":""},{"id":"","desc":"","title":""}...],
	 * 		"keywords":["",""...]}	}
	 */
	public JSONObject getExpansion(String query){
		JSONObject jsonObj = new JSONObject();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try{
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL_SELECT_EXPANSION_BY_KEYWORDS);
			pstmt.setString(1, query);
			if (pstmt.execute()) {
				rs = pstmt.getResultSet();
				JSONObject expan=new JSONObject();
				while (rs.next()) {
					//将查询结果转化为json格式
					//获得所有的events
					JSONArray eventsArray=new JSONArray();
					if(rs.getString("eventsId")!=null){
						String eventsIdString[]=rs.getString("eventsId").split(",");
						int[] eventsId=new int[eventsIdString.length];
						for(int i=0;i<eventsIdString.length;i++){
							eventsId[i]=Integer.parseInt(eventsIdString[i]); 
							EventDAO eventDao=new EventDAO();
							Event event=eventDao.getEventById(eventsId[i]);//根据event的id进行查询，并且获得相关属性
							JSONObject eventObj=new JSONObject();
							eventObj.put("id",eventsId[i] );
							eventObj.put("desc",event.getDesc() );
							eventObj.put("title",event.getTitle() );
							eventsArray.add(eventObj);
						}//for
					}
					
					//获得所有的news
					JSONArray newsArray=new JSONArray();
					if(rs.getString("newsId")!=null){
						String newsIdString[]=rs.getString("newsId").split(",");
						int[] newsId=new int[newsIdString.length];
						for(int i=0;i<newsIdString.length;i++){
							newsId[i]=Integer.parseInt(newsIdString[i]); 
							NewsDAO newsDao=new NewsDAO();
							News news=newsDao.getNewsById(newsId[i]);
							JSONObject newsObj=new JSONObject();
							newsObj.put("id",newsId[i] );
							newsObj.put("desc", news.getDescription());
							newsObj.put("title",news.getTitle() );
							newsArray.add(newsObj);
						}//for
					}
					
					//获得所有的扩展词
					String[] expansionTerms=rs.getString("expansionTerms").split(",");
					JSONArray expansionArray=new JSONArray();
					for(String expansion:expansionTerms){
						expansionArray.add(expansion);
					}//for
					expan.put("events", eventsArray);
					expan.put("news", newsArray);
					expan.put("keywords", expansionArray);
				}//while
				jsonObj.put("q", query);
				jsonObj.put("expan", expan);
			}//try
				
		}catch (SQLException e) {
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
	
		
		return jsonObj;
	}
	
	/**
	 * @param String keywords
	 * @return Term term
	 * 根据查询关键词查找相关的查询扩展信息
	 * 返回形式：term:{keyWords:"",eventId:{1,2,3,……},newsId:{1,2,3,……}，expansionTerm:{"","","",……}}
	 */
	public Expansion getExpansionByKeywords(String keywords){
		Expansion expansion=new Expansion();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try{
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL_SELECT_EXPANSION_BY_KEYWORDS);
			pstmt.setString(1, keywords);
			if (pstmt.execute()) {
				rs = pstmt.getResultSet();
				if (rs.next()) {
					expansion.setTerm(keywords);
					if(rs.getString("eventsId")!=null){
						String eventsIdString[]=rs.getString("eventsId").split(",");
						int[] eventsId=new int[eventsIdString.length];
						for(int i=0;i<eventsIdString.length;i++){
							eventsId[i]=Integer.parseInt(eventsIdString[i]); 
							expansion.addEventId(eventsId[i]);
						}//for
					}
					
					//获得所有的news
					String newsIdString[]=rs.getString("newsId").split(",");
					int[] newsId=new int[newsIdString.length];
					for(int i=0;i<newsIdString.length;i++){
						newsId[i]=Integer.parseInt(newsIdString[i]); 
						expansion.addNewsId(newsId[i]);
					}//for
					//获得所有的扩展词
					String[] expansionTerms=rs.getString("expansionTerms").split(",");
					for(String expansionTerm:expansionTerms){
						expansion.addExpansionTerms(expansionTerm);
					}

				}//if
				else
				{
					expansion=null;
				}//如果数据库中不存在该扩展，则返回空
				
			}//try
				
		}catch (SQLException e) {
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
		return expansion;
	}
	
	/**
	 * @param Term
	 * 将单个的扩展存入数据库
	 *
	 */
	public void insertExpansion(Expansion expansion){
		if(expansion==null)
			return;
		//如果数据库中已存在，则只进行更新操作
		if(getExpansionByKeywords(expansion.getTerm())!=null)
			updateExpansion(expansion);
		//若不存在，则进行插入操作
		else{
			Connection conn = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try {
				conn = dataSource.getConnection();
				pstmt = conn.prepareStatement(SQL_INSERT_EXPANSION,Statement.RETURN_GENERATED_KEYS);
				pstmt.setString(1, expansion.getTerm());
				/*将与查询词相关的事件id以字符串的形式存储，中间用‘，’隔开，以防止过度冗余，存储形式如"1,2,3",说明与该词相关的事件是事件1、事件2和事件3，
				 * 并且对于事件只存id号，如果需要其他详细信息，可以根据id号进行进一步的查询 */
				String EventsIdString=intArrayToString(expansion.getEventId());
				pstmt.setString(2, EventsIdString);
				
				/*对于新闻的存储方法同事件的存储方式*/
				String NewsIdString=intArrayToString(expansion.getNewsId());
				pstmt.setString(3, NewsIdString);
				
				String ExpansionTermString=stringArrayToString(expansion.getExpansionTerms());
				pstmt.setString(4, ExpansionTermString);
				
				pstmt.executeUpdate();
				
			}catch (SQLException e) {
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
			}//finally
		}//else
		
	}
	
	/**
	 * @param ArrayList<Term> terms
	 * 该方法是对查询扩展的存储过程，在对每一个词计算出对应的查询扩展以后，将其以一定的形式存入数据库中，以方便客户端的查询请求
	 *
	 */	
	public void insertExpansion(ArrayList<Expansion> expansions) {
		if(expansions.isEmpty()||expansions==null){
			System.out.println("terms is null");
			return;
		}
		else{
			Connection conn = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try {
				conn = dataSource.getConnection();
				pstmt = conn.prepareStatement(SQL_INSERT_EXPANSION,Statement.RETURN_GENERATED_KEYS);
				for(Expansion expansion:expansions){
					//如果数据库中已存在，则只进行更新操作
					if(getExpansionByKeywords(expansion.getTerm())!=null)
						updateExpansion(expansion);
					//若不存在，则进行插入操作
					else{
						pstmt.setString(1, expansion.getTerm());
						
						/*将与查询词相关的事件id以字符串的形式存储，中间用‘，’隔开，以防止过度冗余，存储形式如"1,2,3",说明与该词相关的事件是事件1、事件2和事件3，
						 * 并且对于事件只存id号，如果需要其他详细信息，可以根据id号进行进一步的查询 */
						String EventsIdString=intArrayToString(expansion.getEventId());
						pstmt.setString(2, EventsIdString);
						
						/*对于新闻的存储方法同事件的存储方式*/
						String NewsIdString=intArrayToString(expansion.getNewsId());
						pstmt.setString(3, NewsIdString);
						
						String ExpansionTermString=stringArrayToString(expansion.getExpansionTerms());
						pstmt.setString(4, ExpansionTermString);
						
						pstmt.executeUpdate();
					}//else
					
				}//for
			
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
			}//finally
		}//else
	}
	
	/**
	 * @param Term term
	 * 此方法实现对数据库原有的扩展进行更新，即如果数据库中已经存在关于某个查询关键词的扩展，并且要插入的expansion的关键词与该词相同，则将插入操作改为更新操作
	 *
	 */	
	public void updateExpansion(Expansion expansion){
		Expansion existExpansion=getExpansionByKeywords(expansion.getTerm());
		//将数据库中已有的扩展和该扩展合并
		if(existExpansion.getEventId()==null||existExpansion.getEventId().isEmpty()){
			existExpansion.addEventId(expansion.getEventId());
			}
		else{
			for(int eid:expansion.getEventId()){
				if(!existExpansion.getEventId().contains(eid))
					existExpansion.addEventId(eid);
			}
		}
		
		for(int nid:expansion.getNewsId()){
			if(!existExpansion.getNewsId().contains(nid))
				existExpansion.addNewsId(nid);
		}
		for(String expansionTerm:expansion.getExpansionTerms()){
			if(!existExpansion.getExpansionTerms().contains(expansionTerm))
				existExpansion.addExpansionTerms(expansionTerm);
		}
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL_UPDATE_EXPANSION);
			pstmt.setString(1, intArrayToString(existExpansion.getEventId()));
			pstmt.setString(2, intArrayToString(existExpansion.getNewsId()));
			pstmt.setString(3, stringArrayToString(existExpansion.getExpansionTerms()));
			pstmt.setString(4, existExpansion.getTerm());
			pstmt.executeUpdate();
		}catch (SQLException e) {
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
		}//finally	
	}
	
	/**
	 * @param ArrayList<Integer>intArr
	 * 将一个int型的列表转化为一个字符串形式，中间用逗号分隔
	 *
	 */	
	public String intArrayToString(ArrayList<Integer>intArr){
		if(intArr.isEmpty()||intArr==null)
			return null;
		String s=null;
		for(int i=0;i<intArr.size();i++){
			if(i==0)
				s=intArr.get(0).toString();
			else
			{
				s=s+","+intArr.get(i).toString();
			}
		}//for
		return s;
		
	}
	
	/**
	 * @param ArrayList<String>stringArr
	 * 将一个字符串型的列表转化为一个字符串形式，中间用逗号分隔
	 *
	 */	
	public String stringArrayToString(ArrayList<String>stringArr){
		if(stringArr.isEmpty()||stringArr==null)
			return null;
		String s=null;
		for(int i=0;i<stringArr.size();i++){
			if(i==0)
				s=stringArr.get(0);
			else
			{
				s=s+","+stringArr.get(i);
			}
		}
		return s;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		QueryExpansionDAO qeDao=new QueryExpansionDAO();
		qeDao.getExpansionByKeywords("海南");
		System.out.println(qeDao.getExpansion("海南"));

	}

}
