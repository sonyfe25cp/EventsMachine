package gossip.summary;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 生成摘要
 * 
 * @author zhangchangmin
 *
 */
public class GenerateSummary {

	private String autoSummaryResult;// 存放摘要结果

	private int finalSentencesCount = Parameters.FinalSentencesCount;
	
	Map<Integer,String> summap=new HashMap<Integer,String>();
	String[][] keywordWeight;
	String[][] topNSen;
	Map<Integer,String> map;
	
	public GenerateSummary(){
		
	}

	/**
	 * 响应请求，计算事件描述
	 * 
	 * @param pages	事件相关新闻
	 * @return	事件描述
	 */
	public String description(List<Integer> pages){
		Map<Integer,String[][]>  keymap=new HashMap<Integer,String[][]>();//文档，keywordWeight
		Map<Integer,String[][]> senmap=new HashMap<Integer,String[][]>();//文档，topNSen
		for(int i=0;i<pages.size();i++){
			int id = pages.get(i);
			if(id != 0){
				System.out.println("id :  "+id);
				summap.put(pages.get(i), summary(pages.get(i)));
				keymap.put(pages.get(i), keywordWeight);
				senmap.put(pages.get(i), topNSen);
			}
		}
		return summarization(keymap,senmap);
	}
	
	/**
	 * 单文档摘要
	 * 
	 * @param id	文档在数据库的ID
	 * @return	返回单个文档的摘要
	 */
	public String summary(int id) {
		SegParaSentence sps = new SegParaSentence(id);
		ChooseKeywords ckw=new ChooseKeywords(sps);
		// 存放分句信息数组, 格式：段号 句号 句子起始位置 句子结束位置
		int[][] sentences = sps.getSentencesInfo();
		// 包含频率信息的词元数组信息, 格式：词元 起始位置 频率
		String[][] topNTermInfo = ckw.getTopNTermInfo();
		// 段落,句子，词元关系数组, 格式：段号 句号 词元 起始位置 频率
		String[][] paraSenTermCon = this.getParaSenTermConnection(
				sentences, topNTermInfo);
		// 各个关键字的总权重, 格式为：句子，词元，总权重
		keywordWeight = getKeywordWeight(
				paraSenTermCon, sps.getSplitedParagraph().length, sentences);
		// 按照各个句子中出现词元总权重的总和选择摘要结果
		String[][] senWeight = getSenWeight(keywordWeight);
		String[] splitedSentence = sps.getSplitedSentence();
		finalSentencesCount = Parameters.FinalSentencesCount;
		topNSen=getTopNSen(senWeight,splitedSentence);
		return autoSummaryResult(topNSen);
	}
	/**
	 * 按句子在原文中的顺序输出摘要结果
	 * 
	 * @param senWeight	涉及句子权重
	 * @param splitedSentence 句子数组
	 * @return 返回摘要
	 */
	private String autoSummaryResult(String[][] topNSen) {
		StringBuffer sb = new StringBuffer();
		int[] indexN=new int[finalSentencesCount];//记录权重最大的N个句子在原文中的位置
		for(int i=0;i<topNSen.length;i++){
			indexN[i]=Integer.parseInt(topNSen[i][0]);
		}
		//按句子在原文中的顺序排序
		int temp;
		for(int i=0;i<indexN.length;i++){
			for(int j=0;j<indexN.length;j++){
				if(indexN[i]<indexN[j]){
					temp=indexN[i];
					indexN[i]=indexN[j];
					indexN[j]=temp;
				}
			}
		}
		//按句子在原文中的顺序输出
		for(int i=0;i<finalSentencesCount;i++){
			for(int j=0;j<topNSen.length;j++)
				if(indexN[i]==Integer.parseInt(topNSen[j][0]))
					sb.append(topNSen[j][2].trim()+"。");
		}
		return sb.toString();
	}
	/**
	 * 多文档摘要
	 * @param keymap	文档，keywordWeight
	 * @param senmap	文档，topNSen
	 * @return	返回多文档摘要
	 */
	public String summarization(Map<Integer,String[][]>  keymap,
			Map<Integer,String[][]> senmap){
		StringBuffer sb=new StringBuffer();
		for(int l=0;l<finalSentencesCount;l++){
			Iterator<Integer> one=senmap.keySet().iterator();
			double score=0;
			int key=-1;int senId=-1;
			//选出权重最大的句子
			while(one.hasNext()){
				int n=one.next();
				for(int i=0;i<senmap.get(n).length;i++)
					if(Double.parseDouble(senmap.get(n)[i][1]) > score){
						score=Double.parseDouble(senmap.get(n)[i][1]);
						key=n;senId=i;
					}
			}
//			System.out.println(key+"--"+senId);
//			System.out.println(senId+":"+senmap.get(key)[senId][2]);
			if(key==-1)	break;
			sb.append(senmap.get(key)[senId][2].trim()).append("。");
			if(sb.length()>999){
				int start=sb.indexOf(senmap.get(key)[senId][2].trim());
				sb.delete(start, sb.length());
				break;
			}
			Iterator<Integer> two=keymap.keySet().iterator();
			while(two.hasNext()){
				int n2=two.next();
				for(int i=0;i<keymap.get(n2).length;i++){
					if(senmap.get(key)[senId][2].toLowerCase()
							.indexOf(keymap.get(n2)[i][1])!=-1){
						keymap.get(n2)[i][2]=""+0;
					}
				}
				for(int i=0;i<senmap.get(n2).length;i++){
					double sum=0;
					for(int j=0;j<keymap.get(n2).length;j++){
						if(senmap.get(n2)[i][0].equals(keymap.get(n2)[j][0])){
							sum+=Double.parseDouble(keymap.get(n2)[j][2]);
						}
					}
					senmap.get(n2)[i][1]=""+sum;
				}
			}
		}
//		System.out.println(sb.toString());
		return sb.toString();
	}

	
	/**
	 * 选出权重最大的前N个句子[句子ID，权重，内容]
	 * 
	 * @param senWeight	句子权重
	 * @param splitedSentence	句子内容
	 * @return	返回权重最大的N个句子
	 */
	public String[][] getTopNSen(String[][] senWeight,String[] splitedSentence){
		if (finalSentencesCount > senWeight.length) {
			finalSentencesCount = senWeight.length;
		}
//		System.out.println(senWeight.length);
		String[][] topNSen=new String[finalSentencesCount][3];
//		int[] indexN=new int[finalSentencesCount];//记录权重最大的N个句子在原文中的位置
		//选出权重最大的N个句子
		int k=0,l=0;
		String temp1,temp2;
		for (int i = 0; i < senWeight.length; i++) {//每次循环选出一个最大值
			k=i;
			for (int j = i + 1; j < senWeight.length; j++) {
				if(Double.parseDouble(senWeight[j][1]) > Double.parseDouble(senWeight[k][1])){
					k=j;
				}
			}
			if(k!=i){
				temp1=senWeight[i][0];temp2=senWeight[i][1];
				senWeight[i][0]=senWeight[k][0];senWeight[i][1]=senWeight[k][1];
				senWeight[k][0]=temp1;senWeight[k][1]=temp2;
			}
			topNSen[i][0]=senWeight[i][0];
			topNSen[i][1]=senWeight[i][1];
			topNSen[i][2]=splitedSentence[Integer.parseInt(senWeight[i][0])-1];
//			indexN[i]=Integer.parseInt(senWeight[i][0]);
			l++;
			if(l==finalSentencesCount)
				break;
		}
		return topNSen;
	}

