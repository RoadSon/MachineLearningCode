package ML.Viterbi;


public class Viterbi {
	private static double[][] A = {{0.5,0.2,0.3},{0.3,0.5,0.2},{0.2,0.3,0.5}};
	private static double[][] B = {{0.5,0.5},{0.4,0.6},{0.7,0.3}};
	private static double[] pi = {0.2,0.4,0.4};
	private static int[] o = {1,2,1};
	private static int observeNum = 3;
	private static int statesNum = 3;
	private static double[][] delta = new double[3][3];
	private static int[][] w = new int[3][3];
	private static int[] path = new int[3];
	public static void viterbi() {
		//初始化
		for(int state=0;state<statesNum;state++) {
			delta[0][state] = pi[state] * B[state][o[0]-1];
			w[0][state] = 0;
		}
		//递推
		for(int observe=1;observe<observeNum;observe++) {
			for(int state=0;state<statesNum;state++) {
				double max = -Double.MAX_VALUE;
				int maxIndex = 0;
				for(int states=0;states<statesNum;states++) {
					double temp = delta[observe-1][states] * A[states][state] * B[state][o[observe]-1];
					if(temp > max) {
						max = temp;
						maxIndex = states;
					}
				}
				w[observe][state] = maxIndex;
				delta[observe][state] = max;
			}
		}
		System.out.println("δ矩阵为：");
		for(int i=0;i<3;i++) {
			for(int j=0;j<3;j++) {
				System.out.print(String.format("%.2f", delta[i][j])+" ");
			}
			System.out.println();
		}
	}
	public static void print() {
		//终止
		int bestPathIndex = 0;
		double bestPathPro = -Double.MAX_VALUE;
		for(int state=0;state<statesNum;state++) {
			double temp = delta[observeNum-1][state];
			if(temp > bestPathPro) {
				bestPathPro = temp;
				bestPathIndex = state;
			}
		}
		path[2] = bestPathIndex;
		System.out.println("最优路径概率："+String.format("%.4f", bestPathPro));
		//最优路径回溯
		for(int i=1;i>=0;i--) {
			path[i] = w[i+1][path[i+1]];
		}
		System.out.print("最优状态序列：");
		for(int p:path) {
			System.out.print((p+1)+" ");
		}
	}
	public static void main(String[] args) {
		viterbi();
		print();
	}
}
