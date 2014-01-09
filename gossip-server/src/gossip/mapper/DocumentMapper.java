package gossip.mapper;

import gossip.model.Document;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface DocumentMapper {
	
	public Document getDocumentById(int id);
	
	public List<Document> getDocumentsByIds(@Param("idArray")List<Integer> idArray);
	
	public List<Document> getAllDocuments();
	
	public int getAllWordsCount();

}