	/**
	 * 计算句子权重
	 * 
	 * @param keywordWeight
	 *            词元总权重数组 格式为：句子，词元，总权重
	 * @return 句子权重
	 */
	private String[][] getSenWeight(String[][] keywordWeight) {
		// 格式为：句子，总权重
		Set<String> set = new HashSet<String>();
		for (int i = 0; i < keywordWeight.length; i++) {
			set.add(keywordWeight[i][0]);
		}
		if(set.contains("1")){
			set.remove("1");
		}
		String[][] senWeight = new String[set.size()][2];
		Iterator<String> iter = set.iterator();
		int index = 0;
		while (iter.hasNext()) {
			int sentencesIndex = Integer.parseInt(iter.next());
			double sum = 0.0;
			for (int i = 0; i < keywordWeight.length; i++) {
				if (sentencesIndex == Integer.parseInt(keywordWeight[i][0])
						&& sentencesIndex != 1) {
					sum += Double.parseDouble(keywordWeight[i][2]);
				}
			}
			senWeight[index][0] = "" + sentencesIndex;// 句子下标
			senWeight[index][1] = "" + sum;// 总和
			index++;
		}
		return senWeight;
	}

	/**
	 * 计算各个关键字的总权重
	 * 
	 * @param paraSenTermCon
	 *            格式为：段号,句子，词元，词元起始位置，频率 的数组
	 * @param paraCount
	 *            段落数
	 * @param sentences
	 *            格式：段号 句号 句子起始位置 句子结束位置
	 * @return 返回带权重的关键字数组	格式为：句子，词元，总权重
	 */
	private String[][] getKeywordWeight(String[][] paraSenTermCon,
			int paraCount, int[][] sentences) {
//		int paragraphSum = splitedParagraph.length;
		// 格式为：句子，词元，总权重（各个关键字的总权重）
		String[][] keywordWeight = new String[paraSenTermCon.length][3];
		for (int i = 0; i < paraSenTermCon.length; i++) {
			int distanceweight = 0;// 距离权重
			for (int j = 0; j < sentences.length; j++) {
				if ((Integer.parseInt(paraSenTermCon[i][1]) == sentences[j][1])
						&& (Integer.parseInt(paraSenTermCon[i][0]) == sentences[j][0])) {// 相同句子，相同段落
					int keywordOffset = Integer
							.parseInt(paraSenTermCon[i][3]);// 词元起始下标
					int sentenceStartOffset = sentences[j][2];
					int sentencesEndOffset = sentences[j][3];
					if (keywordOffset >= sentenceStartOffset
							&& keywordOffset < sentencesEndOffset) {// 如果词元在句子中
						// 计算距离权重
						if ((keywordOffset - sentenceStartOffset) > (sentencesEndOffset - keywordOffset)) {// 正反向匹配词元权重，取最小的
							distanceweight = sentencesEndOffset - keywordOffset;// 反向权重
						} else {
							distanceweight = keywordOffset
									- sentenceStartOffset;// 正向权重
						}
					}
				}
			}
			double totalWeight = 0.0;
			int paragraphIndex = Integer.parseInt(paraSenTermCon[i][0]);// 段落下标
			// 如果是标题，第一段和最后一段；除了计算频率权重，还要计算位置权重
			// 计算公式为：总权重=频率权重（出现次数）*0.3+位置权重（2，1.5，1.4）*0.5+距离权重（词元离句子首和句子尾的最小距离）*0.2
			if (distanceweight == 0) {
				distanceweight = 1;
			}
			if (paragraphIndex == 1) {// 标题
				totalWeight += Integer.parseInt(paraSenTermCon[i][4])
						* Parameters.FrequencyRatio
						+ Parameters.TitleWeight
						* Parameters.PointRatio
						+ distanceweight * Parameters.DistanceRatio;
			} else if (paragraphIndex == 2) {// 第一段
				totalWeight += Integer.parseInt(paraSenTermCon[i][4])
						* Parameters.FrequencyRatio
						+ Parameters.FirstParaWeight
						* Parameters.PointRatio
						+ distanceweight * Parameters.DistanceRatio;
			} else if (paragraphIndex == paraCount) {// 最后一段
				totalWeight += Integer.parseInt(paraSenTermCon[i][4])
						* Parameters.FrequencyRatio
						+ Parameters.LastParaWeight
						* Parameters.PointRatio
						+ distanceweight * Parameters.DistanceRatio;
			} else {// 其他段落
				totalWeight += Integer.parseInt(paraSenTermCon[i][4])
						* Parameters.FrequencyRatio
						+ distanceweight * Parameters.DistanceRatio;
			}
			keywordWeight[i][0] = paraSenTermCon[i][1];// 句子
			keywordWeight[i][1] = paraSenTermCon[i][2];// 词元
			keywordWeight[i][2] = "" + totalWeight;// 总权重
		}
		return keywordWeight;
	}

