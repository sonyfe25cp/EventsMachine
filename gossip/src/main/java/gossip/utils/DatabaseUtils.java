package gossip.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;

import edu.bit.dlde.utils.DLDEConfiguration;

public class DatabaseUtils {

	private static DataSource dataSource;

	static String driver = DLDEConfiguration.getInstance("gossip.properties")
			.getValue("driver");
	static String dbUser = DLDEConfiguration.getInstance("gossip.properties")
			.getValue("dbUser");
	static String dbPassword = DLDEConfiguration.getInstance("gossip.properties")
			.getValue("dbPassword");
	static String dbUrl = DLDEConfiguration.getInstance("gossip.properties")
			.getValue("dbUrl");
	public static DataSource getInstance() {
		if (dataSource == null) {
			BasicDataSource ds = new BasicDataSource();
			ds.setDriverClassName(driver);
			ds.setUrl(dbUrl);
			ds.setUsername(dbUser);
			ds.setPassword(dbPassword);
			ds.setInitialSize(50);
			ds.setMaxActive(100);
			ds.setMaxIdle(30);
			ds.setMaxWait(10000);
			dataSource = ds;
		}
		return dataSource;
	}
	
	public static Connection getConnection(){
		Connection conn = null;
		try {
			Class.forName(driver);
			conn =DriverManager.getConnection(dbUrl, dbUser, dbPassword);
			
//			conn = dataSource.getConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}
	
}
