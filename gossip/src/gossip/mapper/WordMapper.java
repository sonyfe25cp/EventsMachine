package gossip.mapper;

import gossip.model.Word;

public interface WordMapper {

	public Word getWordByValue(String value);
	
	public void insertWord(Word word);
	
	
}
