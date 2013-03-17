package gossip.dao;

import gossip.utils.DatabaseUtils;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

public class BaseDaoImpl implements IBaseDao {

	// public DLDEWebPage getPageById(int id) {
	// System.out.println("DLDEWebPage Id:"+id);
	// DLDEWebPage page=new DLDEWebPage();
	// page.setDLDE_ID(id+"");
	// return page;
	// }

	private DataSource dataSource;
	protected Connection conn;

	public BaseDaoImpl() {
		init();
	}

	@Override
	public void init() {
//		try {
			if (dataSource == null) {
//				dataSource = DatabaseUtils.getInstance();
			}
			if (conn == null) {
				conn = DatabaseUtils.getConnection();
//				conn = dataSource.getConnection();
			}
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

	@Override
	public void close() {
		try {
			if (conn != null || conn.isClosed() == false)
				conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

}
