package gossip.utils;

import java.util.List;

import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;

public class TokenizerUtils {
	
	public static String[] tokenizer(String body){
		List<Term> terms = ToAnalysis.parse(body);
		String[] res = new String[terms.size()];
		int i = 0;
		for(Term term : terms){
			res[i] = term.getName();
			i ++;
		}
		return res;
	}

}
