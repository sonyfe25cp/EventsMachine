package gossip.server.dao;

import gossip.model.ClickLog;

import java.util.List;

public interface ClickLogDao {
	
	public boolean addClickLog(ClickLog clickLog);
	
	public List<ClickLog> getClickLogByUsername(String username);
}
