package gossip.gossip.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;

public class TokenizerUtils {
	
	private static HashSet<String> stopwords;
	
	static{
		stopwords = getStopWords();
	}
	
	public static String[] tokenizer(String body){
		List<Term> terms = ToAnalysis.parse(body);
		String[] res = new String[terms.size()];
		int i = 0;
		for(Term term : terms){
			String tmp = term.getName();
			if(tmp.trim().length() == 0){
				continue;
			}
			if(stopwords.contains(tmp)){
				continue;
			}
			res[i] = tmp;
			i ++;
		}
		return res;
	}

	public static String[] tokenizerUnique(String[] terms){
		HashSet<String> set = new HashSet<String>();
		for(String tmp : terms){
			set.add(tmp);
		}
		String[] words = new String[set.size()];
		int j = 0;
		for(String tmp : set){
			words[j] = tmp;
			j++;
		}
		return words;
	}
	public static String arrayToString(String[] array){
		String str = "";
		for(String tmp : array){
			str +=(tmp+";");
		}
		return str;
	}
	
	public static HashSet<String> getStopWords(){
		HashSet<String> set = new HashSet<String>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File("conf/stopwords")));
			String line = null;
			while(( line = br.readLine()) !=null){
				set.add(line);
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return set;
	}

	public static boolean hasInteger(String string){
		String regex = "[0-9]+";
		Pattern patter = Pattern.compile(regex);
		Matcher matcher = patter.matcher(string);
		if(matcher.find()){
			return true;
		}else{
			return false;
		}
	}
	
	public static void main(String[] args){
		String str ="我爱北京天安门，天安门上太阳升";
		String  tmp = arrayToString(tokenizer(str));
		System.out.println(tmp);
		
		boolean flag = hasInteger(str);
		System.out.println("hasInteger:"+ flag);
		
//		HashSet<String> hash = getStopWords();
//		for(String word : hash){
//			System.out.println(word);
//		}
	}
}
