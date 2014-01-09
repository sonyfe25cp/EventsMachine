package gossip.gossip.queryExpansion;

import gossip.gossip.utils.DatabaseUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

/**
 * 训练用于筛选扩展词的贝叶斯分类模型，输入为标注数据集及其所有类别，输出为关键词的每个特征对应的类别的概率
 * @author yulong
 *
 */
public class Bayes {
	
	private DataSource dataSource;
	
	private Map<String, Double> featuresProbability = new HashMap<String, Double>();
	
	private int termTotalCountC = 0;//语料集中的词组数
	private int newsCount = 0;
	
	/**
	 * 初始化dataSource，以及从数据库中读取termTotalCountC和newsCount
	 */
	public void init(){
		dataSource = DatabaseUtils.getInstance();
		Connection conn = null;
		Statement stat = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		//初始化整个文档集的词语数量
		String termCountCSql = "select sum(totalwordscount) as totalcount from document";
		String newsCountSql = "select count(*) as newscount from news";
		try {
			conn = dataSource.getConnection();
			stat = conn.createStatement();
			rs = stat.executeQuery(termCountCSql);
			if(rs.next()){
				termTotalCountC = rs.getInt("totalcount");
			}
			rs1 = stat.executeQuery(newsCountSql);
			if(rs.next()){
				newsCount = rs1.getInt("newscount");
			}
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 训练过程，根据提供的所有扩展词信息，得到分类模型，即每个特征值对应的类别的概率
	 * @param trainTerms
	 */
	public void train(List<TrainTerm> trainTerms){
		//用于存放每个类别的单词数量,并进行初始化
		Map<Integer, Integer> categoryCount = new HashMap<Integer, Integer>();
		categoryCount.put(0, 0);
		categoryCount.put(1, 0);
		
		Map<String, Integer> features = new HashMap<String, Integer>();
		//对特征map进行初始化,i表示类别，0-非扩展词1-扩展词，j表示特征项，0-tfr;1-tfc;…;6-bm25,k表示每个特征项的取值，1-特征值大于0.5;0-特征项的值小于0.5
		for(int i = 0;i < 2;i++)
		{
			for(int j = 0;j < 7;j++){
				for(int k = 0;k < 2; k++){
					String tag = String.valueOf(i)+String.valueOf(j) + String.valueOf(k);
					features.put(tag, 0);
					
				}
			}
		}
		for(TrainTerm term : trainTerms)
		{
			List<Double> values = new ArrayList<Double>();
			values.add(term.getTfr());
			values.add(term.getTfc());
			values.add(term.getWeightInR());
			values.add(term.getCooc());
			values.add(term.getKld());
			values.add(term.getLca());
			values.add(term.getBm25());
			if(term.getCategory() == 0)
			{
				categoryCount.put(0, categoryCount.get(0)+1);

			    for(int i = 0;i < values.size();i++)
			    {
			    	if(values.get(i) < 0.5)
			    	{
			    		String tag = "0" + String.valueOf(i) + "0";
			    		features.put(tag, features.get(tag) + 1);
			    	}
			    	else if(values.get(i) >= 0.5)
			    	{
			    		String tag = "0" + String.valueOf(i) + "1";
			    		features.put(tag, features.get(tag) + 1);
			    	}
			    }
			}//if
			else if(term.getCategory() == 1)
			{
				categoryCount.put(1, categoryCount.get(1)+1);

			    for(int i = 0;i < values.size();i++)
			    {
			    	if(values.get(i) < 0.5)
			    	{
			    		String tag = "1" + String.valueOf(i) + "0";
			    		features.put(tag, features.get(tag) + 1);
			    	}
			    	else if(values.get(i) >= 0.5)
			    	{
			    		String tag = "1" + String.valueOf(i) + "1";
			    		features.put(tag, features.get(tag) + 1);
			    	}
			    }
			}//else
			
		}//for
		
		//特征统计完成，接下来计算条件概率p(f|c),并存放在featuresProbability中
		for(int i = 0;i < 2;i++)
		{
			for(int j = 0;j < 7;j++){
				for(int k = 0;k < 2; k++){
					String tag = String.valueOf(i)+String.valueOf(j)+String.valueOf(k);
					//每项加1,防止出现0的情况
					double probability = (double)(features.get(tag) + 1) / (categoryCount.get(i) + 1);
					featuresProbability.put(tag, probability);
				}
			}
		} 
		
	}//train

	/**
	 * 在训练过程之前，需要得到所有的扩展词及其信息，以及计算扩展词的所有特征
	 * @return
	 */
	public List<TrainTerm> getTrainTerms(){
		dataSource = DatabaseUtils.getInstance();
		List<TrainTerm> trainTerms = new ArrayList<TrainTerm>();
		List<List<TrainTerm>> queryTerms = new ArrayList<List<TrainTerm>>();
		List<List<TrainTerm>> expansionTerms = new ArrayList<List<TrainTerm>>();
		
		Connection conn = null;
		Statement stat = null;
		PreparedStatement pstat = null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		String sql1 = "select * from trainterm";
		String sql2 = "select * from termenum where keywords = ?";
		try {
			stat = conn.createStatement();
			rs = stat.executeQuery(sql1);
			while(rs.next()){
				String[] queryWords = rs.getString("querywords").split(";");
				String[] expansionWords = rs.getString("expansionwords").split(";");
				List<TrainTerm> queryTerm = new ArrayList<TrainTerm>();
				List<TrainTerm> expansionTerm = new ArrayList<TrainTerm>();
				for(String word : queryWords)
				{
					TrainTerm term = new TrainTerm(word);
					pstat = conn.prepareStatement(sql2);
					pstat.setString(1, word);
					rs2 = pstat.executeQuery();
					term.setProperty(rs.getString("property"));
					term.setDocIdString(rs.getString("docIds"));
					term.setBodyCountString(rs.getString("bodycounts"));
					term.setTitleCountString(rs.getString("titlecounts"));
					term.getCountMap();
					term.setWeight(1.0);
					queryTerm.add(term);
				}
				queryTerms.add(queryTerm);
				for(String word : expansionWords)
				{
					String[] expansion = word.split(":");
					TrainTerm term = new TrainTerm(expansion[0]);
					term.setCategory(Integer.valueOf(expansion[1]));
					pstat = conn.prepareStatement(sql2);
					pstat.setString(1, word);
					rs2 = pstat.executeQuery();
					term.setProperty(rs.getString("property"));
					term.setDocIdString(rs.getString("docIds"));
					term.setBodyCountString(rs.getString("bodycounts"));
					term.setTitleCountString(rs.getString("titlecounts"));
					term.getCountMap();
					expansionTerm.add(term);
				}
				expansionTerms.add(expansionTerm);
			}
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//计算扩展词的特征
		computeFeatures(queryTerms, expansionTerms);
		//将所有训练集中的词整合
		for(int i =0;i<expansionTerms.size();i++){
			trainTerms.addAll(expansionTerms.get(i));
		}
		return trainTerms;
	}
	
	/**
	 * 计算扩展词特征的过程
	 * @param queryTerms
	 * @param expansionTerms
	 */
	public void computeFeatures(List<List<TrainTerm>> queryTerms, List<List<TrainTerm>> expansionTerms){
		if(queryTerms==null||expansionTerms==null){
			System.out.println("训练集中查询词和扩展词为空");
			return;
		}
		for(int i = 0;i < queryTerms.size();i++)
		{
			List<TrainTerm> queryTerm = queryTerms.get(i);
			List<String> queryWords = new ArrayList<String>();
			for(TrainTerm term : queryTerm){
				queryWords.add(term.getWords());
			}
			//获取包含查询词的文档集
			List<Document> documents = getDocumentFromDB(queryTerms.get(i));
			List<Document> relatedDocs = new ArrayList<Document>();
			//计算文档的BM25值并排序，返回得分最高的10篇文档
			relatedDocs = getRelatedDocs(queryTerms.get(i), documents);
			//计算与查询词对应的标注扩展词的各项特征
			List<TrainTerm> trainTerms = expansionTerms.get(i);
			for(TrainTerm term : trainTerms)
			{
				String word = term.getWords();
				int frequenceInR = 0;
				int frequenceInC = 0;
				int termTotalCountR = 0;//相关文档中的词语总数
				//计算关键词在相关文档中的频率
				for(Document doc : relatedDocs)
				{
					termTotalCountR = termTotalCountR + doc.getTotalWordsCount();
					if(doc.getBodyWordsMap().containsKey(term.getWords())){
						frequenceInR += doc.getBodyWordsMap().get(term.getWords());
					}
				}
				term.setFrequenceInR(frequenceInR);
				
				//计算关键词在整个文档集上的频率
				List<Integer> frequences = new ArrayList<Integer>(term.getBodyCountMap().values());
				for(int fre : frequences){
					frequenceInC += fre;
				}
				term.setFrequenceInC(frequenceInC);
				
				//kld
				double kld = computeKLD(frequenceInR, frequenceInC, termTotalCountR, termTotalCountC);
				term.setKld(kld);
				
				//在相关文档集合以及整个文档集合上的频率特征
				double tfr = (double)term.getFrequenceInR()/termTotalCountR;
				term.setTfr(tfr);
				double tfc = (double)term.getFrequenceInC()/termTotalCountC;
				term.setTfc(tfc);
				
				//在相关文档集上的权值之和
				double weightInR = 0.0;
				int dft = term.getBodyCountMap().size();//dft表示包含词term的文档数
				double idf = Math.log((double)newsCount/dft);
				weightInR = idf*frequenceInR;
				term.setWeightInR(weightInR);
				
				//在相关文档集上的共现文档频率
				int dfr = 0;
				for(Document doc : relatedDocs){
					boolean isCoocurDoc = true;
					HashMap<String, Integer> bodyWordsMap = doc.getBodyWordsMap();
					if(!bodyWordsMap.containsKey(word)){
						continue;
					}
					int j =0;
					while(j < queryWords.size()){
						if(!bodyWordsMap.containsKey(queryWords.get(j))){
							isCoocurDoc = false;
							break;
						}
						j++;
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
					for(Document doc : relatedDocs){
						HashMap<String, Integer> bodyWordsMap =doc.getBodyWordsMap();
						if(bodyWordsMap.containsKey(q)&&bodyWordsMap.containsKey(word)){
							coocur = coocur + bodyWordsMap.get(q)*bodyWordsMap.get(word);
						}
					}//候选扩展词与查询词q的共现频率
					
					double  codegree = (idf*Math.log(coocur)+1)/Math.log(termTotalCountR);
					TrainTerm query = new TrainTerm();
					for(TrainTerm t: queryTerm ){
						if(t.getWords().equals(q)){
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
				double avgdl = ((double)(termTotalCountR)/(relatedDocs.size()));//相关文档集的平均长度
				double bm25R = 0.0;
				int k = 2;
				double b = 0.75;
				for(Document doc : relatedDocs){
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
			}//for term，对于特定查询中某一个扩展词的特征

		}//for 每一次查询（包含一组查询关键词、一组扩展词和相关文档）
	}
	
	/**
	 * 根据词的keyword从数据库中读取关键词的所有信息，以备计算特征所需
	 * @param query
	 */
	public void getTrainTermInfoFromDB(List<TrainTerm> query){
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet rs = null;
		String sql = "select * from termenum where keywords = ?";
		
		System.out.println("开始从数据库读取查询词信息");
		try {
			
			stat = conn.prepareStatement(sql);
			for(TrainTerm term : query){
				stat.setString(1, term.getWords());
				rs = stat.executeQuery();
				term.setProperty(rs.getString("property"));
				term.setDocIdString(rs.getString("docIds"));
				term.setBodyCountString(rs.getString("bodycounts"));
				term.setTitleCountString(rs.getString("titlecounts"));
				term.getCountMap();
				term.setWeight(1.0);
			}
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	/**
	 * 根据关键词信息读取包含该关键词的文档
	 * @param trainTerms
	 * @return
	 */
	public List<Document> getDocumentFromDB(List<TrainTerm> trainTerms){
		List<Document> documents = new ArrayList<Document>();
		System.out.println("开始读取包含查询词的文档");
		if(trainTerms==null){
			return null;
		}
		List<Document> docs = new ArrayList<Document>();
		Set<Integer> docIds = new HashSet<Integer>();
		for(TrainTerm term : trainTerms){
			String[] ids = term.getDocIdString().split(";");
			for(String id : ids){
				docIds.add(Integer.parseInt(id));
			}
		}
		if(docIds==null||docIds.size()==0)
			return null;
			
		Connection conn = null;
		Statement stat = null;
		ResultSet rs = null;
		String docSql = "select * from document where id in (";
		Iterator<Integer> it = docIds.iterator();
		while(it.hasNext()){
			docSql = docSql + "'" + it.next() + "',";
		}
		docSql = docSql.substring(0, docSql.length()-1);
		docSql = docSql + ")";
		//System.out.println(docSql);
		
		try {
			conn = dataSource.getConnection();
			stat = conn.createStatement();
			rs = stat.executeQuery(docSql);
			while(rs.next()){
				Document doc = new Document();
				doc.setId(rs.getInt("id"));
				doc.setTotalWordsCount(rs.getInt("totalwordscount"));
				doc.setTitleWordsCount(rs.getInt("titlewordscount"));
				if(rs.getInt("bodywordscount")>300){
					continue;
				}
				doc.setBodyWordsCount(rs.getInt("bodywordscount"));
				doc.setBodyWords(rs.getString("bodywords"));
				doc.setTitleWords(rs.getString("titlewords"));
				documents.add(doc);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			try {
				System.out.println(rs.getInt("bodywordscount"));
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		//进行一次简单筛选，只将包含所有查询词的文档选出
		for(Document doc : documents){
			boolean flag = true;
			for(TrainTerm term : trainTerms){
				if(!doc.getBodyWordsMap().containsKey(term.getWords())){
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
	 * 根据查询词，使用BM25方法检索与查询词最相关的文档集
	 * @param queryTerms
	 * @param documents
	 * @return
	 */
	public List<Document> getRelatedDocs(List<TrainTerm> queryTerms, List<Document> documents){
		System.out.println("开始计算文档的bm25值");
		double avgdl = 0.0;
		for(Document doc : documents){
			avgdl += doc.getBodyWordsCount();
		}
		avgdl = (double)avgdl/documents.size();
		
		for(Document doc : documents){
			double BM25 = 0.0;
			for(TrainTerm queryTerm : queryTerms){
				String word = queryTerm.getWords();
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
		//取bm25最大的10篇文档返回
		if(documents.size()>10){
			documents = documents.subList(0, 10);
		}
		System.out.println("bm25计算完毕，并进行排序");
		return documents;
		
	}
	
	/**
	 * 从数据库中读取语料库中所有关键词个数信息
	 * @return
	 */
	public int getWordsCountC(){
		//从数据库中读取整个文档集的词项总数
		dataSource = DatabaseUtils.getInstance();
		Connection conn = null;
		Statement stat = null;
		ResultSet rs = null;
		//初始化整个文档集的词语数量
		String termCountC = "select sum(totalwordscount) as totalcount from document";
		try {
			conn = dataSource.getConnection();
			stat = conn.createStatement();
			rs = stat.executeQuery(termCountC);
			if(rs.next()){
				termTotalCountC = rs.getInt("totalcount");
			}
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return termTotalCountC;

	}
	
	/**
	 * 计算bm25值
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
	 * 计算kld值，KLD(t)=[p_R (t)-p_C (t)]×log (p_R (t))/(p_C (t) )
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
}
