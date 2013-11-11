package gossip.gossip.service;

import gossip.gossip.utils.TokenizerUtils;
import gossip.model.News;

import java.util.HashSet;

public class SimCompute {
	
	static{
		
	}
	
	public static double cosineSim(News n1, News n2){
		
		String body1 = n1.getBody();
		String body2 = n2.getBody();
		
		String[] tokens1 = TokenizerUtils.tokenizer(body1);
		String[] tokens2 = TokenizerUtils.tokenizer(body2);
		
		HashSet<String> set = new HashSet<String>();
		for(String token: tokens1){
			set.add(token);
		}
		for(String token: tokens2){
			set.add(token);
		}

		int[] v1 = computeVector(set, tokens1);
		int[] v2 = computeVector(set, tokens2);
		
		double rel = cos(v1, v2);
		
		return rel;
	}
	
	private static double cos(int[] v1, int[] v2){
		double mult = 0;
		for(int i = 0 ; i < v1.length ; i++){
			double tmp = v1[i] * v2[i];
			mult+=tmp;
		}
		double lv1 = computeL1(v1);
		double lv2 = computeL1(v2);
		
		double cos = mult / lv1*lv2;
		return cos;
	}
	private static double computeL1(int[] v1){
		double squre = 0;
		for(int v : v1){
			double tmp = v*v;
			squre += tmp;
		}
		return Math.sqrt(squre);
	}

	private static int[] computeVector(HashSet<String> set, String[] array){
		int[] vector = new int[set.size()];
		int i = 0;
		for(String token : set){
			if(contains(array, token)){
				vector[i] = 1;
			}else{
				vector[i] = 0;
			}
			i++;
		}
		return vector;
	}
	private static boolean contains(String[] array, String word){
		for(String token: array){
			if(word.equals(token)){
				return true;
			}
		}
		return false;
	}

	
	public static void main(String[] args){
			News n1 = new News();
			n1.setBody("我爱北京天安门");
			News n2 = new News();
			n2.setBody("天安门上太阳生");
			
			cosineSim(n1,n2);
			double sim = cosineSim(n1,n2);
			System.out.println(sim);
	}
}
