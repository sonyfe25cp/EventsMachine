package gossip.mapper;

import gossip.model.Word;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface WordMapper {

	public Word getWordByValue(String value);
	
	public void insertWords(@Param("word")Word word);
	
	public List<Word> getWords();
	
	
}
