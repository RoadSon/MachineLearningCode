package ML.BP.StandardBP;
/**
 * BP神经网络
 * @author RoadSon
 */
import java.util.Random;

public class standardBP {
	/*
	 * 下面定义的变量和西瓜书上图5.7的变量相同，其中n是学习率，predict_y是bp模型输出，y是样本输出
	 */
	private double[] x;
	private double[][] v;
	private double[] gama;
	private double[] b;
	private double[][] w;
	private double[] theta;
	private double[] predict_y;
	private double[] y;
	private double n;
	private double[] g;
	private double[] e;
	double error;
	private int d;		//输入层数
	private int q;		//隐藏层数
	private int l;		//输出层数
	/*
	 * 初始化各变量
	 */
	public standardBP(int d,int q,int l,double n) {
		this.d = d;
		this.q = q;
		this.l = l;
		this.n =n;
		x = new double[d];
		v = new double[d][q];
		gama = new double[q];
		b = new double[q];
		w = new double[q][l];
		theta = new double[l];
		y = new double[l];
		g = new double[l];
		e = new double[q];
		predict_y = new double[l];
		error = Double.MAX_VALUE;
		randomInit();
	}
	/*
	 * 以标准正态分布随机初始化参数v，gama，w，theta
	 */
	public void randomInit() {
		Random random = new Random();
		boolean initGama = false;
		for(int i=0;i<d;i++) {
			for(int j=0;j<q;j++) {
				v[i][j] = random.nextGaussian();
				if(!initGama) {
					gama[j] = random.nextGaussian();
				}
			}
			initGama = true;
		}
		boolean initTheta = false;
		for(int i=0;i<q;i++) {
			for(int j=0;j<l;j++) {
				w[i][j] = random.nextGaussian();
				if(!initTheta) {
					theta[j] = random.nextGaussian();
				}
			}
			initTheta = true;
		}
	}
	/*
	 * 定义Sigmoid函数
	 */
	public double sigmoid(double x) {
		return 1/(1+Math.exp(-x));
	}
	/*
	 * 前向传播
	 */
	public void forward() {
		/*
		 * 计算输入层到隐藏层
		 */
		double alpha = 0;
		for(int i=0;i<q;i++) {
			for(int j=0;j<d;j++) {
				alpha += v[j][i]*x[j];
			}
			b[i] = sigmoid(alpha - gama[i]);
		}
		/*
		 * 计算隐藏层到输出层
		 */
		double beta = 0;
		for(int i=0;i<l;i++) {
			for(int j=0;j<q;j++) {
				beta += w[j][i]*b[j];
			}
			predict_y[i] = sigmoid(beta - theta[i]);
		}
	}
	/*
	 * 计算均方误差以及g和e
	 */
	public double computeErrorANDget_g_e() {
		double sumError = 0.0;
		for(int i=0;i<l;i++) {
			g[i] = predict_y[i]*(1-predict_y[i])*(y[i]-predict_y[i]);
			sumError += (y[i]-predict_y[i])*(y[i]-predict_y[i]);
		}
		for(int i=0;i<q;i++) {
			double sumWG = 0.0;
			for(int j=0;j<l;j++) {
				sumWG += w[i][j]*g[j];
			}
			e[i] = b[i]*(1-b[i])*sumWG;
		}
		return sumError/2;
	}
	/*
	 * 更新w、theta、v和gama参数
	 */
	public void update() {
		/*
		 * 更新w和theta
		 */
		boolean updateTheta = false;
		for(int i=0;i<q;i++) {
			for(int j=0;j<l;j++) {
				w[i][j] = w[i][j] + n*g[j]*b[i];
				if(!updateTheta) {
					theta[j] = theta[j] - n*g[j];
					updateTheta = true;
				}
			}
		}
		/*
		 * 更新v和gama
		 */
		boolean updateGama = false;
		for(int i=0;i<d;i++) {
			for(int j=0;j<q;j++) {
				v[i][j] = v[i][j] + n*e[j]*x[i];
				if(!updateGama) {
					gama[j] = gama[j] - n*e[j];
				}
			}
		}
	}
	/*
	 * 训练模型
	 */
	public void train(double[] x,double[] y) {
		this.x = x;
		this.y = y;
		forward();
		error = computeErrorANDget_g_e();
		update();
	}
	/*
	 * 评估模型
	 */
	public double[] predict(double[] x) {
		this.x = x;
		forward();
		return predict_y;
	}
}
