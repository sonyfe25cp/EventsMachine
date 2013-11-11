package gossip.feature;

import java.util.LinkedList;
import java.util.List;

/**
 * @author ChenJie
 *
 * 用于计算特征
 * Sep 26, 2013
 */
public abstract class FeatureFactory {
	
	private List<FeatureHandler> handlers = new LinkedList<FeatureHandler>();
	
	protected abstract double[] compute(Object object);
	
	public void addHandler(FeatureHandler handler){
		this.handlers.add(handler);
	}
	public void removeHandler(FeatureHandler handler){
		this.handlers.remove(handler);
	}
	
	
	/**
	 * 计算对象的特征
	 * @param object
	 * @return
	 */
	public double[] getFeature(Object object){
		double feature[] = new double[0];
		if(handlers.size() == 0){
			System.out.println("请加入特征处理函数，再运行程序");
			System.exit(0);
		}
		for(FeatureHandler handler : handlers){
			double[] subFeatures = handler.handle(object);
			feature = combine(feature, subFeatures);
		}
		return feature;
	}

	private double[] combine(double[] array1, double[] array2){
		double[] com = new double[array1.length+array2.length];
		for(int i = 0 ; i < array1.length; i ++){
			com[i] = array1[i];
		}
		for(int i = 0 ; i < array2.length; i ++){
			com[i+array1.length] = array2[i];
		}
		return com;
	}
}
