package ML.Adaboost;

public abstract class Classifier {
	public double errorRate;
	public int errorNumber;
	public abstract int classify(Instance instance) ;
}
