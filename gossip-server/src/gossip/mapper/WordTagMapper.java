package gossip.mapper;

import gossip.model.WordTag;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface WordTagMapper {
	
	public List<WordTag> getRandomWordTags(@Param("idList")List<Integer> idList);
	
	public void updateWordTag(WordTag wordTag);
	
	public WordTag getWordTagByWord(String keyword);
	
	public WordTag getWordTagById(int id);
	
	public int getWordTagCount();

}
