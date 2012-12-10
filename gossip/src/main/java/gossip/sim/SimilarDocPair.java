package gossip.sim;

/**
 * 用来保存文档对相似度的类。它实现了Comparable的接口。
 */
public class SimilarDocPair implements Comparable<Object> {
	int doc1;
	int doc2;
	double similarity;

	/**
	 * @param doc1 文档1
	 * @param doc2 文档2
	 * @param similarity 文档1和文档2的相似读
	 */
	public SimilarDocPair(int doc1, int doc2, double similarity) {
		super();
		this.doc1 = doc1;
		this.doc2 = doc2;
		this.similarity = similarity;
	}

	public String toString() {
		return doc1 + " " + doc2 + " " + similarity;
	}

	/**
	 * 比较两个DocPairSimilarity，根据降序的特点返回值，也就是a.compareTo(b),当a>b时返回-1
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Object o) {
		if(!(o instanceof SimilarDocPair))
			return -1;
		
		SimilarDocPair s2 = (SimilarDocPair) o;
		if (s2.getSimilarity() > similarity) {
			return 1;
		} 
		if(s2.getSimilarity()<similarity)
			return -1;
		return 0;
	}

	public int getDoc1() {
		return doc1;
	}

	public void setDoc1(int doc1) {
		this.doc1 = doc1;
	}

	public int getDoc2() {
		return doc2;
	}

	public void setDoc2(int doc2) {
		this.doc2 = doc2;
	}

	public double getSimilarity() {
		return similarity;
	}

	public void setSimilarity(double similarity) {
		this.similarity = similarity;
	}
}
