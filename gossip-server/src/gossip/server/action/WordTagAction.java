package gossip.server.action;

import gossip.model.WordTag;
import gossip.server.service.WordTagService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/wordTagging")
public class WordTagAction {
	@Autowired
	private WordTagService wordTagService;
	
	@RequestMapping(value= "/getWordTagRandom")
	@ResponseBody
	public List<WordTag> getWordTagRandom(){
		return wordTagService.getWordTagRandom();
	}
	
	@RequestMapping(value= "/updateApprove")
	@ResponseBody
	public void updateApprove(@RequestParam(value = "id", required = true) String id){
		wordTagService.updateApprove(id);
	}
	
	@RequestMapping(value= "/updateAgainst")
	@ResponseBody
	public void updateAgainst(@RequestParam(value = "id", required = true) String id){
		wordTagService.updateAgainst(id);
	}

}
