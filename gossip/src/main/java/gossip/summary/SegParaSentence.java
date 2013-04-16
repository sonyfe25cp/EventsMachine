package gossip.summary;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermFreqVector;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.NIOFSDirectory;

import edu.bit.dlde.utils.DLDEConfiguration;

/**
 * 分段、分句、分词
 * 
 * @author zhangchangmin
 * 
 */
public class SegParaSentence {

	String indexPath = DLDEConfiguration.getInstance("gossip.properties")
			.getValue("IndexPath");

	private String sourceStr = "";// 从文件中读到的内容

	private String[] splitedParagraph;// 存放分段内容

	private String[] splitedSentence;// 存放分句内容

	private int[][] paragraphsInfo;// 存放分段信息

	private int[][] sentencesInfo;// 存放分句信息

	private String[] termArray;// 有效分词(长度>1,减小计算量)

	private int[] termFreq;// 有效分词对应的频率

	private String[][] termLocation;// 词元位置信息

	public SegParaSentence() {

	}

	public SegParaSentence(int id) {
		sourceStr = readNewsFile(id);
		// System.out.println(sourceStr);
		splitedParagraph = sourceStr.split("\r\n");// 分段
		paragraphsInfo = getParagraphsInfo(splitedParagraph);// 分段信息
		sentencesInfo = getSentencesInParagraph(splitedParagraph);// 分句
		termLocation = this.termLocationInfo();
	}

	/**
	 * 根据文档ID读取索引，获取原始文档及分词情况
	 * 
	 * @param docId 数据库中的id
	 *            
	 * @return 返回原始文档的title和body组成的字符串
	 */
	public String readNewsFile(int id) {
		StringBuffer sourceStr = new StringBuffer();
		// 打开索引
		NIOFSDirectory dir = null;
		IndexReader ir = null;
		IndexSearcher is =null;
		try {
			dir = new NIOFSDirectory(new File(indexPath));
			ir = IndexReader.open(dir, true);
			is = new IndexSearcher(ir);
			TermQuery query = new TermQuery(new Term("id",id+""));
			TopDocs tdocs = is.search(query, 2);
			ScoreDoc[] sdocs = tdocs.scoreDocs;
			Document doc = null;
			int docId = 0;
			for(ScoreDoc sdoc : sdocs){
				docId =  sdoc.doc;
				doc = is.doc(sdoc.doc);
			}
			if(doc == null)
				return "";
			String tit = doc.get("title");
			String con = doc.get("body").replaceAll("\\s*", "");
			sourceStr.append(tit).append("\r\n").append(con);
			TermFreqVector tfv = ir.getTermFreqVector(docId, "body");
			String[] terms = tfv.getTerms();
			int[] freqs = tfv.getTermFrequencies();
			TermFreqVector tfv4titlle = ir.getTermFreqVector(docId, "title");
			String[] terms4title = tfv4titlle.getTerms();
			int[] freqs4title = tfv4titlle.getTermFrequencies();
			// 统计长度>1的term个数
			int gtone = 0;
			for (int j = 0; j < terms.length; j++) {
				if (terms[j].length() > 1 && freqs[j] > 1) {
					gtone++;
				}
			}
			// 只保留长度>1的term，减小计算量
			String[] term = new String[gtone];
			int[] freq = new int[gtone];
			gtone = 0;
			for (int k = 0; k < terms.length; k++) {
				if (terms[k].length() > 1 && freqs[k] > 1) {
					for (int l = 0; l < terms4title.length; l++) {
						if (terms[k].equals(terms4title[l])) {
							freqs[k] += freqs4title[l];
							break;
						}
					}
					term[gtone] = terms[k];
					freq[gtone] = freqs[k];
					gtone++;
				}
			}
			this.setTermFreq(freq);
			this.setTermArray(term);
			is.close();
			ir.close();
			dir.close();

		} catch (IOException e2) {
			e2.printStackTrace();
		}
		return sourceStr.toString();
	}

