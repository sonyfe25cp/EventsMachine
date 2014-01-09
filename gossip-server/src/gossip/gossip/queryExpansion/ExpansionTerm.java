package gossip.gossip.queryExpansion;

public class ExpansionTerm {
	private int id;
	private String keyword;
	//frequenceInR,frequenceInC暂存相关文档R和文档集合C中关键词的频率信息，方便后续特征的计算
	private int frequenceInR;
	private int frequenceInC;
	// 关键词在相关文档中的频率特征
	private double tfr;
	// 在整个文档集上的频率特征
	private double tfc;
	// 关键词在整个文档集的逆文档频率
	private double idf;
	// 关键词与查询词的共现文档频率
	private double cooc;
	private double kld;
	private double lca;
	private double bm25;
	// 0表示未出现在标题中
	private int isInTitle = 0;
	//扩展词最终的权重
	private double weight;
	
	public ExpansionTerm(){
		
	}
	
    public ExpansionTerm(int id){
		this.id = id;
	}
    
    public ExpansionTerm(String keyword){
		this.keyword = keyword;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
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

	public double getIdf() {
		return idf;
	}

	public void setIdf(double idf) {
		this.idf = idf;
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

	public int getIsInTitle() {
		return isInTitle;
	}

	public void setIsInTitle(int isInTitle) {
		this.isInTitle = isInTitle;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}
	
	
	
	

}
