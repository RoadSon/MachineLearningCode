package ML.NB.Bayes;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

import ML.Util.ReadTrainData;

public class nbTest {
	public static void main(String[] args) throws IOException {
		ArrayList<Double> errors = new ArrayList<>();
		for(int i=0;i<50;i++) {
			bootStrapping();
			NaiveBayes nBayes = new NaiveBayes();
			nBayes.readTrainData("D:\\Eclipece-java\\Optimization\\src\\ML\\NB\\data\\trainWine.txt");
			nBayes.readTestData("D:\\Eclipece-java\\Optimization\\src\\ML\\NB\\data\\testWine.txt");
			errors.add(1-nBayes.predict());
		}
		double meanError = 0.0;
		for(double error:errors) {
			meanError += error;
		}
		meanError = meanError/50.0;
		
		double sd = 0.0;
		for(double error:errors) {
			sd += (error-meanError)*(error-meanError);
		}
		sd = sd/50.0;
		System.out.println("mean error is "+meanError+" and "+"error’s standard deviation "+sd);
	}
	public static void bootStrapping() throws IOException {
		ArrayList<ArrayList<String>> wineData = ReadTrainData.readTrainData("D:\\Eclipece-java\\Optimization\\src\\ML\\NB\\data\\wine.txt");
		ArrayList<ArrayList<String>> trainDataGenerate = new ArrayList<>();
		ArrayList<Integer> randomFlag = new ArrayList<>();
		BufferedWriter bwTrainData = new BufferedWriter(new FileWriter("D:\\Eclipece-java\\Optimization\\src\\ML\\NB\\data\\trainWine.txt"));
		for(int i=0;i<wineData.size();i++) {
			int indexTrain = (int)(Math.random()*wineData.size());
			if(!randomFlag.contains(indexTrain)) {
				randomFlag.add(indexTrain);
				trainDataGenerate.add(wineData.get(indexTrain));
				for(int j=1;j<wineData.get(indexTrain).size();j++) {
					bwTrainData.write(wineData.get(indexTrain).get(j)+",");
				}
				bwTrainData.write(wineData.get(indexTrain).get(0)+"\n");
			}
		}
		bwTrainData.close();
		BufferedWriter bwTestData = new BufferedWriter(new FileWriter("D:\\Eclipece-java\\Optimization\\src\\ML\\NB\\data\\testWine.txt"));
		for(int i=0;i<wineData.size();i++) {
			if(!trainDataGenerate.contains(wineData.get(i))) {
				for(int j=1;j<wineData.get(i).size();j++) {
					bwTestData.write(wineData.get(i).get(j)+",");
				}
				bwTestData.write(wineData.get(i).get(0)+"\n");
			}
		}
		bwTestData.close();
	}
}
