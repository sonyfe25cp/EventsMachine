package gossip.gossip.test;

import gossip.gossip.utils.DatabaseUtils;
import gossip.model.News;
import gossip.model.Word;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TestDB {

	public static void main(String[] args){
//		DataSource source = DatabaseUtils.getInstance();
		
		try {
//			Connection conn = source.getConnection();
			Connection conn = DatabaseUtils.getConnection();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void test(String[] args){
		News news = new News();
		news.setAuthor("author");
		news.setBody("123123123");
		news.setCrawlAt("");
		news.setDate("20130315");
		news.setDescription("");
		news.setTitle("title");
		news.setStatus(News.NEW);
		
		System.out.println(news.getStatus().toString());
	}
	
	public TestDB(){
		try {
			conn = DatabaseUtils.getInstance().getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	public void close(){
		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private Connection conn;
	/**
	 * @param args
	 * @throws ClassNotFoundException 
	 * @throws SQLException 
	 */
	public void run() throws ClassNotFoundException, SQLException {
		String SQL_INSERT_NEWS = "insert into news(title,body,url,author,description) values(?,?,?,?,?)";

		try {

			News news = new News();
			news.setAuthor("author");
			news.setBody("123123123");
			news.setCrawlAt("");
			news.setDate("20130315");
			news.setDescription("");
			news.setTitle("title");

			PreparedStatement pstmt = null;
			pstmt = conn.prepareStatement(SQL_INSERT_NEWS);
			System.out.println("insert " + news.getTitle());
			pstmt.setString(1, news.getTitle());
			pstmt.setString(2, news.getBody());
			pstmt.setString(3, news.getUrl());
			pstmt.setString(4, news.getAuthor());
			pstmt.setString(5, news.getDescription());
			pstmt.execute();
			pstmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void insertWord(Word word){
		String SQL_INSERT_WORD = "insert into words(name, count, idf) values(?,?,?)";
		try {
			PreparedStatement pstmt = null;
			pstmt = conn.prepareStatement(SQL_INSERT_WORD);
			pstmt.setString(1, word.getName());
			pstmt.setInt(2, word.getCount());
			pstmt.setDouble(3, word.getIdf());
			pstmt.execute();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
