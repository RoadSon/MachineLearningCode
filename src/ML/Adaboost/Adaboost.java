package ML.Adaboost;
import java.util.ArrayList;
import java.util.List;

public class Adaboost {
	Instance[] instances;
	List<Classifier> classifierList = null;	//各个弱分类器
	List<Double> alphaList = null;			//每个弱分类器的权重
	public Adaboost(Instance[] instances) {
		
		this.instances = instances;
	}
	
	/**
	 * adaboost方法训练基学习器
	 * @param T 基学习器的个数
	 */
	public void adaboost(int T) {//T个基学习器
		int len = this.instances.length;
		double[] W = new double[len];	//初始权重
		for(int i = 0; i < len; i ++) {
			W[i] = 1.0 / len;
		}
		classifierList = new ArrayList<Classifier>();
		alphaList = new ArrayList<Double>();
		for(int t = 0; t < T; t++) {//T轮
			Classifier cf = getMinErrorRateClassifier(W);
			classifierList.add(cf);
			double errorRate = cf.errorRate;
			//计算弱分类器的权重
			double alpha = 0.5 * Math.log((1 - errorRate) / errorRate);
			alphaList.add(alpha);
			//更新样例的权重
			double z = 0;
			for(int i = 0; i < W.length; i++) {
				W[i] = W[i] * Math.exp(-alpha * instances[i].label * cf.classify(instances[i]));
				z += W[i];//规范化因子
			}
			for(int i = 0; i < W.length; i++) {
				W[i] /= z;
			}
		}
	}
	
	/**
	 * 训练好基学习器之后预测测试数据
	 * @param instance 测试数据
	 * @return
	 */
	public int predict(Instance instance) {
		double p = 0;
		for(int i = 0; i < classifierList.size(); i++) {
			p += classifierList.get(i).classify(instance) * alphaList.get(i);
		}
		if(p > 0) return 1;
		return -1;
	}
 
	/**
	 * 得到错误率最低的分类器
	 * @param W 权值
	 * @return
	 */
	private Classifier getMinErrorRateClassifier(double[] W) {
		
		double errorRate = Double.MAX_VALUE;
		SimpleClassifier minErrorRateClassifier = null;
		int dimLength = instances[0].dim.length;
		for(int i = 0; i < dimLength; i++) {
			SimpleClassifier sc = new SimpleClassifier();
			sc.train(instances, W, i);//基学习器训训练
			if(errorRate > sc.errorRate){
				errorRate  = sc.errorRate;
				minErrorRateClassifier = sc;
			}
		}
		return minErrorRateClassifier;
	}
	
}
