package gossip.mapper;

import gossip.model.DocTerm;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface DocTermMapper {
	
	public DocTerm getDocTermByName(String keyword);
	
	public List<DocTerm> getDocTermsByName(@Param("keywordArray")List<String> keywordArray);
	
	public List<DocTerm> getAllTerms();

}
