package gossip.feature;

/**
 * @author ChenJie
 * 计算不同特征的统一接口
 * Sep 26, 2013
 */
public interface FeatureHandler {

	public double[] handle(Object object);
	
}
