package gossip.sim;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.MMapDirectory;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKAnalyzer;

import edu.bit.dlde.utils.DLDEConfiguration;

/**
 * 读取相似度的类。 它负责从相似读文件中读取相似度符合一定阈值的相似文件对。
 */
public class SimilarityReader {
	/** 存放相似读文件的路径 **/
	private String simInfoPath = DLDEConfiguration.getInstance(
			"gossip.properties").getValue("SimInfoPath");
	/** 存放从相似度文件里面读取的文档对相似度的列表,必须处于左右边界之中 **/
	private List<SimilarDocPair> similarDocPairList;
	/** 存放相同文档对的列表，相似读大于右边界 **/
	private List<SimilarDocPair> sameDocPairList;
	private double leftFrontier = Double.parseDouble(DLDEConfiguration
			.getInstance("gossip.properties").getValue("LeftFrontier"));// 相似阈值,左边界
	private double rightFrontier = Double.parseDouble(DLDEConfiguration
			.getInstance("gossip.properties").getValue("RightFrontier"));// 相同阈值，右边界
	private String indexPath = DLDEConfiguration.getInstance("gossip.properties")
			.getValue("IndexPath");
	private String simbakPath = DLDEConfiguration.getInstance("gossip.properties")
			.getValue("SimBackupPath");
	/**
	 * 从索引中删除被认定为相同的文档
	 */
	public void refineIndex(List<SimilarDocPair> sameList) {// 删掉重复
		IndexWriter iw = null;
		IKAnalyzer analyzer = new IKAnalyzer();
		try {
			MMapDirectory dir = new MMapDirectory(new File(indexPath));

			IndexWriterConfig conf = new IndexWriterConfig(Version.LUCENE_35,
					analyzer);

			iw = new IndexWriter(dir, conf);

			for (SimilarDocPair sr : sameList) {
				iw.deleteDocuments(new Term("id", sr.getDoc2() + ""));
			}
			iw.commit();
			iw.close();
		} catch (Exception e) {

		}
	}