	/**
	 * 定位段落在sourceStr中的位置
	 * 
	 * @param splitParagraph
	 *            分割出的段落的内容
	 * @return 返回一个段落和原文内容的关系的数组
	 */
	private int[][] getParagraphsInfo(String[] splitParagraph) {
		// 用于存放分段信息
		// 格式为：段号 段起 段尾
		int[][] paragraphInfo = new int[splitParagraph.length][3];// 用三列矩阵表示分段信息
		String sourceStr = this.getSourceStr();
		for (int i = 0; i < paragraphInfo.length; i++) {
			int paragraphBegin = sourceStr.indexOf(splitParagraph[i]);// 得到每段的起始位置
			int paragraphEnd = paragraphBegin + splitParagraph[i].length();// 得到每段的结束位置
			for (int j = 0; j < 3; j++) {
				if (j == 0) {
					paragraphInfo[i][j] = i + 1;// 段号
				} else if (j == 1) {
					paragraphInfo[i][j] = paragraphBegin;// 每段的起始位置
				} else {
					paragraphInfo[i][j] = paragraphEnd;// 每段的结束位置
				}
			}
		}
		return paragraphInfo;
	}

	/**
	 * 定位句子在sourceStr中的位置
	 * 
	 * @param splitParagraph
	 *            分割出的段落
	 * @return 返回段落和句子关系的数组
	 */
	private int[][] getSentencesInParagraph(String[] splitParagraph) {
		// 用于存放分句信息
		// 格式： 段号 句号 句子起始位置 句子结束位置
		int[][] sentenceInfo = new int[getSentenceCount(splitParagraph)][4];// 用四列矩阵表示分句信息
		String sourceStr = this.getSourceStr();// 获得读取字符串的内容
		int sentenceIndex = 0;// 用于记录数组的下标
		StringBuffer sb = new StringBuffer();// 用于存放句子内容
		for (int i = 0; i < splitParagraph.length; i++) {
			String paragraphStr = splitParagraph[i];// 每段的字符串内容
			String[] sentenceInParagraph;
			if (paragraphStr.endsWith("。") || paragraphStr.contains("。")) {// 包含半角句号的按照句号切分
				sentenceInParagraph = paragraphStr.split("。");
			} else {
				String[] asOne = { paragraphStr };
				sentenceInParagraph = asOne;
			}
			for (int j = 0; j < sentenceInParagraph.length; j++) {
				int sentenceBegin = sourceStr.indexOf(sentenceInParagraph[j]);// 句子在sourceStr中的起始位置
				int sentenceEnd = sentenceBegin
						+ sentenceInParagraph[j].length();// 句子在sourceStr中的结束位置
				// String subStr = sourceStr.substring(sentenceBegin,
				// sentenceEnd);
				if (!sentenceInParagraph[j].trim().equals("")) {
					sb.append(sentenceInParagraph[j].trim() + "\r\n");
					for (int k = 0; k < 4; k++) {
						if (k == 0) {
							sentenceInfo[sentenceIndex][k] = i + 1;// 段号
						} else if (k == 1) {
							sentenceInfo[sentenceIndex][k] = sentenceIndex + 1;// 句号
						} else if (k == 2) {
							sentenceInfo[sentenceIndex][k] = sentenceBegin;// 句子起始位置
						} else {
							sentenceInfo[sentenceIndex][k] = sentenceEnd;// 句子结束位置
						}
					}
					sentenceIndex++;
				}
			}
		}
		String[] splitSentences = sb.toString().split("\r\n");
		this.setSplitedSentence(splitSentences);// 将分出的句子存储在splitSentence对象中
		return sentenceInfo;
	}

	/**
	 * 获得要切分的句子总数
	 * 
	 * @param splitParagraph
	 *            切分出的段落
	 * @return 返回句子总数
	 */
	private int getSentenceCount(String[] splitParagraph) {
		int sentenceCount = 0;
		for (int i = 0; i < splitParagraph.length; i++) {
			String paragraphStr = splitParagraph[i];// 每段的字符串内容
			if (paragraphStr.endsWith("。") || paragraphStr.contains("。")) {// 包含半角句号的按照句号切分
				String[] sentenceInParagraph = paragraphStr.split("。");
				for (int j = 0; j < sentenceInParagraph.length; j++) {
					if (!sentenceInParagraph[j].trim().equals("")) {
						sentenceCount++;
					}
				}
			} else {// 不包含句号的就将整段切分成一个句子
				String sentenceInParagraph = paragraphStr;
				if (!sentenceInParagraph.trim().equals("")) {
					sentenceCount++;
				}
			}
		}
		return sentenceCount;
	}

