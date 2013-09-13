package gossip.event;

import gossip.model.News;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.json.JSONObject;

/**
 * 用来保存事件的各种信息的数据结构。 重载了toString方法，提供到JSONObject的转换，以及转换自JSONObject的方法。
 * 
 * @author lins
 */
public class Event implements Serializable {
	private static final long serialVersionUID = 8954198578600418283L;
	
	private List<News> newsList;
	
	public void add(News news){
		
	}
	
	
	
	
	
	

	final String delimiter = ";";

	public int id = -1;// 事件id
	public String title; // 事件主标题
	public double recommended = 0.0;
	// 下面这个属性感觉不要再建一个表，干脆就直接合一下放event这个表里面，表连接太耗时了
	public Map<String, Double> keyWords = new HashMap<String, Double>(); // 关键词-权重
	public List<Integer> pages = new LinkedList<Integer>();// 页面id,已经排过序的
	public long createTime;
	public String desc;// 事件描述
	public String img;
	public String startedLocation;

	/**
	 * @return 返回新闻在索引的id的列表，该列表里面的新闻是已经排过序的
	 */
	public List<Integer> getPages() {
		return pages;
	}

	/**
	 * 往事件里面的新闻id列表的pos位置添加新闻id
	 */
	public void addPage(int page, int pos) {
		if (pages.contains(pages)) {
			pages.remove(page);
		}
		pos = Math.min(pos, pages.size());
		pages.add(pos, page);
	}

	/**
	 * 往尾部添加新闻
	 */
	public void addPage(int page) {
		if (!pages.contains(pages))
			pages.add(page);
	}

	public void setPages(List<Integer> pages) {
		this.pages = pages;
	}

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

	/**
	 * @return 返回key-weight组成的键值对
	 */
	public Map<String, Double> getKeyWords() {
		return keyWords;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	/**
	 * 往关键词里面添加新的关键词,或者修改原本的权重
	 */
	public void addKeyWord(String key, Double weight) {
		keyWords.put(key, weight);
	}

	public void setKeyWords(Map<String, Double> keyWords) {
		this.keyWords = keyWords;
	}

	public double getRecommended() {
		return recommended;
	}

	public void setRecommended(double recommended) {
		this.recommended = recommended;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getStartedLocation() {
		return startedLocation;
	}

	public void setStartedLocation(String startedLocation) {
		this.startedLocation = startedLocation;
	}

	/**
	 * 转换成一个JSONObject方便网络传输
	 */
	public JSONObject toJSONObject() {
		return JSONObject.fromObject(this);
	}

//	public String toJson(){
//		StringBuilder sb = new StringBuilder();
//		sb.append("{");
//		
//		sb.append("");
//		
//		sb.append("}");
//		
//	}
	
	/**
	 * 从JSONObject转换为Event方便网络传输。 不能使用JSONObject的方法，会出错
	 */
	public static Event fromJSONObject(JSONObject jsonObj) {
		Event e = new Event();
		for (Field field : Event.class.getFields()) {
			String name = field.getName();
			Object obj = jsonObj.get(name);
			if (obj == null)
				continue;
			try {
				if (name.equals("title"))// 类似obj instanceof List耦合度太高
					e.getClass().getField(name).set(e, (String) obj);
				else if (name.equals("keyWords")) {
					@SuppressWarnings("unchecked")
					Map<String, Integer> m = (JSONObject) obj;
					e.getClass().getField(name).set(e, m);
				} else if (name.equals("pages"))
					e.getClass().getField(name).set(e, (List<?>) obj);
				else if (name.equals("id"))
					e.getClass().getField(name).set(e, obj);
				else if (name.equals("createTime"))
					e.getClass().getField(name).set(e, obj);
				else if (name.equals("recommended"))
					e.getClass().getField(name).set(e, obj);
			} catch (IllegalArgumentException e1) {
				e1.printStackTrace();
			} catch (IllegalAccessException e1) {
				e1.printStackTrace();
			} catch (NoSuchFieldException e1) {
				e1.printStackTrace();
			} catch (SecurityException e1) {
				e1.printStackTrace();
			}
		}
		return e;
	}

	public String toString() {
		return toJSONObject().toString();
	}

	public String toSQLString() {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
		return String
				.format("title=\"%s\",recommended=%f,keywords=%s,pages=%s,create_time=\"%s\" where id=%d",
						title, recommended, getKeyWordsStr(), getPagesStr(),
						df.format(new Date(createTime)), id);
	}

	public String getUTF8Title() {
		try {
			return new String(title.getBytes(), "utf-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
			return title;
		}
	}

	public String getKeyWordsStr() {
		StringBuilder sb = new StringBuilder();

		Iterator<Entry<String, Double>> it = keyWords.entrySet().iterator();
		Entry<String, Double> e;
		String[] key = new String[5];
		double[] values = new double[5];
		while (it.hasNext()) {
			e = it.next();
			double v = e.getValue();
			int idx = biggerThan(v, values);
			if (idx != -1) {
				key[idx] = e.getKey();
				values[idx] = v;
			}
			// sb.append(e.getKey()).append(":").append(e.getValue()).append(";");
		}
		for (int i = 0; i < key.length && i < keyWords.size(); i++) {
			sb.append(key[i]).append(":").append(values[i]).append(";");
		}
		// sb.append("\"");

		try {
			return new String(sb.toString().getBytes(), "utf-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
			return sb.toString();
		}
	}

	private int biggerThan(double a, double[] b) {
		int i = 0;
		for (double tmp : b) {
			if (a > tmp)
				return i;
			i++;
		}
		return -1;
	}

	public String getPagesStr() {
		StringBuilder sb = new StringBuilder();

		Iterator<Integer> it = pages.iterator();
		Integer i;
		while (it.hasNext()) {
			i = it.next();
			sb.append(i).append(";");
		}
		// sb.append();

		return sb.toString();
	}

	public List<News> getNewsList() {
		return newsList;
	}

	public void setNewsList(List<News> newsList) {
		this.newsList = newsList;
	}

	public static void main(String args[]) {
		// Event e = new Event();
		// e.setId(0);
		// e.addPage(1, 4);
		// e.addPage(2, 0);
		// e.addPage(3);
		// e.setTitle("test");
		// e.setCreateTime(0);
		// e.setRecommended(1.01);
		// e.addKeyWord("123", 1.0);
		// e.addKeyWord("123", 1.5);
		// System.out.println(e.toString());
		// JSONObject jsonObj = JSONObject.fromObject(e);
		// System.out.println(jsonObj);
		// System.out.println(Event.fromJSONObject(jsonObj));
	}

}
