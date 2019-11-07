package ML.Bagging.DtBagging;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import javax.rmi.CORBA.Util;

import ML.DT.Tree.ID3;
import ML.Util.ReadTestData;
import ML.Util.ReadTrainData;

public class Bagging {
	private ArrayList<String> label;                 //训练数据样本标签，例如色泽,根蒂,敲声,纹理,脐部,触感
	private ArrayList<ArrayList<String>> trainData;   //训练数据，例如青绿,蜷缩,浊响,清晰,凹陷,硬滑,是
	private ArrayList<ArrayList<String>> generateData;
	private ArrayList<ArrayList<String>> testData;
	private ID3[] id3s;
	public Bagging() throws FileNotFoundException {
		label = new ArrayList<>();
		trainData = new ArrayList<>();
		generateData = new ArrayList<>();
		id3s = new ID3[3];
		trainData = ReadTrainData.readTrainData("D:\\Eclipece-java\\Optimization\\src\\ML\\DT\\data\\watermelon_2_train.txt");
		testData = ReadTestData.readTestData("D:\\Eclipece-java\\Optimization\\src\\ML\\DT\\data\\watermelon_2_test.txt");
		label = ReadTrainData.getLabel();
	}
	public ArrayList<String> getLabel() {
		return label;
	}
	public ArrayList<ArrayList<String>> getTrainData() {
		return trainData;
	}
	public ArrayList<ArrayList<String>> getGenerateData() {
		return generateData;
	}
	public void generateDT() throws FileNotFoundException {
		for(int count=0;count<3;count++) {
			for(int i=0;i<trainData.size();i++) {
				int index = (int)(Math.random()*trainData.size());
				generateData.add(trainData.get(index));
			}
			id3s[count] = new ID3();
			id3s[count].trainData = generateData;
			id3s[count].label = ReadTrainData.getLabel();
			id3s[count].testData = testData;
			id3s[count].getSumClasses();
			id3s[count].predictAll();
			System.out.println(id3s[count].predictResult);
		}
	}
	public static void main(String[] args) throws FileNotFoundException {
		Bagging bagging = new Bagging();
		bagging.generateDT();
	}
}
