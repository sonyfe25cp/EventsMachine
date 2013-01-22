package gossip.server.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;

import edu.bit.dlde.utils.DLDELogger;

import gossip.server.commons.DBCommons;
import gossip.server.model.User;

/**
 * Mysql数据库的UserDao实现类
 * 
 * @author liyan
 *
 */
public class UserDaoImpl implements UserDao {

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
	public boolean existUser(String username, String password) {
		boolean result = false;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			String sql = "select password from user where username = ?";
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, username);
			rs = pstmt.executeQuery();

			if(rs.next()) {
				if(rs.getString("password").equals(password)) {
					result = true;
				}
			}
		} catch (SQLException e) {
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
		return result;
	}

	@Override
	public boolean existUser(String username) {
		boolean result = false;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			String sql = "select * from user where username = ?";
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, username);
			rs = pstmt.executeQuery();

			if(rs.next()) {
				result = true;
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
		return result;
	}

	@Override
	public boolean existEmail(String email) {
		boolean result = false;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			String sql = "select * from user where email = ?";
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, email);
			rs = pstmt.executeQuery();

			if(rs.next()) {
				result = true;
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
		return result;
	}

	@Override
	public User getUserByName(String username) {
		User user = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			String sql = "select * from user where username = ?";
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, username);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				user = new User();
				user.setUsername(rs.getString("username"));
				user.setPassword(rs.getString("password"));
				user.setEmail(rs.getString("email"));
				user.setRole(rs.getString("role"));
				if(rs.getTimestamp("last_login_time") != null) {
					user.setLastLoginTime(rs.getTimestamp("last_login_time").getTime());
				}
				user.setLastLoginIp(rs.getString("last_login_ip"));
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
		return user;
	}
	
	@Override
	public boolean addUser(User user) {
		boolean result = false;
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			if(user.getRole() == null) {
				user.setRole("user");
			}
			String sql = "insert into user(username,password,email,role) values(?,?,?,?)";
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, user.getUsername());
			pstmt.setString(2, user.getPassword());
			pstmt.setString(3, user.getEmail());
			pstmt.setString(4, user.getRole());
			
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
	public boolean updateLoginInfo(String username, long date, String ip) {
		boolean result = false;
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			String sql = "update user set last_login_time=?, last_login_ip=? where username=?";
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setTimestamp(1, new Timestamp(date));
			pstmt.setString(2, ip);
			pstmt.setString(3, username);
			
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

	
}
