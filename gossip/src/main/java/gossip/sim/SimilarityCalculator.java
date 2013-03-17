package gossip.sim;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.TermFreqVector;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.MMapDirectory;

import edu.bit.dlde.math.VectorCompute;
import edu.bit.dlde.utils.DLDEConfiguration;
import edu.bit.dlde.utils.DLDELogger;
import gossip.utils.DateTrans;

/**
 * 一个用来计算相似读的工具类。能够计算文件相似度，过滤过于相似和不相似文档对，保存相似文档对
 * 
 * @author ChenJie
 * 
 */
public class SimilarityCalculator {
	private DLDELogger logger = new DLDELogger();
	private double leftFrontier = Double.parseDouble(DLDEConfiguration
			.getInstance("gossip.properties").getValue("LeftFrontier"));// 相似阈值,左边界
	private double rightFrontier = Double.parseDouble(DLDEConfiguration
			.getInstance("gossip.properties").getValue("RightFrontier"));// 相同阈值，右边界
	private String indexPath = DLDEConfiguration.getInstance(
			"gossip.properties").getValue("IndexPath");// 索引路径
	private String simInfoPath = DLDEConfiguration.getInstance(
			"gossip.properties").getValue("SimInfoPath");// 存放相似读信息的路径

	/**
	 * 计算并获得所有的field那个域的相似度,横跨所有时间
	 * 
	 * @param field
	 *            域，例如body，title
	 * @return 相似文档对列表
	 */
	public List<SimilarDocPair> getSimilarDocPairs(String field) {
		return getSimilarDocPairs(0, 10, field);
	}

