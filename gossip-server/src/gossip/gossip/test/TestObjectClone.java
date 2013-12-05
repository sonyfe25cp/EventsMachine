package gossip.gossip.test;

import gossip.model.SimilarityCache;

import java.util.ArrayList;

public class TestObjectClone {
	
	public static void main(String[] args){
		ArrayList<SimilarityCache> cacheList = new ArrayList<SimilarityCache>();
		cacheList.add(new SimilarityCache());
		
		ArrayList<SimilarityCache> newlist = cacheList;
		
		cacheList = new ArrayList<SimilarityCache>();
		
		for(SimilarityCache cache : newlist){
			if(cache != null){
				System.out.println("not nul");
			}
		}
	}

}