	Date latest;
	/**
	 * 根据给定的field和date从文件中读取文档相似度结果。 解析找到的文档相似读文件，通过比较阈值来确定相似文档和相同文档。
	 * 
	 * @param field
	 *            域，也就是body，title等等，不能为空。
	 * @param date
	 *            时间，精确到日，标准格式是2012-5-20。当给定的date是null时则读取最新的一个相似度文件。
	 *            假如给定的时间没有对应的文件则不会进行操作。
	 */
	public void readSimilarityFromFile(String field, String date) {
		/** 当field为空时直接返回 **/
		if (field == null)
			return;

		File simInfoFile = null;
		/** 当date为空时，则找出最新的文档 **/
		if (date == null||date.equals("")) {
			File dir = new File(simInfoPath);
			File[] files = dir.listFiles();
			Date dateOfFile;
			latest = new Date(0);
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			for (File f : files) {
				try {
					dateOfFile = dateFormat.parse(getDateFromSimInfoFileName(f.getName()));
					if (dateOfFile.after(latest)) {
						latest = dateOfFile;
						simInfoFile = f;
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		} else {// 否则根据命名规则读文件
			String simInfoFileName;
			simInfoFileName = SimilarityReader.getSimInfoFileName(simInfoPath,	field, date);
			/** 当文件不存在是直接返回 **/
			simInfoFile = new File(simInfoFileName);
			DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
			try {
				latest = dateFormat.parse(date);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			if (!simInfoFile.exists())
				return;
		}

		if(simInfoFile==null)
			return;
		
		/** 读取相似度文件,并根据相似读将结果加入列表 **/
		similarDocPairList = new ArrayList<SimilarDocPair>();
		sameDocPairList = new ArrayList<SimilarDocPair>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(simInfoFile));
			String line = br.readLine();
			SimilarDocPair sdp = null;
			while (line != null) {
				sdp = parseFromLine(line);
				if (sdp.getSimilarity() > rightFrontier) {// 大于阈值的为相同页面，没有意义
					sameDocPairList.add(sdp);
					line = br.readLine();
					continue;
				}
				if (sdp.getSimilarity() < leftFrontier) {// 小于阈值的没有意义
					break;
				}
				similarDocPairList.add(sdp);
				line = br.readLine();
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		simInfoFile.renameTo(new File(simbakPath + simInfoFile.getName()));
	}

	/**
	 * 获得经readSimilarityFromFile处理过的文档名字里面包含的时间
	 */
	public Date getDateInLastProcessedFile(){
		return latest;
	}
	
	/**
	 * 将从相似读文件里读入的每行string解析为SimilarDocPair
	 */
	private SimilarDocPair parseFromLine(String line) {
		// logger.info("sim--"+i+"--"+j+"--:"+sim);
		try {
			String[] docs = line.split(" ");

			int doc1 = Integer.parseInt(docs[0]);
			int doc2 = Integer.parseInt(docs[1]);

			double sim = Double.parseDouble(docs[2]);
			SimilarDocPair sr = new SimilarDocPair(doc1, doc2, sim);
			return sr;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 统一定义如何命名相似度文件
	 */
	public static String getSimInfoFileName(String base, String field,
			String date) {
		return base + (field == null ? "" : ("_" + field)) + "_" + date
				+ ".txt";
	}

	/**
	 * 统一定义了如何从相似度文件中读取时间
	 * YYYY-MM-DD
	 */
	public static String getDateFromSimInfoFileName(String fileName) {
		int begin = fileName.lastIndexOf("_") + 1, end = fileName.length() - 4;
		String date= fileName.substring(begin, end);//20120712
		String year = date.substring(0, 4);
		String month = date.substring(4,6);
		String day = date.substring(6, 8);
		return year+"-"+month+"-"+day;
	}

	public String getSimilarityFilePath() {
		return simInfoPath;
	}

	public void setSimilarityFilePath(String similarityFilePath) {
		this.simInfoPath = similarityFilePath;
	}

	public List<SimilarDocPair> getSimilarDocPairList() {
		return similarDocPairList;
	}

	public void setSimilarDocPairList(List<SimilarDocPair> similarDocPairList) {
		this.similarDocPairList = similarDocPairList;
	}

	public List<SimilarDocPair> getSameDocPairList() {
		return sameDocPairList;
	}

	public void setSameDocPairList(List<SimilarDocPair> sameDocPairList) {
		this.sameDocPairList = sameDocPairList;
	}

	public double getLeftFrontier() {
		return leftFrontier;
	}

	public void setLeftFrontier(double leftFrontier) {
		this.leftFrontier = leftFrontier;
	}

	public double getRightFrontier() {
		return rightFrontier;
	}

	public void setRightFrontier(double rightFrontier) {
		this.rightFrontier = rightFrontier;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SimilarityReader sreader = new SimilarityReader();
		sreader.readSimilarityFromFile("title", null);
		// List<SimilarDocPair> sameList = sreader.getSameDocPairList();
		List<SimilarDocPair> srList = sreader.getSimilarDocPairList();

		Collections.sort(srList);

		// for(SimResult sr:sameList){
		// System.out.println(sr);
		// }

		/*
		 * , new Comparator() {
		 * 
		 * public int compare(Object o1, Object o2) { SimilarDocPair s1 =
		 * (SimilarDocPair) o1; SimilarDocPair s2 = (SimilarDocPair) o2; if
		 * (s1.getDoc1() > s2.getDoc1()) { return 1; } else { return -1; } }
		 * 
		 * }
		 */
		//
		System.out
				.println("---------------------------------------------------------------------");
		// sreader.refineIndex(sameList);
		for (SimilarDocPair sr : srList) {
			System.out.println(sr);
		}
	}

}
