package gossip.server.service;

import gossip.mapper.UserMapper;
import gossip.model.User;

import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
	
	@Autowired
	private UserMapper userMapper;
	
	
	public User getUser(String username){
		User user = userMapper.getUserByName(username);
		if(user != null && user.getLastLoginTime() != null){
			user.setLoginTime(user.getLastLoginTime().getTime());
		}
		return user;
	}
	
	public boolean existUser(String username){
		User user = userMapper.getUserByName(username);
		if(user != null){
			return true;
		}
		return false;
	}
	
	public boolean existUser(String username, String password){
		User user = userMapper.getUserByName(username);
		if(user == null){
			return false;
		}
		String pwd = user.getPassword();
		if(pwd.equals(password.trim())){
			return true;
		}
		return false;
	}
	
	public boolean existEmail(String email){
		User user = userMapper.getUserByEmail(email);
		if(user != null){
			return true;
		}
		return false;
	}
	
	public void addUser(User user){
		userMapper.addUser(user);
	}
	
	public void updateLoginInfo(String username, long loginTime, String loginIp){
		User user = userMapper.getUserByName(username);
		if(user != null){
			user.setLastLoginTime(new Date(loginTime));
			user.setLastLoginIp(loginIp);
			userMapper.updateUser(user);
		}
	}

	public UserMapper getUserMapper() {
		return userMapper;
	}

	public void setUserMapper(UserMapper userMapper) {
		this.userMapper = userMapper;
	}
	
	
}
