package ML.Adaboost;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class AdaboostTest {
	public static double getTestAcc(Adaboost ab,String path) {
		ArrayList<ArrayList<String>> testData = Util.LoadData(path);
		ArrayList<String> testLabel = Util.LoadLabel(path);
		Instance[] instances = new Instance[testData.size()];
		for(int i=0;i<testData.size();i++) {
			double[] ins = new double[testData.get(i).size()];
			for(int j=0;j<testData.get(i).size();j++) {
				ins[j] = Double.parseDouble(testData.get(i).get(j));
			}
			instances[i] = new Instance(ins, (int)Double.parseDouble(testLabel.get(i)));
		}
		double wrong = 0.0;
		for(int i=0;i<instances.length;i++) {
			if(ab.predict(instances[i]) != instances[i].label) {
				wrong += 1;
			}
		}
		return wrong/instances.length;
	}
	public static void main(String[] args) throws IOException {
			Util.bootStrapping("D:\\Eclipece-java\\Optimization\\src\\ML\\Adaboost\\data\\sonar.txt", "D:\\Eclipece-java\\Optimization\\src\\ML\\Adaboost\\data\\SonarTrain.txt", "D:\\Eclipece-java\\Optimization\\src\\ML\\Adaboost\\data\\SonarTest.txt");
			ArrayList<ArrayList<String>> trainData = Util.LoadData("D:\\Eclipece-java\\Optimization\\src\\ML\\Adaboost\\data\\SonarTrain.txt");
			ArrayList<String> trainLabel = Util.LoadLabel("D:\\Eclipece-java\\Optimization\\src\\ML\\Adaboost\\data\\SonarTrain.txt");
			Instance[] instances = new Instance[trainData.size()];
			for(int i=0;i<trainData.size();i++) {
				double[] ins = new double[trainData.get(i).size()];
				for(int j=0;j<trainData.get(i).size();j++) {
					ins[j] = Double.parseDouble(trainData.get(i).get(j));
				}
				instances[i] = new Instance(ins, (int)Double.parseDouble(trainLabel.get(i)));
			}
			Adaboost ab = new Adaboost(instances);
			int[] classNum = {1,10,50,100,500,1000,2000};
			BufferedWriter br = new BufferedWriter(new FileWriter("E:\\桌面\\adaboostResult.txt"));
			for(int i=0;i<classNum.length;i++) {
				ab.adaboost(classNum[i]);
				double trainError = getTestAcc(ab, "D:\\Eclipece-java\\Optimization\\src\\ML\\Adaboost\\data\\SonarTrain.txt");
				double testError = getTestAcc(ab, "D:\\Eclipece-java\\Optimization\\src\\ML\\Adaboost\\data\\SonarTest.txt");
				System.out.print("分类器个数："+classNum[i]+"\t"+"训练错误率："+trainError+"\t");
				System.out.print("测试错误率："+testError+"\n");
				br.write(classNum[i]+","+trainError+","+testError+"\n");
			}
			br.close();
		}
}
