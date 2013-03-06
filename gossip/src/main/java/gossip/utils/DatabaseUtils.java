package gossip.utils;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;

public class DatabaseUtils {

	private static DataSource dataSource;

	public static DataSource getInstance() {
		if (dataSource == null) {
			BasicDataSource basicDataSource = new BasicDataSource();
			basicDataSource.setDriverClassName("com.mysql.jdbc.Driver");
			basicDataSource
					.setUrl("jdbc:mysql://localhost:3306/gossip?useUnicode=true");
			basicDataSource.setUsername("root");
			basicDataSource.setPassword("");
			dataSource = basicDataSource;
		}
		return dataSource;
	}

}
