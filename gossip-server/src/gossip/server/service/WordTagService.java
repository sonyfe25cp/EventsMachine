package gossip.server.service;

import gossip.mapper.WordTagMapper;
import gossip.model.WordTag;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WordTagService {
	@Autowired
	private WordTagMapper wordTagMapper;
	
	public List<WordTag> getWordTagRandom(){
		int wordTagCount = wordTagMapper.getWordTagCount();
		List<Integer> ids = new ArrayList<Integer>();
		for(int i=0;i<20;i++){
			int id = new Random().nextInt(wordTagCount);
			ids.add(id);
		}
		List<WordTag> wordTags= wordTagMapper.getRandomWordTags(ids);
		return wordTags;
	}
	
	public void updateApprove(String id){
		if(id.trim().equals("")||id==null){
			return;
		}
		int keywordId = Integer.parseInt(id);
		WordTag wordTag = wordTagMapper.getWordTagById(keywordId);
		int approve = wordTag.getApprove();
		wordTag.setApprove(approve+1);
		wordTagMapper.updateWordTag(wordTag);
	}
	
	public void updateAgainst(String id){
		if(id.trim().equals("")||id==null){
			return;
		}
		int keywordId = Integer.parseInt(id);
		WordTag wordTag = wordTagMapper.getWordTagById(keywordId);
		int against = wordTag.getAgainst();
		wordTag.setAgainst(against+1);
		wordTagMapper.updateWordTag(wordTag);
	}

}
