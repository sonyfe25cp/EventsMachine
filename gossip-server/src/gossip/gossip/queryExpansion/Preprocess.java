package gossip.gossip.queryExpansion;

import gossip.gossip.utils.DatabaseUtils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

public class Preprocess {
	
	DataSource dataSource = DatabaseUtils.getInstance();
	
	public static void main(String[] args){
		Preprocess pp = new Preprocess();
		pp.removeDuplicateNews();
	}
	
	/**
	 * 对数据库中的新闻文档进行预处理，去除重复的文档
	 */
	public void removeDuplicateNews(){
		List<String> titles = new ArrayList<String>();
		List<Integer> duplicateIds = new ArrayList<Integer>();
		Connection conn = null;
		Statement stat = null;
		ResultSet rs = null;
		String sql = "select id, title from news";
		try {
			conn = dataSource.getConnection();
			stat = conn.createStatement();
			rs = stat.executeQuery(sql);
			while(rs.next()){
				String title = rs.getString("title");
				int id = rs.getInt("id");
				if(titles.contains(title)){
					duplicateIds.add(id);
				}
				else
					titles.add(title);
			}
			String deleteSql = "delete from news where id in(";
			for(int i =0;i<duplicateIds.size();i++){
				if(i<duplicateIds.size()-1){
					deleteSql =deleteSql + "'" + duplicateIds.get(i) + "',";
				}
				else
					deleteSql =deleteSql + "'" + duplicateIds.get(i) + "')";
			}
			stat.executeUpdate(deleteSql);
			
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
