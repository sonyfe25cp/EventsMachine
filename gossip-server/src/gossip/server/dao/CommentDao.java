package gossip.server.dao;

import gossip.model.Comment;

import java.util.List;

public interface CommentDao {

	public boolean addComment(Comment comment);
	
	public List<Comment> getCommentByUsername(String username);
}
