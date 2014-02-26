package gossip.gossip.service;

import gossip.mapper.SimilarityCacheMapper;
import gossip.model.SimilarityCache;

import java.util.ArrayList;
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
		
//		String pair = SimilarityCache.generatePair(id1, id2);
		String type = SimilarityCache.News;
		
		return getSimilarityCacheByFSAndType(id1, id2, type);
	}
	public double getEventSimilarity(int id1, int id2){
//		String pair = SimilarityCache.generatePair(id1, id2);
		String type = SimilarityCache.Event;
		return getSimilarityCacheByFSAndType(id1, id2, type);
	}
	
	private double getSimilarityCacheByFSAndType(int first, int second, String type){
		SimilarityCache cache = similarityCacheMapper.getSimilarityCacheByFSAndType(first, second, type);
		if(cache != null){
			return cache.getSimilarity();
		}else{
			return 0;
		}
	}
	
//	private double getSimilarityCache(String pair, String type){
//		SimilarityCache cache = similarityCacheMapper.getSimilarityCacheByPairAndType(pair, type);
//		if(cache != null){
//			return cache.getSimilarity();
//		}else{
//			return 0;
//		}
//	}
	/**
	 * 有可能造成内存泄漏，待测
	 * @param similarityCache
	 */
	public synchronized  void insert(SimilarityCache similarityCache){
		if(cacheSet.size() > 5000){
//			HashSet<SimilarityCache> newList = cacheSet;
			batchInsert(cacheSet);
			cacheSet = new HashSet<SimilarityCache>();
		}
		cacheSet.add(similarityCache);
	}
	private void batchInsert(final HashSet<SimilarityCache> cacheList){
		new Thread(){
			public void run(){
				List<SimilarityCache> list = new ArrayList<SimilarityCache>();
				list.addAll(cacheList);
				similarityCacheMapper.batchInsert(list);
			}
		}.start();
	}
	
	public Map<String, SimilarityCache> getSimilarityCacheMapByPartIdAndCacheType(int id, String cacheType){
		List<SimilarityCache> similarityCacheList = similarityCacheMapper.getSimilarityCacheByFirstAndType(id, cacheType);
		Map<String, SimilarityCache>  map = null;
		if(similarityCacheList != null && similarityCacheList.size() > 0){
			map = new HashMap<String, SimilarityCache>();
			for(SimilarityCache cache : similarityCacheList){
				int second = cache.getSecond();
				map.put(id+"-"+second, cache);
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
