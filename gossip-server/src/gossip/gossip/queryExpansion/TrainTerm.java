package gossip.gossip.queryExpansion;

import java.util.HashMap;

public class TrainTerm {
	private int id;
	// 单词
	private String words;
	// 单词的属性
	private String property;
	// 单词的倒排索引，记录单词出现的文档号，以及在文档中出现的频率（title和body分开存储）
	private String docIdString;
	private String titleCountString;
	private String bodyCountString;
	private HashMap<Integer, Integer> titleCountMap = new HashMap<Integer, Integer>();
	private HashMap<Integer, Integer> bodyCountMap = new HashMap<Integer, Integer>();

	// frequenceInR,frequenceInC暂存相关文档R和文档集合C中关键词的频率信息，方便后续特征的计算
	private int frequenceInR;
	private int frequenceInC;
	// 关键词在相关文档中的频率特征
	private double tfr;
	// 在整个文档集上的频率特征
	private double tfc;
	// 关键词在相关文档集上的权重，即tf*idf之和
	private double weightInR;
	// 关键词与查询词的共现文档频率
	private double cooc;
	private double kld;
	private double lca;
	private double bm25;
	// 标注数据中关键词所属的类别，0表示非扩展词，1表示是扩展词
	private int category;
	private double weight = 0.0;

	public TrainTerm() {

	}

	public TrainTerm(String words) {
		this.words = words;
	}

	public void getCountMap() {
		String[] docIds = docIdString.split(";");
		String[] titleCounts = titleCountString.split(";");
		String[] bodyCounts = bodyCountString.split(";");
		if (docIds.length != titleCounts.length
				|| docIds.length != bodyCounts.length
				|| titleCounts.length != bodyCounts.length) {
			System.out.println("词语与文档数量不匹配！！！DocTerm.java");
			return;
		} else {
			for (int i = 0; i < docIds.length; i++) {
				titleCountMap.put(Integer.parseInt(docIds[i]),
						Integer.parseInt(titleCounts[i]));
				bodyCountMap.put(Integer.parseInt(docIds[i]),
						Integer.parseInt(bodyCounts[i]));
			}
		}
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getWords() {
		return words;
	}

	public void setWords(String words) {
		this.words = words;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public String getDocIdString() {
		return docIdString;
	}

	public void setDocIdString(String docIdString) {
		this.docIdString = docIdString;
	}

	public String getTitleCountString() {
		return titleCountString;
	}

	public void setTitleCountString(String titleCountString) {
		this.titleCountString = titleCountString;
	}

	public String getBodyCountString() {
		return bodyCountString;
	}

	public void setBodyCountString(String bodyCountString) {
		this.bodyCountString = bodyCountString;
	}

	public HashMap<Integer, Integer> getTitleCountMap() {
		return titleCountMap;
	}

	public void setTitleCountMap(HashMap<Integer, Integer> titleCountMap) {
		this.titleCountMap = titleCountMap;
	}

	public HashMap<Integer, Integer> getBodyCountMap() {
		return bodyCountMap;
	}

	public void setBodyCountMap(HashMap<Integer, Integer> bodyCountMap) {
		this.bodyCountMap = bodyCountMap;
	}

	public int getFrequenceInR() {
		return frequenceInR;
	}

	public void setFrequenceInR(int frequenceInR) {
		this.frequenceInR = frequenceInR;
	}

	public int getFrequenceInC() {
		return frequenceInC;
	}

	public void setFrequenceInC(int frequenceInC) {
		this.frequenceInC = frequenceInC;
	}

	public double getTfr() {
		return tfr;
	}

	public void setTfr(double tfr) {
		this.tfr = tfr;
	}

	public double getTfc() {
		return tfc;
	}

	public void setTfc(double tfc) {
		this.tfc = tfc;
	}

	public double getWeightInR() {
		return weightInR;
	}

	public void setWeightInR(double weightInR) {
		this.weightInR = weightInR;
	}

	public double getCooc() {
		return cooc;
	}

	public void setCooc(double cooc) {
		this.cooc = cooc;
	}

	public double getKld() {
		return kld;
	}

	public void setKld(double kld) {
		this.kld = kld;
	}

	public double getLca() {
		return lca;
	}

	public void setLca(double lca) {
		this.lca = lca;
	}

	public double getBm25() {
		return bm25;
	}

	public void setBm25(double bm25) {
		this.bm25 = bm25;
	}

	public int getCategory() {
		return category;
	}

	public void setCategory(int category) {
		this.category = category;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}
	
	

}
