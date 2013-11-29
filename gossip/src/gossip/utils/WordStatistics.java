package gossip.utils;

import gossip.mapper.WordMapper;
import gossip.model.News;
import gossip.model.Word;
import gossip.service.Service;
import gossip.test.TestDB;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

public class WordStatistics extends Service{

	private WordMapper wordMapper = session.getMapper(WordMapper.class) ;
	
	private HashMap<String,Integer> wordsMap = null;//每个文档中的词计算多份
	private HashMap<String,Integer> idfMap = null;//每个文档中的词只算一份
	
	private int fileCount = 0 ;
	
	private void recordAllWords(){
		BatchNewsReader bnr = new BatchNewsReader();
		List<News> newsList = bnr.next();
		while(newsList!=null){
			
			for(News news : newsList){
				String titleWords = news.getTitleWords();
				String[] titleWordsArray = titleWords.split(";");
				
				String bodyWords = news.getBodyWords();
				String[] bodyWordsArray = bodyWords.split(";");
				
				setWords(titleWordsArray);
				setWords(bodyWordsArray);
				
				setIDF(titleWordsArray, bodyWordsArray);
				
				fileCount ++;
			}
			newsList = bnr.next();
		}
	}
	
	private void setWords(String[] words){
		if(wordsMap == null){
			wordsMap = new HashMap<String,Integer>();
		}
		for(String word : words){
			if(TokenizerUtils.hasInteger(word)){
				continue;
			}
			if(wordsMap.containsKey(word)){
				int count = wordsMap.get(word);
				wordsMap.put(word, count+1);
			}else{
				wordsMap.put(word, 1);
			}
		}
	}
	
	/**
	 * 将title和body的分词合并为一份无重复的数组
	 * 然后计算将词存入idf表
	 * @param titleWords
	 * @param bodyWords
	 */
	private void setIDF(String[] titleWords, String[] bodyWords){
		if(idfMap == null){
			idfMap = new HashMap<String,Integer>();
		}
		String[] newArray = new String[titleWords.length + bodyWords.length];
		for(int i = 0; i< titleWords.length ; i++){
			newArray[i] = titleWords[i];
		}
		for(int i = 0; i< bodyWords.length  ; i++){
			int index  = titleWords.length + i;
			newArray[index] = bodyWords[i];
		}
		
		String[] array = TokenizerUtils.tokenizerUnique(newArray);
		for(String word : array){
			if(TokenizerUtils.hasInteger(word)){
				continue;
			}
			if(idfMap.containsKey(word)){
				int count = idfMap.get(word);
				idfMap.put(word, count+1);
			}else{
				idfMap.put(word, 1);
			}
		}
	}
	
	private double countIDF(String word){
		int count = idfMap.get(word);
		double idf = Math.log10((double)fileCount/count);
		return idf;
	}
	
	public void batchComputeWords(){
		recordAllWords();
		TestDB tdb = new TestDB();
		for(Entry<String, Integer> tmp : wordsMap.entrySet()){
			String value = tmp.getKey();
			int count = tmp.getValue();
			Word word = new Word();
			word.setCount(count);
			word.setName(value);
			double idf = countIDF(value);
			word.setIdf(idf);
			System.out.println(word);
//			wordMapper.insertWords(word);
			tdb.insertWord(word);
		}
		tdb.close();
	}
	public void testInsert(){
		Word word = new Word();
		word.setCount(1);
		word.setName("a");
		double idf = 0.1;
		word.setIdf(idf);
		wordMapper.insertWords(word);
		
//		TestDB tdb = new TestDB();
//		tdb.insertWord(word);
//		tdb.close();
	}
	public void testGetWords(){
		List<Word> words = wordMapper.getWords();
		for(Word word: words){
			System.out.println(word.getName());
		}
	}
	
	public WordMapper getWordMapper() {
		return wordMapper;
	}

	public void setWordMapper(WordMapper wordMapper) {
		this.wordMapper = wordMapper;
	}

	public HashMap<String, Integer> getWordsMap() {
		return wordsMap;
	}

	public void setWordsMap(HashMap<String, Integer> wordsMap) {
		this.wordsMap = wordsMap;
	}

	public HashMap<String, Integer> getIdfMap() {
		return idfMap;
	}

	public void setIdfMap(HashMap<String, Integer> idfMap) {
		this.idfMap = idfMap;
	}

	public int getFileCount() {
		return fileCount;
	}

	public void setFileCount(int fileCount) {
		this.fileCount = fileCount;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		WordStatistics wst = new WordStatistics();
		wst.batchComputeWords();
//		wst.testInsert();
//		wst.testGetWords();
		System.out.println(wst.getFileCount());
		
	}

}
