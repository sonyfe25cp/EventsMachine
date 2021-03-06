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
import gossip.gossip.utils.DatabaseUtils;
import gossip.model.Comment;

/**
 * Mysql数据库的CommentDao实现类
 * @author liyan
 *
 */
public class CommentDaoImpl implements CommentDao {

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
			dataSource = DatabaseUtils.getInstance();
		}
	}
	
	@Override
	public boolean addComment(Comment comment) {
		boolean result = false;
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			String sql = "insert into comment(content,objectid,username,create_time) values(?,?,?,?)";
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, comment.getContent());
			pstmt.setInt(2, comment.getObjectId());
			pstmt.setString(3, comment.getUsername());
			pstmt.setTimestamp(4, new Timestamp(comment.getCreateTime()));
			
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
	public List<Comment> getCommentByUsername(String username) {
		List<Comment> comments = new ArrayList<Comment>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			String sql = "select * from comment where username = ?";
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, username);
			rs = pstmt.executeQuery();

			while(rs.next()) {
				Comment comt = new Comment();
				comt.setId(rs.getInt("id"));
				comt.setContent(rs.getString("content"));
				comt.setObjectId(rs.getInt("objectid"));
				comt.setUsername(rs.getString("username"));
				if(rs.getTimestamp("create_time") != null) {
					comt.setCreateTime(rs.getTimestamp("create_time").getTime());
				}
				comments.add(comt);
			}
			if(comments.size() == 0) {
				comments = null;
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
		return comments;
	}

}
