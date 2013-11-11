package gossip.mapper;

import gossip.model.User;

public interface UserMapper {

	public User getUserByName(String username);

	public User getUserByEmail(String email);

	public void addUser(User user);

	public void updateUser(User user);

}
