package gossip.gossip.service;

import gossip.gossip.utils.TokenizerUtils;
import gossip.mapper.DocTermMapper;
import gossip.mapper.DocumentMapper;
import gossip.mapper.NewsMapper;
import gossip.model.DocTerm;
import gossip.model.Document;
import gossip.model.News;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.ansj.domain.Term;
import org.ansj.recognition.NatureRecognition;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserSearchService {
	
	@Autowired
	private DocTermMapper docTermMapper;
	
	@Autowired
	private DocumentMapper documentMapper;
	
	@Autowired
	private QueryExpansionService qEService;
	
	@Autowired
	private NewsMapper newsMapper;
	
	public List<News> search(String query){
		System.out.println("开始查询： " + query);
		List<String> queryWords = new ArrayList<String>();
		List<Term> terms = ToAnalysis.parse(query);
		new NatureRecognition(terms).recognition();
		for (Term term : terms) {
			String keyword = term.getName();
			String property = term.getNatrue().natureStr;
			if (!TokenizerUtils.wordFilter(keyword))
				continue;
			if((!(queryWords.contains(keyword)))&&property.startsWith("n")){
				queryWords.add(keyword);
			}
			
		}// for queryTerms
//		queryWords.add("雅安");
//		queryWords.add("地震");
		System.out.println("分词完毕");
		List<DocTerm> queryTerms = docTermMapper.getDocTermsByName(queryWords);
		//对文档词的形式进行解析
		for(DocTerm term : queryTerms){
			term.getCountMap();
			term.setWeight(3.0);
			System.out.println("查询词： " + term.getKeyword() + "   docid:" + term.getDocIdString());
		}
		
		List<Document> documents = getDocuments(queryTerms);
		
		List<Document> relatedDocs = new ArrayList<Document>();
		//计算文档的BM25值并排序，返回得分最高的10篇文档
		relatedDocs = getRelatedDocs(queryTerms, documents);
		if(relatedDocs.size()>10){
			relatedDocs = relatedDocs.subList(0, 10);
		}
		List<DocTerm> expansionTerms = qEService.getExpansionTerms(queryTerms, relatedDocs);
		List<Integer> relatedDocsId = searchSecond(queryTerms, expansionTerms);
		List<News> news = new ArrayList<News>();
		news = newsMapper.getNewsListByIds(relatedDocsId);
		return news;
	}
	
	/**
	 * 将包含查询词的文档从数据库中取出，计算BM25时仅考虑这些文档，不必计算整个文档集
	 * @param queryTerms
	 * @return
	 */
	public List<Document> getDocuments(List<DocTerm> queryTerms){
		System.out.println("开始读取包含查询词的文档");
		if(queryTerms==null){
			return null;
		}
		List<Document> documents = new ArrayList<Document>();
		List<Document> docs = new ArrayList<Document>();
		List<Integer> docIds = new ArrayList<Integer>();
		for(DocTerm term : queryTerms){
			String[] ids = term.getDocIdString().split(";");
			for(String id : ids){
				if(!docIds.contains(Integer.parseInt(id))){
					docIds.add(Integer.parseInt(id));
				}
			}
		}
		if(docIds==null||docIds.size()==0)
			return null;
		documents = documentMapper.getDocumentsByIds(docIds);	
	
		//进行一次简单筛选，只将包含所有查询词的文档选出
		for(Document doc : documents){
			boolean flag = true;
			for(DocTerm term : queryTerms){
				if(!doc.getBodyWordsMap().containsKey(term.getKeyword())){
					flag = false;
					break;
				}
			}
			if(flag==true){
				docs.add(doc);
			}
		}
		int i = 0;
		System.out.println(docs.size());
		if(docs.size()<10){
			if(i<documents.size()){
				docs.add(documents.get(i));
				i++;
			}
		}
		System.out.println("读取查询词文档完毕");
		for(Document doc : docs){
			System.out.println(doc.getId());
		}
		return docs;
	}
	
	/**
	 * 
	 * @param queryTerms
	 * @param documents
	 * @return
	 */
	public List<Document> getRelatedDocs(List<DocTerm> queryTerms, List<Document> documents){
		System.out.println("开始计算文档的bm25值");
		double avgdl = 0.0;
		for(Document doc : documents){
			avgdl += doc.getBodyWordsCount();
		}
		avgdl = (double)avgdl/documents.size();
		
		for(Document doc : documents){
			double BM25 = 0.0;
			for(DocTerm queryTerm : queryTerms){
				String word = queryTerm.getKeyword();
				//如果文档标题中含有该词，则权重提高
				if(doc.getTitleWordsMap().containsKey(word)){
					BM25 = BM25 + (double)1/queryTerms.size();
				}
				
				if(doc.getBodyWordsMap().containsKey(word)){
					int tf = doc.getBodyWordsMap().get(word);
					int dl = doc.getBodyWordsCount();
					BM25 = BM25 + queryTerm.getWeight()*computeBM25(tf, dl, avgdl);
				}
			}
			doc.setBM25(BM25);
		}
		Collections.sort(documents, new Comparator<Document>(){
			public int compare(Document doc1, Document doc2){
				if(doc1.getBM25() < doc2.getBM25())
					return 1;
				else if(doc1.getBM25() > doc2.getBM25())
					return -1;
				else
					return 0;
			}
		});
//		//取bm25最大的10篇文档返回
//		if(documents.size()>10){
//			documents = documents.subList(0, 9);
//		}
		System.out.println("bm25计算完毕，并进行排序");
		return documents;
		
	}
	
	/**
	 * 
	 * @param tf
	 * @param dl
	 * @param avgdl
	 * @return
	 */
	public double computeBM25(int tf, int dl, double avgdl){
		int k =2;//参数k
		double b = 0.75;//参数b
		double divideA = (double)tf*(k+1);//分子
		double divideB = b*((double)dl/avgdl);//分母的一部分
		double bm25 = divideA/(tf+k*(1-b+b*divideB));
		return bm25;
	}
	
	/**
	 * 
	 * @param queryTerm
	 * @param expansionTerms
	 * @return
	 */
	public List<Integer> searchSecond(List<DocTerm> queryTerm, List<DocTerm> expansionTerms){
		System.out.println("第二次查询开始");
		List<DocTerm> terms = new ArrayList<DocTerm>();
		terms.addAll(queryTerm);
		terms.addAll(expansionTerms);
		List<Document> documents = getDocumentsByExpansion(terms);
		List<Document> relatedDocument = getRelatedDocs(terms, documents);
		List<Integer> relatedDocs = new ArrayList<Integer>();
		int count = 0;
		for(Document doc : relatedDocument){
			relatedDocs.add(doc.getId());
			count++;
			if(count>=30){
				break;
			}
		}
		return relatedDocs;
	}
	
	/**
	 * 先取出包含扩展词的文档，再计算BM25进行进一步筛选
	 * @param queryTerms
	 * @return
	 */
	public List<Document> getDocumentsByExpansion(List<DocTerm> queryTerms){
		System.out.println("开始读取包含查询词的文档");
		if(queryTerms==null){
			return null;
		}
		List<Document> documents = new ArrayList<Document>();
		List<Document> docs = new ArrayList<Document>();
		List<Integer> docIds = new ArrayList<Integer>();
		for(DocTerm term : queryTerms){
			String[] ids = term.getDocIdString().split(";");
			for(String id : ids){
				if(!docIds.contains(Integer.parseInt(id))){
					docIds.add(Integer.parseInt(id));	
				}
			}
		}
		if(docIds==null||docIds.size()==0)
			return null;
			
		documents = documentMapper.getDocumentsByIds(docIds);

		System.out.println("读取查询词文档完毕");
		return documents;
	}

}
