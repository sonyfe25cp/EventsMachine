package gossip.gossip.queryExpansion;

import gossip.gossip.utils.DatabaseUtils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

public class TestSql {
	private static DataSource dataSource;
	
	
	public static void main(String[] args){
		dataSource = DatabaseUtils.getInstance();
		Connection conn = null;
		Statement stat = null;
		ResultSet rs = null;
		
		try {
			conn = dataSource.getConnection();
			ArrayList<Integer> idList = new ArrayList<Integer>();
			idList.add(9);
			idList.add(10);
			idList.add(11);
			idList.add(12);
			idList.add(13);
			String ids = "9,10,11,12,13";
			stat = conn.createStatement();
			String sql = "select sum(id) as cont from words where id in (" + ids +")";
			String sql2 = "select * from words where id in (";
			String sql3 = "select * from document";
//			for(int i=0;i<idList.size();i++){
//				if(i<idList.size()-1){
//					sql2 = sql2 + idList.get(i) + ",";
//				}
//				else
//					sql2 = sql2 + idList.get(i) + ")";
//			}
//			String sql1 = "select * from news";
//			rs = stat.executeQuery(sql);
//			System.out.println("read done!");
//			System.out.println(sql);
//			while(rs.next()){
//				//System.out.println(rs.getInt("id") + "   "  + rs.getString("name"));
//				System.out.println(rs.getInt("cont"));
//			}
//			rs = stat.executeQuery(sql2);
			rs = stat.executeQuery(sql3);
			System.out.println("read done!");
//			System.out.println(sql2);
			List<Document> documents = new ArrayList<Document>();
			int i = 0;
			while(rs.next()){
//				System.out.println(rs.getInt("id") + "   "  + rs.getString("name"));
				//System.out.println(rs.getInt("cont"));
				if(i%2000==0){
					System.out.println(i);
				}
				Document doc = new Document();
				doc.setId(rs.getInt("id"));
				doc.setTotalWordsCount(rs.getInt("totalwordscount"));
				doc.setTitleWordsCount(rs.getInt("titlewordscount"));
				//System.out.println(rs.getInt("bodywordscount"));
				if(rs.getInt("bodywordscount")>=400){
					i++;
					continue;
				}
				doc.setBodyWordsCount(rs.getInt("bodywordscount"));
				doc.setBodyWords(rs.getString("bodywords"));
				doc.setTitleWords(rs.getString("titlewords"));
				documents.add(doc);
				i++;
			}
			System.out.println("done");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{

		}
		
		
	}

}
