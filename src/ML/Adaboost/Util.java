package ML.Adaboost;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import ML.Util.ReadTrainData;

public class Util {
	public static ArrayList<ArrayList<String>> LoadData(String path) {
		ArrayList<ArrayList<String>> data = new ArrayList<>();
		FileInputStream file;
		try {
			file = new FileInputStream(path);
			InputStreamReader ir = new InputStreamReader(file);
			BufferedReader br = new BufferedReader(ir);
			String line = null;
			try {
				while((line = br.readLine())!=null) {
					String[] temp = line.split(",");
					ArrayList<String> lineData = new ArrayList<String>();
					for(int i = 0 ; i<temp.length-1;i++) {
						lineData.add(temp[i]);
					}
					data.add(lineData);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return data;
	}
	public static ArrayList<String> LoadLabel(String path) {
		ArrayList<String> label = new ArrayList<>();
		FileInputStream file;
		try {
			file = new FileInputStream(path);
			InputStreamReader ir = new InputStreamReader(file);
			BufferedReader br = new BufferedReader(ir);
			String line = null;
			try {
				while((line = br.readLine())!=null) {
					String[] temp = line.split(",");
					label.add(temp[temp.length-1]);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return label;
	}
	public static void bootStrapping(String rawDatapath,String trainPath,String testPath) throws IOException {
		ArrayList<ArrayList<String>> rawData = ReadTrainData.readTrainData(rawDatapath);
		ArrayList<ArrayList<String>> trainDataGenerate = new ArrayList<>();
		ArrayList<Integer> randomFlag = new ArrayList<>();
		BufferedWriter bwTrainData = new BufferedWriter(new FileWriter(trainPath));
		for(int i=0;i<rawData.size();i++) {
			int indexTrain = (int)(Math.random()*rawData.size());
			if(!randomFlag.contains(indexTrain)) {
				randomFlag.add(indexTrain);
				trainDataGenerate.add(rawData.get(indexTrain));
				for(int j=0;j<rawData.get(indexTrain).size()-1;j++) {
					bwTrainData.write(rawData.get(indexTrain).get(j)+",");
				}
				bwTrainData.write(rawData.get(indexTrain).get(rawData.get(indexTrain).size()-1)+"\n");
			}
		}
		bwTrainData.close();
		BufferedWriter bwTestData = new BufferedWriter(new FileWriter(testPath));
		for(int i=0;i<rawData.size();i++) {
			if(!trainDataGenerate.contains(rawData.get(i))) {
				for(int j=0;j<rawData.get(i).size()-1;j++) {
					bwTestData.write(rawData.get(i).get(j)+",");
				}
				bwTestData.write(rawData.get(i).get(rawData.get(i).size()-1)+"\n");
			}
		}
		bwTestData.close();
	}
}