	/**
	 * 用于存放词元出现位置信息[词元,词元起始位置]
	 * 
	 * @return 返回词元相关信息字符数组
	 */
	private String[][] termLocationInfo() {
		// 保存词元相关信息
		// 格式：词元 词元起始位置
		String[] terms = termArray;
		int[] tFreq = new int[terms.length];
		String[] splitedsentence = this.getSplitedSentence();// 获得分句内容
		int[][] sentences = this.getSentencesInfo();// 获得分句数组信息
		List<String[]> termLocation = new ArrayList<String[]>();
		String[] s;
		for (int i = 0; i < terms.length; i++) {// 迭代词元数组
			int freq = 0;
			for (int j = 0; j < splitedsentence.length; j++) {// 迭代句子内容数组

				String sentence = splitedsentence[j].toLowerCase();// 得到句子内容
				if (sentence.indexOf(terms[i]) != -1) {// 如果句子中包含词元
					freq++;
					int termOffset = sentences[j][2]
							+ sentence.indexOf(terms[i]);// 词元在句子中出现的位置+句子的起始位置
					int s1 = sentence.indexOf(terms[i]) + terms[i].length();// 句子中包含词元的下标+词元长度
					int s2 = sentence.length();// 句子的长度
					s = new String[2];
					s[0] = terms[i];
					s[1] = "" + termOffset;
					termLocation.add(s);
					// 如果存在词元，则从词元截取句子，继续判断子串句子中是否含有该词元，直到找不到为止
					int s3 = 0;
					int s4 = 0;
					if (s1 < s2) {
						String subSentenceStr = sentence.substring(s1, s2);
						int ss = s1;
						while (subSentenceStr.indexOf(terms[i]) != -1) {
							freq++;
							termOffset = sentences[j][2] + ss
									+ subSentenceStr.indexOf(terms[i]);// 句子的起始位置+截取掉的位置+词元在句子中出现的位置
							s3 = subSentenceStr.indexOf(terms[i])
									+ terms[i].length();// 句子中词元的下标+词元长度
							s4 = subSentenceStr.length();
							ss += s3;
							s = new String[2];
							s[0] = terms[i];
							s[1] = "" + termOffset;
							termLocation.add(s);
							if (s3 < s4) {
								subSentenceStr = subSentenceStr.substring(s3,
										s4);
							} else
								break;
						}
					}
				}
			}
			tFreq[i] = freq;
		}
		termFreq = tFreq;
		int n = termLocation.size();
		String[][] termLocationArray = new String[n][2];
		for (int i = 0; i < n; i++) {
			termLocationArray[i][0] = termLocation.get(i)[0];
			termLocationArray[i][1] = termLocation.get(i)[1];
		}
		return termLocationArray;
	}

	// /**
	// * 用于得到所有词元在所有句子中出现次数的总和
	// *
	// * @return 返回出现总次数
	// */
	// private int getAllTermAppearTimes() {
	// int times=0;
	// for(int i=0;i<termFreq.length;i++){
	// times+=termFreq[i];
	// }
	// return times;
	// }
	/**
	 * 组合Term和Freq
	 * 
	 * @return 返回term与freq的组合数组
	 */
	public String[][] comTermsFreq() {
		String[][] termsFreq = new String[termArray.length][2];
		for (int i = 0; i < termFreq.length; i++) {
			termsFreq[i][0] = termArray[i];
			termsFreq[i][1] = "" + termFreq[i];
		}
		return termsFreq;
	}

	public String getSourceStr() {
		return sourceStr;
	}

	public void setSourceStr(String sourceStr) {
		this.sourceStr = sourceStr;
	}

	public int[][] getParagraphsInfo() {
		return paragraphsInfo;
	}

	public void setParagraphsInfo(int[][] paragraphsInfo) {
		this.paragraphsInfo = paragraphsInfo;
	}

	public int[][] getSentencesInfo() {
		return sentencesInfo;
	}

	public void setSentencesInfo(int[][] sentencesInfo) {
		this.sentencesInfo = sentencesInfo;
	}

	public String[] getSplitedParagraph() {
		return splitedParagraph;
	}

	public void setSplitedParagraph(String[] splitedParagraph) {
		this.splitedParagraph = splitedParagraph;
	}

	public String[] getSplitedSentence() {
		return splitedSentence;
	}

	public void setSplitedSentence(String[] splitedSentence) {
		this.splitedSentence = splitedSentence;
	}

	public String[] getTermArray() {
		return termArray;
	}

	public void setTermArray(String[] termArray) {
		this.termArray = termArray;
	}

	public int[] getTermFreq() {
		return termFreq;
	}

	public void setTermFreq(int[] termFreq) {
		this.termFreq = termFreq;
	}

	public String[][] getTermLocation() {
		return termLocation;
	}

	public void setTermLocation(String[][] termLocation) {
		this.termLocation = termLocation;
	}
}
