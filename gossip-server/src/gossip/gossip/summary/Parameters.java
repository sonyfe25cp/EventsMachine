package gossip.gossip.summary;

/**
 * 参数配置
 * 
 * @author zhangchangmin
 *
 */
public class Parameters {

	public static final int MaxKeywords = 10;// 关键词个数

	public static final int FinalSentencesCount = 3;// 抽取的句子总数
	
	public static final double TitleWeight = 2;// 标题权重

	public static final double FirstParaWeight = 1.5;// 第一段权重

	public static final double LastParaWeight = 1.4;// 最后一段权重

	public static final double FrequencyRatio = 0.3;// 频率权重

	public static final double PointRatio = 0.5;// 位置权重参数

	public static final double DistanceRatio = 0.2;// 距离权重参数

	public static final double DistanceExponentExp = 10;// 距离权重补充值

	public static final double DistanceWeightMultiple = 1.0;// 距离权重倍数


}
