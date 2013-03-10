package gossip.utils;

import javax.sql.DataSource;

import com.jolbox.bonecp.BoneCPDataSource;

import edu.bit.dlde.utils.DLDEConfiguration;

public class DatabaseUtils {

	private static DataSource dataSource;

	public static DataSource getInstance() {
		String driver = DLDEConfiguration.getInstance("gossip.properties")
				.getValue("driver");
//		String dbIp = DLDEConfiguration.getInstance("gossip.properties")
//				.getValue("dbIp");
//		String dbPort = DLDEConfiguration.getInstance("gossip.properties")
//				.getValue("dbPort");
		String dbUser = DLDEConfiguration.getInstance("gossip.properties")
				.getValue("dbUser");
		String dbPassword = DLDEConfiguration.getInstance("gossip.properties")
				.getValue("dbPassword");
		String dbUrl = DLDEConfiguration.getInstance("gossip.properties")
				.getValue("dbUrl");
		if (dataSource == null) {
			int max = 20;
			int min = 5;
			int increment = 2;
			try {
				Class.forName(driver);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			BoneCPDataSource datasource = new BoneCPDataSource();

			datasource.setJdbcUrl(dbUrl);
			datasource.setUsername(dbUser);
			datasource.setPassword(dbPassword);
			datasource.setMaxConnectionsPerPartition(max);
			datasource.setMinConnectionsPerPartition(min);
			datasource.setAcquireIncrement(increment);

			dataSource = datasource;
		}
		return dataSource;
	}

}
