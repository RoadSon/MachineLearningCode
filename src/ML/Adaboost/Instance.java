package ML.Adaboost;


public class Instance {
 
	public double[] dim;	//各个维度值
	public int label;		//类别标号
	
	public Instance(double[] dim, int label) {
		this.dim = dim;
		this.label = label;
	}
}
