package gossip.model;

public class SimilarityCache {
	
	private int id;//自增
	private String pair;//10835-10936
	private double similarity;
	private String cacheType;// News or Event
	
	public static final String News = "news";
	public static final String Event = "event";
	
	
	
	public SimilarityCache(int id1, int id2, double similarity, String cacheType) {
		super();
		this.pair = generatePair(id1, id2);
		this.similarity = similarity;
		this.cacheType = cacheType;
	}
	
	public SimilarityCache(String pair, double similarity, String cacheType) {
		super();
		this.pair = pair;
		this.similarity = similarity;
		this.cacheType = cacheType;
	}
	public static String generatePair(int id1, int id2){
		String pair = "";
		if(id1 < id2){
			pair = id1 + "-" + id2;
		}else{
			pair = id2 + "-" + id1;
		}
		return pair;
	}
	public SimilarityCache() {
		super();
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getPair() {
		return pair;
	}
	public void setPair(String pair) {
		this.pair = pair;
	}
	public String getCacheType() {
		return cacheType;
	}
	public void setCacheType(String cacheType) {
		this.cacheType = cacheType;
	}
	public double getSimilarity() {
		return similarity;
	}
	public void setSimilarity(double similarity) {
		this.similarity = similarity;
	}
	
	

}