	/**
	 * 获得格式为：段号,句号，词元，起始位置，频率 的数组
	 * 
	 * @param sentences
	 *            分句信息数组 格式为：段号 句号 句子起始位置 句子结束位置
	 * @param topNTermInfo
	 *            包含频率信息的词元数组信息 格式：词元 起始位置 频率
	 * @return 返回数组
	 */
	private String[][] getParaSenTermConnection(int[][] sentences,
			String[][] topNTermInfo) {
		// 根据词元长度构造此数组
		//段号,句子，词元，起始位置，频率
		String[][] paraSenTermCon = new String[topNTermInfo.length][5];
		int index = 0;
		for (int i = 0; i < sentences.length; i++) {
			for (int j = 0; j < topNTermInfo.length; j++) {
				int termStartOffset = Integer
						.parseInt(topNTermInfo[j][1]);// 词元起始位置
				int sentenceStartOffset = sentences[i][2];// 句子起始位置
				int sentenceEndOffset = sentences[i][3];// 句子结束位置
				if (index < paraSenTermCon.length) {
					// 如果词元在句子中
					if (termStartOffset >= sentenceStartOffset
							&& termStartOffset < sentenceEndOffset) {
						paraSenTermCon[index][0] = ""+ sentences[i][0];//段号
						paraSenTermCon[index][1] = ""+ sentences[i][1];//句号
						paraSenTermCon[index][2] = topNTermInfo[j][0];//词元
						paraSenTermCon[index][3] = topNTermInfo[j][1];//起始位置
						paraSenTermCon[index][4] = topNTermInfo[j][2];//频率
						index++;
					}
				}
			}
		}
		return paraSenTermCon;
	}

	public String getAutoSummaryResult() {
		return autoSummaryResult;
	}

	public void setAutoSummaryResult(String autoSummaryResult) {
		this.autoSummaryResult = autoSummaryResult;
	}
}
