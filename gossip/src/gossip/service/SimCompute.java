package gossip.service;

import gossip.model.News;

import java.util.List;

import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;

public class SimCompute {
	
	static{
		
	}
	
	public static double cosineSim(News n1, News n2){
		
		String body1 = n1.getBody();
		String body2 = n2.getBody();
		
		List<Term> terms1 = ToAnalysis.parse(body1);
	
		for(Term tm : terms1){
			System.out.println(tm.getName());
		}
		
		List<Term> terms2 = ToAnalysis.parse(body2);
		
		for(Term tm : terms2){
			System.out.println(tm.getName());
		}
		
		return 0;
	}

	
	public static void main(String[] args){
			News n1 = new News();
			n1.setBody("我爱北京天安门");
			News n2 = new News();
			n2.setBody("天安门上太阳生");
			
			cosineSim(n1,n2);
	}
}
