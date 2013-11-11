package gossip.feature;

import gossip.model.News;
import gossip.model.Word;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * @author ChenJie
 * 文本词本身的特征信息
 * Sep 26, 2013
 */
public class WordsFeatureHandler implements FeatureHandler{

	private LinkedList<String> dict;
	private HashMap<String, Word> wordsDict;
	
	public WordsFeatureHandler(){
		loadDict();
	}

	@Override
	public double[] handle(Object object) {

		News news = (News)object;
		
		String titleWords = news.getTitleWords();
		String bodyWords = news.getBodyWords();
		
		double[] titleFeature = compute(titleWords, 2);
		double[] bodyFeature = compute(bodyWords, 1);
		
		double[] finalFeature = plus(titleFeature, bodyFeature);
		 
		return finalFeature;
		
	}
	public double[] compute(String wordString, double specialWeight){
		String[] words = wordString.split(";");
		HashMap<String, Integer> tmpMap = arrayToMap(words);
		double[] vec = new double[dict.size()];
		int i = 0;
		for(String column : dict){
			
			Integer tmpCount = tmpMap.get(column);
			if(tmpCount != null){
				Word wordInDict = wordsDict.get(column);
				double idf = wordInDict.getIdf();
				
				double weight = tmpCount * idf * specialWeight;
				vec[i] = weight;
			}else{
				vec[i] = 0;
			}
			
			i++;
		}
		
		return vec;
	}
	
	private HashMap<String, Integer> arrayToMap(String[] array){
		HashMap<String , Integer> map = new HashMap<String, Integer>();
		for(String word : array){
			Integer count = map.get(word);
			if(count == null){
				map.put(word, 1);
			}else{
				map.put(word, count+1);
			}
		}
		return map;
	}
	
	public void loadDict(){
		
	}
	
	public double[] plus(double[] a , double[] b){
		for(int i = 0; i < a.length; i ++){
			a[i] = a[i] + b[i];
		}
		return a;
	}
	

}
