package gossip.gossip.service;

import gossip.model.News;
import gossip.model.SimilarityCache;

import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GossipSimCompute {
	
	@Autowired
	private GossipSimilarityCacheService gossipSimilarityCacheService;
	
	/**
	 * 计算新闻n1和n2的相似度
	 * 需要保存结果到缓存
	 * @param n1
	 * @param n2
	 * @return
	 */
	public double cosineSim(News n1, News n2){

		int id1 = n1.getId();
		int id2 = n2.getId();

		if(n1.getTitleWords() == null || n1.getTitleWords().length() == 0 || n2.getTitleWords() == null || n2.getTitleWords().length() == 0){
			return 0;
		}
		String[] title1 = n1.getTitleWords().split(";");
		String[] title2 = n2.getTitleWords().split(";");
		
		double titleSim = cosineArraySim(title1, title2);
		
		String[] body1 = n1.getBodyWords().split(";");
		String[] body2 = n2.getBodyWords().split(";");
		double bodySim = cosineArraySim(body1, body2);
		double rel = 0.7 * titleSim + 0.3 * bodySim;
		
		SimilarityCache similarityCache = new SimilarityCache(id1, id2, rel, SimilarityCache.News);
		gossipSimilarityCacheService.insert(similarityCache);
		
		return rel;
	}
	
	private static double cosineArraySim(String[] tokens1, String[] tokens2){
		HashSet<String> set = new HashSet<String>();
		HashSet<String> set1 = new HashSet<String>();
		HashSet<String> set2 = new HashSet<String>();
		for(String token: tokens1){
			set1.add(token);
		}
		for(String token: tokens2){
			set2.add(token);
		}
		set.addAll(set1);
		set.addAll(set2);
		
		int[] v1 = computeVector(set, set1);
		int[] v2 = computeVector(set, set2);
		
		double rel = cos(v1, v2);
		return rel;
	}
	
	public static double cos(int[] v1, int[] v2){
		double mult = 0;
		for(int i = 0 ; i < v1.length ; i++){
			double tmp = v1[i] * v2[i];
			mult+=tmp;
		}
		double lv1 = computeL1(v1);
		double lv2 = computeL1(v2);
		
		double cos = mult / (lv1*lv2);
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

	private static int[] computeVector(HashSet<String> set, HashSet<String> array){
		int[] vector = new int[set.size()];
		int i = 0;
		for(String token : set){
			if(array.contains(token)){
				vector[i] = 1;
			}else{
				vector[i] = 0;
			}
			i++;
		}
		return vector;
	}

	
	public static void main(String[] args){
//			News n1 = new News();
//			n1.setBody("我爱北京天安门");
//			News n2 = new News();
//			n2.setBody("天安门上太阳生");
//			GossipSimCompute gsc = new GossipSimCompute();
//			gsc.cosineSim(n1,n2);
//			double sim = gsc.cosineSim(n1,n2);
//			System.out.println(sim);
			
			int[] v1 = {1,0,0,1,0,0,0,0,0};
			int[] v2 = {0,1,1,0,1,0,0,0,0};
			System.out.println(cos(v1,v2));
	}
}
