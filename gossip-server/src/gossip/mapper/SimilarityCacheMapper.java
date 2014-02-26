package gossip.mapper;

import gossip.model.SimilarityCache;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface SimilarityCacheMapper {
	
	public void insert(SimilarityCache similarityCache);
	
	public void batchInsert(@Param("similarityCacheList")List<SimilarityCache> similarityCacheList);
	
	public SimilarityCache getSimilarityCacheById(int id);
	
	public SimilarityCache getSimilarityCacheByPairAndType(@Param("pair") String pair, @Param("cacheType") String cacheType);
	
	public SimilarityCache getSimilarityCacheByFSAndType(@Param("first") int first,@Param("second") int second, @Param("cacheType") String cacheType);

	public void deletebyPartIdAndCacheType(@Param("partId") String partId, @Param("cacheType") String cacheType);

	public List<SimilarityCache> getSimilarityCacheByPartIdAndType(@Param("partId") String partId, @Param("cacheType") String cacheType);
	
	public List<SimilarityCache> getSimilarityCacheByFirstAndType(@Param("first") int first, @Param("cacheType") String cacheType);
	
}
