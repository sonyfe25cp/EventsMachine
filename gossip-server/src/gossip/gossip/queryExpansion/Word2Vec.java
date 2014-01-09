package gossip.gossip.queryExpansion;

import gossip.gossip.utils.DatabaseUtils;
import gossip.gossip.utils.TokenizerUtils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import javax.sql.DataSource;

import org.ansj.domain.Term;
import org.ansj.recognition.NatureRecognition;
import org.ansj.splitWord.analysis.ToAnalysis;

public class Word2Vec {
	
	private DataSource dataSource;
	
	private static HashSet<String> stopwords = TokenizerUtils.getStopWords();
	
	public static void main(String[] args) throws IOException{
		Word2Vec w2v = new Word2Vec();
		//w2v.getChineseTrain();
		//w2v.getSoGouNewsTrain("/home/yulong/data/sougoucorpus.txt", "/home/yulong/data/sogounews.txt");
		//w2v.mergeNews("/home/yulong/data/sougoucorpus.txt", "/home/yulong/data/souhucorpus.txt", "/home/yulong/data/sscorpus.txt");
		//w2v.getSoGouNewsTrain("/home/yulong/data/sscorpus.txt", "/home/yulong/data/ssnews.txt");
		//以下为读取相似度文件，判断相似度过程
		w2v.loadModel("/home/yulong/data/sogounews.bin");
		//System.out.println("load model over");
		//System.out.println(w2v.getDistance("北大","北京大学"));
		System.out.println("One word analysis");  
        Set<WordEntry> result = new TreeSet<WordEntry>();  
        result = w2v.distance("病死猪");  
        Iterator iter = result.iterator();  
        while (iter.hasNext()) {  
            WordEntry word = (WordEntry) iter.next();  
            System.out.println(word.name + " " + word.score);  
        }  
//  
//        System.out.println("*******************************");  
//        System.out.println("Three word analysis");  
//        result = w2v.analogy("雅安", "地震", "汶川");  
//        iter = result.iterator();  
//        while (iter.hasNext()) {  
//            WordEntry word = (WordEntry) iter.next();  
//            System.out.println(word.name + " " + word.score);  
//        }  

	}
	