	public List<SimilarDocPair> getSimilarDocPairs(int date, int range, String field){
		if (field == null)
			return null;
		List<SimilarDocPair> sdplist = new ArrayList<SimilarDocPair>();
		try {
			/** 读索引 **/
			MMapDirectory dir = new MMapDirectory(new File(indexPath));
			IndexReader ir = IndexReader.open(dir, true);
			IndexSearcher is = new IndexSearcher(ir);
			TopDocs docs = null;

			logger.info("maxDoc:"+is.maxDoc());
			
			/** 将读出来的东西放进List<DocVector>，设总共N个;同时向前追溯back天 **/
			int endDate = kdaysbefore(date,range);
			NumericRangeQuery nrq= NumericRangeQuery.newIntRange("crawlat", endDate,date, true, true);
			docs = is.search(nrq,is.maxDoc());
			ScoreDoc[] sdocs = docs.scoreDocs;
			logger.info(field + "field "+"date at - "+date+"to "+endDate +", sdocs.size: "+sdocs.length);
			List<DocVector> vectors = new ArrayList<DocVector>(sdocs.length);
			DocVector docVector = null;
			for (int i = 0; i < sdocs.length; i++) {
				int docNumber = sdocs[i].doc;
				TermFreqVector vector = ir.getTermFreqVector(docNumber, field);
				String id = is.doc(docNumber).get("id");//---------
				docVector = new DocVector(id, vector);
				vectors.add(docVector);
			}
			TermFreqVector v1;
			TermFreqVector v2;
			double similarity;
			for (int i = 0; i < vectors.size() ; i ++) {
				int id1 = vectors.get(i).getDocId();
				v1 = vectors.get(i).getVector();
				for (int j = i + 1; j < vectors.size(); j++) {
					v2 = vectors.get(j).getVector();
					int id2 = vectors.get(j).getDocId();
					similarity = calculateSimilarity(v1, v2);
					sdplist.add(new SimilarDocPair(id1, id2, similarity));
				}
			}
			/** 根据相似度降序排列 **/
			Collections.sort(sdplist);

			is.close();
			ir.close();
			dir.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return sdplist;
	}
	
	private int kdaysbefore(int date,int range){
		int day = date;
		for(int i = 0 ; i < range ; i ++){
			day = DateTrans.theDayBeforeYYMMDD(day);
		}
		return day;
	}
	
	/**
	 * 查date那天,field那个域的文档，然后计算他们的相似度
	 * 
	 * @param date
	 *            int表示的date具体计算方式为year * 12 * 31 + month * 31 + day
	 * @param field
	 *            域，也就是body，title等等
	 * @param back 回溯back天
	 * @return 降序排列的SimilarDocPair列表，该列表并未进行删除不在相似度范围内的文本对
	 */
//	public List<SimilarDocPair> getSimilarDocPairs(int date, String field,int back) {
//		if (field == null)
//			return null;
//
//		List<SimilarDocPair> sdplist = new ArrayList<SimilarDocPair>();
//		try {
//			/** 读索引 **/
//			MMapDirectory dir = new MMapDirectory(new File(indexPath));
//			IndexReader ir = IndexReader.open(dir, true);
//			IndexSearcher is = new IndexSearcher(ir);
//			TopDocs docs = null;
//
//			logger.info("maxDoc:"+is.maxDoc());
//			
//			/** 将读出来的东西放进List<DocVector>，设总共N个;同时向前追溯back天 **/
//			docs = is.search(new TermQuery(new Term("crawlat", date + "")),is.maxDoc());
//			ScoreDoc[] sdocs = docs.scoreDocs;
//			logger.info("date at - "+date+" sdocs.size: "+sdocs.length);
//			List<DocVector> docVectorsA = new ArrayList<DocVector>(sdocs.length);
//			List<DocVector> docVectorsB = new ArrayList<DocVector>();
//			DocVector docVector = null;
//			for (int i = 0; i < sdocs.length; i++) {
//				int docNumber = sdocs[i].doc;
//				TermFreqVector vector = ir.getTermFreqVector(docNumber, field);
//				is.doc(docNumber).get("id");//---------
//				docVector = new DocVector(docNumber, vector);
//				docVectorsA.add(docVector);
//				docVectorsB.add(docVector);
//			}
//			int newDate = DateTrans.theDayBeforeYYMMDD(date);
//			for(int tmp = 0 ; tmp<back ; tmp++){
//				docs = is.search(
//						new TermQuery(new Term("date_int", newDate + "")),
//						is.maxDoc());
//				sdocs = docs.scoreDocs;
//				logger.info("date at "+newDate+" sdocs.size: "+sdocs.length);
//				for (int i = 0; i < sdocs.length; i++) {
//					int docNumber = sdocs[i].doc;
//					TermFreqVector vector = ir.getTermFreqVector(docNumber, field);
//					docVector = new DocVector(docNumber, vector);
//					docVectorsB.add(docVector);
//				}
//				try{
//					newDate = DateTrans.theDayBeforeYYMMDD(newDate);
//				}catch (DateException e) {
//					break;
//				}
//			}
//
//			/** 计算N*M次相似读，并将结果放入List<SimilarDocPair> **/
//			TermFreqVector v1;
//			TermFreqVector v2;
//			int doc1, doc2;
//			double similarity;
//			for (int i = 0; i < docVectorsA.size(); i++) {
//				v1 = docVectorsA.get(i).getVector();
//				doc1 = docVectorsA.get(i).getDocId();
//				for (int j = i + 1; j < docVectorsB.size(); j++) {
//					v2 = docVectorsB.get(j).getVector();
//					doc2 = docVectorsB.get(j).getDocId();
//					similarity = calculateSimilarity(v1, v2);
//					sdplist.add(new SimilarDocPair(doc1, doc2, similarity));
//				}
//			}
//			/** 根据相似度降序排列 **/
//			Collections.sort(sdplist);
//
//			is.close();
//			ir.close();
//			dir.close();
//		}catch (Exception e) {
//			e.printStackTrace();
//		}
//		return sdplist;
//	}

	/**
	 * 根据rightFrontier和leftFrontier对SimilarDocPair列表进行重铸。
	 * 输入的srlist不会被修改，返回的是一个新的SimilarDocPair列表。
	 * 
	 * @param sdplist
	 *            需要被重铸的对象
	 * @return 新建立的一个List<SimilarDocPair>
	 */
	public List<SimilarDocPair> refineSimList(List<SimilarDocPair> sdplist) {
		List<SimilarDocPair> list = new ArrayList<SimilarDocPair>();
		for (SimilarDocPair sdp : sdplist) {
			if (sdp.getSimilarity() == 0) {
				break;
			}
			if (sdp.getSimilarity() > rightFrontier) {
				continue;
			}
			if (sdp.getSimilarity() < leftFrontier) {
				break;
			}
			list.add(sdp);
		}
		return list;
	}

	/**
	 * 保存相似文档对到txt文件
	 */
	public void saveSimilarDocPairs(String field, List<SimilarDocPair> sdplist) {
		saveSimilarDocPairs(field, sdplist, null);
	}

	/**
	 * 保存相似文档对到txt文件
	 */
	public void saveSimilarDocPairs(List<SimilarDocPair> sdplist) {
		saveSimilarDocPairs(null, sdplist, null);
	}

	/**
	 * 保存相似文档对到txt文件
	 */
	public void saveSimilarDocPairs(List<SimilarDocPair> sdplist, String date) {
		saveSimilarDocPairs(null, sdplist, date);
	}

	/**
	 * 所有的saveSimilarDocPairs()最终调用的方法，它根据配置文件的路径和{@link SimilarityReader}
	 * 里面的命名方法保存相似文档对到txt文件
	 */
	public void saveSimilarDocPairs(String field, List<SimilarDocPair> sdplist,
			String date) {
		if (sdplist == null || sdplist.size() == 0) {
			System.out.println("no data to write...");
			return;
		}

		String today = "";
		if (date == null) {
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			today = df.format(new Date()) + "_all";
		} else {
			today = date;
		}

		String simInfoFileName = SimilarityReader.getSimInfoFileName(
				simInfoPath, field, today);

		FileWriter fw;
		try {
			fw = new FileWriter(new File(simInfoFileName));
			for (SimilarDocPair sr : sdplist) {
				if (sr.getSimilarity() == 0) {
					break;
				}
				if (sr.getSimilarity() > rightFrontier) {
					continue;
				}
				if (sr.getSimilarity() < leftFrontier) {
					break;
				}
				fw.write(sr.toString() + "\n");
			}
			fw.flush();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 共内部使用的计算两个向量相似读的方法
	 */
	double calculateSimilarity(TermFreqVector vector1, TermFreqVector vector2) {
		if(vector1==null||vector2==null)
			return 0.0;
		
		String terms1[] = vector1.getTerms();
		String terms2[] = vector2.getTerms();

		HashMap<String, Integer> totalMap = new HashMap<String, Integer>();
		for (String term : terms1) {
			totalMap.put(term, 1);
		}
		for (String term : terms2) {
			if (totalMap.containsKey(term)) {
				totalMap.put(term, 3);
			} else {
				totalMap.put(term, 2);
			}
		}
		int length = totalMap.size();
		double v1[] = new double[length];
		double v2[] = new double[length];
		int i = 0;
		for (Entry<String, Integer> entry : totalMap.entrySet()) {
			int value = entry.getValue();
			switch (value) {
			case 1:
				v1[i] = 1;
				v2[i] = 0;
				break;
			case 2:
				v2[i] = 1;
				v1[i] = 0;
				break;
			case 3:
				v1[i] = 1;
				v2[i] = 1;
				break;
			default:
				logger.info("somthing wrong");
			}
			i++;
		}

		// VectorCompute.viewVector(v1);
		// VectorCompute.viewVector(v2);

		double sim = VectorCompute.cosValue(v1, v2);
		// logger.info("sim:" + sim);

		return sim;
	}

	/**
	 * 这个是干啥咩子的捏
	 */
	@Deprecated
	public void sim_field_2(String field) {
		int totalNum = 0;
		List<SimilarDocPair> srlist = new ArrayList<SimilarDocPair>();
		try {
			MMapDirectory dir = new MMapDirectory(new File(indexPath));
			IndexReader ir = IndexReader.open(dir, true);
			totalNum = ir.maxDoc();
			System.out.println("total:" + totalNum);
			TermFreqVector[] vectors = new TermFreqVector[totalNum];
			for (int i = 0; i < totalNum; i++) {
				TermFreqVector vector = ir.getTermFreqVector(i, field);
				vectors[i] = vector;
			}
			ir.close();
			dir.close();

			TermFreqVector v1;
			TermFreqVector v2;
			for (int i = 0; i < vectors.length; i++) {
				v1 = vectors[i];
				// long t1=System.currentTimeMillis();
				for (int j = i + 1; j < vectors.length; j++) {
					v2 = vectors[j];
					double sim = calculateSimilarity(v1, v2);
					// logger.info("sim--"+i+"--"+j+"--:"+sim);
					srlist.add(new SimilarDocPair(i, j, sim));
				}
				// long t2 = System.currentTimeMillis();
				// logger.info("t2-t1:"+(t2-t1));
				logger.info("i: " + i + " over");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {

		}
		Collections.sort(srlist);
		saveSimilarDocPairs(field, srlist);
	}

	/**
	 * 用来保存文本id及其项频率向量的类
	 */
	public class DocVector {
		private int docId;
		private TermFreqVector vector;

		public DocVector(String docId, TermFreqVector vector){
			super();
			this.docId = Integer.parseInt(docId);
			this.vector = vector;
		}
		
		public DocVector(int docId, TermFreqVector vector) {
			super();
			this.docId = docId;
			this.vector = vector;
		}

		public int getDocId() {
			return docId;
		}

		public void setDocId(int docId) {
			this.docId = docId;
		}

		public TermFreqVector getVector() {
			return vector;
		}

		public void setVector(TermFreqVector vector) {
			this.vector = vector;
		}
	}
}
