package ML.Adaboost;

public class SimpleClassifier extends Classifier{
	double threshold ;	//分类的阈值
	int dimNum;			//对哪个维度分类
	int fuhao = 1;		//对阈值两边的处理
	
	public int classify(Instance instance) {
		
		if(instance.dim[dimNum] >= threshold) {
			return fuhao;
		}else {
			return -fuhao;
		}
	}
	
	/**
	 * 训练出threshold（阈值）和fuhao（大于小于号）
	 * @param instances 样本数据
	 * @param W 样例的权重
	 * @param dim 对样例的哪个维度进行训练
	 */
	public void train(Instance[] instances, double[] W, int dimNum) {
		
		errorRate = Double.MAX_VALUE;
		this.dimNum = dimNum;
		double adaThreshold = 0;
		int adaFuhao = 0;
		for(Instance instance : instances) {
			threshold = instance.dim[dimNum];
			for(int fuhaoIt = 0; fuhaoIt < 2; fuhaoIt ++) {//循环两次判断，当大于阈值时，应该返回1还是-1
				fuhao = -fuhao;
				double error = 0;
				int errorNum = 0;
				for(int i = 0; i< instances.length; i++) {
					if(classify(instances[i]) != instances[i].label) {
						error += W[i];
						errorNum++;
					}
				}
				if(error < errorRate){
					errorRate = error;
					errorNumber = errorNum;
					adaThreshold = threshold;
					adaFuhao = fuhao;
				}
			}
		}
		threshold = adaThreshold;
		fuhao = adaFuhao;
	}
}
