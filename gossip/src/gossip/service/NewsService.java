package gossip.service;

import gossip.model.News;

public class NewsService {
	
	private double lambda = 0.5;

	public double compairNews(News n1, News n2){
		return SimCompute.cosineSim(n1, n2);
	}
	
	public boolean isSimilar(News n1, News n2){
		double sim = SimCompute.cosineSim(n1, n2);
		if(sim > lambda){
			return true;
		}else
			return false;
	}
}
