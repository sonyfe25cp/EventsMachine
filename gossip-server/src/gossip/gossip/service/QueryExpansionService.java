package gossip.gossip.service;

import gossip.mapper.DocTermMapper;
import gossip.mapper.DocumentMapper;
import gossip.model.DocTerm;
import gossip.model.Document;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class QueryExpansionService {
	
	@Autowired
	private DocTermMapper docTermMapper;
	
	@Autowired
	private DocumentMapper documentMapper;

	private int termTotalCountC = 0;// 语料集中的词组数
	private int termTotalCountR = 0;// 相关文档集中的词组数
	private int newsCount = 64746;
	private List<Document> relatedDocuments = new ArrayList<Document>();
	private List<DocTerm> queryTerms = new ArrayList<DocTerm>();// 考虑要不要改成map？？？？
	private List<String> queryWords = new ArrayList<String>();
	// candidateTerms存放候选扩展词
	private List<DocTerm> candidateTerms = new ArrayList<DocTerm>();
	
	
	public QueryExpansionService(){
		
	}
	
	/**
	 * 根据初始查询词和相关文档集查找扩展词
	 * @param query，初始查询词
	 * @param relatedDocs，第一次检索返回的相关文档集
	 * @return
	 */
	public List<DocTerm> getExpansionTerms(List<DocTerm> queryTerms, List<Document> relatedDocs){
		//初始化
		init(queryTerms, relatedDocs);
		//得到相关文档中的所有词组
		candidateTerms = getTermsInRD();
		//对每个词统计信息，并计算KLD特征,需要的参数有frequenceInR，frequenceInC，termTotalCountR,termTotalCountC
		for(int i =0;i<candidateTerms.size();i++){
			DocTerm docTerm = candidateTerms.get(i);
			String word = docTerm.getKeyword();
			int frequenceInR = 0;
			int frequenceInC = 0;
			//计算关键词在相关文档中的频率
			for(Document doc : relatedDocuments){
				if(doc.getBodyWordsMap().containsKey(word)){
					frequenceInR += doc.getBodyWordsMap().get(word);
				}
			}
			docTerm.setFrequenceInR(frequenceInR);
			
			//计算关键词在整个文档集上的频率
			List<Integer> frequences = new ArrayList<Integer>(docTerm.getBodyCountMap().values());
			for(int fre : frequences){
				frequenceInC += fre;
			}
			docTerm.setFrequenceInC(frequenceInC);
			double kld = computeKLD(frequenceInR, frequenceInC, termTotalCountR, termTotalCountC);
			docTerm.setKld(kld);
			//实验时先将权值设为kld值
			docTerm.setWeight(kld);
		}
		System.out.println("计算kld值结束，按照kld排序并筛选");
		//对候选扩展词candidateTerms按照kld排序，选出前50个词
		filterTermByKLD();
		System.out.println("kld筛选完毕");
		
		for(DocTerm exTerm : candidateTerms){
			System.out.println(exTerm.getKeyword()+": " + exTerm.getKld());
		}
		
		//对candidateTerms中的候选扩展词计算剩余的特征
		computerFeatures(queryTerms, candidateTerms);
		//按照权值排序，并返回前10个词
		orderByWeight(candidateTerms);
		return candidateTerms;
	}
	
	/**
	 * 初始化过程，主要从数据库中读取必要的数据信息，如相关文档集，查询词统计信息，语料集中的词组数等
	 * @param query
	 * @param relatedDocs
	 */
	public void init(List<DocTerm> queryTerms, List<Document> relatedDocs){
		System.out.println("查询扩展步骤初始化");
		this.queryTerms = queryTerms;
		for(DocTerm term : queryTerms){
			queryWords.add(term.getKeyword());
		}
		relatedDocuments = relatedDocs;
		//计算伪相关文档中词项总数
		for(Document doc : relatedDocs){
			termTotalCountR = termTotalCountR + doc.getTotalWordsCount();
		}
		//从数据库中读取整个文档集的词项总数
		termTotalCountC = documentMapper.getAllWordsCount();
		
		System.out.println("初始化完毕");
	}
	

	/**
	 * 得到相关文档relatedDocuments中的全部词
	 * @return
	 */
	public List<DocTerm> getTermsInRD(){
		System.out.println("读取相关文档中的词语");
		List<String> terms = new ArrayList<String>();
        List<DocTerm> docTerms = new ArrayList<DocTerm>();
        if(relatedDocuments==null||relatedDocuments.size()==0){
        	return null;
        }
		for(Document doc : relatedDocuments){
			String[] bodyWords = new String[doc.getBodyWordsMap().keySet().size()];
			doc.getBodyWordsMap().keySet().toArray(bodyWords);
			//System.out.println(bodyWords);
			//考虑到标题中的词一般会在内容中再次出现，这里只添加内容（body）中的词
			for(String words : bodyWords){
				if((!terms.contains(words))&&(!queryWords.contains(words))){
					terms.add(words);
				}
			}
		}//for document
		System.out.println("相关文档集合中的词语总数： " + terms.size());
		System.out.println("相关文档集合中的词语分析完毕，准备从数据库中读取");
		docTerms = docTermMapper.getDocTermsByName(terms);
		System.out.println("读取相关文档词语完毕");
		if(docTerms==null||docTerms.size()==0){
			return null;
		}
		//对文档词形式进行解析
		for(DocTerm term : docTerms){
			term.getCountMap();
		}
		return docTerms;
	}
	
	/**
	 * KLD(t)=[p_R (t)-p_C (t)]×log (p_R (t))/(p_C (t) )
	 * @param frequenceInR
	 * @param frequenceInC
	 * @param totalCountR
	 * @param totalCountC
	 * @return
	 */
	public double computeKLD(int frequenceInR, int frequenceInC, int totalCountR, int totalCountC){
		double prt = (double)frequenceInR/totalCountR;
		double pct = (double)frequenceInC/totalCountC;
		double kld = (prt-pct)*Math.log10(prt/pct);
		return kld;
	}
	
	/**
	 * 对候选扩展词集合candidateTerms按照KLD值进行排序，并选出最大的前50个词，同时把文档关键词集合进行筛选，使之与
	 * 过滤后的candidateTerms对应
	 * @param candidateTerms
	 */
	public void filterTermByKLD(){
		Collections.sort(candidateTerms, new Comparator<DocTerm>(){
			public int compare(DocTerm term1, DocTerm term2){
				if(term1.getKld() < term2.getKld())
					return 1;
				else if(term1.getKld() > term2.getKld())
					return -1;
				else
					return 0;
			}
		});
		if(candidateTerms.size()>50){
			candidateTerms = candidateTerms.subList(0, 49);
		}
	}
	
	/**
	 * 通过kld对文档词进行筛选以后，对留下的候选扩展词计算剩余的特征，包含频率信息，在相关文档集上的权重信息，共现文档频率，
	 * 统计相关性（lca），相关文档集上的BM25之和
	 * @param queryTerm
	 * @param candidateTerms
	 */
	public void computerFeatures(List<DocTerm> queryTerm, List<DocTerm> candidateTerms){
		System.out.println("开始计算扩展词特征");	
		for(DocTerm term : candidateTerms){
			//在相关文档集合以及整个文档集合上的频率特征
			double tfr = (double)term.getFrequenceInR()/termTotalCountR;
			term.setTfr(tfr);
			double tfc = (double)term.getFrequenceInC()/termTotalCountC;
			term.setTfc(tfc);

			//在相关文档集上的权值之和
			term.setWeightInR(computeWeightInR(term));
			double weightInR = 0.0;
			String word = term.getKeyword();
			int frequenceInR = 0;
			int dft = term.getBodyCountMap().size();//dft表示包含词term的文档数
			double idf = Math.log((double)newsCount/dft);
			for(Document doc : relatedDocuments){
				if(doc.getBodyWordsMap().containsKey(word)){
					frequenceInR += doc.getBodyWordsMap().get(word);
				}
			}
			weightInR = idf*frequenceInR;
			term.setWeightInR(weightInR);

			//在相关文档集上的共现文档频率
			int dfr = 0;
			for(Document doc : relatedDocuments){
				boolean isCoocurDoc = true;
				HashMap<String, Integer> bodyWordsMap =doc.getBodyWordsMap();
				if(!bodyWordsMap.containsKey(word)){
					continue;
				}
				int i =0;
				while(i < queryWords.size()){
					if(!bodyWordsMap.containsKey(queryWords.get(i))){
						isCoocurDoc = false;
						break;
					}
					i++;
				}
				if(isCoocurDoc == true){
					dfr++;
				}
			}//for dfr
			double cooc = Math.log(dfr + 0.5);
			term.setCooc(cooc);
			/*共现文档频率计算完毕*/

			//候选扩展词与查询词的统计相关性
			double similarity = 0.0;
			for(String q : queryWords){
				int coocur = 0;
				for(Document doc : relatedDocuments){
					HashMap<String, Integer> bodyWordsMap =doc.getBodyWordsMap();
					if(bodyWordsMap.containsKey(q)&&bodyWordsMap.containsKey(word)){
						coocur = coocur + bodyWordsMap.get(q)*bodyWordsMap.get(word);
					}
				}//候选扩展词与查询词q的共现频率
				
				double  codegree = (idf*Math.log(coocur)+1)/Math.log(termTotalCountR);
				DocTerm query = new DocTerm();
				for(DocTerm t: queryTerms ){
					if(t.getKeyword().equals(q)){
						query=t;
						break;
					}
				}
				int dfq = query.getBodyCountMap().size();
				double idfq = Math.log((double)newsCount/dfq);
				double sim = idfq*Math.log(0.1+codegree);
				similarity +=sim;
			}//for q
			term.setLca(similarity);

			//候选扩展词的BM25值
			double avgdl = 0.0;//相关文档集的平均长度
			for(Document doc : relatedDocuments){
				avgdl += doc.getTotalWordsCount();
			}
			avgdl = (double)avgdl/relatedDocuments.size();
			
			double bm25R = 0.0;
			int k = 2;
			double b = 0.75;
			for(Document doc : relatedDocuments){
				if(doc.getBodyWordsMap().containsKey(word)){
					int tf = doc.getBodyWordsMap().get(word);
					int dl = doc.getTotalWordsCount();
					double divideA = tf*(k+1);//分子
					double divideB = b*((double)dl/avgdl);//分母的一部分
					double bm25 = divideA/(tf+k*(1-b+b*divideB));
					bm25R +=bm25;
				}
			}
			term.setBm25(bm25R);
		}
		System.out.println("扩展词特征计算完毕");	
	}
	
	/**
	 * 计算文档词在相关文档集上的权重信息
	 * @param term
	 * @return
	 */
	public double computeWeightInR(DocTerm term){
		double weightInR = 0.0;
		String word = term.getKeyword();
		int frequenceInR = 0;
		//dft表示包含词term的文档数
		int dft = term.getBodyCountMap().size();
		double idf = Math.log((double)newsCount/dft);
		for(Document doc : relatedDocuments){
			if(doc.getBodyWordsMap().containsKey(word)){
				frequenceInR += doc.getBodyWordsMap().get(word);
			}
		}
		weightInR = idf*frequenceInR;
		return weightInR;
	}
	
	/**
	 * 按照候选扩展词的权重进行排序，取排名前10的词作为扩展词
	 * @param candidateTerms
	 */
	public void orderByWeight(List<DocTerm> candidateTerms){
		Collections.sort(candidateTerms, new Comparator<DocTerm>(){
			public int compare(DocTerm term1, DocTerm term2){
				if(term1.getWeight() < term2.getWeight())
					return 1;
				else if(term1.getWeight() > term2.getWeight())
					return -1;
				else
					return 0;
			}
		});
		if(candidateTerms.size()>10){
			candidateTerms = candidateTerms.subList(0, 9);
		}
		//对weight进行归一化
		double max = 0, min = 0;
		for(DocTerm term : candidateTerms){
			if(term.getWeight() > max){
				max = term.getWeight();
			}
			if(term.getWeight() < min){
				min = term.getWeight();
			}
		}
		for(DocTerm term : candidateTerms){
			double weight = term.getWeight();
			term.setWeight(((weight-min)/(max-min)) + 1.0);
		}
	}
	

	public int getTermTotalCountC() {
		return termTotalCountC;
	}

	public void setTermTotalCountC(int termTotalCountC) {
		this.termTotalCountC = termTotalCountC;
	}

	public int getTermTotalCountR() {
		return termTotalCountR;
	}

	public void setTermTotalCountR(int termTotalCountR) {
		this.termTotalCountR = termTotalCountR;
	}

	public int getNewsCount() {
		return newsCount;
	}

	public void setNewsCount(int newsCount) {
		this.newsCount = newsCount;
	}

	public List<Document> getRelatedDocuments() {
		return relatedDocuments;
	}

	public void setRelatedDocuments(List<Document> relatedDocuments) {
		this.relatedDocuments = relatedDocuments;
	}

	public List<DocTerm> getQueryTerms() {
		return queryTerms;
	}

	public void setQueryTerms(List<DocTerm> queryTerms) {
		this.queryTerms = queryTerms;
	}

	public List<String> getQueryWords() {
		return queryWords;
	}

	public void setQueryWords(List<String> queryWords) {
		this.queryWords = queryWords;
	}

	public List<DocTerm> getCandidateTerms() {
		return candidateTerms;
	}

	public void setCandidateTerms(List<DocTerm> candidateTerms) {
		this.candidateTerms = candidateTerms;
	}

}
