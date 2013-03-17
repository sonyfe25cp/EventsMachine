package gossip.server.dao;

import java.util.List;

import gossip.server.model.ClickLog;

public interface ClickLogDao {
	
	public boolean addClickLog(ClickLog clickLog);
	
	public List<ClickLog> getClickLogByUsername(String username);
}
