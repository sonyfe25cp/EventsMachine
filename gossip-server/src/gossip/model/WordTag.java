package gossip.model;

public class WordTag {
	private int id;
	private String keywords;
	private int approve;
	private int against;
	
	public WordTag(){
		
	}
	
	public WordTag(String keywords){
		this.keywords = keywords;
	}
	
	public WordTag(String keywords, int approve, int against){
		this.keywords = keywords;
		this.approve = approve;
		this.against = against;
	}
	
	
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getKeywords() {
		return keywords;
	}
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	public int getApprove() {
		return approve;
	}
	public void setApprove(int approve) {
		this.approve = approve;
	}
	public int getAgainst() {
		return against;
	}
	public void setAgainst(int against) {
		this.against = against;
	}
	
}
