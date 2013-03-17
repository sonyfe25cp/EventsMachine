package gossip.summary;


import java.util.ArrayList;
import java.util.List;

/**
 * 选取关键字
 * 
 * @author zhangchangmin
 *
 */
public class ChooseKeywords {

	private String[][] topNTermInfo;// 包含频率信息的词元数组信息 格式：词元 起始位置 频率

	private static int maxKeywords = Parameters.MaxKeywords;

	public ChooseKeywords(SegParaSentence sps) {
		String[][] termLocation = sps.getTermLocation();// 得到词元位置信息
		String[][] termsFreq = sps.comTermsFreq();//词元频率
		String[][] topNTerm = this.getTopNTerm(termsFreq);//前N个词
		topNTermInfo=calTopNTermInfo(termLocation,topNTerm);//前N个词的位置频率信息
	}

	/**
	 * 得到 最终选择出的maxKeywords个包含频率信息的词元数组
	 * 
	 * @param termLocation
	 *           所有词元位置信息
	 * @param topNTerm
	 *           前N个词元
	 * @return 返回包含频率信息的词元数组[词元 起始位置 频率]
	 */
	private String[][] calTopNTermInfo(String[][] termLocation,String[][] topNTerm) {
		// 存放统计频率后的十个词元
		// 格式：词元 起始位置 频率
		String[][] synWordFrequency = new String[getTopNTermCount(topNTerm)][3];
		int maxTenSynWordIndex = 0;
		for (int i = 0; i < topNTerm.length; i++) {// 先迭代选出的十个词元
			for (int j = 0; j < termLocation.length; j++) {
				// 取出和选出词元名称相同词元的起始位置信息
				if (topNTerm[i][0].equals(termLocation[j][0])) {
					synWordFrequency[maxTenSynWordIndex][0] = topNTerm[i][0];// 词元
					synWordFrequency[maxTenSynWordIndex][1] = termLocation[j][1];// 起始位置
					synWordFrequency[maxTenSynWordIndex][2] = topNTerm[i][1];// 出现频率
					maxTenSynWordIndex++;
				}
			}
		}
		return synWordFrequency;
	}

	/**
	 * 获取前N个词元的总出现频率
	 * 
	 * @param topNterm
	 * @return
	 */
	private int getTopNTermCount(String[][] topNTerm) {
		int topNTermCount = 0;
		for (int i = 0; i < topNTerm.length; i++) {
			topNTermCount += Integer.parseInt(topNTerm[i][1]);
		}
		return topNTermCount;
	}

	/**
	 * 选择maxKeywords个关键字
	 * 
	 * @param termsFreq	词元频率数组
	 * @return 返回选中的词元数组
	 */
	private String[][] getTopNTerm(String[][] termsFreq) {
		if (termsFreq.length < maxKeywords) {// 如果长度小于maxSynWords(10)个，则直接取出即可
			String[][] topNTerm = new String[termsFreq.length][2];
			System.arraycopy(termsFreq, 0, topNTerm, 0, termsFreq.length);// 通过
			return topNTerm;
		} else {//选出前N个词
			String[][] topNTerm = new String[maxKeywords][2];
			int topNTermIndex = 0;
			int k;
			String temp1,temp2;
			for (int i = 0; i < termsFreq.length; i++) {//每次循环选出一个最大值
				k=i;
				for (int j = i + 1; j < termsFreq.length; j++) {
					if(Integer.parseInt(termsFreq[j][1]) > Integer.parseInt(termsFreq[k][1])){
						k=j;
					}
				}
				if(k!=i){
					temp1=termsFreq[i][0];temp2=termsFreq[i][1];
					termsFreq[i][0]=termsFreq[k][0];termsFreq[i][1]=termsFreq[k][1];
					termsFreq[k][0]=temp1;termsFreq[k][1]=temp2;
				}
				if(termsFreq[i][0].length()!=1){//词长>1
					topNTerm[topNTermIndex][0] = termsFreq[i][0];// 存放词元名称
					topNTerm[topNTermIndex][1] = termsFreq[i][1];// 存放词元出现频率
//					System.out.println("maxTenSynWord["+topNTermIndex+"][0]="+termsFreq[i][0]);
//					System.out.println("maxTenSynWord["+topNTermIndex+"][1]="+termsFreq[i][1]);
					topNTermIndex++;
				}
				if(topNTermIndex==maxKeywords){
					break;
				}
			}
			return topNTerm;
		}
	}

	public String[][] getTopNTermInfo() {
		return topNTermInfo;
	}

	public void setTopNTermInfo(String[][] topNTermInfo) {
		this.topNTermInfo = topNTermInfo;
	}
}
