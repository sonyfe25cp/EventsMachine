


public class News {

	private int id;// id
	private String title;// 标题
	private String body;// 正文
	private String url;
	private String author = "";
	private String description = "";// 摘要
	private String date = "";
	private String fromSite = "";
	private String crawlAt = "";
	private String status = NEW;// 新闻的状态，新建，被索引，更新，删除
	private String startedLocation = "";
	private String keywords = "";
	private String titleWords;
	private String bodyWords;
	private String eventStatus;// 跟事件的关系，已属于事件和不属于事件，默认为0，标志没有被某事件包含，若为1，则已经被包含在某事件
	private float emotion;//情感得分

	public final static int Evented = 1;
	public final static int UnEvented = 0;

	public final static String NEW = "new";
	public final static String INDEX = "index";
	public final static String DELETE = "delete";
	public final static String UPDATE = "update";

	public String toJson() {
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		sb.append("\"id\":" + id);
		sb.append(",");
		sb.append("\"title\":\"" + title + "\"");
		sb.append(",");
		sb.append("\"desc\":\"" + description + "\"");
		sb.append(",");
		sb.append("\"author\":\"" + author + "\"");
		sb.append(",");
		sb.append("\"body\":\"" + body + "\"");
		sb.append(",");
		sb.append("\"publish_at\":\"" + date + "\"");
		sb.append(",");
		sb.append("\"source\":\"" + url + "\"");
		sb.append(",");
		sb.append("\"started_location\":\"" + startedLocation + "\"");
		sb.append(",");
		sb.append("\"keywords\":\"" + keywords + "\"");
		sb.append("}");
		return sb.toString();
	}


	public String toString() {
		return "title:" + title + "\n" + "crawlat:" + crawlAt + "\n";
	}


	// private int transTodayToInt(){
	// Date date = Calendar.getInstance().getTime();
	// String time = DateTimeUtil.getFormatDay(date);
	// return Integer.parseInt(time);
	// }

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getFromSite() {
		return fromSite;
	}

	public void setFromSite(String fromSite) {
		this.fromSite = fromSite;
	}

	public String getCrawlAt() {
		return crawlAt;
	}

	public void setCrawlAt(String crawlAt) {
		this.crawlAt = crawlAt;
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

	public String getTitleWords() {
		return titleWords;
	}

	public void setTitleWords(String titleWords) {
		this.titleWords = titleWords;
	}

	public String getBodyWords() {
		return bodyWords;
	}

	public void setBodyWords(String bodyWords) {
		this.bodyWords = bodyWords;
	}

	public String getEventStatus() {
		return eventStatus;
	}

	public void setEventStatus(String eventStatus) {
		this.eventStatus = eventStatus;
	}

	public float getEmotion() {
		return emotion;
	}

	public void setEmotion(float emotion) {
		this.emotion = emotion;
	}

}