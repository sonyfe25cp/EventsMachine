package gossip.server.dao;

import gossip.server.model.User;

/**
 * 与用户相关的数据库操作接口
 * 
 * @author liyan
 *
 */
public interface UserDao {

	/**
	 * 检查是否存在指定用户名，及密码是否正确
	 * @param username
	 * @param password
	 * @return
	 */
	public boolean existUser(String username, String password);
	
	/**
	 * 检查是否存在指定用户名
	 * @param username
	 * @return
	 */
	public boolean existUser(String username);
	
	/**
	 * 检查是否存在指定Email
	 * @param email
	 * @return
	 */
	public boolean existEmail(String email);
	
	/**
	 * 根据用户名得到用户信息（用户名唯一）
	 * @param username
	 * @return
	 */
	public User getUserByName(String username);
	
	/**
	 * 添加用户
	 * @param user
	 * @return
	 */
	public boolean addUser(User user);
	
	/**
	 * 更新用户的登陆信息，时间及IP
	 * @param username
	 * @return
	 */
	public boolean updateLoginInfo(String username, long date, String ip);
}
