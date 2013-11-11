package gossip.mapper;

import gossip.model.Word;

import java.util.List;

public interface WordMapper {

	public Word getWordByValue(String value);
	
	public void insertWords(Word word);
	
	public List<Word> getWords();
	
	
}