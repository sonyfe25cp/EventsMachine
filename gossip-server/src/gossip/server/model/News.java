package gossip.server.model;

public class News {
	private int id;// id
	private String title;// 标题
	private String body;// 正文
	private String url;
	private String author = "";
	private String description = "";// 摘要
	private String date = "";
	private String fromsite = "";
	private String crawlat = "";
	private String status = NEW;
	private String startedLocation = "";
	private String keywords = "";

	public final static String NEW = "new";
	public final static String INDEX = "index";
	public final static String DELETE = "delete";
	public final static String UPDATE = "update";

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getFromsite() {
		return fromsite;
	}

	public void setFromsite(String fromsite) {
		this.fromsite = fromsite;
	}

	public String getCrawlat() {
		return crawlat;
	}

	public void setCrawlat(String crawlat) {
		this.crawlat = crawlat;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStartedLocation() {
		return startedLocation;
	}

	public void setStartedLocation(String startedLocation) {
		this.startedLocation = startedLocation;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

}
