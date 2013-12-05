package gossip.gossip.service;

import gossip.mapper.SimilarityCacheMapper;
import gossip.model.SimilarityCache;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 相似度计算的缓存服务
 * @author coder
 *
 */
@Service
public class GossipSimilarityCacheService {
	
	static HashSet<SimilarityCache> cacheSet = new HashSet<SimilarityCache>();

	@Autowired
	private SimilarityCacheMapper similarityCacheMapper;
	
	public double getNewsSimilarity(int id1, int id2){
		
		String pair = SimilarityCache.generatePair(id1, id2);
		String type = SimilarityCache.News;
		
		return getSimilarityCache(pair, type);
	}
	public double getEventSimilarity(int id1, int id2){
		String pair = SimilarityCache.generatePair(id1, id2);
		String type = SimilarityCache.News;
		return getSimilarityCache(pair, type);
	}
	
	private double getSimilarityCache(String pair, String type){
		SimilarityCache cache = similarityCacheMapper.getSimilarityCacheByPairAndType(pair, type);
		if(cache != null){
			return cache.getSimilarity();
		}else{
			return 0;
		}
	}
	/**
	 * 有可能造成内存泄漏，待测
	 * @param similarityCache
	 */
	public synchronized  void insert(SimilarityCache similarityCache){
//		System.out.println(similarityCache.getPair());
		if(cacheSet.size() > 5000){
//			System.out.println("cache size : "+cacheSet.size());
			HashSet<SimilarityCache> newList = cacheSet;
			batchInsert(newList);
			cacheSet = new HashSet<SimilarityCache>();
//			System.out.println("cache size2 : "+cacheSet.size());
		}
//		System.out.println("cache size3 : "+cacheSet.size());
		cacheSet.add(similarityCache);
	}
	private void batchInsert(final HashSet<SimilarityCache> cacheList){
		new Thread(){
			public void run(){
				for(SimilarityCache cache : cacheList){
					similarityCacheMapper.insert(cache);
				}
			}
		}.start();
	}
	
	public Map<String, SimilarityCache> getSimilarityCacheMapByPartIdAndCacheType(int id, String cacheType){
		String partId = id +"-";
		List<SimilarityCache> similarityCacheList = similarityCacheMapper.getSimilarityCacheByPartIdAndType(partId, cacheType);
		Map<String, SimilarityCache>  map = null;
		if(similarityCacheList != null && similarityCacheList.size() > 0){
			map = new HashMap<String, SimilarityCache>();
			for(SimilarityCache cache : similarityCacheList){
				String pair = cache.getPair();
				map.put(pair, cache);
			}
		}
		return map;
	}
	
	/**
	 * 删除包含该ID的缓存
	 * @param id
	 * @param cacheType
	 */
	public void deleteByPartIdAndCacheType(int id, String cacheType){
		String pair1 = id+"-";
		String pair2 = "-" + id;
		
		similarityCacheMapper.deletebyPartIdAndCacheType(pair1, cacheType);
		
		similarityCacheMapper.deletebyPartIdAndCacheType(pair2, cacheType);
		
	}
	
	public void batchDeleteNewsPartId(List<Integer> ids){
		if(ids != null && ids.size()> 0)
			for(Integer id : ids){
				deleteByPartIdAndCacheType(id, SimilarityCache.News);
			}
	}
	
	
}
