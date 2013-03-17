package gossip.server.commons;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;

import edu.bit.dlde.utils.DLDEConfiguration;

public class DBCommons {
	private static String dbIp = DLDEConfiguration.getInstance("gossip.properties")
			.getValue("dbIp");
	private static String dbPort = DLDEConfiguration.getInstance("gossip.properties")
			.getValue("dbPort");
	private static String dbUser = DLDEConfiguration.getInstance("gossip.properties")
			.getValue("dbUser");
	private static String dbPassword = DLDEConfiguration.getInstance("gossip.properties")
			.getValue("dbPassword");
	public static DataSource getDataSource() {
		BasicDataSource basicDataSource = new BasicDataSource();
		basicDataSource.setDriverClassName("com.mysql.jdbc.Driver");
		basicDataSource
				.setUrl("jdbc:mysql://"+dbIp+":"+dbPort+"/gossip?useUnicode=true");
		basicDataSource.setUsername(dbUser);
		basicDataSource.setPassword(dbPassword);
		return basicDataSource;
	}
}
