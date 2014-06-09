

public class Word {

	private int id;
	private String name;//词
	private int count;//统计出现的次数
	private double idf;//整个索引中的逆文档率
	
	
	public Word() {
		
	}
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("name:"+name);
		sb.append("\r");
		sb.append("count:"+count);
		sb.append("\r");
		sb.append("idf:"+idf);
		return sb.toString();
	}
	public Word(String name) {
		super();
		this.name = name;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public double getIdf() {
		return idf;
	}
	public void setIdf(double idf) {
		this.idf = idf;
	}
}
