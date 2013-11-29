package gossip.gossip.action;

import gossip.gossip.service.GossipBatchUpdateNews;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/gossip/test")
public class GossipTestAction {

	@Autowired
	private GossipBatchUpdateNews gossipBatchUpdateNews;
	
	@RequestMapping("/batchSplitNews")
	public void batchSplitNews(){
		gossipBatchUpdateNews.batchUpdate();
	}
}
