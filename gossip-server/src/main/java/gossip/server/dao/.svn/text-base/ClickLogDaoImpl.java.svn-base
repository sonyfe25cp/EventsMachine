package gossip.server.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import edu.bit.dlde.utils.DLDELogger;
import gossip.server.commons.DBCommons;
import gossip.server.model.ClickLog;

public class ClickLogDaoImpl implements ClickLogDao {
	private DataSource dataSource;
	private DLDELogger logger;

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public DLDELogger getLogger() {
		return logger;
	}

	public void setLogger(DLDELogger logger) {
		this.logger = logger;
	}

	public void init() {
		if(logger == null) {
			logger = new DLDELogger();
		}
		if(dataSource == null){
			dataSource = DBCommons.getDataSource();
		}
	}
	
	@Override
	public boolean addClickLog(ClickLog clickLog) {
		boolean result = false;
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			String sql = "insert into clicklog(username,objectid,query,create_time) values(?,?,?,?)";
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, clickLog.getUsername());
			pstmt.setInt(2, clickLog.getObjectId());
			pstmt.setString(3, clickLog.getQuery());
			pstmt.setTimestamp(4, new Timestamp(clickLog.getCreateTime()));

			if(pstmt.executeUpdate() == 1) {
				result = true;
			}
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	@Override
	public List<ClickLog> getClickLogByUsername(String username) {
		List<ClickLog> clicklogs = new ArrayList<ClickLog>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			String sql = "select * from clicklog where username = ?";
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, username);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				ClickLog cl = new ClickLog();
				cl.setUsername(rs.getString("username"));
				cl.setObjectId(rs.getInt("objectid"));
				cl.setQuery(rs.getString("query"));
				if(rs.getTimestamp("create_time") != null) {
					cl.setCreateTime(rs.getTimestamp("create_time").getTime());
				}
				clicklogs.add(cl);
			}
			if(clicklogs.size() == 0) {
				clicklogs = null;
			}
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return clicklogs;
	}

}
