package ML.BP.StandardBP;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.xml.soap.SAAJResult;


public class testBP {
	public static void main(String[] args) throws IOException {
		String line = null;
		standardBP bp = new standardBP(2, 3, 1, 0.6);
		int epoch = 0;
		double error = Double.MAX_VALUE;
		while(epoch<2000 && error>0.01) {
			BufferedReader bufferedReader = new BufferedReader(new FileReader("D:\\Eclipece-java\\Optimization\\src\\ML\\BP\\data\\trainData.txt"));
			error = 0;
			epoch+=1;
			while((line = bufferedReader.readLine())!=null) {
				String[] in = line.split(",");
				double[] x = new double[2];
				double[] y = new double[1];
				x[0] = Double.parseDouble(in[0]);
				x[1] = Double.parseDouble(in[1]);
				y[0] = Double.parseDouble(in[2]);
				bp.train(x, y);
				error += bp.error;
			}
			System.out.println("训练次数："+epoch+"  error："+error);
		}
		BufferedReader bufferedReader2 = new BufferedReader(new FileReader("D:\\Eclipece-java\\Optimization\\src\\ML\\BP\\data\\testData.txt"));
		String lineTest = null;
		while((lineTest = bufferedReader2.readLine())!=null) {
			String[] testIn = lineTest.split(",");
			double[] x = new double[2];
			double[] y = new double[1];
			x[0] = Double.parseDouble(testIn[0]);
			x[1] = Double.parseDouble(testIn[1]);
			double[] out = bp.predict(x);
			System.out.println(String.valueOf(x[0])+"  "+String.valueOf(x[1])+"  PredictResult  "+out[0]);
		}
	}
}
