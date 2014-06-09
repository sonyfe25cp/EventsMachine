
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 用来保存事件的各种信息的数据结构。 重载了toString方法，提供到JSONObject的转换，以及转换自JSONObject的方法。
 * 
 * @author lins
 */
public class Event implements Serializable {

    private static final long serialVersionUID = 8954198578600418283L;

    private List<News> newsList;

    //事件中增加一个新闻
    public void add(News news) {
        int newsId = news.getId();
        if (pagesList == null) {
            this.pagesList = new LinkedList<Integer>();
        }
        if (!pagesList.contains(newsId)) {
            this.pagesList.add(newsId);
            if (newsList == null) {
                this.newsList = new ArrayList<News>();
            }
            this.newsList.add(news);
        }
    }

    public Event() {
        Date now = new Date(System.currentTimeMillis());
        this.createAt = now;
    }

    public void mergeEvent(Event event) {
        List<News> nList = event.getNewsList();
        for (News news : nList) {
            this.getNewsList().add(news);
        }
        this.updateAt = event.getUpdateAt();
    }

    final String delimiter = ";";

    public int id;// 事件id

    public String title; // 事件主标题

    public double recommended = 0.0;

    // 下面这个属性感觉不要再建一个表，干脆就直接合一下放event这个表里面，表连接太耗时了
    private String keyWords;//

    private Map<String, Double> keyWordsMap = new HashMap<String, Double>(); // 关键词-权重

    public List<Integer> pagesList = new LinkedList<Integer>();// 页面id,已经排过序的

    public String pages;

    public long createTime;

    public String desc;// 事件描述

    public String img;

    public String startedLocation;

    public Date createAt;//创建时间 20131101

    public Date updateAt;

    public String importantPeople;//重要人物

    public float emotion;//情感得分

    public static final String Importance = "importance";

    public static final String Time = "time";

    /**
     * @return 返回新闻在索引的id的列表，该列表里面的新闻是已经排过序的
     */
    public List<Integer> getPagesList() {
        return pagesList;
    }

    /**
     * 往事件里面的新闻id列表的pos位置添加新闻id
     */
    public void addPage(int page, int pos) {
        if (pagesList.contains(page)) {
            pagesList.remove(page);
        }
        pos = Math.min(pos, pagesList.size());
        pagesList.add(pos, page);
    }

    /**
     * 往尾部添加新闻
     */
    public void addPage(int page) {
        if (!pagesList.contains(page)) pagesList.add(page);
    }

    public void setPagesList(List<Integer> pagesList) {
        this.pagesList = pagesList;
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
        keyWordsMap.put(key, weight);
    }

    public void setKeyWords(Map<String, Double> keyWordsMap) {
        this.keyWordsMap = keyWordsMap;
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

    public String toSQLString() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
        return String.format(
                "title=\"%s\",recommended=%f,keywords=%s,pages=%s,create_time=\"%s\" where id=%d",
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

        Iterator<Entry<String, Double>> it = keyWordsMap.entrySet().iterator();
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
        for (int i = 0; i < key.length && i < keyWordsMap.size(); i++) {
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
            if (a > tmp) return i;
            i++;
        }
        return -1;
    }

    public String getPagesStr() {
        StringBuilder sb = new StringBuilder();

        Iterator<Integer> it = pagesList.iterator();
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

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public Date getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }

    public String getKeyWords() {
        return keyWords;
    }

    public void setKeyWords(String keyWords) {
        this.keyWords = keyWords;
    }

    public Map<String, Double> getKeyWordsMap() {
        return keyWordsMap;
    }

    public void setKeyWordsMap(Map<String, Double> keyWordsMap) {
        this.keyWordsMap = keyWordsMap;
    }

    public String getPages() {
        return pages;
    }

    public void setPages(String pages) {
        this.pages = pages;
    }

    public String getImportantPeople() {
        return importantPeople;
    }

    public void setImportantPeople(String importantPeople) {
        this.importantPeople = importantPeople;
    }

    public float getEmotion() {
        return emotion;
    }

    public void setEmotion(float emotion) {
        this.emotion = emotion;
    }

}