	public void mergeNews(String sougouPath, String souhuPath, String targetPath){
		BufferedInputStream bis = null;
		BufferedReader br = null;
		InputStreamReader isr = null;
		FileOutputStream fos = null;
		OutputStreamWriter osw = null;
		BufferedWriter bw = null;
		try {
			isr = new InputStreamReader(new FileInputStream(sougouPath), "UTF-8");
			//bis = new BufferedInputStream(new FileInputStream(sougouPath));
			br = new BufferedReader(isr);
			fos = new FileOutputStream(targetPath);
			osw = new OutputStreamWriter(fos, "UTF-8"); 
			bw = new BufferedWriter(osw);
			String str = "";
			int i = 0;
			while((str=br.readLine())!=null){
				bw.write(str);
				bw.newLine();
				if(i%10000==0){
					System.out.println("sougou: " + i);
				}
				i++;
			}
			System.out.println("sougou read done!!");
			
			isr = new InputStreamReader(new FileInputStream(souhuPath), "UTF-8");
			//bis = new BufferedInputStream(new FileInputStream(sougouPath));
			br = new BufferedReader(isr);
			
			i=0;
			while((str=br.readLine())!=null){
				bw.write(str);
				bw.newLine();
				if(i%10000==0){
					System.out.println("souhu: " + i);
				}
				i++;
			}
			System.out.println("souhu read done!!");
			br.close();
			bw.close();
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	/**
	 *将搜狗新闻分词，并存在sogou.txt中
	 */
	public void getSoGouNewsTrain(String srcPath, String tarPath){
		BufferedReader br = null;
		InputStreamReader isr = null;
		FileOutputStream fos = null;
		OutputStreamWriter osw = null;
		BufferedWriter bw = null;
		try {
			isr = new InputStreamReader(new FileInputStream(srcPath), "UTF-8");
			br = new BufferedReader(isr);
			fos = new FileOutputStream(tarPath);
			osw = new OutputStreamWriter(fos, "UTF-8"); 
			bw = new BufferedWriter(osw);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String str = "";
		int line = 0;
		String tagStart = "<content>";
		String tagEnd = "</content>";
		try {
			System.out.println("before read");
			while((str= br.readLine())!= null){
//				if(line>20){
//					break;
//				}
				if(line%1000==0){
					System.out.println("----news: " + line + "----");
				}
				str = str.replaceAll("\\s{1,}", "");
				int start = str.indexOf(tagStart);
				int end = str.indexOf(tagEnd);
				//得到content中的内容
				str = str.substring(start + tagStart.length(), end);
				if(str==null||str.equals("")){
					continue;
				}
//				System.out.println("--------------"+line+"------------");
//				System.out.println(str);
				//分词
				List<Term> bodyTerms = ToAnalysis.parse(str);
				new NatureRecognition(bodyTerms).recognition();
				String contentWords = "";
				for(Term term : bodyTerms){
					if(!term.getNatrue().natureStr.startsWith("n")){
						continue;
					}
					String word = term.getName();
					//去掉空格
					word = word.replaceAll("\\s{1,}", "");
					//去掉数字
					if ((!isChinese(word)) && (!isEnglish(word))) {
						continue;
					}
					//去掉停用词
					if(!stopwords.contains(word)){
						contentWords = contentWords + word +" ";
					}
				}//for
				bw.write(contentWords);
				bw.newLine();
				line++;
				
			}//while
			br.close();
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 将数据库中所有新闻分词并存在文件中
	 */
	public void getChineseTrain(){
		String sql = "select body from news";
		dataSource = DatabaseUtils.getInstance();
		Connection conn = null;
		Statement stat = null;
		ResultSet rs = null;
		String body = "";
		FileOutputStream fos = null;
		OutputStreamWriter osw = null;
		BufferedWriter bw = null;
		try {
			conn = dataSource.getConnection();
			stat = conn.createStatement();
			rs = stat.executeQuery(sql);
			fos = new FileOutputStream("/home/yulong/data/gossip.txt");
			osw = new OutputStreamWriter(fos, "UTF-8"); 
			bw = new BufferedWriter(osw);
			int count = 0;
			while(rs.next()){
				if(count%1000 == 0){
					System.out.println("-----count = " + count + "-------");
				}
				body = rs.getString("body");
				List<Term> bodyTerms = ToAnalysis.parse(body);
				new NatureRecognition(bodyTerms).recognition();
				String bodyWords = "";
				for(Term term : bodyTerms){
					//如果不是名词，放弃
					if(!term.getNatrue().natureStr.startsWith("n")){
						continue;
					}
					String word = term.getName();
					//去掉空格
					word = word.replaceAll("\\s{1,}", "");
					//去掉数字
					if ((!isChinese(word)) && (!isEnglish(word))) {
						continue;
					}
					//去掉停用词
					if(!stopwords.contains(word)){
						bodyWords = bodyWords + word +" ";
					}
				}//for
				bw.write(bodyWords);
				bw.newLine();
				count++;
			}//while
			bw.close();
			fos.close();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private HashMap<String, float[]> wordMap = new HashMap<String, float[]>();  
	  
    private int words;  
    private int size;  
    private int topNSize = 40;  
  
    /** 
     * 加载模型 
     *  
     * @param path 
     *            模型的路径 
     * @throws IOException 
     */  
    public void loadModel(String path) throws IOException {  
        DataInputStream dis = null;  
        BufferedInputStream bis = null;  
        double len = 0;  
        float vector = 0;  
        try {  
            bis = new BufferedInputStream(new FileInputStream(path));  
            dis = new DataInputStream(bis);  
            // //读取词数   
            words = Integer.parseInt(readString(dis));  
            // //大小   
            size = Integer.parseInt(readString(dis));  
  
            String word;  
            float[] vectors = null;  
            for (int i = 0; i < words; i++) {  
                word = readString(dis);  
                vectors = new float[size];  
                len = 0;  
                for (int j = 0; j < size; j++) {  
                    vector = readFloat(dis);  
                    len += vector * vector;  
                    vectors[j] = (float) vector;  
                }  
                len = Math.sqrt(len);  
  
                for (int j = 0; j < vectors.length; j++) {  
                    vectors[j] = (float) (vectors[j] / len);  
                }  
                wordMap.put(word, vectors);  
                dis.read();  
            }  
  
        } finally {  
            bis.close();  
            dis.close();  
        }  
    }  
  
    private static final int MAX_SIZE = 50;  
  
    /** 
     * 得到近义词 
     *  
     * @param word 
     * @return 
     */  
    public Set<WordEntry> distance(String word) {  
        float[] wordVector = getWordVector(word); //200
        System.out.println("vector size: " + wordVector.length);
        if (wordVector == null) {  
            return null;  
        }  
        Set<Entry<String, float[]>> entrySet = wordMap.entrySet();  
        System.out.println("wordMap size: " + entrySet.size());
        float[] tempVector = null;  
        List<WordEntry> wordEntrys = new ArrayList<WordEntry>(topNSize);  
        String name = null;  
        for (Entry<String, float[]> entry : entrySet) {  
            name = entry.getKey();  
            if (name.equals(word)) {//????  
                continue;  
            }  
            float dist = 0;  
            tempVector = entry.getValue();  
            for (int i = 0; i < wordVector.length; i++) {  
                dist += wordVector[i] * tempVector[i];  
            }  
            insertTopN(name, dist, wordEntrys);  
        }  
        return new TreeSet<WordEntry>(wordEntrys);  
    }  
    
    /**
     * 求两个词之间的距离
     * @param word1
     * @param word2
     * @return
     */
    public double getDistance(String word1, String word2){
    	word1 = word1.replaceAll("\\s{1,}", "");
    	word2 = word2.replaceAll("\\s{1,}", "");
    	float[] wv1 = getWordVector(word1);  
        float[] wv2 = getWordVector(word2); 
        if (wv1 == null || wv2 == null) {  
            return 0;  
        }  
        double distance = 0.0;
        int size = wv1.length < wv2.length?wv1.length:wv2.length;
        for(int i = 0; i < size; i++){
        	distance = distance + wv1[i]*wv2[i];
        }
        return distance;
    }
  
    /** 
     * 近义词 
     *  
     * @return 
     */  
    public TreeSet<WordEntry> analogy(String word0, String word1, String word2) {  
        float[] wv0 = getWordVector(word0);  
        float[] wv1 = getWordVector(word1);  
        float[] wv2 = getWordVector(word2);  
  
        if (wv1 == null || wv2 == null || wv0 == null) {  
            return null;  
        }  
        float[] wordVector = new float[size];  
        for (int i = 0; i < size; i++) {  
            wordVector[i] = wv1[i] + wv0[i] + wv2[i];  
        }  
        float[] tempVector;  
        String name;  
        List<WordEntry> wordEntrys = new ArrayList<WordEntry>(topNSize);  
        for (Entry<String, float[]> entry : wordMap.entrySet()) {  
            name = entry.getKey();  
            if (name.equals(word0) || name.equals(word1) || name.equals(word2)) {  
                continue;  
            }  
            float dist = 0;  
            tempVector = entry.getValue();  
            for (int i = 0; i < wordVector.length; i++) {  
                dist += wordVector[i] * tempVector[i];  
            }  
            insertTopN(name, dist, wordEntrys);  
        }  
        return new TreeSet<WordEntry>(wordEntrys);  
    }  
  
    /**
     * 每次替换相关性最小的词
     * @param name
     * @param score
     * @param wordsEntrys
     */
    private void insertTopN(String name, float score,  
            List<WordEntry> wordsEntrys) {  
        if (wordsEntrys.size() < topNSize) {  
            wordsEntrys.add(new WordEntry(name, score));  
            return;  
        }  
        float min = Float.MAX_VALUE;  
        int minOffe = 0;  
        for (int i = 0; i < topNSize; i++) {  
            WordEntry wordEntry = wordsEntrys.get(i);  
            if (min > wordEntry.score) {  
                min = wordEntry.score;  
                minOffe = i;  
            }  
        }  
  
        if (score > min) {  
            wordsEntrys.set(minOffe, new WordEntry(name, score));  
        }  
  
    }  
  
    public class WordEntry implements Comparable<WordEntry> {  
        public String name;  
        public float score;  
  
        public WordEntry(String name, float score) {  
            this.name = name;  
            this.score = score;  
        }  
  
        @Override  
        public String toString() {  
            return this.name + "\t" + score;  
        }  
  
        @Override  
        public int compareTo(WordEntry o) {  
            if (this.score > o.score) {  
                return -1;  
            } else {  
                return 1;  
            }  
        }  
  
    }  
  
    /** 
     * 得到词向量 
     *  
     * @param word 
     * @return 
     */  
    public float[] getWordVector(String word) {  
        return wordMap.get(word);  
    }  
  
    public static float readFloat(InputStream is) throws IOException {  
        byte[] bytes = new byte[4];  
        is.read(bytes);  
        return getFloat(bytes);  
    }  
  
    /** 
     * 读取一个float 
     *  
     * @param b 
     * @return 
     */  
    public static float getFloat(byte[] b) {  
        int accum = 0;  
        accum = accum | (b[0] & 0xff) << 0;  
        accum = accum | (b[1] & 0xff) << 8;  
        accum = accum | (b[2] & 0xff) << 16;  
        accum = accum | (b[3] & 0xff) << 24;  
        return Float.intBitsToFloat(accum);  
    }  
  
    /** 
     * 读取一个字符串 
     *  
     * @param dis 
     * @return 
     * @throws IOException 
     */  
    private static String readString(DataInputStream dis) throws IOException {  
        byte[] bytes = new byte[MAX_SIZE];  
        byte b = dis.readByte();  
        int i = -1;  
        StringBuilder sb = new StringBuilder();  
        while (b != 32 && b != 10) {  
            i++;  
            bytes[i] = b;  
            b = dis.readByte();  
            if (i == 49) {  
                sb.append(new String(bytes));  
                i = -1;  
                bytes = new byte[MAX_SIZE];  
            }  
        }  
        sb.append(new String(bytes, 0, i + 1));  
        return sb.toString();  
    }  
    
    /* 以下两个函数都是判断字符串类型的函数，在扩展词的过滤中用到 */

	private static final boolean isChinese(char c) {
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
		if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
				|| ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
				|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
				|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
			return true;
		}
		return false;
	}

	/* 准确判断字符串是否为中文 */
	public static final boolean isChinese(String strName) {
		strName = strName.replaceAll("\\s{1,}", "");
		char[] ch = strName.toCharArray();
		for (int i = 0; i < ch.length; i++) {
			char c = ch[i];
			if (!isChinese(c)) {
				return false;
			}
		}
		return true;
	}

	public boolean isEnglish(String str) {
		return str.replaceAll("\\s{1,}", "").matches("[a-zA-Z]");
	}

	// 判断字符串为数字
	public static boolean isNum(String str) {
		return str.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
	}
  
    public int getTopNSize() {  
        return topNSize;  
    }  
  
    public void setTopNSize(int topNSize) {  
        this.topNSize = topNSize;  
    }  
  
    public HashMap<String, float[]> getWordMap() {  
        return wordMap;  
    }  
  
    public int getWords() {  
        return words;  
    }  
  
    public int getSize() {  
        return size;  
    }  

	
	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

}
